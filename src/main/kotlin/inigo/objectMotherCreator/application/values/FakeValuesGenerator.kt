package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.application.infoholders.ClassInfo

interface FakeValuesGenerator {
    fun randomString() : String
    fun randomInteger() : String
    fun randomLong() : String
    fun randomBoolean() : String
    fun randomMap(name: String): String
    fun randomList(classCanonicalName: String): String
    fun createDefaultValueFor(name: String, classInfo: ClassInfo?): String
    fun randomOtherTypes(classInfo: ClassInfo?, name: String) : String
}
