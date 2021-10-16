package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import inigo.objectMotherCreator.application.FakeValuesGenerator
import inigo.objectMotherCreator.application.FakerGenerator
import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.infraestructure.OMFile
import inigo.objectMotherCreator.infraestructure.PluginLauncher

class ObjectCreateOnCaretSelectedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val ideShits = IdeaShits(e)
        if (ideShits.isCaretInFileType("java")) {
            PluginLauncher().doObjectMotherCreation(ideShits, "java", FakerGenerator())
        } else if (ideShits.isCaretInFileType("groovy")) {
            PluginLauncher().doObjectMotherCreation(ideShits, "groovy", FakerGenerator())
        }
    }

    override fun update(e: AnActionEvent) {
        val ideShits = IdeaShits(e)
        ideShits.setMenuItemEnabled(
            ideShits.isCaretInFileType("java") ||
                    ideShits.isCaretInFileType("groovy"))
    }
}

class ObjectCreateFileSeletedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val ideShits = IdeaShits(e)
        val selectedFile = ideShits.getCurrentVirtualFile()
        if (isFileSelectedOfType(selectedFile, "java")) {
            PluginLauncher().doObjectMotherCreation(ideShits, "java", FakerGenerator())
        } else if (isFileSelectedOfType(selectedFile, "groovy")) {
            PluginLauncher().doObjectMotherCreation(ideShits, "groovy", FakerGenerator())
        }
    }

    override fun update(e: AnActionEvent) {
        val ideShits = IdeaShits(e)
        val selectedFile = ideShits.getCurrentVirtualFile()
        ideShits.setMenuItemEnabled(
            isFileSelectedOfType(selectedFile, "java")
                    ||  isFileSelectedOfType(selectedFile, "groovy"))
    }

    private fun isFileSelectedOfType(selectedFile: OMFile?, extension: String) =
        selectedFile != null &&
                (selectedFile.toString().endsWith(".$extension"))
}
