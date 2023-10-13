package inigo.objectMotherCreator.infraestructure.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.*
import inigo.objectMotherCreator.application.values.mappings.DefaultMappings
import inigo.objectMotherCreator.infraestructure.config.persistence.PluginState
import java.util.*

@State(name="inigo.objectMotherCreator.infraestructure.config.persistence.PluginState", storages = [
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

    override fun loadState(state: PluginState) {
        this.pluginState = state
    }

    fun getGeneratorFor(type: String) : String {
        return pluginState.getMappingForType(type).firstOrNull()?.get(2)?.split(",")?.random() ?: ""
    }

    fun getImportsFor(type: String) : Vector<String> {
        return Vector(pluginState.getMappingForType(type).firstOrNull { it.get(1).isNotEmpty() }?.get(1)?.split(",") ?: emptyList())
    }

    fun getFakerClassName(): String {
        return pluginState.fakerClassname
    }

    fun getPrefixes(): String {
        return pluginState.prefixes
    }

    fun getMappings(): Vector<Vector<String>> {
        return pluginState.mappings
    }
}
