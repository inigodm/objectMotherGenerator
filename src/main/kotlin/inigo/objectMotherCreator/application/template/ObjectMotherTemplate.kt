package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.application.values.FakerGenerator
import inigo.objectMotherCreator.application.values.JavaFakerGenerator
import inigo.objectMotherCreator.application.values.KotlinFakerGenerator

interface ObjectMotherTemplate {
    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun buildObjectMotherTemplate(extension: String, fakerGenerator: FakerGenerator): ObjectMotherTemplate {
            return when (extension) {
                "kt" -> KotlinObjectMotherTemplate(fakerGenerator)
                else -> { // Note the block
                    JavaObjectMotherTemplate(fakerGenerator)
                }
            }
        }
    }
    fun createObjectMotherSourceCode(clazz: ClassInfo): String
    fun getNeededObjectMothers(): List<ClassInfo>

}