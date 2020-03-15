package inigo.objectMotherCreator

import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiParameterList
import com.intellij.psi.PsiTypeElement

class ObjectMotherTemplate() {
    val classesToTreat = mutableListOf<PsiJavaClassInfo>()

    fun assignValues(clazz: PsiJavaClassInfo) {
        classesToTreat.add(clazz)
        while (classesToTreat.isNotEmpty()){
            var aux = buildClass(classesToTreat.removeAt(0))
            createFile(aux, "FILENAME")
        }

    }

    fun createFile(classString: String, fileName: String){
        println("BEGIN-----$fileName-------")
        println(classString)
        println("END-------$fileName-------")
    }

    fun buildClass(clazz: PsiJavaClassInfo): String{
        var res = buildPackage(clazz.packageName)
        res += buildImports(clazz.constructors.get(0))
        res += buildClass(clazz.clazz.name.toString() , clazz.constructors)
        return res
    }

    fun buildPackage(packageName: String): String{
        return "package $packageName\n\n"
    }

    fun buildImports(methodInfo: PsiMethodInfo): String{
        return "import com.github.javafaker.Faker;\n\n"
    }

    fun buildClass(className: String, constructors: List<PsiMethodInfo>): String{
        var res = "public class ${className}Mother{\n"
        constructors.forEach{ res += buildMotherConstructor(className, it)};
        return "$res\n}"
    }

    private fun buildMotherConstructor(className: String, methodInfo: PsiMethodInfo): Any? {
        return """  public static $className random$className(){
        Faker faker = new Faker();
        return new $className(${buildArgumentsData(methodInfo.args)});
    }"""
    }

    private fun buildArgumentsData(params: MutableList<PsiParametersInfo>): String {
        return params.map { createDefaultValueFor(it) }.joinToString { it }
    }

    private fun createDefaultValueFor(param: PsiParametersInfo): String {
        return when (param.name) {
            "String" -> {
                "\n\t\t\t\tfaker.ancient().hero()"
            }
            "int" -> {
                "\n\t\t\t\tfaker.number.randomNumber()"
            }
            "Integer" -> {
                "\n\t\t\t\tfaker.number.randomNumber()"
            }
            "long" -> {
                "\n\t\t\t\tfaker.number.randomLong()"
            }
            "Long" -> {
                "\n\t\t\t\tfaker.number.randomLong()"
            }
            else -> {
                var clazzInfo = param.clazzInfo
                if (clazzInfo != null){
                    classesToTreat.add(clazzInfo)
                    "\n\t\t\t\trandom${clazzInfo.clazz.name}()"
                }else{
                    "new ${param.name}()"
                }
            }
        }
    }
}
