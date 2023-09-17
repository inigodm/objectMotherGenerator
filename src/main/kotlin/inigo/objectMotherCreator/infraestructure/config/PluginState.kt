package inigo.objectMotherCreator.infraestructure.config

import java.util.*

class PluginState(
) {
    private lateinit var fakerClassname: String
    private lateinit var prefixes: String
    private lateinit var mappings: List<List<String>>
    constructor(faker: String, prefix: String, maps: List<List<String>>) : this() {
        fakerClassname = faker
        prefixes = prefix
        mappings = maps
    }
    fun getFakerClassName() = fakerClassname
    fun getPrefixes() = prefixes
    fun getMappings() : List<List<String>> {
        return mappings
    }
    fun setMappings(mapping: List<List<String>>) {
        this.mappings = mapping
    }

    fun setFakerClassName(clazz: String){
      this.fakerClassname = clazz
    }

    fun setPrefixes(prefix: String) {
        this.prefixes = prefix
    }

}
