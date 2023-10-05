package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.infraestructure.config.IntellijPluginService
import inigo.objectMotherCreator.model.infogenerated.MotherClassGeneratedData

class JavaFakeValuesGenerator(defaults: DefaultMappings = DefaultMappings()) : FakeValuesGenerator(defaults = defaults) {
    override fun reset() {
        neededObjectMotherClasses.clear()
    }

    override fun randomMap(name: String, motherClassGeneratedData: MotherClassGeneratedData): String {
        motherClassGeneratedData.addImport("java.util.Map")
        val types = TypedClass.findTypesFrom(name)
        return """Map.of(${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className, motherClassGeneratedData)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className, motherClassGeneratedData)},
				        ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className, motherClassGeneratedData)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className, motherClassGeneratedData)})"""
    }

    override fun randomList(classCanonicalName: String, motherClassGeneratedData: MotherClassGeneratedData): String {
        motherClassGeneratedData.addImport("java.util.List")
        val types = TypedClass.findTypesFrom(classCanonicalName)
        val type = types.getOrNull(0)?.types?.getOrNull(0)?.className
        return """List.of(
            ${createDefaultValueForTypedClass(type, motherClassGeneratedData)},
            ${createDefaultValueForTypedClass(type, motherClassGeneratedData)})""".trimMargin()
    }

    override fun randomOtherTypes(
        classInfo: ClassInfo?,
        name: String
    ) = if (classInfo != null) {
        neededObjectMotherClasses.add(classInfo)
        "${IntellijPluginService.getInstance().state.prefixes}${classInfo.clazz!!.getName()}()"
    } else {
        "new ${name}()"
    }
}
