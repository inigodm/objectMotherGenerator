package inigo.objectMotherCreator.infraestructure

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import inigo.objectMotherCreator.model.infoExtractor.*
import org.jetbrains.jps.model.java.JavaSourceRootType
import java.io.File

class IdeaShits(val e: AnActionEvent) {
    fun getCurrentOMFile(): OMFile {
        return OMFile(e.getData(CommonDataKeys.PSI_FILE)!!)
    }

    fun getCurrentOMVirtualFile(): OMVirtualFile {
        return OMVirtualFile(e.getData(PlatformDataKeys.VIRTUAL_FILE))
    }

    fun isCaretInFileType(extension: String) =
        e.getData(CommonDataKeys.PSI_FILE)!!.language.displayName.equals(extension, ignoreCase = true)

    fun setMenuItemEnabled(enabled: Boolean) {
        e.presentation.isEnabledAndVisible = enabled
    }

    fun obtainTestSourceDirectory(): OMDirectory? {
        val dirVirtualFile: VirtualFile = findTestSourceDirectory() ?: return null
        return OMDirectory(PsiManager.getInstance(e.project!!).findDirectory(dirVirtualFile)!!)
    }

    fun openFileInNewTab(file: File) {
        FileEditorManager.getInstance(e.project!!).openFile(findVirtualFile(file), true)
    }

    fun getProject(): Project? {
        return e.project
    }

    fun findClass(qualifiedName: String): OMClass? {
        val psiClass = JavaPsiFacade.getInstance(e.project!!).findClass(qualifiedName, GlobalSearchScope.projectScope(e.project!!))
        return if (psiClass == null) {
            null
        } else {
            OMJavaClass(psiClass)
        }
    }

    fun openFilesInEditor(createdFiles : List<String>) {
        createdFiles.forEach {
            val file = File(it)
            if (file.exists()) {
                openFileInNewTab(file)
            }
        }
    }

    private fun findTestSourceDirectory(): VirtualFile? {
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

    private fun findVirtualFile (file: File) : VirtualFile {
        return LocalFileSystem.getInstance().findFileByIoFile(file)!!
    }

    private fun buildFileChooserDescriptor(): FileChooserDescriptor {
        val descriptor = FileChooserDescriptor(
            false,
            true,
            false,
            false,
            false,
            false)
        descriptor.title = "Choose project/module's test srcDir root in which ObjectMother's package will be created"
        descriptor.description =
            "The source directory for ObjectMother's package must be set. Package would be automatically calculated"
        return descriptor
    }

}
