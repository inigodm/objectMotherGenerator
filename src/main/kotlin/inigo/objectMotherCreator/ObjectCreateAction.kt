package inigo.objectMotherCreator

import com.intellij.ide.util.TreeFileChooser
import com.intellij.ide.util.TreeFileChooserDialog
import com.intellij.ide.util.TreeFileChooserFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiJavaFile
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class ObjectCreateAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val caretModel = e.getRequiredData(CommonDataKeys.EDITOR).caretModel
        if (isCaretInJavaFile(caretModel, e)) {
            createObjectMother(project, e)
        }
    }

    private fun isCaretInJavaFile(caretModel: CaretModel, e: AnActionEvent) =
        (caretModel.currentCaret.hasSelection() && e.getData(CommonDataKeys.PSI_FILE)!!.language.displayName.equals("java", ignoreCase = true))

    override fun update(e: AnActionEvent) {
        val caretModel = e.getRequiredData(CommonDataKeys.EDITOR).caretModel
        e.presentation.isEnabledAndVisible = caretModel.currentCaret.hasSelection()
    }
}

class ObjectCreateFileSeletedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val selectedFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        if (isAnyJavaFileSelected(selectedFile)) {
            createObjectMother(project, e)
        }
    }

    override fun update(e: AnActionEvent) {
        val selectedFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        e.presentation.isVisible = isAnyJavaFileSelected(selectedFile)
        e.presentation.isEnabled = e.presentation.isVisible
        e.presentation.isEnabledAndVisible = e.presentation.isEnabled
    }

    private fun isAnyJavaFileSelected(selectedFile: VirtualFile?) =
        selectedFile != null && selectedFile.toString().endsWith(".java")
}

private fun createObjectMother(
    project: Project,
    e: AnActionEvent
) {
    val creator = ObjectMotherGenerator(e.getData(CommonDataKeys.PSI_FILE) as PsiJavaFile)
    var descriptor = FileChooserDescriptor(false, true, false, false, false, false)
    descriptor.title = "Choose project/module's test sourceDir in which ObjectMother's package will exist"
    descriptor.description = "The source directory for ObjectMother's package must be set. Package name is automatically calculated"
    FileChooser.chooseFile(
        descriptor,
        project,
        e.getData(CommonDataKeys.VIRTUAL_FILE),
        { creator.generateObjectMother(project, it) })
}
