package inigo.objectMotherCreator.infraestructure.OMActions

import com.intellij.openapi.actionSystem.AnActionEvent
import inigo.objectMotherCreator.infraestructure.IdeaShits

class ObjectCreateOnCaretSelectedAction : OMCreationAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val ideShits = IdeaShits(e)
        allowedFileExtensions.forEach {extension ->
            if (ideShits.isCaretInFileType(extension)) {
                ideShits.openFilesInEditor(execute(ideShits, extension))
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val ideShits = IdeaShits(e)
        ideShits.setMenuItemEnabled(
            allowedFileExtensions.map(ideShits::isCaretInFileType).filter { it }.any())
    }
}
