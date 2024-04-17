package inigo.objectMotherCreator.model.infogenerated

import inigo.objectMotherCreator.application.infoholders.MethodInfo
import inigo.objectMotherCreator.infraestructure.config.IntellijPluginService

abstract class MotherClassGeneratedData(var packageCode: String = "", var imports: MutableSet<String> = mutableSetOf(), var code: String = "") {
    abstract fun toSource(): String
    abstract fun addImport(import: String)
    abstract fun addAllImports(impotList: List<String>)
    abstract fun buildPackage(packageName: String)
    abstract fun buildImports(neededConstructors: List<MethodInfo>)

    abstract fun buildClass(className: String, constructors: List<MethodInfo>, motherClassGeneratedData: MotherClassGeneratedData)
    fun getFakerCanonicalClassname() : String {
        return IntellijPluginService.getInstance().getFakerClassName()
    }

    fun getMethodPrefix() : String = IntellijPluginService.getInstance().getPrefixes()

    fun getFakerClassName() : String = IntellijPluginService.getInstance().getFakerClassName().substringAfterLast(".")
}
