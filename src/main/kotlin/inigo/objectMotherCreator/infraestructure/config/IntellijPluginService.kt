package inigo.objectMotherCreator.infraestructure.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.*

@State(name="inigo.objectMotherCreator.infraestructure.config.PluginState", storages = [
    Storage("objectmothercreatorconfig.xml", roamingType = RoamingType.DISABLED)
])
@Service
class IntellijPluginService: PersistentStateComponent<PluginState> {
    companion object {
        val DEFAULT_STATE = PluginState("com.github.javafaker.Faker", "random")
        fun getInstance(): IntellijPluginService {
            return ServiceManager.getService(IntellijPluginService::class.java)
        }
    }

    private var pluginState = DEFAULT_STATE

    override fun getState(): PluginState {
        return pluginState;
    }

    fun getPluginState() = pluginState
    fun setPluginState(state: PluginState) {
        this.pluginState = state
    }

    override fun loadState(state: PluginState) {
        this.pluginState = state
    }

    fun reset() {
        this.pluginState = DEFAULT_STATE
    }
}
