package inigo.objectMotherCreator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiJavaFile

class ObjectCreateFileSeletedAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (isAnyJavaFileSelected(selectedFile)) {
            val testDir = obtainTestDirectory(project, e.getData(CommonDataKeys.VIRTUAL_FILE))
            val creator = ObjectMotherGenerator(testDir, e.getData(CommonDataKeys.PSI_FILE) as PsiJavaFile)
            creator.generateObjectMother(project);
        }
    }

    override fun update(e: AnActionEvent) {
        val selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = isAnyJavaFileSelected(selectedFile)
    }

    private fun isAnyJavaFileSelected(selectedFile: VirtualFile?) =
        selectedFile != null && selectedFile.toString().endsWith(".java")

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
