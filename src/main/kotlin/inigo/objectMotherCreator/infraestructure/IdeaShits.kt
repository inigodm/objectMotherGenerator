package inigo.objectMotherCreator.infraestructure

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import org.jetbrains.annotations.Nullable

class IdeaShits(val e: AnActionEvent) {
    fun getCurrentPSIFile(): PsiFile? {
        return e.getData(CommonDataKeys.PSI_FILE)!!
    }

    fun getCurrentVirtualFile(): VirtualFile {
        return e.getData(PlatformDataKeys.VIRTUAL_FILE)!!
    }

    fun isCaretInJavaFile() =
        e.getData(CommonDataKeys.PSI_FILE)!!.language.displayName.equals("java", ignoreCase = true)

    fun setMenuItemEnabled(enabled: Boolean) {
        e.presentation.isEnabledAndVisible = enabled
    }
}
