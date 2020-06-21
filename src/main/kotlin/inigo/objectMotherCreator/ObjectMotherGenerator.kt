package inigo.objectMotherCreator

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope

class ObjectMotherGenerator(var root: PsiJavaFile) {
    lateinit var packageStr: String
    lateinit var childClassName: String


    fun generateObjectMother(project: Project){
        packageStr = root.packageStatement?.packageName ?: "";
        childClassName = root.name.substringBefore(".");
        val template = ObjectMotherBuilder(root, project)
        val infoExtractor = PsiJavaFileInfo(root, project)
        template.buildFor(infoExtractor.mainClass)
    }
}

class PsiJavaFileInfo(var root: PsiJavaFile, var project: Project){

    lateinit var packageStr: String
    lateinit var psiClasses: Array<out PsiClass>
    lateinit var mainClass: PsiJavaClassInfo
    var imports: Array<out PsiImportStatementBase>? = null

    init{
        extractFileInfo()
    }

    fun extractFileInfo(){
        packageStr = root.packageStatement?.packageName ?: "";
        imports = root.importList?.allImportStatements
        psiClasses = root.classes
        mainClass = PsiJavaClassInfo(mainClass(psiClasses), packageStr, project)
    }

    fun mainClass(psiClasses: Array<out PsiClass>): PsiClass{
        return psiClasses.filter { it.modifierList!!.textMatches("public") }.first()
    }

    }

class PsiJavaClassInfo(var clazz: PsiClass, var packageName: String, var project: Project){
    lateinit var fields: Array<out PsiField>
    lateinit var methods: Array<out PsiMethod>
    lateinit var constructors: List<PsiMethodInfo>
    init{
        extractClassInfo()
    }

    fun extractClassInfo(){
        fields = clazz.allFields
        methods = clazz.allMethods
        constructors = clazz.constructors.map { PsiMethodInfo(it, project) }.toList()
    }
}

class PsiMethodInfo(var method: PsiMethod, var project: Project){
    var args =  mutableListOf<PsiParametersInfo>()
    lateinit var name: String
    init{
        extractMethodInfo()
    }

    fun extractMethodInfo(){
        name = method.name
        method.parameterList
            .parameters.map { args.add(PsiParametersInfo(it, project)) }
    }

}

class PsiParametersInfo(var param: PsiParameter, var project: Project){
    lateinit var name: String
    var clazzInfo: PsiJavaClassInfo? = null

    init{
        extractParamInfo()
    }

    fun extractParamInfo(){
        name = param.typeElement?.type?.getPresentableText() ?: ""
        var aux = param.type.getCanonicalText(true)
        var clazz = JavaPsiFacade.getInstance(project)
            .findClass(aux, GlobalSearchScope.projectScope(project))
        if (clazz != null) {
            clazzInfo = PsiJavaClassInfo(clazz, clazz.qualifiedName!!.substringBefore(name), project)
        }
    }
}
