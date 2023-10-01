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

    companion object {
        //TODO aqui esta el problema del test
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
        val mapping = defaults.mappings.filter { it.className.equals(name) }.firstOrNull()
        if (mapping != null) {
            mapping.imports.forEach{ motherClassGeneratedData.addImport("import $it") }
            return mapping.generator.random()
        }

        return when {
            name.matches("^[\\s\\S]*Map[<]{0,1}[\\S\\s]*[>]{0,1}\$".toRegex()) -> {
                randomMap(name, motherClassGeneratedData)
            }
            name.matches("^[\\s\\S]*List[<]{0,1}[\\S]*[>]{0,1}\$".toRegex()) -> {
                randomList(name, motherClassGeneratedData)
            }
            else -> {
                randomOtherTypes(classInfo, name)
            }
        }
    }

    fun createDefaultValueForTypedClass(clazz: String?, motherClassGeneratedData: MotherClassGeneratedData): String{
        if (clazz == null)  return defaults.randomString()
        return createDefaultValueFor(clazz, null, motherClassGeneratedData)
    }
}
