package inigo

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import inigo.objectMotherCreator.ClassInfo
import inigo.objectMotherCreator.MethodInfo
import inigo.objectMotherCreator.ParametersInfo
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class `Needed info should be extracted in holders` {
    @MockK lateinit var param : PsiParameter
    @MockK lateinit var facade : JavaPsiFacade
    @MockK lateinit var project : Project

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(JavaPsiFacade::class)
        every { param.type.getCanonicalText(true) } returns "canonical"
        every { param.typeElement?.type?.getPresentableText() } returns "text"
        every { JavaPsiFacade.getInstance(any()) } returns facade
        every { GlobalSearchScope.projectScope(project) } returns mockk()
    }

    @Test
    fun `Parameters info should extract psiparameter name`() {
        every { facade.findClass(any(), any()) } returns null

        val sut = ParametersInfo(param, project)

        assertEquals(sut.name, "text")
    }

    @Test
    fun `Parameters info should find clazzinfo if param type defined in project`() {
        val psiClass = mockk<PsiClass>()
        every { psiClass.qualifiedName } returns "package.className"
        every { psiClass.allFields } returns emptyArray()
        every { psiClass.allMethods } returns emptyArray()
        every { psiClass.constructors } returns emptyArray()
        every { facade.findClass(any(), any()) } returns psiClass

        val sut = ParametersInfo(param, project)

        assertEquals(sut.name, "text")
        assertEquals(sut.clazzInfo!!.clazz, psiClass)
        assertEquals(sut.clazzInfo!!.packageName, "package")
    }

    @Test
    fun `MethodInfo should extract name`() {
        val psiMethod = mockk<PsiMethod>()
        every { param.type.getCanonicalText(true) } returns "canonical"
        every { psiMethod.name } returns "methodName"
        every { psiMethod.parameterList.parameters } returns arrayOf(param)
        val psiClass = mockk<PsiClass>()
        every { psiClass.qualifiedName } returns "package.className"
        every { psiClass.allFields } returns emptyArray()
        every { psiClass.allMethods } returns emptyArray()
        every { psiClass.constructors } returns emptyArray()
        every { facade.findClass(any(), any()) } returns psiClass

        val sut = MethodInfo(psiMethod, project)

        assertEquals(sut.name, "methodName")
    }

    @Test
    fun `ClassInfo should extract fields, methods and constructors`() {
        val psiClass = mockk<PsiClass>()
        var psiField = mockk<PsiField>()
        var psiMethod = mockk<PsiMethod>()
        every { psiClass.qualifiedName } returns "package.className"
        every { psiClass.allFields } returns arrayOf(psiField)
        every { psiClass.allMethods } returns arrayOf(psiMethod)
        every { psiClass.constructors } returns emptyArray()

        var sut = ClassInfo(psiClass, "package.className", project)

        assertEquals(sut.fields.size, 1)
        assertEquals(sut.methods.size, 1)
        assertEquals(sut.fields[0], psiField)
        assertEquals(sut.methods[0], psiMethod)
    }
}
