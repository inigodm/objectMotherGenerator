package inigo.objectMotherCreator

import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiParameterList
import com.intellij.psi.PsiTypeElement
import com.intellij.psi.util.PsiTypesUtil

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
        return res
    }

    private fun buildMotherConstructor(className: String, methodInfo: PsiMethodInfo): Any? {
        return """  public static $className random$className(){
        Faker faker = new Faker();
        return new $className(${buildArgumentsData(methodInfo.args)});
    }"""
    }

    private fun buildArgumentsData(params: MutableList<PsiParametersInfo>): String {
        var res = ""
        params.map { res += createDefaultValueFor(it) }
        return res
    }

    private fun createDefaultValueFor(param: PsiParametersInfo): Any {
        return when (param.name) {
            "String" -> {
                "\n\t\t\t\tfaker.ancient().hero(),"
            }
            "int" -> {
                "\n\t\t\t\tfaker.number.randomNumber(),"
            }
            "Integer" -> {
                "\n\t\t\t\tfaker.number.randomNumber(),"
            }
            "long" -> {
                "\n\t\t\t\tfaker.number.randomLong(),"
            }
            "Long" -> {
                "\n\t\t\t\tfaker.number.randomLong(),"
            }
            else -> {
                if (param.clazzInfo != null){
                    classesToTreat.add(param.clazzInfo!!)
                    "\n\t\t\t\trandom${param.clazzInfo!!.clazz?.name ?: param.name}(),"
                }else{
                    "new ${param.name}"
                }
            }
        }
    }


    fun buildArguments(constructorArguments: PsiParameterList): String{
        return constructorArguments
            .parameters
            .map {
                buildArgument(it)
            }.joinToString { it }
    }

    fun buildArgument(clazz: PsiParameter): String{
        val name: String = clazz.type.presentableText
        val typeElementString: String = clazz.toString()
        return getRandomObjectOfType(clazz.typeElement!!)
    }

    private fun psiTypeToString(typeElement: PsiTypeElement): String? {
        val name: String = typeElement.type.presentableText
        val typeElementString: String = typeElement.toString()
        val dot = typeElementString.indexOf(".")
        if (dot != -1) {
            val outerClasses =
                typeElementString.substring(typeElementString.indexOf(":") + 1, typeElementString.lastIndexOf("."))
            return outerClasses.replace("\\.".toRegex(), "\\$") + "$" + name
        }
        return name
    }

    fun getRandomObjectOfType(className: PsiTypeElement): String {
        return when (className.text.toString()) {
            "String" -> {
                "\n\t\t\tfaker.ancient().hero()"
            }
            "int" -> {
                "\n\t\t\tfaker.number.randomNumber()"
            }
            "Integer" -> {
                "\n\t\t\tfaker.number.randomNumber()"
            }
            "long" -> {
                "\n\t\t\tfaker.number.randomLong()"
            }
            "Long" -> {
                "\n\t\t\tfaker.number.randomLong()\n"
            }
            else -> {
                //JavaPsiFacade.getInstance().findClass()
                "\n\t\t\t$className()"
            }
        }
    }
}
