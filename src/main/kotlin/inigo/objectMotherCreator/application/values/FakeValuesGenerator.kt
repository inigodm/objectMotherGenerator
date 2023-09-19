package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.infogenerated.MotherClassGeneratedData

abstract class FakeValuesGenerator(val neededObjectMotherClasses: MutableList<ClassInfo> = mutableListOf()) {

    abstract fun reset()
    abstract fun randomMap(name: String, motherClassGeneratedData: MotherClassGeneratedData): String
    abstract fun randomList(classCanonicalName: String, motherClassGeneratedData: MotherClassGeneratedData): String
    abstract fun randomOtherTypes(classInfo: ClassInfo?, name: String) : String

    companion object {
        var strings = listOf(
            "faker.ancient().god()",
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
            "faker.yoda().quote()"
        )

        fun build(type: String) : FakeValuesGenerator {
            return if (type.toLowerCase() == "kt") {
                KotlinFakeValuesGenerator()
            } else {
                JavaFakeValuesGenerator()
            }
        }

        val mappings = setOf(
            ClassMapping(listOf("java.util.UUID"),"UUID.randomUUID()", "UUID"),
            ClassMapping(listOf("java.time.Instant"),"Instant.now()", "Instant"),
            ClassMapping(listOf("java.sql.Timestamp","java.time.Instant"),
                "Timestamp.from(Instant.now())","Timestamp"),
            ClassMapping(generator = randomString(), className = "String"),
            ClassMapping(generator = randomInteger(), className = "Int"),
            ClassMapping(generator = randomInteger(), className = "Integer"),
            ClassMapping(generator = randomInteger(), className = "int"),
            ClassMapping(generator = randomLong(), className = "long"),
            ClassMapping(generator = randomLong(), className = "Long"),
            ClassMapping(generator = randomBoolean(), className = "Boolean"),
            ClassMapping(generator = randomBoolean(), className = "boolean")
        )

        fun randomString(): String {
            return strings.random()
        }

        fun randomInteger() : String {
            return "faker.number().randomDigit()"
        }

        fun randomLong() : String {
            return "faker.number().randomNumber()"
        }

        fun randomBoolean(): String {
            return "faker.bool().bool()"
        }
    }



    fun createDefaultValueFor(name: String, classInfo: ClassInfo?, motherClassGeneratedData: MotherClassGeneratedData): String {
        val mapping = mappings.filter { it.className.equals(name) }.firstOrNull()
        if (mapping != null) {
            mapping.imports.forEach{ motherClassGeneratedData.addImport("import $it") }
            return mapping.generator
        }

        return when {
            name.matches("^[\\s\\S]*Map[<]{0,1}[\\S\\s]*[>]{0,1}\$".toRegex()) -> {
                randomMap(name, motherClassGeneratedData)
            }
            name.matches("^[\\s\\S]*List[<]{0,1}[\\S]*[>]{0,1}\$".toRegex()) -> {
                randomList(name, motherClassGeneratedData)
            }
            else -> {
                randomOtherTypes(classInfo, name)
            }
        }
    }

    fun createDefaultValueForTypedClass(clazz: String?, motherClassGeneratedData: MotherClassGeneratedData): String{
        if (clazz == null)  return randomString()
        return createDefaultValueFor(clazz, null, motherClassGeneratedData)
    }
}
