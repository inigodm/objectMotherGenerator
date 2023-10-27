package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.infoholders.MethodInfo
import inigo.objectMotherCreator.application.infoholders.ParametersInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.application.values.JavaFakeValuesGenerator
import inigo.objectMotherCreator.model.infogenerated.MotherClassGeneratedData
import inigo.objectMotherCreator.model.infogenerated.JavaMotherClassGeneratedData


class JavaObjectMotherTemplate(var fakeValuesGenerator: FakeValuesGenerator = JavaFakeValuesGenerator()):
    ObjectMotherTemplate() {

    override fun createObjectMotherSourceCode(clazz: ClassInfo) : String {
        fakeValuesGenerator.reset()
        val classCode = JavaMotherClassGeneratedData()
        classCode.packageCode = buildPackage(clazz.packageStr)
        classCode.addAllImports(buildImports(clazz.constructors))
        classCode.code = buildClass(clazz.clazz!!.getName().toString(), clazz.constructors, classCode)
        return classCode.toSource()
    }

    fun buildPackage(packageName: String): String {
        return "package $packageName"
    }

    fun buildImports(neededConstructors: List<MethodInfo>): List<String> {
        val result = mutableListOf<String>()
        result.add(getFakerCanonicalClassname())
        if (neededConstructors.isNotEmpty()) {
            neededConstructors[0].args.filter { (it.clazzInfo?.clazz?.getName() ?: "") != "" }
                .forEach {
                    result.add("static ${it.clazzInfo?.clazz?.getQualifiedName()}ObjectMother.${getMethodPrefix()}${it.clazzInfo?.clazz?.getName()}") }
        }
        return result
    }

    fun buildClass(className: String, constructors: List<MethodInfo>, motherClassGeneratedData: MotherClassGeneratedData): String {
        var res = "public class ${className}ObjectMother{\n"
        if (constructors.isNotEmpty()) {
            var i = 0
            constructors.forEach { res += buildMotherConstructor(className, it, i++, motherClassGeneratedData) };
        } else {
            res += buildMotherConstructor(className)
        }
        return "$res\n}"
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
