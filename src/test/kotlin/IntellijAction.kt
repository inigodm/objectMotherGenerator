import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.vfs.VirtualFile
import inigo.objectMotherCreator.ObjectCreateFileSeletedAction
import inigo.objectMotherCreator.ObjectCreateOnCaretSelectedAction
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class `Option in menu appears` {
    @MockK
    lateinit var actionEvent : AnActionEvent
    @MockK
    lateinit var caret : CaretModel
    lateinit var presentation : Presentation
    @BeforeEach
    fun setUp () {
        MockKAnnotations.init(this)
        presentation = spyk<Presentation>()
    }

    @Test
    fun `when a caret is selected` () {
        every { actionEvent.getRequiredData(CommonDataKeys.EDITOR).caretModel } returns caret
        every { caret.currentCaret.hasSelection() } returns true
        every { actionEvent.presentation } returns presentation
        every { actionEvent.getData(CommonDataKeys.PSI_FILE)!!.language.displayName } returns "java"
        val sut = ObjectCreateOnCaretSelectedAction()

        sut.update(actionEvent)

        assertTrue { actionEvent.presentation.isEnabledAndVisible }
    }

    @Test
    fun `when selected file is java file` () {
        val file = mockk<VirtualFile>()
        every { actionEvent.getData(PlatformDataKeys.VIRTUAL_FILE) } returns file
        every { file.toString() } returns "name.java"
        every { caret.currentCaret.hasSelection() } returns true
        every { actionEvent.presentation } returns presentation
        val sut = ObjectCreateFileSeletedAction()

        sut.update(actionEvent)

        assertTrue { actionEvent.presentation.isEnabledAndVisible }
        assertTrue { actionEvent.presentation.isEnabled }
        assertTrue { actionEvent.presentation.isVisible }
    }
}
