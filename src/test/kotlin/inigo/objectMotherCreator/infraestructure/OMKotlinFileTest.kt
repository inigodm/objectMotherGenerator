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
import inigo.objectMotherCreator.model.infoExtractor.om.OMClass
import inigo.objectMotherCreator.model.infoExtractor.om.OMDirectory
import inigo.objectMotherCreator.model.infoExtractor.omjava.OMJavaMethod
import inigo.objectMotherCreator.model.infoExtractor.omkotlin.OMKtClass
import inigo.objectMotherCreator.model.infoExtractor.omkotlin.OMKtMethod
import inigo.objectMotherCreator.model.infoExtractor.omkotlin.OMKtParameter
import io.mockk.verify
import org.jetbrains.kotlin.idea.refactoring.fqName.fqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.nj2k.postProcessing.type
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtConstructor
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.types.KotlinType
import org.junit.Ignore

class OMKotlinFileTest {

    lateinit var psiJavaFile: PsiJavaFile
    lateinit var ktMethod: OMKtMethod<*>
    lateinit var psiMethod: PsiMethod
    lateinit var OMClass: OMClass
    lateinit var ktClass: KtClass
    lateinit var ktParameter: KtParameter
    lateinit var psiParameter : PsiParameter
    lateinit var psiDirectory: PsiDirectory
    lateinit var ktConstructor: KtConstructor<*>

    @BeforeEach
    fun setup() {
        psiJavaFile = mockk(relaxed = true)
        ktClass = mockk(relaxed = true)
        ktMethod = mockk(relaxed = true)
        psiMethod = mockk(relaxed = true)
        psiParameter = mockk(relaxed = true)
        psiDirectory = mockk(relaxed = true)
        ktParameter = mockk(relaxed = true)
        ktConstructor = mockk(relaxed = true)
        ktParameter = mockk(relaxed = true)
        MockKAnnotations.init(this)
    }

    @Test
    fun `should tell whether class is public or not` () {
        every { ktClass.modifierList!!.text } returns "public"
        OMClass = OMKtClass(ktClass, "com.test")

        assertThat(OMClass.isPublic()).isTrue()
    }

    @Test
    fun `should get method name` () {
        every { psiMethod.name } returns "methodName"
        val OMMethod = OMJavaMethod(psiMethod)

        assertThat(OMMethod.getName()).isEqualTo("methodName")
    }

    @Test
    fun `should obtain method parameters`() {
        every { ktConstructor.getValueParameters() } returns listOf(ktParameter)

        val OMMethod = OMKtMethod(ktConstructor)

        assertThat(OMMethod.getParameters()).isEqualTo(listOf(OMKtParameter(ktParameter)))
    }

    @Test
    fun `should get parameters name or void` () {
        val param = OMKtParameter(ktParameter)
        val ktUserType: KtUserType = mockk(relaxed = true)
        every { ktParameter.typeReference?.typeElement} returns ktUserType

        assertThat(param.getNameOrVoid()).isEqualTo("")

        every { ktUserType.referencedName } returns "paramName"

        assertThat(param.getNameOrVoid()).isEqualTo("paramName")
    }

    @Ignore
    fun `should get parameters class canonical name` () {
        val param = OMKtParameter(ktParameter)
        val ktType : KotlinType = mockk(relaxed = true)
        val fqName: FqName = mockk(relaxed = true)
        every { ktParameter.type() } returns ktType
        every { ktType.fqName } returns fqName
        every { fqName.asString() } returns "canonicalName"

        assertThat(param.getClassCanonicalName()).isEqualTo("canonicalName")
    }
}
