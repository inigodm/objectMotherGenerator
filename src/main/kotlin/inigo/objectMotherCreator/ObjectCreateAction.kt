package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import java.io.File

abstract class ObjectCreateAction : AnAction() {

    fun createObjectMother(project: Project, e: AnActionEvent) {
        FileChooser.chooseFile(buildFileChooserDescriptor(), project, e.getData(CommonDataKeys.VIRTUAL_FILE)) {
            generateJavaObjectMother(e, project, it)
        }
    }

    private fun buildFileChooserDescriptor(): FileChooserDescriptor {
        var descriptor = FileChooserDescriptor(false, true, false, false, false, false)
        descriptor.title = "Choose project/module's test srcDir root in which ObjectMother's package will be created"
        descriptor.description =
            "The source directory for ObjectMother's package must be set. Package would be automatically calculated"
        return descriptor
    }

    private fun generateJavaObjectMother(e: AnActionEvent, project: Project, testSrcDir: VirtualFile) {
        val builder = ObjectMotherBuilder(JavaFileCreator(project), JavaObjectMotherTemplate())
        val dir = PsiManager.getInstance(project).findDirectory(testSrcDir)
        val fileInfoExtractor = JavaFileInfo(e.getData(CommonDataKeys.PSI_FILE) as PsiJavaFile, project)
        builder.buildFor(fileInfoExtractor, dir)
        openFilesInEditor(project, builder.objectMotherFileNames)
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

class ObjectCreateOnCaretSelectedAction : ObjectCreateAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        if (isCaretInJavaFile(e)) {
            createObjectMother(project, e)
        }
    }

    private fun isCaretInJavaFile(e: AnActionEvent) =
        e.getData(CommonDataKeys.PSI_FILE)!!.language.displayName.equals("java", ignoreCase = true)

    override fun update(e: AnActionEvent) {
        val caretModel = e.getRequiredData(CommonDataKeys.EDITOR).caretModel
        e.presentation.isEnabledAndVisible = caretModel.currentCaret.hasSelection()
    }
}

class ObjectCreateFileSeletedAction : ObjectCreateAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val selectedFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        if (isAnyTreateableFileSelected(selectedFile)) {
            createObjectMother(project, e)
        }
    }

    override fun update(e: AnActionEvent) {
        val selectedFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        e.presentation.isVisible = isAnyTreateableFileSelected(selectedFile)
        e.presentation.isEnabled = e.presentation.isVisible
        e.presentation.isEnabledAndVisible = e.presentation.isEnabled
    }

    private fun isAnyTreateableFileSelected(selectedFile: VirtualFile?) =
        selectedFile != null && selectedFile.toString().endsWith(".java")
}
