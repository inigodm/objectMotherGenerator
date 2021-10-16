package inigo.objectMotherCreator.application

import inigo.objectMotherCreator.ClassInfo

interface ObjectMotherTemplate {
    fun buildObjectMotherCode(clazz: ClassInfo): String
    fun getNeededObjectMothers(): List<ClassInfo>
}
