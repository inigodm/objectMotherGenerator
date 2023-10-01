package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.infogenerated.MotherClassGeneratedData

abstract class FakeValuesGenerator(val neededObjectMotherClasses: MutableList<ClassInfo> = mutableListOf(),
    val defaults: DefaultMappings = DefaultMappings()
) {
    abstract fun reset()
    abstract fun randomMap(name: String, motherClassGeneratedData: MotherClassGeneratedData): String
    abstract fun randomList(classCanonicalName: String, motherClassGeneratedData: MotherClassGeneratedData): String
    abstract fun randomOtherTypes(classInfo: ClassInfo?, name: String) : String

    val MAP_PATTERN = "^[\\s\\S]*Map[<]{0,1}[\\S\\s]*[>]{0,1}\$".toRegex()
    val LIST_PATTERM = "^[\\s\\S]*List[<]{0,1}[\\S]*[>]{0,1}\$".toRegex()

    companion object {
        @JvmStatic
        fun build(type: String): FakeValuesGenerator {
            return if (type.toLowerCase() == "kt") {
                KotlinFakeValuesGenerator()
            } else {
                JavaFakeValuesGenerator()
            }
        }
    }
    fun createDefaultValueFor(name: String, classInfo: ClassInfo?, motherClassGeneratedData: MotherClassGeneratedData): String {
        val mapping = defaults.searchMappingFor(name)
        if (mapping != null) {
            motherClassGeneratedData.addAllImports(mapping.imports)
            return mapping.randomValue()
        }
        //TODO change this: should be a normal mapping
        return when {
            name.matches(MAP_PATTERN) -> randomMap(name, motherClassGeneratedData)
            name.matches(LIST_PATTERM) -> randomList(name, motherClassGeneratedData)
            else -> randomOtherTypes(classInfo, name)
        }
    }

    fun createDefaultValueForTypedClass(clazz: String?, motherClassGeneratedData: MotherClassGeneratedData): String{
        if (clazz == null)  return defaults.randomString()
        return createDefaultValueFor(clazz, null, motherClassGeneratedData)
    }
}
