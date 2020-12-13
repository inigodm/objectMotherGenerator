package inigo.objectMotherCreator

import com.intellij.ide.IdeBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import inigo.objectMotherCreator.infraestructure.IdeaShits
import java.io.File


interface FileCreator {
    @Throws(IncorrectOperationException::class)
    fun createFile(directory: PsiDirectory, name: String, code: String)

    @Throws(IncorrectOperationException::class)
    fun findOrCreateDirectoryForPackage(packageName: String, srcDirectory: PsiDirectory?): PsiDirectory?
}
class JavaFileCreator(var ideaShits: IdeaShits): FileCreator {

    @Throws(IncorrectOperationException::class)
    override fun createFile(directory: PsiDirectory, name: String, code: String) {
        return CommandProcessor.getInstance().executeCommand(
            ideaShits.getProject(),
            {
                ApplicationManager.getApplication()
                    .runWriteAction<PsiFile> {
                        try {
                            return@runWriteAction makeFileIfDoesntExists(directory, name, code)
                        } catch (e: Exception) {
                            e.printStackTrace();
                            return@runWriteAction null
                        }
                    }
            }, IdeBundle.message("command.create.new.subdirectory"), null
        )
    }

    @Throws(IncorrectOperationException::class)
    override fun findOrCreateDirectoryForPackage(packageName: String, srcDirectory: PsiDirectory?): PsiDirectory? {
        var psiDirectory: PsiDirectory?
        psiDirectory = srcDirectory
        packageName.split(".").forEach {
            val foundExistingDirectory = psiDirectory!!.findSubdirectory(it)
            if (foundExistingDirectory == null) {
                psiDirectory = createSubdirectory(psiDirectory!!, it)
            } else {
                psiDirectory = foundExistingDirectory
            }
        }
        return psiDirectory
    }

    private fun makeFileIfDoesntExists(directory: PsiDirectory, name: String, code: String) : PsiFile? {
        var file = directory.findFile(name)
        if (null == file){
            file = makeFile(directory, name, code)
        }
        return file
    }

    private fun makeFile(directory: PsiDirectory, name: String, code: String): PsiFile? {
        val file = directory.createFile(name)
        val documentManager = PsiDocumentManager.getInstance(ideaShits.getProject()!!)
        documentManager.getDocument(file)?.insertString(0, code)
        return file
    }


    @Throws(IncorrectOperationException::class)
    private fun createSubdirectory(
        oldDirectory: PsiDirectory,
        name: String
    ): PsiDirectory? {
        val psiDirectory = arrayOfNulls<PsiDirectory>(1)
        val exception = arrayOfNulls<IncorrectOperationException>(1)
        CommandProcessor.getInstance().executeCommand(
            ideaShits.getProject(),
            {
                psiDirectory[0] = ApplicationManager.getApplication()
                    .runWriteAction<PsiDirectory> {
                        try {
                            return@runWriteAction oldDirectory.createSubdirectory(name)
                        } catch (e: IncorrectOperationException) {
                            exception[0] = e
                            return@runWriteAction null
                        }
                    }
            }, IdeBundle.message("command.create.new.subdirectory"), null
        )
        if (exception[0] != null) throw exception[0]!!
        return psiDirectory[0]
    }

    fun createFile(baseDir: PsiDirectory?, clazzInfo: ClassInfo, javaCode: String): String {
        val directory = findOrCreateDirectoryForPackage(clazzInfo.packageName, baseDir)!!
        createFile(directory, "${clazzInfo.clazz.name}ObjectMother.java", javaCode)
        return "${directory.virtualFile.canonicalPath}${File.separator}${clazzInfo.clazz.name}ObjectMother.java"
    }
}
