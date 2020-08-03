package inigo.objectMotherCreator

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import java.io.File

class ObjectMotherBuilder(var project: Project) {
    val classesTreated = mutableListOf<String>()
    var template = JavaObjectMotherTemplate()
    var fileCreator = FileCreator(project)

    fun buildFor(root: PsiJavaFile, testSrcDir: VirtualFile) {
        val infoExtractor = JavaFileInfo(root, project)
        val dir = PsiManager.getInstance(project).findDirectory(testSrcDir)
        val classesToTreat = mutableListOf<PsiJavaClassInfo>()
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
