package inigo.objectMotherCreator

import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.infraestructure.JavaDirectory
import java.io.File

class PluginLauncher {

    fun doObjectMotherCreation(ideaShits: IdeaShits) {
        val dir = ideaShits.obtainTestSourceDirectory() ?: return
        objectMotherCreation(ideaShits, dir)
    }

    private fun objectMotherCreation(ideaShits: IdeaShits, testSrcDir: JavaDirectory) {
        val creator = ObjectMotherCreator(JavaFileCreator(ideaShits), JavaObjectMotherTemplate())
        val fileInfoExtractor = JavaFileInfo(ideaShits.getCurrentJavaFile() , ideaShits)
        creator.createObjectMotherFor(fileInfoExtractor, testSrcDir)
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
