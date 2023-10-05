package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.infraestructure.config.IntellijPluginService

abstract class ObjectMotherTemplate {
    companion object {
        fun buildObjectMotherTemplate(extension: String, fakeValuesGenerator: FakeValuesGenerator): ObjectMotherTemplate {
            return when (extension) {
                "kt" -> KotlinObjectMotherTemplate(fakeValuesGenerator)
                else -> { // Note the block
                    JavaObjectMotherTemplate(fakeValuesGenerator)
                }
            }
        }
    }

    val ideaState = IntellijPluginService.getInstance().state;
    abstract fun createObjectMotherSourceCode(clazz: ClassInfo): String

    fun getFakerCanonicalClassname() : String {
        return ideaState.fakerClassname
    }

    fun getMethodPrefix() : String = ideaState.prefixes

    fun getFakerClassName() : String = ideaState.fakerClassname.substringAfterLast(".")
}
