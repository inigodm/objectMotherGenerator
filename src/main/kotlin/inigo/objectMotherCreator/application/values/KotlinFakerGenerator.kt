package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.TypedClass
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.ClassCode
import inigo.objectMotherCreator.model.KotlinClassCode

class KotlinFakerGenerator(classCode: ClassCode = KotlinClassCode()): FakerGenerator(classCode) {
    override fun randomList(classCanonicalName: String): String {
        classCode.addImport("import java.util.List")
        val types = TypedClass.findTypesFrom(classCanonicalName)
        val type = types.getOrNull(0)?.types?.getOrNull(0)?.className
        return """listOf(
            ${createDefaultValueForTypedClass(type)},
            ${createDefaultValueForTypedClass(type)})""".trimMargin()
    }

    override fun randomOtherTypes(classInfo: ClassInfo?, name: String): String {
        return if (classInfo != null) {
            neededObjectMotherClasses.add(classInfo)
            "random${classInfo.clazz!!.getName()}()"
        } else {
            "${name}()"
        }
    }

    override fun randomMap(name: String): String {
        classCode.addImport("import java.util.Map")
        val types = TypedClass.findTypesFrom(name)
        return """mapOf(${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className)},
				        ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className)})"""
    }

    private fun createDefaultValueForTypedClass(clazz: String?): String{
        if (clazz == null)  return randomString()
        return createDefaultValueFor(clazz, null)
    }
}
