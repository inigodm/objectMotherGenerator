package inigo.objectMotherCreator.infraestructure.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.*
import inigo.objectMotherCreator.application.values.mappings.DefaultMappings
import inigo.objectMotherCreator.application.values.mappings.Mappings.Companion.GENERATORS
import inigo.objectMotherCreator.application.values.mappings.Mappings.Companion.IMPORTS
import inigo.objectMotherCreator.infraestructure.config.persistence.PluginState
import java.util.Vector

@State(name="inigo.objectMotherCreator.infraestructure.config.persistence.PluginState", storages = [
    Storage("objectmothercreatorconfig.xml", roamingType = RoamingType.DISABLED)
])
@Service
class IntellijPluginService: PersistentStateComponent<PluginState> {
    companion object {

        fun defaultState() : PluginState {
            val v : Vector<Vector<String>> = Vector()
            DefaultMappings().mappings.forEach {
                v.add(Vector(it))
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
        return pluginState.getMappingForType(type).firstOrNull()?.get(GENERATORS)?.split(",")?.random()?.trim() ?: ""
    }

    fun getImportsFor(type: String) : Vector<String> {
        return Vector(pluginState.getMappingForType(type).firstOrNull { it.get(IMPORTS).isNotEmpty() }?.get(IMPORTS)?.split(",") ?: emptyList())
    }

    fun getFakerClassName(): String {
        return pluginState.fakerClassname
    }

    fun getPrefixes(): String {
        return pluginState.prefixes
    }

    fun getMappingsCopy(): Vector<Vector<String>> {
        val vector = Vector<Vector<String>> ()
        pluginState.mappings.forEach {
            val aux = Vector<String>()
            aux.addAll(it)
            vector.add(aux)
        }
        return vector
    }

    fun getMappings(): Vector<Vector<String>> {
        return pluginState.mappings
    }
}
