package inigo.objectMotherCreator

import inigo.objectMotherCreator.infraestructure.*


class ClassInfo(var clazz: JavaClass? = null, var packageStr: String = "", var root: JavaFile? = null, var ideaShits: IdeaShits) {
    lateinit var methods: List<JavaMethod>
    lateinit var constructors: List<MethodInfo>

    init {
        if (clazz == null) {
            extractInfo()
        } else {
            methods = clazz!!.getAllMethods()
            constructors = clazz!!.getAllConstructors().map { MethodInfo(it, ideaShits) }.toList()
        }
    }

    fun extractInfo(){
        val javaClasses = root!!.getClasses()
        clazz =  mainClass(javaClasses)
        methods = clazz!!.getAllMethods()
        packageStr = root!!.getPackageNameOrVoid()
        constructors = clazz!!.getAllConstructors().map { MethodInfo(it, ideaShits) }.toList()
    }

    fun mainClass(javaClasses: List<JavaClass>): JavaClass {
        return javaClasses.filter {
            it.isPublic()
        }.first()
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
            clazzInfo = ClassInfo(clazz, clazz.getPackageName(), ideaShits = ideaShits)
        }
    }
}
