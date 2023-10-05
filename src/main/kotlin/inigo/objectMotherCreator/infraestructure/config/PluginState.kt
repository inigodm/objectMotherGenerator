package inigo.objectMotherCreator.infraestructure.config

import com.intellij.util.xmlb.annotations.OptionTag

class PluginState() {
    lateinit var fakerClassname: String
    lateinit var prefixes: String
    @OptionTag(converter = MappingConverter::class)
    lateinit var mappings: MutableList<Collection<String>>
    constructor(fakerClassname: String,
                prefixes: String,
                mappings: MutableList<Collection<String>>) : this() {
        this.fakerClassname = fakerClassname
        this.prefixes = prefixes
        this.mappings = mappings
    }
}
