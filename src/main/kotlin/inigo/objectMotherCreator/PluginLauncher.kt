package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import org.jetbrains.jps.model.java.JavaSourceRootType
import java.io.File

class PluginLauncher {

    fun doObjectMotherCreation(e: AnActionEvent) {
        var dirVirtualFile: VirtualFile? = findTestSourceDirectory(e)
        if (dirVirtualFile == null){
            return
        }
        val dir = PsiManager.getInstance(e.project!!).findDirectory(dirVirtualFile!!)
        objectMotherCreation(e, dir)
    }

    private fun findTestSourceDirectory(e: AnActionEvent): VirtualFile? {
        val selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE)!!
        val module = ProjectFileIndex.getInstance(e.project!!).getModuleForFile(selectedFile)!!
        val moduleRootManager = ModuleRootManager.getInstance(module)
        val testSources = moduleRootManager.getSourceRoots(JavaSourceRootType.TEST_SOURCE)
        var dirVirtualFile: VirtualFile? = null
        if (testSources.size == 1) {
            dirVirtualFile = testSources[0]
        }
        if (dirVirtualFile == null) {
            FileChooser.chooseFile(buildFileChooserDescriptor(), e.project, e.getData(CommonDataKeys.VIRTUAL_FILE)) {
                dirVirtualFile = it
            }
        }
        return dirVirtualFile
    }

    private fun buildFileChooserDescriptor(): FileChooserDescriptor {
        val descriptor = FileChooserDescriptor(false, true, false, false, false, false)
        descriptor.title = "Choose project/module's test srcDir root in which ObjectMother's package will be created"
        descriptor.description =
            "The source directory for ObjectMother's package must be set. Package would be automatically calculated"
        return descriptor
    }

    private fun objectMotherCreation(e: AnActionEvent, testSrcDir: PsiDirectory?) {
        val project = e.project!!
        val creator = ObjectMotherCreator(JavaFileCreator(project), JavaObjectMotherTemplate())
        val fileInfoExtractor = JavaFileInfo(e.getData(CommonDataKeys.PSI_FILE) as PsiJavaFile, e.project!!)
        creator.createObjectMotherFor(fileInfoExtractor, testSrcDir)
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
