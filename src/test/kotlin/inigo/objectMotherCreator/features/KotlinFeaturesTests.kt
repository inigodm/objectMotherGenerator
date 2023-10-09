package inigo.objectMotherCreator.features

import inigo.objectMotherCreator.application.JavaFileCreator
import inigo.objectMotherCreator.application.ObjectMotherCreator
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.template.KotlinObjectMotherTemplate
import inigo.objectMotherCreator.application.values.*
import inigo.objectMotherCreator.application.values.mappings.ClassMapping
import inigo.objectMotherCreator.application.values.mappings.DefaultMappings
import inigo.objectMotherCreator.givenStandartStateOptions
import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.model.infoExtractor.om.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

class KotlinFeaturesTests {

    lateinit var objectMotherCreator: ObjectMotherCreator

    @MockK
    lateinit var omFile: OMFile

    @MockK
    lateinit var omDir: OMDirectory

    @MockK
    lateinit var ideShits: IdeaShits

    @MockK
    lateinit var fileCreator: JavaFileCreator

    @SpyK
    var defaults: DefaultMappings = spyk(DefaultMappings())

    @MockK
    lateinit var fakeValuesGenerator: FakeValuesGenerator

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(FakeValuesGenerator.Companion)
        defaults.mappings = setOf(ClassMapping(generator = listOf ("faker.howIMetYourMother().highFive()"), className = "String")) + defaults.mappings
        fakeValuesGenerator = KotlinFakeValuesGenerator(defaults)
        every { fileCreator.buildFile(any(), any(), any(), any()) } returns Unit
        every { ideShits.findClass("java.lang.String") } returns null
        every { ideShits.findClass("java.util.List") } returns null
        every { ideShits.findClass("java.util.Map") } returns null
        every { ideShits.findClass("java.util.UUID") } returns null
        every { ideShits.findClass("java.time.Instant") } returns null
        every { ideShits.findClass("java.sql.Timestamp") } returns null
        every { fileCreator.createdFileName() } returns "createdObjectMother"
    }

    @AfterEach
    fun shutdown() {
        unmockkAll()
    }

    @Test
    fun `should create a object mother for a class`() {
        givenStandartStateOptions()
        val omParameterA = createParam("List<String>", java.util.List::class.java.canonicalName)
        val omParameterB = createParam("UUID", UUID::class.java.canonicalName)
        val omParameterC = createParam("Instant", Instant::class.java.canonicalName)
        val omParameterD = createParam("Timestamp", Timestamp::class.java.canonicalName)
        val omParameter3 = createStringParam()
        val omConstructor2 = createConstructor("B", omParameter3, omParameterA, omParameterB, omParameterC, omParameterD)
        val omCollaborator = createClass( "B", "packagename", false, omConstructor2)
        val omParameter2 = createParam("B", "packagename.B")
        val omParameter1 = createStringParam()
        val omConstructor = createConstructor("A", omParameter1, omParameter2)
        every { ideShits.findClass("packagename.B") } returns omCollaborator
        val omClass = createClass( "A", "packageName", true, omConstructor)
        every { omFile.getPackageNameOrVoid() } returns "packagename"
        every { omFile.getClasses() } returns listOf(omClass, omCollaborator)
        val classInfo = ClassInfo(omClass, "packagename", omFile, ideShits)
        objectMotherCreator = ObjectMotherCreator(fileCreator, KotlinObjectMotherTemplate(fakeValuesGenerator))

        objectMotherCreator.createObjectMotherFor(classInfo, omDir, "kt", fakeValuesGenerator)

        verify(exactly = 1) { fileCreator.buildFile(any(), any(), eq(simpleA), any()) }
        verify(exactly = 1) { fileCreator.buildFile(any(), any(), eq(simpleB), any()) }
    }

    @Test
    fun `should create a object mother for a class with parameters`() {
        givenStandartStateOptions()
        val omParameter1 = createParam("Map<String, Integer>", java.util.Map::class.java.canonicalName)
        val omConstructor = createConstructor("A", omParameter1)
        val omClass = createClass("A", "packagename", true, omConstructor)
        every { omFile.getPackageNameOrVoid() } returns "packagename"
        every { omFile.getClasses() } returns listOf(omClass)
        val classInfo = ClassInfo(omClass, "packagename", omFile, ideShits)
        objectMotherCreator = ObjectMotherCreator(fileCreator, KotlinObjectMotherTemplate(fakeValuesGenerator))

        objectMotherCreator.createObjectMotherFor(classInfo, omDir, "kt", fakeValuesGenerator)

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
import java.util.UUID
import java.time.Instant
import java.sql.Timestamp

class BObjectMother{
    companion object {
    fun randomB(): B {
        val faker = Faker()
        return B(
				faker.howIMetYourMother().highFive(), 
				listOf(
            faker.howIMetYourMother().highFive(),
            faker.howIMetYourMother().highFive()), 
				UUID.randomUUID(), 
				Instant.now(), 
				Timestamp.from(Instant.now()))
    }
	}
}""".trimIndent()

    var parametrized = """package packagename

import com.github.javafaker.Faker

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

    fun createParam(type: String, canonicalName: String): OMParameter {
        val param: OMParameter = mockk(relaxed = true)
        every { param.getClassCanonicalName() } returns canonicalName
        every { param.getNameOrVoid() } returns type
        return param
    }

    fun createStringParam() : OMParameter {
        return createParam("String", java.lang.String::class.java.canonicalName)
    }

    fun createConstructor(className: String, vararg params: OMParameter) : OMMethod {
        val omMethod : OMMethod = mockk(relaxed = true)
        every { omMethod.getName() } returns className
        every { omMethod.getParameters() } returns params.asList()
        return omMethod
    }

    fun createClass(className: String,
                    packageName: String,
                    isPublic: Boolean = true,
                    vararg constructors: OMMethod): OMClass {
        val omClass: OMClass = mockk(relaxed = true)
        every { omClass.getName() } returns className
        every { omClass.getPackageName() } returns packageName
        every { omClass.isPublic() } returns isPublic
        every { omClass.getAllConstructors() } returns constructors.asList()
        every { omClass.getQualifiedName() } returns packageName + "." + className
        return omClass
    }
}
