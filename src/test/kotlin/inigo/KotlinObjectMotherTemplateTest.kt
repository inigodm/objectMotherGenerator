package inigo

import fixedClassInfo
import fixedMethodInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.application.template.KotlinObjectMotherTemplate
import inigo.objectMotherCreator.application.values.KotlinFakeValuesGenerator
import inigo.objectMotherCreator.givenStandartStateOptions
import inigo.objectMotherCreator.infraestructure.config.IntellijPluginService
import inigo.objectMotherCreator.model.infogenerated.MotherClassGeneratedData
import inigo.objectMotherCreator.model.infogenerated.KotlinMotherClassGeneratedData
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KotlinObjectMotherTemplateTest {
    @SpyK
    var fakeValuesGenerator: FakeValuesGenerator = KotlinFakeValuesGenerator()
    lateinit var motherClassGeneratedData : MotherClassGeneratedData
    @MockK(relaxed = true)
    lateinit var service: IntellijPluginService
    @BeforeEach
    fun setUp () {
        MockKAnnotations.init(this)
        motherClassGeneratedData = KotlinMotherClassGeneratedData()
        givenStandartStateOptions(service)
    }

    @Test
    fun `build package line`() {
        val sut = KotlinObjectMotherTemplate(KotlinFakeValuesGenerator())

        assertEquals(sut.buildPackage("packagename").trim(), "package packagename")
    }

    @Test
    fun `build import line for faker if other classes ar in diferent package`() {
        val sut = KotlinObjectMotherTemplate(KotlinFakeValuesGenerator())
        val res = sut.buildImports(listOf(fixedMethodInfo()))

        assertEquals(res, listOf("com.github.javafaker.Faker",
            "qualified.clazzNameObjectMother.Companion.randomclazzName"))}

    @Test
    fun `build class code with default constructor if no constructors`() {
        val sut = KotlinObjectMotherTemplate(KotlinFakeValuesGenerator())

        val res = sut.buildClass("className", listOf(), motherClassGeneratedData)

        assertEquals(res, """class classNameObjectMother{
    companion object {
    fun randomclassName():  className{
        return className()
    }
	}
}""")
    }

    @Test
    fun `build class code using existing first constructor if any constructors exist`() {
        val sut = KotlinObjectMotherTemplate(KotlinFakeValuesGenerator())

        val res = sut.buildClass("className", listOf(fixedMethodInfo()), motherClassGeneratedData)

        assertEquals(res, """class classNameObjectMother{
    companion object {
    fun randomclassName(): className {
        val faker = Faker()
        return className(
				randomclazzName())
    }
	}
}""")
    }

    @Test
    fun `build class code using existing first constructor if any constructors exists`() {
        assertThatWorksWithType("int", "faker.number().randomDigit()")
        assertThatWorksWithType("Integer", "faker.number().randomDigit()")
        assertThatWorksWithType("long", "faker.number().randomNumber()")
        assertThatWorksWithType("Long", "faker.number().randomNumber()")
        assertThatWorksWithType("Boolean", "faker.bool().bool()")
        assertThatWorksWithType("boolean", "faker.bool().bool()")
    }

    private fun assertThatWorksWithType(type: String, expectedGenerator: String) {
        val sut = KotlinObjectMotherTemplate(KotlinFakeValuesGenerator())

        val res = sut.buildClass("className", listOf(fixedMethodInfo(type)), motherClassGeneratedData)

        assertEquals(res, """class classNameObjectMother{
    companion object {
    fun randomclassName(): className {
        val faker = Faker()
        return className(
				$expectedGenerator)
    }
	}
}""")
    }

    @Test
    fun `have to return needed object classes`() {
        val sut = KotlinObjectMotherTemplate(fakeValuesGenerator)

        sut.buildClass("className", listOf(fixedMethodInfo()), motherClassGeneratedData)

        assertFalse { sut.fakeValuesGenerator.neededObjectMotherClasses.isEmpty() }
    }

    @Test
    fun `should build objectmother when asked to`() {
        val sut = KotlinObjectMotherTemplate(fakeValuesGenerator)

        val res = sut.createObjectMotherSourceCode(fixedClassInfo())

        assertEquals(res, """package packagename

import com.github.javafaker.Faker

class clazznameObjectMother{
    companion object {
    fun randomclazzname():  clazzname{
        return clazzname()
    }
	}
}""")
    }
}
