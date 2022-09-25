package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.values.FakerGenerator

interface ObjectMotherTemplate {
    companion object {
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
}