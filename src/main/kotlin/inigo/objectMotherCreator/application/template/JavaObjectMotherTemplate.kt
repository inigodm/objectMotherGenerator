package inigo.objectMotherCreator.application.template

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.infoholders.MethodInfo
import inigo.objectMotherCreator.application.infoholders.ParametersInfo
import inigo.objectMotherCreator.application.TypedClass
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.model.ClassCode


class JavaObjectMotherTemplate(var fakerGenerator: FakeValuesGenerator): ObjectMotherTemplate {
    val neededObjectMotherClasses = mutableListOf<ClassInfo>()
    lateinit var classCode: ClassCode

    override fun createObjectMotherSourceCode(clazz: ClassInfo) : String {
        classCode = ClassCode()
        neededObjectMotherClasses.clear()
        classCode.packageCode = buildPackage(clazz.packageStr)
        classCode.imports = buildImports(clazz.constructors)
        classCode.code = buildClass(clazz.clazz!!.getName().toString(), clazz.constructors)
        return classCode.packageCode + classCode.imports + classCode.code
    }

    override fun getNeededObjectMothers(): List<ClassInfo> {
        return neededObjectMotherClasses
    }

    fun buildPackage(packageName: String): String {
        return "package $packageName;\n\n"
    }

    fun buildImports(neededConstructors: List<MethodInfo>): String {
        var res = "import com.github.javafaker.Faker;\n\n"
        if (neededConstructors.isNotEmpty()) {
            res += neededConstructors[0].args.filter { (it.clazzInfo?.clazz?.getName() ?: "") != "" }
                .map { "import static ${it.clazzInfo?.clazz?.getQualifiedName()}ObjectMother.random${it.clazzInfo?.clazz?.getName()}" }
                .joinToString(separator = ";\n")
                .ifNotEmpty { "$it;\n\n" }
        }
        return res
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
                "\t\t\t\t${createDefaultValueFor(it.name, it.clazzInfo)}" }.joinToString { it }
    }

    private fun createDefaultValueFor(name: String, classInfo: ClassInfo?): String {
        return when {
            name == "String" -> {
                fakerGenerator.randomString()
            }
            name == "int" -> {
                fakerGenerator.randomInteger()
            }
            name == "Integer" -> {
                fakerGenerator.randomInteger()
            }
            name == "long" -> {
                fakerGenerator.randomLong()
            }
            name == "Long" -> {
                fakerGenerator.randomLong()
            }
            name.matches("^[\\s\\S]*Map[<]{0,1}[\\S\\s]*[>]{0,1}\$".toRegex()) -> {
                randomMap(name);
            }
            name.matches("^[\\s\\S]*List[<]{0,1}[\\S]*[>]{0,1}\$".toRegex()) -> {
                randomList(name);
            }
            else -> {
                if (classInfo != null) {
                    neededObjectMotherClasses.add(classInfo)
                    "random${classInfo.clazz!!.getName()}()"
                } else {
                    "new ${name}()"
                }
            }
        }
    }

    private fun randomMap(name: String): String {
        addImportIfNeeded("java.util.Map")
        val types = TypedClass.findTypesFrom(name)
        return """Map.of(${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className)},
				        ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className)})"""
    }

    private fun randomList(classCanonicalName: String): String {
        addImportIfNeeded("java.util.List")
        val types = TypedClass.findTypesFrom(classCanonicalName)
        val type = types.getOrNull(0)?.types?.getOrNull(0)?.className
        return """List.of(
            ${createDefaultValueForTypedClass(type)},
            ${createDefaultValueForTypedClass(type)})""".trimMargin()
    }

    private fun addImportIfNeeded(canonicalClassToImport: String) {
        if (! classCode.imports.contains(canonicalClassToImport)) {
            classCode.imports += "import $canonicalClassToImport;\n"
        }
    }

    private fun createDefaultValueForTypedClass(clazz: String?): String{
        if (clazz == null)  return fakerGenerator.randomString()
        return createDefaultValueFor(clazz, null)
    }

    fun String.ifNotEmpty(doThis: (String) -> String) : String {
        return if (this.isEmpty()) { "" } else { doThis(this) }
    }
}

