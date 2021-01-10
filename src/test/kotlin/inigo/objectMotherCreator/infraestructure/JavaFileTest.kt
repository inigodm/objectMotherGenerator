package inigo.objectMotherCreator.infraestructure

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.intellij.psi.*
import io.mockk.verify

class JavaFileTest {

    lateinit var psiJavaFile: PsiJavaFile
    lateinit var psiMethod: PsiMethod
    lateinit var javafile : JavaFile
    lateinit var javaClass: JavaClass
    lateinit var psiClass: PsiClass
    lateinit var psiParameter : PsiParameter
    lateinit var psiDirectory: PsiDirectory

    @BeforeEach
    fun setup() {
        psiJavaFile = mockk(relaxed = true)
        psiClass = mockk(relaxed = true)
        psiMethod = mockk(relaxed = true)
        psiParameter = mockk(relaxed = true)
        psiDirectory = mockk(relaxed = true)
        MockKAnnotations.init(this)
    }

    @Test
    fun `should get from java file package name or void` () {
        javafile = JavaFile(psiJavaFile as PsiFile)
        assertThat( javafile.getPackageNameOrVoid()).isEqualTo( "")
        every { psiJavaFile.packageStatement?.packageName } returns "packagename"
        assertThat( javafile.getPackageNameOrVoid()).isEqualTo( "packagename")
    }

    @Test
    fun `should get classes from javaFile as an array of JavaClasses` () {
        javafile = JavaFile(psiJavaFile as PsiFile)
        val clazz = mockk<PsiClass>()
        every { psiJavaFile.classes } returns arrayOf(clazz)

        assertThat(javafile.getClasses()).isEqualTo(listOf(JavaClass(clazz)))
    }

    @Test
    fun `should tell whether class is public or not` () {
        every { psiClass.modifierList!!.text } returns "public"
        javaClass = JavaClass(psiClass)

        assertThat(javaClass.isPublic()).isTrue()
    }

    @Test
    fun `should get method name` () {
        every { psiMethod.name } returns "methodName"
        val javaMethod = JavaMethod(psiMethod)

        assertThat(javaMethod.getName()).isEqualTo("methodName")
    }

    @Test
    fun `should obtain method parameters`() {
        every { psiMethod.parameterList.parameters } returns arrayOf(psiParameter)

        val javaMethod = JavaMethod(psiMethod)

        assertThat(javaMethod.getParameters()).isEqualTo(listOf(JavaParameter(psiParameter)))
    }

    @Test
    fun `should get parameters name or void` () {
        val param = JavaParameter(psiParameter)

        assertThat(param.getNameOrVoid()).isEqualTo("")

        every { psiParameter.typeElement?.type?.getPresentableText() } returns "paramName"

        assertThat(param.getNameOrVoid()).isEqualTo("paramName")
    }

    @Test
    fun `should get parameters class canonical name` () {
        val param = JavaParameter(psiParameter)

        every { psiParameter.type.getCanonicalText(true) } returns "canonicalName"

        assertThat(param.getClassCanonicalName()).isEqualTo("canonicalName")
    }

    @Test
    fun `should find a subdirectory if exists` () {
        val directory = JavaDirectory(psiDirectory)
        val returned = psiDirectory
        every { psiDirectory.findSubdirectory(any()) } returns returned

        assertThat(directory.findSubdirectory("subdirectory")).isEqualTo(JavaDirectory(returned))
    }

    @Test
    fun `should return null if subdirectory doesn't exists` () {
        val directory = JavaDirectory(psiDirectory)

        every { psiDirectory.findSubdirectory(any()) } returns null

        assertThat(directory.findSubdirectory("subdirectory")).isEqualTo(null)
    }

    @Test
    fun `should create a new subdirectory` () {
        val directory = JavaDirectory(psiDirectory)

        every { psiDirectory.createSubdirectory(any()) } returns psiDirectory

        assertThat(directory.createSubdirectory("subdirectory")).isEqualTo(JavaDirectory(psiDirectory))
        verify { psiDirectory.createSubdirectory("subdirectory") }
    }
}
