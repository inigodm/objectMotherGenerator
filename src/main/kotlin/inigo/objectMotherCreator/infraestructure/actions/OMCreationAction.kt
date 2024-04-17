package inigo.objectMotherCreator.infraestructure.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.command.CommandProcessor
import inigo.objectMotherCreator.application.ObjectMotherCreator
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.template.ObjectMotherTemplate
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.infraestructure.*
import inigo.objectMotherCreator.model.infoExtractor.om.OMDirectory

abstract class OMCreationAction : AnAction() {
    val allowedLanguages = listOf("java", "groovy", "kotlin")
    val extensions = mapOf("java" to "java", "groovy" to "groovy", "kotlin" to "kt")
    fun execute(ideShits: IdeaShits, extension: String,) : List<String> {

        val dir = ideShits.obtainTestSourceDirectory() ?: return emptyList()
        return launchObjectMotherCreation(ideShits, extension, dir)
    }

    private fun launchObjectMotherCreation(ideaShits: IdeaShits,
                                           extension: String,
                                           dir: OMDirectory
    ): MutableList<String> {
        val classInfo = ClassInfo(
            root = ideaShits.getCurrentOMFile(),
            ideaShits = ideaShits
        )
        val fakerValuesGenerator = FakeValuesGenerator.build(extension)
        val objectMotherTemplate = ObjectMotherTemplate.buildObjectMotherTemplate(extension)
        val creator = ObjectMotherCreator(IdeaJavaFileCreator(ideaShits, CommandProcessor.getInstance()),
            objectMotherTemplate)
        creator.createObjectMotherFor(classInfo, dir, extension, fakerValuesGenerator)
        return creator.objectMotherFileNames
    }
}

