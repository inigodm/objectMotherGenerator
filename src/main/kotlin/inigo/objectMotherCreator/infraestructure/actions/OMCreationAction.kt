package inigo.objectMotherCreator.infraestructure.actions

import com.intellij.openapi.actionSystem.AnAction
import inigo.objectMotherCreator.application.ObjectMotherCreator
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.application.values.FakerGenerator
import inigo.objectMotherCreator.application.template.JavaObjectMotherTemplate
import inigo.objectMotherCreator.infraestructure.*
import inigo.objectMotherCreator.model.infoExtractor.OMDirectory

abstract class OMCreationAction : AnAction() {
    val allowedLanguages = listOf("java", "groovy", "kotlin")
    val extensions = mapOf("java" to "java", "groovy" to "groovy", "kotlin" to "kt")
    fun execute(ideShits: IdeaShits, extension: String,
                fakeValuesGenerator: FakeValuesGenerator = FakerGenerator()) : List<String> {
        val dir = ideShits.obtainTestSourceDirectory() ?: return emptyList()
        return launchObjectMotherCreation(ideShits, extension, dir, fakeValuesGenerator)
    }

    private fun launchObjectMotherCreation(ideaShits: IdeaShits,
                                           extension: String,
                                           dir: OMDirectory,
                                           fakeValuesGenerator: FakeValuesGenerator
    ): MutableList<String> {
        val classInfo = ClassInfo(
            root = ideaShits.getCurrentOMFile(),
            ideaShits = ideaShits
        )
        val creator = ObjectMotherCreator(IdeaJavaFileCreator(ideaShits), JavaObjectMotherTemplate(fakeValuesGenerator))
        creator.createObjectMotherFor(classInfo, dir, extension)
        return creator.objectMotherFileNames
    }
}

