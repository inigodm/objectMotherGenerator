package inigo.objectMotherCreator.infraestructure.config

import java.util.*

class PluginState(private var fakerClassname: String = "com.github.javafaker.Faker",
                  private var prefixes: String = "random",
                  private var mappings: Vector<Vector<String>>) {
    fun getFakerClassName() = fakerClassname
    fun getPrefixes() = prefixes
    fun getMappings() = mappings
    fun setMappings(mapping: Vector<Vector<String>>) {
        this.mappings = mapping
    }

    fun setFakerClassName(clazz: String){
      this.fakerClassname = clazz
    }

    fun setPrefixes(prefix: String) {
        this.prefixes = prefix
    }

}
