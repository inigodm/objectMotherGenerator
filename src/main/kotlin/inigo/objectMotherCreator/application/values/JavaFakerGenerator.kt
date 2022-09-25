package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.TypedClass
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.ClassCode

class JavaFakerGenerator(): FakerGenerator() {
    override fun reset() {
        neededObjectMotherClasses.clear()
    }

    override fun randomMap(name: String, classCode: ClassCode): String {
        val types = TypedClass.findTypesFrom(name)
        return """Map.of(${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className, classCode)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className, classCode)},
				        ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className, classCode)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className, classCode)})"""
    }

    override fun randomList(classCanonicalName: String, classCode: ClassCode): String {
        val types = TypedClass.findTypesFrom(classCanonicalName)
        val type = types.getOrNull(0)?.types?.getOrNull(0)?.className
        return """List.of(
            ${createDefaultValueForTypedClass(type, classCode)},
            ${createDefaultValueForTypedClass(type, classCode)})""".trimMargin()
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
