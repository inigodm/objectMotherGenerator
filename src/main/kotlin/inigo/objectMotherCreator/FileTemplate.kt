package inigo.objectMotherCreator


interface ObjectMotherTemplate {
    fun buildObjectMotherCode(clazz: ClassInfo): String
    fun getNeededObjectMothers(): List<ClassInfo>
}

class JavaObjectMotherTemplate: ObjectMotherTemplate {
    val neededObjectMotherClasses = mutableListOf<ClassInfo>()

    override fun buildObjectMotherCode(clazz: ClassInfo): String {
        neededObjectMotherClasses.clear()
        var res = buildPackage(clazz.packageStr)
        res += buildImports(clazz.constructors)
        return res + buildClass(clazz.clazz!!.getName().toString(), clazz.constructors)
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
        return params.map { createDefaultValueFor(it) }.joinToString { it }
    }

    private fun createDefaultValueFor(param: ParametersInfo): String {
        return when (param.name) {
            "String" -> {
                "\n\t\t\t\t${fakerRandomString()}"
            }
            "int" -> {
                "\n\t\t\t\tfaker.number().randomDigit()"
            }
            "Integer" -> {
                "\n\t\t\t\tfaker.number().randomDigit()"
            }
            "long" -> {
                "\n\t\t\t\tfaker.number().randomNumber()"
            }
            "Long" -> {
                "\n\t\t\t\tfaker.number().randomNumber()"
            }
            else -> {
                val clazzInfo = param.clazzInfo
                if (clazzInfo != null) {
                    neededObjectMotherClasses.add(clazzInfo)
                    "\n\t\t\t\trandom${clazzInfo.clazz!!.getName()}()"
                } else {
                    "new ${param.name}()"
                }
            }
        }
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
