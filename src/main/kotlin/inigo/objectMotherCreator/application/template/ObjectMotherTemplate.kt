package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo

interface ObjectMotherTemplate {
    fun createObjectMotherSourceCode(clazz: ClassInfo): String
    fun getNeededObjectMothers(): List<ClassInfo>
}
