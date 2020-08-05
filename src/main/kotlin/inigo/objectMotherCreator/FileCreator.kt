package inigo.objectMotherCreator

import com.intellij.ide.IdeBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException

class FileCreator(var project: Project) {

    @Throws(IncorrectOperationException::class)
    fun createJavaFile(directory: PsiDirectory, name: String, code: String) {
        println("Creating file $name in $directory")
        return CommandProcessor.getInstance().executeCommand(
            project,
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

    private fun makeFileIfDoesntExists(directory: PsiDirectory, name: String, code: String) : PsiFile? {
        println("Creating file $name in $directory")
        var file = directory.findFile(name)
        if (null == file){
            file = makeFile(directory, name, code)
        }
        return file
    }

    private fun makeFile(directory: PsiDirectory, name: String, code: String): PsiFile? {
        val file = directory.createFile(name)
        val documentManager = PsiDocumentManager.getInstance(project)
        documentManager.getDocument(file)?.insertString(0, code)
        return file
    }


    @Throws(IncorrectOperationException::class)
    fun findOrCreateDirectoryForPackage(packageName: String, srcDirectory: PsiDirectory?): PsiDirectory? {
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

    @Throws(IncorrectOperationException::class)
    private fun createSubdirectory(
        oldDirectory: PsiDirectory,
        name: String
    ): PsiDirectory? {
        val psiDirectory = arrayOfNulls<PsiDirectory>(1)
        val exception = arrayOfNulls<IncorrectOperationException>(1)
        CommandProcessor.getInstance().executeCommand(
            project,
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
}
