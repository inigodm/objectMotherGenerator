package inigo.objectMotherCreator.application.values

class DefaultMappings {

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

    var mappings = setOf(
        ClassMapping(listOf("java.util.UUID"), listOf("UUID.randomUUID()"), "UUID"),
        ClassMapping(listOf("java.time.Instant"), listOf("Instant.now()"), "Instant"),
        ClassMapping(
            listOf("java.sql.Timestamp", "java.time.Instant"),
            listOf("Timestamp.from(Instant.now())"), "Timestamp"
        ),
        ClassMapping(generator = strings, className = "String"),
        ClassMapping(generator = randomInteger(), className = "Int"),
        ClassMapping(generator = randomInteger(), className = "Integer"),
        ClassMapping(generator = randomInteger(), className = "int"),
        ClassMapping(generator = randomLong(), className = "long"),
        ClassMapping(generator = randomLong(), className = "Long"),
        ClassMapping(generator = randomBoolean(), className = "Boolean"),
        ClassMapping(generator = randomBoolean(), className = "boolean")
    )

    fun searchMappingFor(className : String) : ClassMapping? {
        return mappings.filter { it.className.equals(className) }.firstOrNull()
    }

    fun randomString(): String {
        return strings.random()
    }

    fun randomInteger(): List<String> {
        return listOf("faker.number().randomDigit()")
    }

    fun randomLong(): List<String> {
        return listOf("faker.number().randomNumber()")
    }

    fun randomBoolean(): List<String> {
        return listOf("faker.bool().bool()")
    }
}
