package inigo.objectMotherCreator.infraestructure.config

class PluginState(private var fakerClassname: String = "com.github.javafaker.Faker", private var prefixes: String = "random",
    private var mappings: Map<String, String>) {
    fun getFakerClassName() = fakerClassname
    fun getPrefixes() = prefixes
    fun getMappings() = mappings
    fun setMappings(mapping: Map<String, String>) {
        this.mappings = mapping
    }

    fun setFakerClassName(clazz: String){
      this.fakerClassname = clazz
    }

    fun setPrefixes(prefix: String) {
        this.prefixes = prefix
    }

}
