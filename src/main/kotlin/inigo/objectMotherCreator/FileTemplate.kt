package inigo.objectMotherCreator

import inigo.objectMotherCreator.infraestructure.regex

interface ObjectMotherTemplate {
    fun buildObjectMotherCode(clazz: ClassInfo): String
    fun getNeededObjectMothers(): List<ClassInfo>
}

data class ClassCode(var packageCode: String = "", var imports: String = "", var code: String = "")

class JavaObjectMotherTemplate: ObjectMotherTemplate {
    val neededObjectMotherClasses = mutableListOf<ClassInfo>()
    lateinit var classCode:ClassCode

    override fun buildObjectMotherCode(clazz: ClassInfo) : String {
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

    fun buildImports(methodsInfo: List<MethodInfo>): String {
        var res = "import com.github.javafaker.Faker;\n\n"
        if (methodsInfo.isNotEmpty()) {
            var aux = methodsInfo.get(0).args.filter { it.clazzInfo?.clazz?.getName() ?: "" != "" }
                .map {
                    "import static ${it.clazzInfo?.clazz?.getQualifiedName()}ObjectMother.random${it.clazzInfo?.clazz?.getName()}"
                }
                .joinToString(separator = ";\n")
            if (aux.isNotEmpty()) {
                res += "$aux;\n\n";
            }
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
                fakerRandomString()
            }
            name == "int" -> {
                "faker.number().randomDigit()"
            }
            name == "Integer" -> {
                "faker.number().randomDigit()"
            }
            name == "long" -> {
                "faker.number().randomNumber()"
            }
            name == "Long" -> {
                "faker.number().randomNumber()"
            }
            name.matches("^[\\s\\S]*Map[<]{0,1}[\\S\\s]*[>]{0,1}\$".toRegex()) -> {
                randomMap(name);
            }
            name.matches("^[\\s\\S]*List[<]{0,1}[\\S]*[>]{0,1}\$".toRegex()) -> {
                randomList(name, classInfo);
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
        if (! classCode.imports.contains("java.util.Map")) {
            classCode.imports += "import java.util.Map;\n"
        }
        val types = getGroups(name)

        return """Map.of(${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className)},
				        ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)}, 
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(1)?.className)})"""
    }

    private fun randomList(name: String, classInfo: ClassInfo?): String {
        if (! classCode.imports.contains("java.util.List")) {
            classCode.imports += "import java.util.List;\n"
        }
        val types = getGroups(name)
        return """List.of(
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)},
            ${createDefaultValueForTypedClass(types.getOrNull(0)?.types?.getOrNull(0)?.className)})""".trimMargin()
    }

    private fun createDefaultValueForTypedClass(clazz: String?): String{
        if (clazz == null)  return fakerRandomString()
        return createDefaultValueFor(clazz, null)
    }
}

fun fakerRandomString(): String {
    return listOf("faker.ancient().god()",
    "faker.ancient().primordial()",
    "faker.ancient().titan()",
    "faker.artist().name()",
    "faker.backToTheFuture().character()",
    "faker.backToTheFuture().quote()",
    "faker.beer().name()",
    "faker.buffy().characters()",
    "faker.buffy().quotes()",
    "faker.chuckNorris().fact()",
    "faker.dragonBall().character()",
    "faker.funnyName().name()",
    "faker.friends().character()",
    "faker.friends().quote()",
    "faker.gameOfThrones().character()",
    "faker.gameOfThrones().quote()",
    "faker.hipster().word()",
    "faker.hitchhikersGuideToTheGalaxy().character()",
    "faker.hitchhikersGuideToTheGalaxy().marvinQuote()",
    "faker.hitchhikersGuideToTheGalaxy().quote()",
    "faker.lebowski().quote()",
    "faker.howIMetYourMother().character()",
    "faker.howIMetYourMother().catchPhrase()",
    "faker.howIMetYourMother().highFive()",
    "faker.howIMetYourMother().quote()",
    "faker.lordOfTheRings().location()",
    "faker.princessBride().quote()",
    "faker.princessBride().character()",
    "faker.rickAndMorty().quote()",
    "faker.rickAndMorty().character()",
    "faker.slackEmoji().activity()",
    "faker.superhero().name()",
    "faker.yoda().quote()").random()


}

fun getGroups(canonicalText: String): List<TypedClass> {
    if (canonicalText.isEmpty()) {
        return mutableListOf()
    }
    var (type, value) = regex.find(canonicalText.trim())?.destructured
        ?: return mutableListOf(TypedClass(canonicalText.trim()))
    val types = mutableListOf<String>()
    var index: Int
    while (value.isNotEmpty()) {
        if (!value.contains("<")) { // A, B, C...
            types.addAll(value.split(","))
            value = ""
        } else if (value.contains(",")) { // A<B>, ...
            if (value.indexOf(",") < value.indexOf("<")) {
                types.add(value.substringBefore(","))
                value = value.substringAfter(",")
            } else {
                index = indexOfTheFirstClosing(value)
                types.add(value.substring(0, index))
                value = value.substring(index)
                if (value.isNotEmpty()) {
                    value = value.substringAfter(",")
                }
            }
        } else {
            index = indexOfTheFirstClosing(value)
            types.add(value.substring(0, index))
            value = ""
        }
    }
    return mutableListOf(TypedClass(type.trim(), types.flatMap { getGroups(it.trim()) }))
}

private fun indexOfTheFirstClosing(canonicalText: String): Int {
    var opening = 0
    var index = 0
    var i = 0
    canonicalText.chars().forEach {
        if (index != 0) {
            return@forEach
        }
        if (it == '<'.code) {
            opening++
        } else {
            if (it == '>'.code) {
                opening--
                if (opening == 0) {
                    index = i + 1
                }
            }
        }
        i++
    }
    return index
}

data class TypedClass(var className: String, var types: List<TypedClass> = mutableListOf())
