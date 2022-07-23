package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator

interface ObjectMotherTemplate {
    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun buildObjectMotherTemplate(extension: String, fakerGenerator: FakeValuesGenerator): ObjectMotherTemplate {
            return when (extension) {
                "kotlin" -> JavaObjectMotherTemplate(fakerGenerator)
                else -> { // Note the block
                    JavaObjectMotherTemplate(fakerGenerator)
                }
            }
        }
    }
    fun createObjectMotherSourceCode(clazz: ClassInfo): String
    fun getNeededObjectMothers(): List<ClassInfo>

}