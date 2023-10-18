package inigo.objectMotherCreator.application.values.mappings

import java.util.*

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
        listOf("UUID", "java.util.UUID", "UUID.randomUUID()"),
        listOf("Timestamp", "java.sql.Timestamp, java.time.Instant","Timestamp.from(Instant.now())"),
        listOf("Instant", "java.time.Instant", "Instant.now()"),
        listOf("String", "", strings.joinToString(",")),
        listOf("Int", "", "faker.number().randomDigit()"),
        listOf("Integer", "", "faker.number().randomDigit()"),
        listOf("int", "", "faker.number().randomDigit()"),
        listOf("long", "", "faker.number().randomNumber()"),
        listOf("Long", "", "faker.number().randomNumber()"),
        listOf("Boolean", "", "faker.bool().bool()"),
        listOf("boolean", "", "faker.bool().bool()")
    )

    override fun random(type: String): String {
        return mappings.filter { it[0] == type }.firstOrNull()?.get(2)?.split(", ")?.random() ?: ""
    }

    override fun importsFor(name: String): Vector<String> {
        return Vector(mappings.filter { it[0] == name }.firstOrNull()?.get(1)?.split(", ") ?: Vector())
    }
}
