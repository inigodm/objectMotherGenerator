package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiPlainTextFile
import java.util.*

class ObjectCreateAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val caretModel = e.getRequiredData(CommonDataKeys.EDITOR).caretModel
        if (caretModel.currentCaret.hasSelection()
            && e.getData(CommonDataKeys.PSI_FILE)!!.language.displayName.equals("java", ignoreCase = true)) {
            val creator = ObjectMotherGenerator(e.getData(CommonDataKeys.PSI_FILE) as PsiJavaFile)
            creator.generateObjectMother(project);
        }
    }

    override fun update(e: AnActionEvent) {
        val caretModel = e.getRequiredData(CommonDataKeys.EDITOR).caretModel
        e.presentation.isEnabledAndVisible = caretModel.currentCaret.hasSelection()
    }
}

class ObjectCreateFileSeletedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (isAnyJavaFileSelected(selectedFile)) {
            val creator = ObjectMotherGenerator(e.getData(CommonDataKeys.PSI_FILE) as PsiJavaFile)
            creator.generateObjectMother(project);
        }
    }

    override fun update(e: AnActionEvent) {
        val selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = isAnyJavaFileSelected(selectedFile)
    }

    private fun isAnyJavaFileSelected(selectedFile: VirtualFile?) =
        selectedFile != null && selectedFile.toString().endsWith(".java")
}
