package inigo.objectMotherCreator

import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.infraestructure.JavaDirectory
import java.io.File

class PluginLauncher {

    fun doObjectMotherCreation(ideaShits: IdeaShits, extension: String = "java") {
        val dir = ideaShits.obtainTestSourceDirectory() ?: return
        objectMotherCreation(ideaShits, dir, extension)
    }

    private fun objectMotherCreation(ideaShits: IdeaShits, destDirectory: JavaDirectory, extension: String) {
        val creator = ObjectMotherCreator(JavaFileCreator(ideaShits), JavaObjectMotherTemplate())
        val javaFile = ClassInfo(
            root = ideaShits.getCurrentJavaFile() ,
            ideaShits = ideaShits)
        creator.createObjectMotherFor(javaFile, destDirectory, extension)
        openFilesInEditor(ideaShits, creator.objectMotherFileNames)
    }

    private fun openFilesInEditor(ideaShits: IdeaShits, createdFiles : List<String>) {
        createdFiles.forEach {
            val file = File(it)
            if (file.exists()) {
                ideaShits.openFileInNewTab(file)
            }
        }
    }
}
