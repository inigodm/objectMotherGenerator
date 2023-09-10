package inigo.objectMotherCreator.infraestructure.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.*
import java.util.*

@State(name="inigo.objectMotherCreator.infraestructure.config.PluginState", storages = [
    Storage("objectmothercreatorconfig.xml", roamingType = RoamingType.DISABLED)
])
@Service
class IntellijPluginService: PersistentStateComponent<PluginState> {
    companion object {

        val DEFAULT_STATE = defaultState()
        fun defaultState() : PluginState {
            val s = Vector<String>()
            val v = Vector<Vector<String>>()
            s.add("")
            s.add("")
            v.add(s)
            return PluginState("com.github.javafaker.Faker", "random", v)
        }

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
