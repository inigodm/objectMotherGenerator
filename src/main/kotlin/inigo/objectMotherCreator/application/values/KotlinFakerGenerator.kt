package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.TypedClass
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.ClassCode
import inigo.objectMotherCreator.model.KotlinClassCode

class KotlinFakerGenerator(): FakerGenerator() {
    override fun reset() {
        neededObjectMotherClasses.clear()
    }

    override fun randomList(classCanonicalName: String, classCode: ClassCode): String {
        val types = TypedClass.findTypesFrom(classCanonicalName)
        val type = types.getOrNull(0)?.types?.getOrNull(0)?.className
        return """listOf(
            ${createDefaultValueForTypedClass(type, classCode)},
            ${createDefaultValueForTypedClass(type, classCode)})""".trimMargin()
    }

    override fun randomOtherTypes(classInfo: ClassInfo?, name: String): String {
        return if (classInfo != null) {
            neededObjectMotherClasses.add(classInfo)
            "random${classInfo.clazz!!.getName()}()"
        } else {
            "${name}()"
        }
    }

    override fun randomMap(name: String, classCode: ClassCode): String {
        val types = TypedClass.findTypesFrom(name)
        return """mapOf(${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className, classCode)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className, classCode)},
				        ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className, classCode)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className, classCode)})"""
    }
}
