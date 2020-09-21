package inigo.objectMotherCreator

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiJavaFile

interface FileInfo{
    fun classesToTread(): List<ClassInfo>
    fun packageName(): String
}

class JavaFileInfo(var root: PsiJavaFile, var project: Project): FileInfo{

    lateinit var packageStr: String
    lateinit var psiClasses: Array<out PsiClass>
    lateinit var mainClass: ClassInfo

    init{
        extractFileInfo()
    }

    fun extractFileInfo(){
        packageStr = root.packageStatement?.packageName ?: ""
        psiClasses = root.classes
        var main =  mainClass(psiClasses)
        mainClass = ClassInfo(main, packageStr, project)
    }

    override fun classesToTread(): List<ClassInfo>{
        return listOf(mainClass)
    }

    override fun packageName(): String {
        return packageStr
    }

    fun mainClass(psiClasses: Array<out PsiClass>): PsiClass {
        return psiClasses.filter {
            it.modifierList!!.text.contains("public")
        }.first()
    }
}
