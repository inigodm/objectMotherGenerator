package inigo.objectMotherCreator.infraestructure.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.model.infoExtractor.OMVirtualFile

@kotlin.ExperimentalStdlibApi
class ObjectCreateFileSeletedAction : OMCreationAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: return
        val ideShits = IdeaShits(e)
        println("---------------------------Da fuck ${ideShits.getCurrentOMVirtualFile()}")
        val selectedFile = ideShits.getCurrentOMVirtualFile()
        allowedFileExtensions.forEach { extension ->
            if (isFileSelectedOfType(selectedFile, extension)) {
                ideShits.openFilesInEditor(execute(ideShits, extension))
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val ideShits = IdeaShits(e)
        val selectedFile = ideShits.getCurrentOMVirtualFile()
        ideShits.setMenuItemEnabled(
            allowedFileExtensions.map{ isFileSelectedOfType(selectedFile, it) }.filter { it }.any())
    }

    private fun isFileSelectedOfType(selectedFile: OMVirtualFile?, extension: String) =
        selectedFile != null &&
                (selectedFile.toString().endsWith(".$extension"))

}
