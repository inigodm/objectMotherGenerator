package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.ClassCode
import inigo.objectMotherCreator.model.JavaClassCode

abstract class FakerGenerator(var classCode : ClassCode = JavaClassCode(),
                              val neededObjectMotherClasses: MutableList<ClassInfo> = mutableListOf()): FakeValuesGenerator {
    companion object {
        fun build(type: String) : FakerGenerator {
            return if (type.toLowerCase() == "kt") {
                KotlinFakerGenerator()
            } else {
                JavaFakerGenerator()
            }
        }
    }

    val importedClasses = mutableSetOf<String>()

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

    override fun randomString(): String {
        return strings.random()
    }

    override fun randomInteger() : String {
        return "faker.number().randomDigit()"
    }

    override fun randomLong() : String {
        return "faker.number().randomNumber()"
    }

    override fun randomBoolean(): String {
        return "faker.bool().bool()"
    }

    override fun createDefaultValueFor(name: String, classInfo: ClassInfo?): String {
        return when {
            name == "UUID" -> {
                if (!importedClasses.contains(name)) {
                    classCode.addImport("import java.util.UUID")
                    importedClasses.add(name)
                }
                "UUID.randomUUID()"
            }
            name == "Instant" -> {
                if (!importedClasses.contains(name)) {
                    classCode.addImport("import java.time.Instant")
                    importedClasses.add(name)
                }
                "Instant.now()"
            }
            name == "Timestamp" -> {
                if (!importedClasses.contains(name)) {
                    classCode.addImport("import java.sql.Timestamp")
                    classCode.addImport("import java.time.Instant")
                    importedClasses.add(name)
                }
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
                randomMap(name);
            }
            name.matches("^[\\s\\S]*List[<]{0,1}[\\S]*[>]{0,1}\$".toRegex()) -> {
                randomList(name);
            }
            else -> {
                randomOtherTypes(classInfo, name)
            }
        }
    }
}
