package inigo

import fixedClassInfo
import fixedMethodInfo
import inigo.objectMotherCreator.application.values.FakeValuesGenerator
import inigo.objectMotherCreator.application.template.JavaObjectMotherTemplate
import inigo.objectMotherCreator.application.values.JavaFakeValuesGenerator
import inigo.objectMotherCreator.givenStandartStateOptions
import inigo.objectMotherCreator.infraestructure.config.IntellijPluginService
import inigo.objectMotherCreator.model.infogenerated.MotherClassGeneratedData
import inigo.objectMotherCreator.model.infogenerated.JavaMotherClassGeneratedData
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JavaObjectMotherTemplateTest {
    @SpyK
    var fakeValuesGenerator: FakeValuesGenerator = JavaFakeValuesGenerator()
    lateinit var motherClassGeneratedData : MotherClassGeneratedData
    @MockK(relaxed = true)
    lateinit var service: IntellijPluginService
    @BeforeEach
    fun setUp () {
        MockKAnnotations.init(this)
        motherClassGeneratedData = JavaMotherClassGeneratedData()
        givenStandartStateOptions(service)
    }


    @Test
    fun `build package line`() {
        val sut = JavaObjectMotherTemplate(JavaFakeValuesGenerator())

        assertEquals(sut.buildPackage("packagename").trim(), "package packagename")
    }

    @Test
    fun `build import line for faker if other classes ar in diferent package`() {
        val sut = JavaObjectMotherTemplate(JavaFakeValuesGenerator())
        val res = sut.buildImports(listOf(fixedMethodInfo()))

        assertEquals(
            res, listOf(
                "com.github.javafaker.Faker",
                "static qualified.clazzNameObjectMother.randomclazzName"
            )
        )
    }

    @Test
    fun `build class code with default constructor if no constructors`() {
        val sut = JavaObjectMotherTemplate()

        val res = sut.buildClass("className", listOf(), motherClassGeneratedData)

        assertEquals(res, """public class classNameObjectMother{
  public static className randomclassName(){
        return new className();
    }
}""")
    }

    @Test
    fun `build class code using existing first constructor if any constructors exist`() {
        val sut = JavaObjectMotherTemplate(JavaFakeValuesGenerator())

        val res = sut.buildClass("className", listOf(fixedMethodInfo()), motherClassGeneratedData)

        assertEquals(res, """public class classNameObjectMother{

    public static className randomclassName(){
        Faker faker = new Faker();
        return new className(
				randomclazzName());
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

    @Test
    fun `strings are generated from list of strings`() {
        assertTrue { listOf("faker.ancient().god()",
                "faker.ancient().primordial()",
                "faker.ancient().titan()",
                "faker.artist().name()",
                "faker.backToTheFuture().character()",
                "faker.backToTheFuture().quote()",
                "faker.beer().name()",
                "faker.buffy().characters()",
                "faker.buffy().quotes()",
                "faker.chuckNorris().fact()",
                "faker.dragonBall().character()",
                "faker.funnyName().name()",
                "faker.friends().character()",
                "faker.friends().quote()",
                "faker.gameOfThrones().character()",
                "faker.gameOfThrones().quote()",
                "faker.hipster().word()",
                "faker.hitchhikersGuideToTheGalaxy().character()",
                "faker.hitchhikersGuideToTheGalaxy().marvinQuote()",
                "faker.hitchhikersGuideToTheGalaxy().quote()",
                "faker.lebowski().quote()",
                "faker.howIMetYourMother().character()",
                "faker.howIMetYourMother().catchPhrase()",
                "faker.howIMetYourMother().highFive()",
                "faker.howIMetYourMother().quote()",
                "faker.lordOfTheRings().location()",
                "faker.princessBride().quote()",
                "faker.princessBride().character()",
                "faker.rickAndMorty().quote()",
                "faker.rickAndMorty().character()",
                "faker.slackEmoji().activity()",
                "faker.superhero().name()",
                "faker.yoda().quote()").contains(JavaFakeValuesGenerator().defaults.random("String")) }
    }

    private fun assertThatWorksWithType(type: String, expectedGenerator: String) {
        val sut = JavaObjectMotherTemplate(JavaFakeValuesGenerator())

        val res = sut.buildClass("className", listOf(fixedMethodInfo(type)), motherClassGeneratedData)

        assertEquals(res, """public class classNameObjectMother{

    public static className randomclassName(){
        Faker faker = new Faker();
        return new className(
				$expectedGenerator);
    }
}""")
    }

    @Test
    fun `have to return needed object classes`() {
        val sut = JavaObjectMotherTemplate(fakeValuesGenerator)

        println(sut.buildClass("className", listOf(fixedMethodInfo()), motherClassGeneratedData))

        assertFalse { sut.fakeValuesGenerator.neededObjectMotherClasses.isEmpty() }
    }

    @Test
    fun `should build objectmother when asked to`() {
        val sut = JavaObjectMotherTemplate(fakeValuesGenerator)

        val res = sut.createObjectMotherSourceCode(fixedClassInfo())

        assertEquals(res, """package packagename;

import com.github.javafaker.Faker;

public class clazznameObjectMother{
  public static clazzname randomclazzname(){
        return new clazzname();
    }
}""")
    }
}
