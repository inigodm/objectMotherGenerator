package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import java.io.File

class PluginLauncher {

    fun doObjectMotherCreation(e: AnActionEvent) {
        FileChooser.chooseFile(buildFileChooserDescriptor(), e.project, e.getData(CommonDataKeys.VIRTUAL_FILE)) {
            val dir = PsiManager.getInstance(e.project!!).findDirectory(it) ?: return@chooseFile
            objectMotherCreation(e, dir)
        }
    }

    private fun buildFileChooserDescriptor(): FileChooserDescriptor {
        var descriptor = FileChooserDescriptor(false, true, false, false, false, false)
        descriptor.title = "Choose project/module's test srcDir root in which ObjectMother's package will be created"
        descriptor.description =
            "The source directory for ObjectMother's package must be set. Package would be automatically calculated"
        return descriptor
    }

    private fun objectMotherCreation(e: AnActionEvent, testSrcDir: PsiDirectory?) {
        val project = e.project!!
        val creator = ObjectMotherCreator(JavaFileCreator(project), JavaObjectMotherTemplate())
        creator.createObjectMotherFor(e, testSrcDir)
        openFilesInEditor(project, creator.objectMotherFileNames)
    }

    private fun openFilesInEditor(project: Project, createdFiles : List<String>) {
        createdFiles.forEach {
            val file = File(it)
            if (file.exists()) {
                FileEditorManager.getInstance(project).openFile(findVirtualFile(file), true)
            }
        }
    }

    private fun findVirtualFile (file: File) : VirtualFile {
        return LocalFileSystem.getInstance().findFileByIoFile(file)!!
    }
}
