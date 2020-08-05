package inigo.objectMotherCreator

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDirectory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ObjectMotherBuilderTest {
    @MockK
    lateinit var project : Project
    @MockK
    lateinit var fileCreator : FileCreator
    @MockK
    lateinit var javaObjectMotherTemplate: JavaObjectMotherTemplate
    @MockK
    lateinit var infoExtractor : JavaFileInfo
    @MockK
    lateinit var dir : PsiDirectory
    @MockK
    lateinit var directory : PsiDirectory
    @MockK
    lateinit var psiJavaClassInfo: PsiJavaClassInfo
    @MockK
    lateinit var clazz : PsiClass

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `extracts info of isolated classes` () {
        every { directory.virtualFile.canonicalPath } returns "file path"
        every { infoExtractor.mainClass } returns psiJavaClassInfo
        every { psiJavaClassInfo.clazz } returns clazz
        every { infoExtractor.mainClass.clazz.name } returns "clazzname"
        every { infoExtractor.mainClass.packageName } returns "package"
        every { javaObjectMotherTemplate.buildJavaFile(any()) } returns "source code"
        every { javaObjectMotherTemplate.neededObjectMotherClasses } returns mutableListOf()
        every { fileCreator.findOrCreateDirectoryForPackage(any(), any())} returns directory
        every { fileCreator.createJavaFile(any(), any(), any()) } returns Unit

        var objectMotherBuilder = ObjectMotherBuilder(fileCreator, javaObjectMotherTemplate);
        objectMotherBuilder.buildFor(infoExtractor, dir)

        assertEquals(objectMotherBuilder.classesTreated, mutableListOf("file path/clazznameObjectMother.java"))
        //verify(exactly = 1) { javaObjectMotherTemplate.buildJavaFile(psiJavaClassInfo) }
        verify(exactly = 1) { fileCreator.findOrCreateDirectoryForPackage("package", dir) }
        verify(exactly = 1) { fileCreator.createJavaFile(directory, "clazznameObjectMother.java", "source code") }
    }

    @Test
    fun `extracts info recursively from classes` () {
        every { directory.virtualFile.canonicalPath } returns "file path"
        every { infoExtractor.mainClass } returns psiJavaClassInfo
        every { psiJavaClassInfo.clazz } returns clazz
        every { infoExtractor.mainClass.clazz.name } returns "clazzname"
        every { infoExtractor.mainClass.packageName } returns "package"
        every { javaObjectMotherTemplate.buildJavaFile(any()) } returns "source code"
        every { javaObjectMotherTemplate.neededObjectMotherClasses } returnsMany listOf(mutableListOf(infoExtractor.mainClass), mutableListOf())
        every { fileCreator.findOrCreateDirectoryForPackage(any(), any())} returns directory
        every { fileCreator.createJavaFile(any(), any(), any()) } returns Unit

        var objectMotherBuilder = ObjectMotherBuilder(fileCreator, javaObjectMotherTemplate);
        objectMotherBuilder.buildFor(infoExtractor, dir)

        assertEquals(objectMotherBuilder.classesTreated,
                mutableListOf("file path/clazznameObjectMother.java", "file path/clazznameObjectMother.java"))
        verify(exactly = 2) { fileCreator.findOrCreateDirectoryForPackage("package", dir) }
        verify(exactly = 2) { fileCreator.createJavaFile(directory, "clazznameObjectMother.java", "source code") }
    }
}
