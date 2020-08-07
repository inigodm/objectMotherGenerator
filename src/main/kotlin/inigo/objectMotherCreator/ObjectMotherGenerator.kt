package inigo.objectMotherCreator

import com.intellij.psi.*
import java.io.File

class ObjectMotherBuilder(var fileCreator: FileCreator, var template: ObjectMotherTemplate) {
    val objectMotherFileNames = mutableListOf<String>()

    fun buildFor(infoExtractor: JavaFileInfo, baseDir: PsiDirectory?) {
        objectMotherFileNames.clear()
        val classesToTreat = mutableListOf<ClassInfo>()
        classesToTreat.add(infoExtractor.mainClass)
        while (classesToTreat.isNotEmpty()) {
            val clazzInfo = classesToTreat.removeAt(0);
            createFile(baseDir,
                    infoExtractor.mainClass.packageName,
                    clazzInfo,
                    template.buildObjectMotherCode(clazzInfo))
            classesToTreat.addAll(template.getNeededObjectMothers())
        }
    }

    private fun createFile(baseDir: PsiDirectory?, packageName: String, clazzInfo: ClassInfo, javaCode: String) {
        val directory = fileCreator.findOrCreateDirectoryForPackage(packageName, baseDir)!!
        fileCreator.createFile(directory, "${clazzInfo.clazz.name}ObjectMother.java", javaCode)
        addGeneratedObjectMotherFileName(directory, clazzInfo)
    }

    private fun addGeneratedObjectMotherFileName(directory: PsiDirectory, clazzInfo: ClassInfo) {
        objectMotherFileNames.add("${directory.virtualFile.canonicalPath}${File.separator}${clazzInfo.clazz.name}ObjectMother.java")
    }
}
