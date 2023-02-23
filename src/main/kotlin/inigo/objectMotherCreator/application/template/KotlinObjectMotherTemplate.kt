package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.infoholders.MethodInfo
import inigo.objectMotherCreator.application.infoholders.ParametersInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.model.infogenerated.MotherClassGeneratedData
import inigo.objectMotherCreator.model.infogenerated.KotlinMotherClassGeneratedData

class KotlinObjectMotherTemplate(var fakeValuesGenerator: FakeValuesGenerator): ObjectMotherTemplate() {

    override fun createObjectMotherSourceCode(clazz: ClassInfo) : String {
        fakeValuesGenerator.reset()
        val classCode = KotlinMotherClassGeneratedData()
        classCode.packageCode = buildPackage(clazz.packageStr)
        classCode.addAllImports(buildImports(clazz.constructors))
        classCode.code = buildClass(clazz.clazz!!.getName().toString(), clazz.constructors, classCode)
        return classCode.toSource()
    }

    fun buildPackage(packageName: String): String {
        return "package $packageName\n\n"
    }

    fun buildImports(neededConstructors: List<MethodInfo>): List<String> {
        val result = mutableListOf<String>()
        result.add("import ${getFakerCanonicalClassname()}")
        if (neededConstructors.isNotEmpty()) {
            neededConstructors[0].args.filter { (it.clazzInfo?.clazz?.getName() ?: "") != "" }
                .forEach {
                    result.add("import ${it.clazzInfo?.clazz?.getQualifiedName()}ObjectMother.Companion.${getMethodPrefix()}${it.clazzInfo?.clazz?.getName()}") }
        }
        return result
    }

    fun buildClass(className: String, constructors: List<MethodInfo>, motherClassGeneratedData: MotherClassGeneratedData): String {
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
        return "$res\n\t}\n}"
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

