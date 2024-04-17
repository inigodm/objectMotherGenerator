package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.application.values.JavaFakeValuesGenerator
import inigo.objectMotherCreator.application.values.KotlinFakeValuesGenerator
import inigo.objectMotherCreator.infraestructure.config.IntellijPluginService
import inigo.objectMotherCreator.model.infogenerated.JavaMotherClassGeneratedData
import inigo.objectMotherCreator.model.infogenerated.KotlinMotherClassGeneratedData
import inigo.objectMotherCreator.model.infogenerated.MotherClassGeneratedData

class ObjectMotherTemplate(val classCode: MotherClassGeneratedData) {
    companion object {
        fun buildObjectMotherTemplate(extension: String, kotlinData: KotlinMotherClassGeneratedData = KotlinMotherClassGeneratedData(), javaData: JavaMotherClassGeneratedData = JavaMotherClassGeneratedData()): ObjectMotherTemplate {
            return when (extension) {
                "kt" -> ObjectMotherTemplate(kotlinData)
                else -> { // Note the block
                    ObjectMotherTemplate(javaData)
                }
            }
        }
    }

    fun createObjectMotherSourceCode(clazz: ClassInfo) : String {
        classCode.buildPackage(clazz.packageStr)
        classCode.buildImports(clazz.constructors)
        classCode.buildClass(clazz.clazz!!.getName().toString(), clazz.constructors, classCode)
        return classCode.toSource()
    }
}
