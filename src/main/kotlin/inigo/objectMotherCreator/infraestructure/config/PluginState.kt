package inigo.objectMotherCreator.infraestructure.config

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
        this.mappings = mappings
    }
}
