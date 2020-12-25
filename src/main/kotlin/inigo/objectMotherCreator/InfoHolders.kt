package inigo.objectMotherCreator

import inigo.objectMotherCreator.infraestructure.*


class ClassInfo(var clazz: JavaClass, var packageName: String, var ideaShits: IdeaShits) {
    lateinit var methods: List<JavaMethod>
    lateinit var constructors: List<MethodInfo>

    init {
        extractInfo()
    }

    fun extractInfo(){
        methods = clazz.getAllMethods()
        constructors = clazz.getAllConstructors().map { MethodInfo(it, ideaShits) }.toList()
    }
}

class MethodInfo(var method: JavaMethod, var ideaShits: IdeaShits){
    var args =  mutableListOf<ParametersInfo>()
    lateinit var name: String
    init{
        extractMethodInfo()
    }

    fun extractMethodInfo(){
        name = method.getName()
        method.getParameters().map { args.add(ParametersInfo(it, ideaShits)) }
    }
}

class ParametersInfo(var param: JavaParameter, var ideaShits: IdeaShits){
    lateinit var name: String
    var clazzInfo: ClassInfo? = null

    init{
        extractParamInfo()
    }

    fun extractParamInfo(){
        name = param.getNameOrVoid()
        findClassInfoIfTypeDefinedInProject()
    }

    private fun findClassInfoIfTypeDefinedInProject() {
        val aux = param.getClassCanonicalName()
        val clazz = ideaShits.findClass(aux)
        if (clazz != null) {
            clazzInfo = ClassInfo(clazz, clazz.getPackageName(), ideaShits)
        }
    }
}
