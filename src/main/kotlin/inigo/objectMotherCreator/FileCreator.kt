package inigo.objectMotherCreator

import com.intellij.ide.IdeBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.impl.file.PsiDirectoryFactory
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
                            return@runWriteAction makeFile(directory, name, code)

                        } catch (e: Exception) {
                            e.printStackTrace();
                            return@runWriteAction null
                        }
                    }
            }, IdeBundle.message("command.create.new.subdirectory"), null
        )
    }

    private fun makeFile(directory: PsiDirectory, name: String, code: String) : PsiFile? {
        println("Creating file $name in $directory")
        var file = directory.findFile(name)
        if (null == file){
            file = directory.createFile(name)
        }
        val documentManager = PsiDocumentManager.getInstance(project)
        documentManager.getDocument(file)?.insertString(0, code)
        return file
    }


    @Throws(IncorrectOperationException::class)
    fun findOrCreateDirectoryForPackage(packageName: String, root: PsiJavaFile): PsiDirectory? {
        var psiDirectory: PsiDirectory?
        var baseDir = PsiDirectoryFactory.getInstance(project).createDirectory(findOrCreateSourceRoot(root))
        psiDirectory = baseDir
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

    fun findOrCreateSourceRoot(root: PsiJavaFile): VirtualFile {
        return ModuleUtilCore.findModuleForFile(root)!!.rootManager!!.sourceRoots
            .filter { it.path.toLowerCase().endsWith("test") }
            .getOrElse(0) {
                return ApplicationManager.getApplication()
                    .runWriteAction<VirtualFile> {
                        var directory = project.baseDir
                        if (ModuleUtilCore.findModuleForFile(root)!!.rootManager!!.sourceRoots.size > 0) {
                            directory = ModuleUtilCore.findModuleForFile(root)!!.rootManager!!.sourceRoots[0].parent
                        }
                        return@runWriteAction createDirectories(directory)
                    }
            }
    }

    fun createDirectories(directory: VirtualFile): VirtualFile {
        return directory.children
            .filter { it.path.equals(directory.path + "/test") }
            .getOrElse(0) {
                return directory.createChildDirectory(directory, "test")
            }
    }
}
