package inigo.objectMotherCreator.infraestructure.config

class PluginState(private var fakerClassname: String = "com.github.javafaker.Faker", private var prefixes: String = "random") {
    fun getFakerClassName() = fakerClassname
    fun getPrefixes() = prefixes

    fun setFakerClassName(clazz: String){
      this.fakerClassname = clazz
    }

    fun setPrefixes(prefix: String) {
        this.prefixes = prefix
    }

}
