package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.infogenerated.MotherClassGeneratedData

class KotlinFakeValuesGenerator(): FakeValuesGenerator() {
    override fun reset() {
        neededObjectMotherClasses.clear()
    }

    override fun randomList(classCanonicalName: String, motherClassGeneratedData: MotherClassGeneratedData): String {
        val types = TypedClass.findTypesFrom(classCanonicalName)
        val type = types.getOrNull(0)?.types?.getOrNull(0)?.className
        return """listOf(
            ${createDefaultValueForTypedClass(type, motherClassGeneratedData)},
            ${createDefaultValueForTypedClass(type, motherClassGeneratedData)})""".trimMargin()
    }

    override fun randomOtherTypes(classInfo: ClassInfo?, name: String): String {
        return if (classInfo != null) {
            neededObjectMotherClasses.add(classInfo)
            "random${classInfo.clazz!!.getName()}()"
        } else {
            "${name}()"
        }
    }

    override fun randomMap(name: String, motherClassGeneratedData: MotherClassGeneratedData): String {
        val types = TypedClass.findTypesFrom(name)
        return """mapOf(${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className, motherClassGeneratedData)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className, motherClassGeneratedData)},
				        ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className, motherClassGeneratedData)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className, motherClassGeneratedData)})"""
    }
}
