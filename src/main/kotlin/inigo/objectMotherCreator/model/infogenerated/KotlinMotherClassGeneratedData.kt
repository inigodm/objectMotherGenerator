package inigo.objectMotherCreator.model.infogenerated

class KotlinMotherClassGeneratedData(packageCode: String = "", imports: MutableSet<String> = mutableSetOf(), code: String = "") : MotherClassGeneratedData(packageCode, imports, code){
    override fun toSource() : String {
        val importsSource = imports.map { "import $it" }.joinToString("\n")
        return packageCode  + importsSource + "\n\n" + code
    }

    override fun addImport(import: String) {
        imports.add(import)
    }

    override fun addAllImports(impotList: List<String>) {
        imports.addAll(impotList)
    }
}
