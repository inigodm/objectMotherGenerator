package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator

interface ObjectMotherTemplate {
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
    fun createObjectMotherSourceCode(clazz: ClassInfo): String
}