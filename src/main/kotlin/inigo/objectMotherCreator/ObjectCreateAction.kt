package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.vfs.VirtualFile
import inigo.objectMotherCreator.infraestructure.IdeaShits

class ObjectCreateOnCaretSelectedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val ideShits = IdeaShits(e)
        if (ideShits.isCaretInJavaFile()) {
            PluginLauncher().doObjectMotherCreation(e)
        }
    }

    override fun update(e: AnActionEvent) {
        val ideShits = IdeaShits(e)
        ideShits.setMenuItemEnabled(ideShits.isCaretInJavaFile())
    }
}

class ObjectCreateFileSeletedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val ideShits = IdeaShits(e)
        val selectedFile = ideShits.getCurrentVirtualFile()
        if (isAnyTreateableFileSelected(selectedFile)) {
            PluginLauncher().doObjectMotherCreation(e)
        }
    }

    override fun update(e: AnActionEvent) {
        val ideShits = IdeaShits(e)
        val selectedFile = ideShits.getCurrentVirtualFile()
        ideShits.setMenuItemEnabled(isAnyTreateableFileSelected(selectedFile))
    }

    private fun isAnyTreateableFileSelected(selectedFile: VirtualFile?) =
        selectedFile != null && selectedFile.toString().endsWith(".java")
}
