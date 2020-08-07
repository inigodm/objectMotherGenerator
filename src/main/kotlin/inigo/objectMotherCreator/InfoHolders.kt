package inigo.objectMotherCreator

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope


class ClassInfo(var clazz: PsiClass, var packageName: String, var project: Project) {
    lateinit var fields: Array<out PsiField>
    lateinit var methods: Array<out PsiMethod>
    lateinit var constructors: List<MethodInfo>

    init {
        extractInfo()
    }

    fun extractInfo(){
        fields = clazz.allFields
        methods = clazz.allMethods
        constructors = clazz.constructors.map { MethodInfo(it, project) }.toList()
    }
}

class MethodInfo(var method: PsiMethod, var project: Project){
    var args =  mutableListOf<ParametersInfo>()
    lateinit var name: String
    init{
        extractMethodInfo()
    }

    fun extractMethodInfo(){
        name = method.name
        method.parameterList.parameters.map { args.add(ParametersInfo(it, project)) }
    }
}

class ParametersInfo(var param: PsiParameter, var project: Project){
    lateinit var name: String
    var clazzInfo: ClassInfo? = null

    init{
        extractParamInfo()
    }

    fun extractParamInfo(){
        name = param.typeElement?.type?.getPresentableText() ?: ""
        findClassInfoIfTypeDefinedInProject()
    }

    private fun findClassInfoIfTypeDefinedInProject() {
        val aux = param.type.getCanonicalText(true)
        val clazz = JavaPsiFacade.getInstance(project).findClass(aux, GlobalSearchScope.projectScope(project))
        if (clazz != null) {
            clazzInfo = ClassInfo(clazz, clazz.qualifiedName!!.substringBeforeLast("."), project)
        }
    }
}
