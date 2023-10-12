package inigo.objectMotherCreator

import com.intellij.openapi.components.ServiceManager
import inigo.objectMotherCreator.infraestructure.config.IntellijPluginService
import inigo.objectMotherCreator.infraestructure.config.PluginState
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import java.util.*

fun givenStandartStateOptions(service: IntellijPluginService) {
        mockkStatic(ServiceManager::class)
        mockkStatic(IntellijPluginService::class)

        val state = PluginState("com.github.javafaker.Faker", "random", Vector())
        every { ServiceManager.getService(IntellijPluginService::class.java) } returns service
        every { service.state } returns state
        mockkObject(IntellijPluginService)
        every { IntellijPluginService.getInstance() } returns service
        every { service.getGeneratorFor("String") } returns "faker.howIMetYourMother().highFive()"
        every { service.getImportsFor("String") } returns Vector()
        every { service.getGeneratorFor("Integer") } returns "faker.number().randomDigit()"
        every { service.getImportsFor("Integer") } returns Vector()
        every { service.getGeneratorFor("int") } returns "faker.number().randomDigit()"
        every { service.getGeneratorFor("int") } returns "faker.number().randomDigit()"
        every { service.getImportsFor("Int") } returns Vector()
        every { service.getImportsFor("Int") } returns Vector()
        every { service.getGeneratorFor("long") } returns "faker.number().randomNumber()"
        every { service.getGeneratorFor("Long") } returns "faker.number().randomNumber()"
        every { service.getImportsFor("Long") } returns Vector()
        every { service.getImportsFor("long") } returns Vector()
        every { service.getGeneratorFor("Boolean") } returns "faker.bool().bool()"
        every { service.getGeneratorFor("boolean") } returns "faker.bool().bool()"
        every { service.getImportsFor("Boolean") } returns Vector()
        every { service.getImportsFor("boolean") } returns Vector()
        every { service.getGeneratorFor("UUID") } returns "UUID.randomUUID()"
        every { service.getImportsFor("UUID") } returns Vector(listOf("java.util.UUID"))
        every { service.getGeneratorFor("Instant") } returns "Instant.now()"
        every { service.getImportsFor("Instant") } returns Vector(listOf("java.time.Instant"))
        every { service.getGeneratorFor("Timestamp") } returns "Timestamp.from(Instant.now())"
        every { service.getImportsFor("Timestamp") } returns Vector(listOf("java.sql.Timestamp", "java.time.Instant"))
        every { service.getPrefixes() } returns "random"
        every { service.getFakerClassName() } returns "com.github.javafaker.Faker"
        every { service.getMappings() } returns Vector()
    }
