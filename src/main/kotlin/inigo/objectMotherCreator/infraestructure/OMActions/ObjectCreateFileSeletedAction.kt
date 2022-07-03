package inigo.objectMotherCreator.infraestructure.OMActions

import com.intellij.openapi.actionSystem.AnActionEvent
import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.model.infoExtractor.OMVirtualFile

class ObjectCreateFileSeletedAction : OMCreationAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val ideShits = IdeaShits(e)
        val selectedFile = ideShits.getCurrentOMFile()
        allowedFileExtensions.forEach { extension ->
            if (isFileSelectedOfType(selectedFile, extension)) {
                ideShits.openFilesInEditor(execute(ideShits, extension))
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val ideShits = IdeaShits(e)
        val selectedFile = ideShits.getCurrentOMFile()
        ideShits.setMenuItemEnabled(
            isFileSelectedOfType(selectedFile, "java")
                    ||  isFileSelectedOfType(selectedFile, "groovy"))
    }

    private fun isFileSelectedOfType(selectedFile: OMVirtualFile?, extension: String) =
        selectedFile != null &&
                (selectedFile.toString().endsWith(".$extension"))

}
