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
            val testDir = obtainTestDirectory(project, e.getData(CommonDataKeys.VIRTUAL_FILE))
            val creator = ObjectMotherGenerator(testDir, e.getData(CommonDataKeys.PSI_FILE) as PsiJavaFile)
            creator.generateObjectMother(project);
        }
    }

    override fun update(e: AnActionEvent) {
        val caretModel = e.getRequiredData(CommonDataKeys.EDITOR).caretModel
        e.presentation.isEnabledAndVisible = caretModel.currentCaret.hasSelection()
    }

    private fun obtainTestDirectory(project: Project, vfile: VirtualFile?): String {
        val module = ModuleUtil.findModuleForFile(vfile!!, project)
        val tests = mutableListOf<VirtualFile>(*ModuleRootManager.getInstance(module!!).getSourceRoots(true))
        val srcs = listOf<VirtualFile>(*ModuleRootManager.getInstance(module).getSourceRoots(false))
        if (srcs.isEmpty()) throw RuntimeException("Source directory is not specified in project")
        tests.removeAll(srcs)
        val test = if (tests.isEmpty()) "test" else tests[0].name
        return vfile.canonicalPath!!.replace(srcs[0].name, test)
    }
}
