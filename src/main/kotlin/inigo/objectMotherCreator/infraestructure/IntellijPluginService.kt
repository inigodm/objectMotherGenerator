package inigo.objectMotherCreator.infraestructure

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.*

@State(name="objectmothercreatorconfig", storages = [
    Storage("objectmothercreatorconfig.xml", roamingType = RoamingType.DISABLED)
])
@Service
class IntellijPluginService: PersistentStateComponent<PluginState> {
    var pluginState = PluginState("com.github.javafaker.Faker")

    companion object {
        fun getAppInstance(): IntellijPluginService {
            return ServiceManager.getService(IntellijPluginService::class.java)
        }
    }
    override fun getState(): PluginState {
        return pluginState;
    }

    override fun loadState(state: PluginState) {
        this.pluginState = state;
    }
}
