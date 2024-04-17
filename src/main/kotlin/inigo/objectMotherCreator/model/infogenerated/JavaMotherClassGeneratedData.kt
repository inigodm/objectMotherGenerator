package inigo.objectMotherCreator.model.infogenerated

import inigo.objectMotherCreator.application.infoholders.MethodInfo
import inigo.objectMotherCreator.application.infoholders.ParametersInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.application.values.JavaFakeValuesGenerator

class JavaMotherClassGeneratedData(packageCode: String = "", imports: MutableSet<String> = mutableSetOf(), code: String = "", val fakeValuesGenerator: FakeValuesGenerator = JavaFakeValuesGenerator()) : MotherClassGeneratedData(packageCode, imports, code){

    override fun toSource() : String {
        var importsSource = ""
        if (imports.isNotEmpty()) {
            importsSource = imports.map { "import $it" }.joinToString(separator = ";\n") + ";"
        }
        return "$packageCode;\n\n$importsSource\n\n$code"
    }

    override fun addImport(import: String) {
        imports.add(import)
    }

    override fun addAllImports(impotList: List<String>) {
        imports.addAll(impotList)
    }


    override fun buildPackage(packageName: String) {
        packageCode = "package $packageName"
    }

    override fun buildImports(neededConstructors: List<MethodInfo>) {
        val result = mutableListOf<String>()
        result.add(getFakerCanonicalClassname())
        if (neededConstructors.isNotEmpty()) {
            neededConstructors[0].args.filter { (it.clazzInfo?.clazz?.getName() ?: "") != "" }
                .forEach {
                    result.add("static ${it.clazzInfo?.clazz?.getQualifiedName()}ObjectMother.${getMethodPrefix()}${it.clazzInfo?.clazz?.getName()}") }
        }
        imports.addAll(result)
    }

    override fun buildClass(className: String, constructors: List<MethodInfo>, motherClassGeneratedData: MotherClassGeneratedData) {
        var res = "public class ${className}ObjectMother{\n"
        if (constructors.isNotEmpty()) {
            var i = 0
            constructors.forEach { res += buildMotherConstructor(className, it, i++, motherClassGeneratedData) };
        } else {
            res += buildMotherConstructor(className)
        }
        code = "$res\n}"
    }

    private fun buildMotherConstructor(className: String, methodInfo: MethodInfo, index: Int, motherClassGeneratedData: MotherClassGeneratedData): Any? {
        return """
    public static $className ${getMethodPrefix()}$className${if(index > 0) index else ""}(){
        ${getFakerClassName()} faker = new ${getFakerClassName()}();
        return new $className(${buildArgumentsData(methodInfo.args, motherClassGeneratedData)});
    }"""
    }

    private fun buildMotherConstructor(className: String): Any? {
        return """  public static $className ${getMethodPrefix()}$className(){
        return new $className();
    }"""
    }

    private fun buildArgumentsData(params: MutableList<ParametersInfo>, motherClassGeneratedData: MotherClassGeneratedData): String {
        return params.map { "\n" +
                "\t\t\t\t${fakeValuesGenerator.createDefaultValueFor(it.name, it.clazzInfo, motherClassGeneratedData)}" }.joinToString { it }
    }
}
