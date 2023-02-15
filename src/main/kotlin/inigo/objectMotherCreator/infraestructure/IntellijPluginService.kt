package inigo.objectMotherCreator.infraestructure

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name="inigo.objectMotherCreator.infraestructure.PluginState", storages = [
    Storage("objectmothercreatorconfig.xml", roamingType = RoamingType.DISABLED)
])
@Service
class IntellijPluginService: PersistentStateComponent<PluginState> {
    private var pluginState = PluginState("com.github.javafaker.Faker", "random")

    companion object {
        fun getInstance(): IntellijPluginService {
            return ServiceManager.getService(IntellijPluginService::class.java)
        }
    }
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
}
