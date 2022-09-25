package inigo.objectMotherCreator.model
abstract class ClassCode(var packageCode: String = "", var imports: MutableSet<String> = mutableSetOf(), var code: String = "") {
    abstract fun toSource(): String
    abstract fun addImport(import: String)
    abstract fun addAllImports(impotList: List<String>)
}
