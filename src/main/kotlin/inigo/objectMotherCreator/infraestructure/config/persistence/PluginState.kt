package inigo.objectMotherCreator.infraestructure.config.persistence

import com.intellij.util.xmlb.annotations.OptionTag
import java.util.Vector

class PluginState() {

    lateinit var fakerClassname: String
    lateinit var prefixes: String
    @OptionTag(converter = MappingConverter::class)
    lateinit var mappings: Vector<Vector<String>>
    constructor(fakerClassname: String,
                prefixes: String,
                mappings: Vector<Vector<String>>) : this() {
        this.fakerClassname = fakerClassname
        this.prefixes = prefixes
        val vector = Vector<Vector<String>> ()
        mappings.forEach {
            val aux = Vector<String>()
            aux.addAll(it)
            vector.add(aux)
        }
        this.mappings = vector
    }

    fun getMappingForType(type: String): List<Vector<String>> {
        return mappings.filter { it[0] == type }
    }
}
