package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.template.JavaObjectMotherTemplate
import inigo.objectMotherCreator.model.infoExtractor.om.OMClass
import inigo.objectMotherCreator.model.infoExtractor.om.OMDirectory
import inigo.objectMotherCreator.infraestructure.IdeaJavaFileCreator
import inigo.objectMotherCreator.application.ObjectMotherCreator
import inigo.objectMotherCreator.application.values.FakerGenerator
import inigo.objectMotherCreator.application.values.JavaFakerGenerator
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ObjectMotherCreatorTest {
    @MockK
    lateinit var project : Project
    @MockK
    lateinit var fileCreator : IdeaJavaFileCreator
    @MockK
    lateinit var javaObjectMotherTemplate: JavaObjectMotherTemplate
    @MockK
    lateinit var infoExtractor : ClassInfo
    @MockK
    lateinit var dir : OMDirectory
    @MockK
    lateinit var classInfo: ClassInfo
    @MockK
    lateinit var clazz : OMClass
    @MockK
    lateinit var e: AnActionEvent
    @SpyK
    var fakerValuesGenerator: FakerGenerator = JavaFakerGenerator()
    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `extracts info of isolated classes` () {
        every { javaObjectMotherTemplate.createObjectMotherSourceCode(any()) } returns "source code"
        every { javaObjectMotherTemplate.getNeededObjectMothers() } returns mutableListOf()
        every { fileCreator.buildFile(any(), any(), any(), "java") } returns Unit
        every { e.project } returns project
        every { fileCreator.createdFileName() } returns "file path/clazznameObjectMother.java"

        val sut = ObjectMotherCreator(fileCreator, javaObjectMotherTemplate);
        sut.createObjectMotherFor(infoExtractor, dir, "java", fakerValuesGenerator)

        assertEquals(sut.objectMotherFileNames, mutableListOf("file path/clazznameObjectMother.java"))
        verify(exactly = 2) { fakerValuesGenerator.neededObjectMotherClasses }
        verify(exactly = 1) { fileCreator.buildFile(dir, infoExtractor, "source code", "java") }
    }

    @Test
    fun `extracts info recursively from classes` () {
        every { classInfo.clazz } returns clazz
        every { classInfo.clazz!!.getName() } returns "clazzname"
        every { classInfo.packageStr } returns "package"
        every { javaObjectMotherTemplate.createObjectMotherSourceCode(any()) } returns "source code"
        every { fakerValuesGenerator.neededObjectMotherClasses } returnsMany listOf(mutableListOf(), mutableListOf(classInfo), mutableListOf())
        every { fileCreator.buildFile(any(), any(), any(), "java") } returns Unit
        every { e.project } returns project
        every { fileCreator.createdFileName() } returns "file path/clazznameObjectMother.java"

        val sut = ObjectMotherCreator(fileCreator, javaObjectMotherTemplate);
        sut.createObjectMotherFor(infoExtractor, dir, "java", fakerValuesGenerator)

        assertEquals(sut.objectMotherFileNames,
                mutableListOf("file path/clazznameObjectMother.java", "file path/clazznameObjectMother.java"))
        verify(exactly = 1) { fileCreator.buildFile(dir, infoExtractor, "source code", "java") }
        verify(exactly = 1) { fileCreator.buildFile(dir, classInfo, "source code", "java") }
    }
}
