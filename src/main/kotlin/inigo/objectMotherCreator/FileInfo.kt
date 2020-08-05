package inigo.objectMotherCreator

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiClass

class JavaFileInfo(var root: PsiJavaFile, var project: Project){

    lateinit var packageStr: String
    lateinit var psiClasses: Array<out PsiClass>
    lateinit var mainClass: ClassInfo

    init{
        extractFileInfo()
    }

    fun extractFileInfo(){
        packageStr = root.packageStatement?.packageName ?: ""
        psiClasses = root.classes
        mainClass = ClassInfo(mainClass(psiClasses), packageStr, project)
    }

    fun mainClass(psiClasses: Array<out PsiClass>): PsiClass {
        psiClasses.forEach { println(it.text) }
        return psiClasses.filter {
            it.modifierList!!.text.contains("public")
        }.first()
    }
}
