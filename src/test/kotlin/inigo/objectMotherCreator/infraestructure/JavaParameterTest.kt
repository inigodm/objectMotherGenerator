package inigo.objectMotherCreator.infraestructure

import assertk.assertThat
import assertk.assertions.isEqualTo
import inigo.objectMotherCreator.application.TypedClass
import inigo.objectMotherCreator.application.getGroups
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class JavaParameterTest{
    lateinit var sut: JavaParameter

    @BeforeEach
    fun setup() {

    }

    @Test
    fun `should return Integer` () {
        val clazz = getGroups("java.lang.Integer")
        assertThat(clazz.get(0).className).isEqualTo("java.lang.Integer")
        assertThat(clazz.get(0).types).isEqualTo(emptyList<Any>())
    }

    @Test
    fun `should return List String` () {
        val clazz = getGroups("java.util.List < java.lang.String >")
        assertThat(clazz.get(0).className).isEqualTo("java.util.List")
        assertThat(clazz.get(0).types).isEqualTo(mutableListOf(TypedClass("java.lang.String")))
    }

    @Test
    fun `should return Map String, Integer` () {
        val clazz = getGroups("java.util.Map < java.lang.String, java.lang.Integer >")
        assertThat(clazz.get(0).className).isEqualTo("java.util.Map")
        assertThat(clazz.get(0).types).isEqualTo(
            mutableListOf(TypedClass("java.lang.String"), TypedClass("java.lang.Integer")))
    }

    @Test
    fun `should return Map String, List Integer` () {
        val clazz = getGroups("java.util.Map < java.lang.String, java.util.List < java.lang.Integer > >")
        assertThat(clazz.get(0).className).isEqualTo("java.util.Map")
        assertThat(clazz.get(0).types).isEqualTo(
            mutableListOf(TypedClass("java.lang.String"), TypedClass("java.util.List", mutableListOf(TypedClass("java.lang.Integer")))))
    }

    @Test
    fun `should return Map String, Map String Integer` () {
        val clazz = getGroups("java.util.Map < java.lang.String, java.util.Map < java.lang.String, java.lang.Integer > >")
        assertThat(clazz.get(0).className).isEqualTo("java.util.Map")
        assertThat(clazz.get(0).types).isEqualTo(
            mutableListOf(
                TypedClass("java.lang.String"),
                TypedClass("java.util.Map",
                    mutableListOf(
                        TypedClass("java.lang.String"),
                        TypedClass("java.lang.Integer")
                    ))
            ))
    }
    @Test
    fun `should return List List Integer` () {
        val clazz = getGroups("java.util.List < java.util.List < java.lang.String> >")
        assertThat(clazz.get(0).className).isEqualTo("java.util.List")
        assertThat(clazz.get(0).types).isEqualTo(
            mutableListOf(
                TypedClass("java.util.List",
                mutableListOf(TypedClass("java.lang.String")))
            ))
    }

    @Test
    fun `should return Map List Integer, String` () {
        val clazz = getGroups("java.util.Map < java.util.List < java.lang.Integer >, java.lang.String >")
        assertThat(clazz.get(0).className).isEqualTo("java.util.Map")
        assertThat(clazz.get(0).types).isEqualTo(
            mutableListOf(
                TypedClass("java.util.List", mutableListOf(TypedClass("java.lang.Integer"))),
                TypedClass("java.lang.String")
            ))
    }

    @Test
    fun `should return A, B, C` () {
        val clazz = getGroups("Class < A, B, C >")
        assertThat(clazz.get(0).className).isEqualTo("Class")
        assertThat(clazz.get(0).types).isEqualTo(
            mutableListOf(
                TypedClass("A"),
                TypedClass("B"),
                TypedClass("C")
            ))
    }

    @Test
    fun `should return A, B, C, D` () {
        val clazz = getGroups("Class<A,B,C<D>>")
        assertThat(clazz.get(0).className).isEqualTo("Class")
        assertThat(clazz.get(0).types).isEqualTo(
            mutableListOf(
                TypedClass("A"),
                TypedClass("B"),
                TypedClass("C", mutableListOf(TypedClass("D")))
            ))
    }

    @Test
    fun `should return Av of B, C, D` () {
        val clazz = getGroups("Class<A<D>,B,C<D>>")
        assertThat(clazz.get(0).className).isEqualTo("Class")
        assertThat(clazz.get(0).types).isEqualTo(
            mutableListOf(
                TypedClass("A", mutableListOf(TypedClass("D"))),
                TypedClass("B"),
                TypedClass("C", mutableListOf(TypedClass("D")))
            ))
    }

    @Test
    fun `should return Av of B, C, Dk` () {
        val clazz = getGroups("Class<A<D>,B,C<D, E>, F<G,H<K>>>")
        assertThat(clazz.get(0).className).isEqualTo("Class")
        assertThat(clazz.get(0).types).isEqualTo(
            mutableListOf(
                TypedClass("A", mutableListOf(TypedClass("D"))),
                TypedClass("B"),
                TypedClass("C", mutableListOf(TypedClass("D"), TypedClass("E"))),
                TypedClass("F", mutableListOf(TypedClass("G"), TypedClass("H", mutableListOf(TypedClass("K")))))
            ))
    }
}
