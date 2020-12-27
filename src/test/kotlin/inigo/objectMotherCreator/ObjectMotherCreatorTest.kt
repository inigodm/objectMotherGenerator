package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import inigo.objectMotherCreator.infraestructure.JavaClass
import inigo.objectMotherCreator.infraestructure.JavaDirectory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ObjectMotherCreatorTest {
    @MockK
    lateinit var project : Project
    @MockK
    lateinit var fileCreator : JavaFileCreator
    @MockK
    lateinit var javaObjectMotherTemplate: JavaObjectMotherTemplate
    @MockK
    lateinit var infoExtractor : ClassInfo
    @MockK
    lateinit var dir : JavaDirectory
    @MockK
    lateinit var directory : JavaDirectory
    @MockK
    lateinit var classInfo: ClassInfo
    @MockK
    lateinit var clazz : JavaClass
    @MockK
    lateinit var e: AnActionEvent

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `extracts info of isolated classes` () {
        every { directory.getOMFile().getCanonicalPath() } returns "file path"
        every { javaObjectMotherTemplate.buildObjectMotherCode(any()) } returns "source code"
        every { javaObjectMotherTemplate.getNeededObjectMothers() } returns mutableListOf()
        every { fileCreator.findOrCreateDirectoryForPackage(any(), any())} returns directory
        every { fileCreator.buildFile(any(), any(), any()) } returns Unit
        every { e.project } returns project
        every { fileCreator.createdFilename } returns "file path/clazznameObjectMother.java"

        val sut = ObjectMotherCreator(fileCreator, javaObjectMotherTemplate);
        sut.createObjectMotherFor(infoExtractor, dir)

        assertEquals(sut.objectMotherFileNames, mutableListOf("file path/clazznameObjectMother.java"))
        verify(exactly = 1) { javaObjectMotherTemplate.getNeededObjectMothers() }
        verify(exactly = 1) { fileCreator.buildFile(dir, infoExtractor, "source code") }
    }

    @Test
    fun `extracts info recursively from classes` () {
        every { directory.getOMFile().getCanonicalPath() } returns "file path"
        every { classInfo.clazz } returns clazz
        every { classInfo.clazz!!.getName() } returns "clazzname"
        every { classInfo.packageStr } returns "package"
        every { javaObjectMotherTemplate.buildObjectMotherCode(any()) } returns "source code"
        every { javaObjectMotherTemplate.getNeededObjectMothers() } returnsMany listOf(mutableListOf(classInfo), mutableListOf())
        every { fileCreator.findOrCreateDirectoryForPackage(any(), any())} returns directory
        every { fileCreator.buildFile(any(), any(), any()) } returns Unit
        every { e.project } returns project
        every { fileCreator.createdFilename } returns "file path/clazznameObjectMother.java"

        val sut = ObjectMotherCreator(fileCreator, javaObjectMotherTemplate);
        sut.createObjectMotherFor(infoExtractor, dir)

        assertEquals(sut.objectMotherFileNames,
                mutableListOf("file path/clazznameObjectMother.java", "file path/clazznameObjectMother.java"))
        verify(exactly = 1) { fileCreator.buildFile(dir, infoExtractor, "source code") }
        verify(exactly = 1) { fileCreator.buildFile(dir, classInfo, "source code") }
    }
}
