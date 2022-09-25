package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.infoholders.MethodInfo
import inigo.objectMotherCreator.application.infoholders.ParametersInfo
import inigo.objectMotherCreator.application.values.FakerGenerator
import inigo.objectMotherCreator.application.values.JavaFakerGenerator
import inigo.objectMotherCreator.model.JavaClassCode


class JavaObjectMotherTemplate(var fakerGenerator: FakerGenerator = JavaFakerGenerator()): ObjectMotherTemplate {

    override fun createObjectMotherSourceCode(clazz: ClassInfo) : String {
        fakerGenerator.neededObjectMotherClasses.clear()
        fakerGenerator.classCode = JavaClassCode()
        fakerGenerator.classCode.packageCode = buildPackage(clazz.packageStr)
        fakerGenerator.classCode.addAllImports(buildImports(clazz.constructors))
        fakerGenerator.classCode.code = buildClass(clazz.clazz!!.getName().toString(), clazz.constructors)
        return fakerGenerator.classCode.toSource()
    }

    override fun getNeededObjectMothers(): List<ClassInfo> {
        return fakerGenerator.neededObjectMotherClasses
    }

    fun buildPackage(packageName: String): String {
        return "package $packageName"
    }

    fun buildImports(neededConstructors: List<MethodInfo>): List<String> {
        val result = mutableListOf<String>()
        result.add("import com.github.javafaker.Faker")
        if (neededConstructors.isNotEmpty()) {
            neededConstructors[0].args.filter { (it.clazzInfo?.clazz?.getName() ?: "") != "" }
                .forEach {
                    result.add("import static ${it.clazzInfo?.clazz?.getQualifiedName()}ObjectMother.random${it.clazzInfo?.clazz?.getName()}") }
        }
        return result
    }

    fun buildClass(className: String, constructors: List<MethodInfo>): String {
        var res = "public class ${className}ObjectMother{\n"
        if (constructors.isNotEmpty()) {
            var i = 0
            constructors.forEach { res += buildMotherConstructor(className, it, i++) };
        } else {
            res += buildMotherConstructor(className)
        }
        return "$res\n}"
    }

    private fun buildMotherConstructor(className: String, methodInfo: MethodInfo, index: Int): Any? {
        return """
    public static $className random$className${if(index > 0) index else ""}(){
        Faker faker = new Faker();
        return new $className(${buildArgumentsData(methodInfo.args)});
    }"""
    }

    private fun buildMotherConstructor(className: String): Any? {
        return """  public static $className random$className(){
        return new $className();
    }"""
    }

    private fun buildArgumentsData(params: MutableList<ParametersInfo>): String {
        return params.map { "\n" +
                "\t\t\t\t${fakerGenerator.createDefaultValueFor(it.name, it.clazzInfo)}" }.joinToString { it }
    }
}
