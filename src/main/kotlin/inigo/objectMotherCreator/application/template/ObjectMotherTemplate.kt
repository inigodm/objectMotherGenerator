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

    abstract fun createObjectMotherSourceCode(clazz: ClassInfo): String

    fun getFakerCanonicalClassname() : String {
        return IntellijPluginService.getInstance().getFakerClassName()
    }

    fun getMethodPrefix() : String = IntellijPluginService.getInstance().getPrefixes()

    fun getFakerClassName() : String = IntellijPluginService.getInstance().getFakerClassName().substringAfterLast(".")
}
