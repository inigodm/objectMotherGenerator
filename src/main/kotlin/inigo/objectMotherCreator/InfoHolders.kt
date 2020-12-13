package inigo.objectMotherCreator

import com.intellij.psi.*
import inigo.objectMotherCreator.infraestructure.IdeaShits


class ClassInfo(var clazz: PsiClass, var packageName: String, var ideaShits: IdeaShits) {
    lateinit var fields: Array<out PsiField>
    lateinit var methods: Array<out PsiMethod>
    lateinit var constructors: List<MethodInfo>

    init {
        extractInfo()
    }

    fun extractInfo(){
        fields = clazz.allFields
        methods = clazz.allMethods
        constructors = clazz.constructors.map { MethodInfo(it, ideaShits) }.toList()
    }
}

class MethodInfo(var method: PsiMethod, var ideaShits: IdeaShits){
    var args =  mutableListOf<ParametersInfo>()
    lateinit var name: String
    init{
        extractMethodInfo()
    }

    fun extractMethodInfo(){
        name = method.name
        method.parameterList.parameters.map { args.add(ParametersInfo(it, ideaShits)) }
    }
}

class ParametersInfo(var param: PsiParameter, var ideaShits: IdeaShits){
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
        val clazz = ideaShits.findClass(aux)
        if (clazz != null) {
            clazzInfo = ClassInfo(clazz, clazz.qualifiedName!!.substringBeforeLast("."), ideaShits)
        }
    }
}
