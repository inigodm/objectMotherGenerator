package inigo.objectMotherCreator.infraestructure.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import inigo.objectMotherCreator.infraestructure.IdeaShits

class ObjectCreateOnCaretSelectedAction : OMCreationAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val ideShits = IdeaShits(e)
        allowedLanguages.forEach { extension ->
            if (ideShits.isCaretInFileType(extension)) {
                ideShits.openFilesInEditor(execute(ideShits, extensions[extension]!!))
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val ideShits = IdeaShits(e)
        ideShits.setMenuItemEnabled(
            allowedLanguages.map(ideShits::isCaretInFileType).filter { it }.any())
    }
}
