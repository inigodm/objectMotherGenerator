package inigo.objectMotherCreator.application.values

class DefaultMappings: Mappings {

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
        ClassMapping(generator = listOf("faker.number().randomDigit()"), className = "Int"),
        ClassMapping(generator = listOf("faker.number().randomDigit()"), className = "Integer"),
        ClassMapping(generator = listOf("faker.number().randomDigit()"), className = "int"),
        ClassMapping(generator = listOf("faker.number().randomNumber()"), className = "long"),
        ClassMapping(generator = listOf("faker.number().randomNumber()"), className = "Long"),
        ClassMapping(generator = listOf("faker.bool().bool()"), className = "Boolean"),
        ClassMapping(generator = listOf("faker.bool().bool()"), className = "boolean")
    )

    fun searchMappingFor(className : String) : ClassMapping? {
        return mappings.filter { it.className.equals(className) }.firstOrNull()
    }

    override fun random(type: String): String {
        return mappings.filter { it.className == type }.first().generator.random()
    }
}
