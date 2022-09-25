package inigo.objectMotherCreator.features

import inigo.objectMotherCreator.application.JavaFileCreator
import inigo.objectMotherCreator.application.ObjectMotherCreator
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.template.JavaObjectMotherTemplate
import inigo.objectMotherCreator.application.template.KotlinObjectMotherTemplate
import inigo.objectMotherCreator.application.values.FakerGenerator
import inigo.objectMotherCreator.application.values.JavaFakerGenerator
import inigo.objectMotherCreator.application.values.KotlinFakerGenerator
import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.model.infoExtractor.om.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KotlinFeaturesTests {

    lateinit var objectMotherCreator: ObjectMotherCreator

    @MockK
    lateinit var omFile: OMFile

    @MockK
    lateinit var omDir: OMDirectory

    @MockK
    lateinit var omClass: OMClass

    @MockK
    lateinit var omCollaborator: OMClass

    @MockK
    lateinit var omConstructor: OMMethod

    @MockK
    lateinit var omConstructor2: OMMethod

    @MockK
    lateinit var omParameter1: OMParameter

    @MockK
    lateinit var omParameter2: OMParameter

    @MockK
    lateinit var omParameter3: OMParameter

    @MockK
    lateinit var ideShits: IdeaShits

    @MockK
    lateinit var fileCreator: JavaFileCreator

    @SpyK
    var fakerGenerator: FakerGenerator = KotlinFakerGenerator()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        every { fileCreator.buildFile(any(), any(), any(), any()) } returns Unit
        every { fileCreator.createdFileName() } returns "createdObjectMother"
        every { fakerGenerator.randomString() } returns "faker.howIMetYourMother().highFive()"
    }

    @Test
    fun `should create a object mother for a class`() {
        every { omParameter3.getClassCanonicalName() } returns java.lang.String::class.java.canonicalName
        every { omParameter3.getNameOrVoid() } returns "String"
        every { omConstructor2.getName() } returns "B"
        every { omConstructor2.getParameters() } returns listOf(omParameter3)
        every { omCollaborator.getName() } returns "B"
        every { omCollaborator.getPackageName() } returns "packagename"
        every { omCollaborator.isPublic() } returns false
        every { omCollaborator.getAllConstructors() } returns listOf(omConstructor2)
        every { omCollaborator.getQualifiedName() } returns "packagename.B"
        every { omParameter2.getClassCanonicalName() } returns "packagename.B"
        every { omParameter2.getNameOrVoid() } returns "B"
        every { omParameter1.getClassCanonicalName() } returns java.lang.String::class.java.canonicalName
        every { omParameter1.getNameOrVoid() } returns "String"
        every { omConstructor.getParameters() } returns listOf(omParameter1, omParameter2)
        every { omConstructor.getName() } returns "A"
        every { ideShits.findClass("java.lang.String") } returns null
        every { ideShits.findClass("packagename.B") } returns omCollaborator
        every { omClass.getName() } returns "A"
        every { omClass.getPackageName() } returns "packagename"
        every { omClass.isPublic() } returns true
        every { omClass.getAllConstructors() } returns listOf(omConstructor)
        every { omClass.getQualifiedName() } returns "${omClass.getPackageName()}.${omClass.getName()}"
        every { omFile.getPackageNameOrVoid() } returns "packagename"
        every { omFile.getClasses() } returns listOf(omClass, omCollaborator)

        val classInfo = ClassInfo(omClass, "packagename", omFile, ideShits)
        objectMotherCreator = ObjectMotherCreator(fileCreator, KotlinObjectMotherTemplate(fakerGenerator))

        objectMotherCreator.createObjectMotherFor(classInfo, omDir, "kt", fakerGenerator)

        verify(exactly = 1) { fileCreator.buildFile(any(), any(), eq(simpleA), any()) }
        verify(exactly = 1) { fileCreator.buildFile(any(), any(), eq(simpleB), any()) }
    }

    @Test
    fun `should create a object mother for a class with parameters`() {
        every { omParameter1.getClassCanonicalName() } returns java.util.Map::class.java.canonicalName
        every { omParameter1.getNameOrVoid() } returns "Map<String, Integer>"
        every { omConstructor.getParameters() } returns listOf(omParameter1)
        every { omConstructor.getName() } returns "A"
        every { ideShits.findClass("java.util.Map") } returns null
        every { omClass.getName() } returns "A"
        every { omClass.getPackageName() } returns "packagename"
        every { omClass.isPublic() } returns true
        every { omClass.getAllConstructors() } returns listOf(omConstructor)
        every { omClass.getQualifiedName() } returns "${omClass.getPackageName()}.${omClass.getName()}"
        every { omFile.getPackageNameOrVoid() } returns "packagename"
        every { omFile.getClasses() } returns listOf(omClass)

        val classInfo = ClassInfo(omClass, "packagename", omFile, ideShits)
        objectMotherCreator = ObjectMotherCreator(fileCreator, KotlinObjectMotherTemplate(fakerGenerator))

        objectMotherCreator.createObjectMotherFor(classInfo, omDir, "kt", fakerGenerator)

        verify(exactly = 1) { fileCreator.buildFile(any(), any(), eq(parametrized), any()) }
    }

    val simpleA = """
package packagename

import com.github.javafaker.Faker
import packagename.BObjectMother.Companion.randomB

class AObjectMother{
    companion object {
    fun randomA(): A {
        val faker = Faker()
        return A(
				faker.howIMetYourMother().highFive(), 
				randomB())
    }
	}
}
""".trimIndent()

    val simpleB = """package packagename

import com.github.javafaker.Faker

class BObjectMother{
    companion object {
    fun randomB(): B {
        val faker = Faker()
        return B(
				faker.howIMetYourMother().highFive())
    }
	}
}""".trimIndent()

    var parametrized = """package packagename

import com.github.javafaker.Faker
import java.util.Map

class AObjectMother{
    companion object {
    fun randomA(): A {
        val faker = Faker()
        return A(
				mapOf(faker.howIMetYourMother().highFive(), 
            faker.number().randomDigit(),
				        faker.howIMetYourMother().highFive(), 
            faker.number().randomDigit()))
    }
	}
}""".trimIndent()
}