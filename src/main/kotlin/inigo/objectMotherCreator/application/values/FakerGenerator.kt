package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.ClassCode

abstract class FakerGenerator(val neededObjectMotherClasses: MutableList<ClassInfo> = mutableListOf()) {

    abstract fun reset()
    abstract fun randomMap(name: String, classCode: ClassCode): String
    abstract fun randomList(classCanonicalName: String, classCode: ClassCode): String
    abstract fun randomOtherTypes(classInfo: ClassInfo?, name: String) : String
    companion object {
        fun build(type: String) : FakerGenerator {
            return if (type.toLowerCase() == "kt") {
                KotlinFakerGenerator()
            } else {
                JavaFakerGenerator()
            }
        }
    }

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

    fun createDefaultValueFor(name: String, classInfo: ClassInfo?, classCode: ClassCode): String {
        return when {
            name == "UUID" -> {
                classCode.addImport("import java.util.UUID")
                "UUID.randomUUID()"
            }
            name == "Instant" -> {
                classCode.addImport("import java.time.Instant")
                "Instant.now()"
            }
            name == "Timestamp" -> {
                classCode.addImport("import java.sql.Timestamp")
                classCode.addImport("import java.time.Instant")
                "Timestamp.from(Instant.now())"
            }
            name == "String" -> {
                randomString()
            }
            name == "int" -> {
                randomInteger()
            }
            name == "Integer" -> {
                randomInteger()
            }
            name == "long" -> {
                randomLong()
            }
            name == "Long" -> {
                randomLong()
            }
            name == "Boolean" -> {
                randomBoolean()
            }
            name == "boolean" -> {
                randomBoolean()
            }
            name.matches("^[\\s\\S]*Map[<]{0,1}[\\S\\s]*[>]{0,1}\$".toRegex()) -> {
                classCode.addImport("import java.util.Map")
                randomMap(name, classCode);
            }
            name.matches("^[\\s\\S]*List[<]{0,1}[\\S]*[>]{0,1}\$".toRegex()) -> {
                classCode.addImport("import java.util.List")
                randomList(name, classCode);
            }
            else -> {
                randomOtherTypes(classInfo, name)
            }
        }
    }

    fun createDefaultValueForTypedClass(clazz: String?, classCode: ClassCode): String{
        if (clazz == null)  return randomString()
        return createDefaultValueFor(clazz, null, classCode)
    }
}
