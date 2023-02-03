package inigo.objectMotherCreator.infraestructure

import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.infoExtractor.om.OMDirectory
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class IdeaJavaFileCreatorTest {
    lateinit var sut: IdeaJavaFileCreator
    lateinit var ideaShits: IdeaShits
    lateinit var commandProcessor: CommandProcessor

    @BeforeEach
    fun setUp () {
        MockKAnnotations.init(this)
        ideaShits = mockk(relaxed = true)
        commandProcessor = mockk(relaxed = true)
        sut = IdeaJavaFileCreator(ideaShits, commandProcessor)
    }

    @Test
    fun `should build a file` () {
        val basedir = mockk<OMDirectory>(relaxed = true)
        val clazzInfo = mockk<ClassInfo>(relaxed = true)
        val project = mockk<Project>(relaxed = true)
        val javaCode = "code"
        val extension = "kt"

        every { clazzInfo.packageStr } returns "pa.cka.ge"
        every { ideaShits.getProject() } returns project
        every { basedir.findSubdirectory("pa") } returns basedir
        every { basedir.findSubdirectory("cka") } returns basedir
        every { basedir.findSubdirectory("ge") } returns basedir

        sut.buildFile(basedir, clazzInfo, javaCode, extension)

        verify { basedir.findSubdirectory("pa") }
        verify { basedir.findSubdirectory("cka") }
        verify { basedir.findSubdirectory("ge") }
        verify { commandProcessor.executeCommand(project, any(), any(), null) }
    }

}
