package inigo.objectMotherCreator.application.values

class FakerGenerator: FakeValuesGenerator {
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
}
