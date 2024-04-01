package inigo.objectMotherCreator.model.infogenerated

class JavaMotherClassGeneratedData(packageCode: String = "", imports: MutableSet<String> = mutableSetOf(), code: String = "") : MotherClassGeneratedData(packageCode, imports, code){

    override fun toSource() : String {
        var importsSource = ""
        if (imports.isNotEmpty()) {
            importsSource = imports.map { "import $it" }.joinToString(separator = ";\n") + ";"
        }
        return "$packageCode;\n\n$importsSource\n\n$code"
    }

    override fun addImport(import: String) {
        imports.add(import)
    }

    override fun addAllImports(impotList: List<String>) {
        imports.addAll(impotList)
    }
}
