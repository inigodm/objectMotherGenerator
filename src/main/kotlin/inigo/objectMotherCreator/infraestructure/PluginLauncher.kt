package inigo.objectMotherCreator.infraestructure

import inigo.objectMotherCreator.ClassInfo
import inigo.objectMotherCreator.JavaFileCreator
import inigo.objectMotherCreator.application.FakeValuesGenerator
import inigo.objectMotherCreator.application.JavaObjectMotherTemplate
import java.io.File

class PluginLauncher {

    fun doObjectMotherCreation(ideaShits: IdeaShits, extension: String, fakeValuesGenerator: FakeValuesGenerator) {
        val dir = ideaShits.obtainTestSourceDirectory() ?: return
        objectMotherCreation(ideaShits, dir, extension, fakeValuesGenerator)
    }

    private fun objectMotherCreation(
        ideaShits: IdeaShits,
        destDirectory: JavaDirectory,
        extension: String,
        fakeValuesGenerator: FakeValuesGenerator
    ) {
        val creator = ObjectMotherCreator(JavaFileCreator(ideaShits), JavaObjectMotherTemplate(fakeValuesGenerator))
        val javaFile = ClassInfo(
            root = ideaShits.getCurrentJavaFile(),
            ideaShits = ideaShits
        )
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
