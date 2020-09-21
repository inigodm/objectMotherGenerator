package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.vfs.VirtualFile

class ObjectCreateOnCaretSelectedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        if (isCaretInJavaFile(e)) {
            PluginLauncher().doObjectMotherCreation(e)
        }
    }

    private fun isCaretInJavaFile(e: AnActionEvent) =
        e.getData(CommonDataKeys.PSI_FILE)!!.language.displayName.equals("java", ignoreCase = true)

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = isCaretInJavaFile(e)
    }
}

class ObjectCreateFileSeletedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val selectedFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        if (isAnyTreateableFileSelected(selectedFile)) {
            PluginLauncher().doObjectMotherCreation(e)
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
