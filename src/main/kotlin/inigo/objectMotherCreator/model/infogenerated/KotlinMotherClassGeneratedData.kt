package inigo.objectMotherCreator.model.infogenerated

import inigo.objectMotherCreator.application.infoholders.MethodInfo
import inigo.objectMotherCreator.application.infoholders.ParametersInfo
import inigo.objectMotherCreator.application.values.KotlinFakeValuesGenerator

class KotlinMotherClassGeneratedData(packageCode: String = "", imports: MutableSet<String> = mutableSetOf(), code: String = "", val fakeValuesGenerator: KotlinFakeValuesGenerator = KotlinFakeValuesGenerator()) : MotherClassGeneratedData(packageCode, imports, code){
    override fun toSource() : String {
        val importsSource = imports.map { "import $it" }.joinToString("\n")
        return packageCode  + importsSource + "\n\n" + code
    }

    override fun addImport(import: String) {
        imports.add(import)
    }

    override fun addAllImports(impotList: List<String>) {
        imports.addAll(impotList)
    }

    override fun buildPackage(packageName: String) {
        packageCode = "package $packageName\n\n"
    }

    override fun buildImports(neededConstructors: List<MethodInfo>) {
        val result = mutableListOf<String>()
        result.add(getFakerCanonicalClassname())
        if (neededConstructors.isNotEmpty()) {
            neededConstructors[0].args.filter { (it.clazzInfo?.clazz?.getName() ?: "") != "" }
                .forEach {
                    result.add("${it.clazzInfo?.clazz?.getQualifiedName()}ObjectMother.Companion.${getMethodPrefix()}${it.clazzInfo?.clazz?.getName()}") }
        }
        imports.addAll(result)
    }

    override fun buildClass(className: String, constructors: List<MethodInfo>, motherClassGeneratedData: MotherClassGeneratedData) {
        var res = """
class ${className}ObjectMother{
    companion object {
""".trim()
        if (constructors.isNotEmpty()) {
            var i = 0
            constructors.forEach { res += buildMotherConstructor(className, it, i++, motherClassGeneratedData) }
        } else {
            res += buildMotherConstructor(className)
        }
        code = "$res\n\t}\n}"
    }

    private fun buildMotherConstructor(className: String, methodInfo: MethodInfo, index: Int, motherClassGeneratedData: MotherClassGeneratedData): Any? {
        return """
    fun ${getMethodPrefix()}$className${if(index > 0) index else ""}(): $className {
        val faker = ${getFakerClassName()}()
        return $className(${buildArgumentsData(methodInfo.args, motherClassGeneratedData)})
    }"""
    }

    private fun buildMotherConstructor(className: String): Any? {
        return """
    fun ${getMethodPrefix()}$className():  $className{
        return $className()
    }"""
    }

    private fun buildArgumentsData(params: MutableList<ParametersInfo>, motherClassGeneratedData: MotherClassGeneratedData): String {
        return params.map { "\n" +
                "\t\t\t\t${fakeValuesGenerator.createDefaultValueFor(it.name, it.clazzInfo, motherClassGeneratedData)}" }.joinToString { it }
    }
}
