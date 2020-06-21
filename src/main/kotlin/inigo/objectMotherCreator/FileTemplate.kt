package inigo.objectMotherCreator

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import kotlin.Any
import kotlin.String
import kotlin.toString


class ObjectMotherBuilder(var root: PsiJavaFile, var project: Project) {
    val classesToTreat = mutableListOf<PsiJavaClassInfo>()
    var fileCreator = FileCreator(project)
    var template = ObjectMotherTemplate(root, project)

    fun buildFor(clazz: PsiJavaClassInfo) {
        classesToTreat.add(clazz)
        var clazzInfo : PsiJavaClassInfo
        var javaCode : String
        var directory: PsiDirectory?
        while (classesToTreat.isNotEmpty()) {
            clazzInfo = classesToTreat.removeAt(0);
            javaCode = template.buildJavaFile(clazzInfo)
            classesToTreat.addAll(template.neededObjectMotherClasses)
            directory = fileCreator.findOrCreateDirectoryForPackage(clazz.packageName, root)
            fileCreator.createJavaFile(directory!!, "${clazzInfo.clazz.name}ObjectMother.java", javaCode)
        }
    }

    fun createJavaFile(clazzInfo: PsiJavaClassInfo, javaCode: String) {
        var directory = fileCreator.findOrCreateDirectoryForPackage(clazzInfo.packageName, root)
        fileCreator.createJavaFile(directory!!, "${clazzInfo.clazz.name}ObjectMother.java", javaCode)
    }
}

class ObjectMotherTemplate(var root: PsiJavaFile, var project: Project) {
    val neededObjectMotherClasses = mutableListOf<PsiJavaClassInfo>()

    fun buildJavaFile(clazz: PsiJavaClassInfo): String {
        neededObjectMotherClasses.clear()
        var res = buildPackage(clazz.packageName)
        res += buildImports(clazz.constructors, clazz.packageName)
        return res + buildClass(clazz.clazz.name.toString(), clazz.constructors)
    }

    fun buildPackage(packageName: String): String {
        return "package $packageName;\n\n"
    }

    fun buildImports(methodsInfo: List<PsiMethodInfo>, packageName: String): String {
        var res = "import com.github.javafaker.Faker;\n\n"
        if (methodsInfo.isNotEmpty()) {
            var aux = methodsInfo.get(0).args.filter { it.clazzInfo?.packageName ?: "" != packageName }
                .filter { it.clazzInfo?.clazz ?: "" != "" }
                .map { "import static ${it.clazzInfo?.clazz?.qualifiedName}ObjectMother.random${it.clazzInfo?.clazz?.name}" }
                .joinToString(separator = ";\n")
            if (aux.isNotEmpty()) {
                res += "$aux;\n\n";
            }
        }
        return res
    }

    fun buildClass(className: String, constructors: List<PsiMethodInfo>): String {
        var res = "public class ${className}ObjectMother{\n"
        if (constructors.isNotEmpty()) {
            constructors.forEach { res += buildMotherConstructor(className, it) };
        } else {
            res += buildMotherConstructor(className)
        }
        return "$res\n}"
    }

    private fun buildMotherConstructor(className: String, methodInfo: PsiMethodInfo): Any? {
        return """  public static $className random$className(){
        Faker faker = new Faker();
        return new $className(${buildArgumentsData(methodInfo.args)});
    }"""
    }

    private fun buildMotherConstructor(className: String): Any? {
        return """  public static $className random$className(){
        return new $className();
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
                if (clazzInfo != null) {
                    neededObjectMotherClasses.add(clazzInfo)
                    "\n\t\t\t\trandom${clazzInfo.clazz.name}()"
                } else {
                    "new ${param.name}()"
                }
            }
        }
    }
}
