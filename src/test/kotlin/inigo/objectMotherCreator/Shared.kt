package inigo.objectMotherCreator

import com.intellij.openapi.components.ServiceManager
import inigo.objectMotherCreator.infraestructure.IntellijPluginService
import inigo.objectMotherCreator.infraestructure.PluginState
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic

    fun givenStandartStateOptions() {
        mockkStatic(ServiceManager::class)mockkStatic(IntellijPluginService::class)

        val service: IntellijPluginService = mockk()
        val state = PluginState("com.github.javafaker.Faker", "random")
        every { ServiceManager.getService(IntellijPluginService::class.java) } returns service
        every { service.state } returns state
    }
