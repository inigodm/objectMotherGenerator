package inigo.objectMotherCreator.application.values.mappings

import java.util.*

interface Mappings {
    fun random(type: String): String
    fun importsFor(name: String): Vector<String>

}
