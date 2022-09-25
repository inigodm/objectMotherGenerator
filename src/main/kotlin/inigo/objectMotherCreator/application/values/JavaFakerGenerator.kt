package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.TypedClass
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.ClassCode
import inigo.objectMotherCreator.model.JavaClassCode

class JavaFakerGenerator(classCode: ClassCode= JavaClassCode()): FakerGenerator(classCode) {

    override fun randomMap(name: String): String {
        val types = TypedClass.findTypesFrom(name)
        classCode.addImport("import java.util.Map")
        return """Map.of(${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className)},
				        ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className)})"""
    }

    override fun randomList(classCanonicalName: String): String {
        val types = TypedClass.findTypesFrom(classCanonicalName)
        classCode.addImport("import java.util.List")
        val type = types.getOrNull(0)?.types?.getOrNull(0)?.className
        return """List.of(
            ${createDefaultValueForTypedClass(type)},
            ${createDefaultValueForTypedClass(type)})""".trimMargin()
    }

    private fun createDefaultValueForTypedClass(clazz: String?): String{
        if (clazz == null)  return randomString()
        return createDefaultValueFor(clazz, null)
    }

    override fun randomOtherTypes(
        classInfo: ClassInfo?,
        name: String
    ) = if (classInfo != null) {
        neededObjectMotherClasses.add(classInfo)
        "random${classInfo.clazz!!.getName()}()"
    } else {
        "new ${name}()"
    }
}
