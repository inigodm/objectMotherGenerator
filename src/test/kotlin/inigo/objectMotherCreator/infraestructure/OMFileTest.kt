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
import inigo.objectMotherCreator.model.infoExtractor.*
import io.mockk.verify
import org.junit.Ignore
class OMFileTest {

    lateinit var psiJavaFile: PsiJavaFile
    lateinit var psiMethod: PsiMethod
    lateinit var javafile : OMFile
    lateinit var OMClass: OMClass
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

    @Ignore
    fun `should get from java file package name or void` () {
        javafile = OMFile(psiJavaFile as PsiFile)
        every { psiJavaFile.containingDirectory } returns null
        assertThat( javafile.getPackageNameOrVoid()).isEqualTo( "")
        every { psiJavaFile.packageStatement?.packageName } returns "packagename"
        assertThat( javafile.getPackageNameOrVoid()).isEqualTo( "packagename")
    }

    @Ignore
    fun `should get classes from javaFile as an array of JavaClasses` () {
        javafile = OMFile(psiJavaFile as PsiFile)
        val clazz = mockk<PsiClass>()
        every { psiJavaFile.classes } returns arrayOf(clazz)

        assertThat(javafile.getClasses()).isEqualTo(listOf(OMJavaClass(clazz)))
    }

    @Test
    fun `should tell whether class is public or not` () {
        every { psiClass.modifierList!!.text } returns "public"
        OMClass = OMJavaClass(psiClass)

        assertThat(OMClass.isPublic()).isTrue()
    }

    @Test
    fun `should get method name` () {
        every { psiMethod.name } returns "methodName"
        val OMMethod = OMMethod(psiMethod)

        assertThat(OMMethod.getName()).isEqualTo("methodName")
    }

    @Test
    fun `should obtain method parameters`() {
        every { psiMethod.parameterList.parameters } returns arrayOf(psiParameter)

        val OMMethod = OMMethod(psiMethod)

        assertThat(OMMethod.getParameters()).isEqualTo(listOf(OMParameter(psiParameter)))
    }

    @Test
    fun `should get parameters name or void` () {
        val param = OMParameter(psiParameter)

        assertThat(param.getNameOrVoid()).isEqualTo("")

        every { psiParameter.typeElement?.type?.getPresentableText() } returns "paramName"

        assertThat(param.getNameOrVoid()).isEqualTo("paramName")
    }

    @Test
    fun `should get parameters class canonical name` () {
        val param = OMParameter(psiParameter)

        every { psiParameter.type.getCanonicalText(true) } returns "canonicalName"

        assertThat(param.getClassCanonicalName()).isEqualTo("canonicalName")
    }

    @Test
    fun `should find a subdirectory if exists` () {
        val directory = OMDirectory(psiDirectory)
        val returned = psiDirectory
        every { psiDirectory.findSubdirectory(any()) } returns returned

        assertThat(directory.findSubdirectory("subdirectory")).isEqualTo(OMDirectory(returned))
    }

    @Test
    fun `should return null if subdirectory doesn't exists` () {
        val directory = OMDirectory(psiDirectory)

        every { psiDirectory.findSubdirectory(any()) } returns null

        assertThat(directory.findSubdirectory("subdirectory")).isEqualTo(null)
    }

    @Test
    fun `should create a new subdirectory` () {
        val directory = OMDirectory(psiDirectory)

        every { psiDirectory.createSubdirectory(any()) } returns psiDirectory

        assertThat(directory.createSubdirectory("subdirectory")).isEqualTo(OMDirectory(psiDirectory))
        verify { psiDirectory.createSubdirectory("subdirectory") }
    }
}
