package inigo.objectMotherCreator.infraestructure.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.*
import inigo.objectMotherCreator.application.values.JavaFakeValuesGenerator
import inigo.objectMotherCreator.application.values.mappings.DefaultMappings
import java.util.*

@State(name="inigo.objectMotherCreator.infraestructure.config.PluginState", storages = [
    Storage("objectmothercreatorconfig.xml", roamingType = RoamingType.DISABLED)
])
@Service
class IntellijPluginService: PersistentStateComponent<PluginState> {
    companion object {

        fun defaultState() : PluginState {
            val v : Vector<Vector<String>> = Vector()
            DefaultMappings().mappings.forEach {
                v.add(Vector(it.toCollection()))
            }
            return PluginState("com.github.javafaker.Faker", "random", v)
        }

        fun getInstance(): IntellijPluginService {
            return ServiceManager.getService(IntellijPluginService::class.java)
        }
    }

    private var pluginState = defaultState()

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
        this.pluginState = defaultState()
    }
    fun getGeneratorFor(type: String) : String {
        return this.pluginState.mappings.filter { it[0] == type }.firstOrNull()?.get(2)?.split(",")?.random() ?: ""
    }

    fun getImportsFor(type: String) : Vector<String> {
        return Vector(this.pluginState.mappings.filter { it[0] == type }.firstOrNull { it.get(1).isNotEmpty() }?.get(1)?.split(",") ?: emptyList())
    }

    fun getFakerClassName(): String {
        return this.pluginState.fakerClassname
    }

    fun getPrefixes(): String {
        return this.pluginState.prefixes
    }

    fun getMappings(): Vector<Vector<String>> {
        return this.pluginState.mappings
    }
}
