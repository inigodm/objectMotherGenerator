package inigo.objectMotherCreator.model.infogenerated

class JavaMotherClassGeneratedData(packageCode: String = "", imports: MutableSet<String> = mutableSetOf(), code: String = "") : MotherClassGeneratedData(packageCode, imports, code){

    override fun toSource() : String {
        var importsSource = imports.map { "import $it" }.joinToString(";\n")
        if (!importsSource.endsWith(";\n")){
            importsSource += ";\n"
        } else {
            importsSource += "\n"
        }
        return packageCode + ";\n\n" + importsSource + "\n" + code
    }

    override fun addImport(import: String) {
        imports.add(import)
    }

    override fun addAllImports(impotList: List<String>) {
        imports.addAll(impotList)
    }
}
