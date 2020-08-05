package inigo.objectMotherCreator

import com.intellij.psi.*
import java.io.File

class ObjectMotherBuilder(var fileCreator: FileCreator, var template: JavaObjectMotherTemplate) {
    val classesTreated = mutableListOf<String>()

    fun buildFor(infoExtractor: JavaFileInfo, dir: PsiDirectory?) {
        classesTreated.clear()
        val classesToTreat = mutableListOf<ClassInfo>()
        classesToTreat.add(infoExtractor.mainClass)
        while (classesToTreat.isNotEmpty()) {
            val clazzInfo = classesToTreat.removeAt(0);
            val javaCode = template.buildJavaFile(clazzInfo)
            val directory = fileCreator.findOrCreateDirectoryForPackage(infoExtractor.mainClass.packageName, dir)!!
            fileCreator.createJavaFile(directory, "${clazzInfo.clazz.name}ObjectMother.java", javaCode)
            classesToTreat.addAll(template.neededObjectMotherClasses)
            classesTreated.add( "${directory.virtualFile.canonicalPath}${File.separator}${clazzInfo.clazz.name}ObjectMother.java")
        }
    }
}
