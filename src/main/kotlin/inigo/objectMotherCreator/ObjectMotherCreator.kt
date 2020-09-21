package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.*
import java.io.File

class ObjectMotherCreator(var fileCreator: FileCreator, var template: ObjectMotherTemplate) {
    val objectMotherFileNames = mutableListOf<String>()

    fun createObjectMotherFor(e: AnActionEvent, baseDir: PsiDirectory?) {
        objectMotherFileNames.clear()
        val classesToTreat = mutableListOf<ClassInfo>()
        val fileInfoExtractor = JavaFileInfo(e.getData(CommonDataKeys.PSI_FILE) as PsiJavaFile, e.project!!)
        val packageName = fileInfoExtractor.packageName()
        classesToTreat.addAll(fileInfoExtractor.classesToTread())
        while (classesToTreat.isNotEmpty()) {
            val clazzInfo = classesToTreat.removeAt(0);
            createFile(baseDir,
                    packageName,
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
