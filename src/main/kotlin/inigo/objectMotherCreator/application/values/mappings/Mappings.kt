package inigo.objectMotherCreator.application.values.mappings

import java.util.*

interface Mappings {
    companion object {
        val CLASSNAME = 0
        val IMPORTS = 1
        val GENERATORS = 2
    }
    fun random(type: String): String
    fun importsFor(name: String): Vector<String>

}
