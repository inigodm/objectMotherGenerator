package inigo.objectMotherCreator.model

class KotlinClassCode(packageCode: String = "", imports: MutableSet<String> = mutableSetOf(), code: String = "") : ClassCode(packageCode, imports, code){
    override fun toSource() : String {
        val importsSource = imports.joinToString("\n")
        return packageCode  + importsSource + "\n\n" + code
    }

    override fun addImport(import: String) {
        imports.add(import)
    }

    override fun addAllImports(impotList: List<String>) {
        imports.addAll(impotList)
    }
}
