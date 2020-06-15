package inigo.objectMotherCreator

import com.intellij.ide.IdeBundle
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.impl.file.PsiDirectoryFactory
import com.intellij.util.IncorrectOperationException
import java.io.File


class ObjectMotherTemplate(var root: PsiJavaFile, var project: Project) {
    val classesToTreat = mutableListOf<PsiJavaClassInfo>()

    fun assignValues(clazz: PsiJavaClassInfo) {
        classesToTreat.add(clazz)
        while (classesToTreat.isNotEmpty()) {
            var clazzInfo = classesToTreat.removeAt(0);
            var aux = buildJavaFile(clazzInfo)
            var directory = findOrCreateDirectoryForPackage(
                clazz.packageName,
                PsiDirectoryFactory.getInstance(project).createDirectory(findOrCreateSourceRoot())
            )
            println("BEGIN-----------")
            println(aux)
            println("END-------------")
            var file = createJavaFile(directory!!, "${clazzInfo.clazz.name}ObjectMother.java", aux)

        }
    }

    fun findOrCreateSourceRoot(): VirtualFile {
        return ModuleUtilCore.findModuleForFile(root)!!.rootManager!!.sourceRoots
            .filter { it.path.toLowerCase().endsWith("test") }
            .getOrElse(0) {
                return ApplicationManager.getApplication()
                    .runWriteAction<VirtualFile> {
                        return@runWriteAction project.baseDir.createChildDirectory(project.baseDir, "test")
                    }
            }
    }

    @Throws(IncorrectOperationException::class)
    fun findOrCreateDirectoryForPackage(packageName: String, baseDir: PsiDirectory?): PsiDirectory? {
        var packageName = packageName
        var psiDirectory: PsiDirectory?
        psiDirectory = baseDir

        packageName.split(".").forEach {
            val foundExistingDirectory = psiDirectory!!.findSubdirectory(it)
            if (foundExistingDirectory == null) {
                println("Creating $psiDirectory")
                psiDirectory = createSubdirectory(psiDirectory!!, it)
            } else {
                psiDirectory = foundExistingDirectory
            }
        }
        return psiDirectory
    }

    @Throws(IncorrectOperationException::class)
    private fun createJavaFile(directory: PsiDirectory, name: String, code: String) : PsiFile? {
        println("Creating file $name in $directory")
        return ApplicationManager.getApplication()
            .runWriteAction<PsiFile> {
                try {
                    return@runWriteAction PsiFileFactory.getInstance(project)
                            .createFileFromText(name, JavaFileType.INSTANCE, code).

                } catch (e: Exception) {
                    e.printStackTrace();
                    return@runWriteAction null
                }
            }
    }

    @Throws(IncorrectOperationException::class)
    private fun createSubdirectory(
        oldDirectory: PsiDirectory,
        name: String
    ): PsiDirectory? {
        val psiDirectory = arrayOfNulls<PsiDirectory>(1)
        val exception = arrayOfNulls<IncorrectOperationException>(1)
        CommandProcessor.getInstance().executeCommand(
            project,
            {
                psiDirectory[0] = ApplicationManager.getApplication()
                    .runWriteAction<PsiDirectory> {
                        try {
                            return@runWriteAction oldDirectory.createSubdirectory(name)
                        } catch (e: IncorrectOperationException) {
                            exception[0] = e
                            return@runWriteAction null
                        }
                    }
            }, IdeBundle.message("command.create.new.subdirectory"), null
        )
        if (exception[0] != null) throw exception[0]!!
        return psiDirectory[0]
    }


    fun createFile(classString: String) {
        println(classString)
    }

    fun buildJavaFile(clazz: PsiJavaClassInfo): String {
        var res = buildPackage(clazz.packageName)
        res += buildImports(clazz.constructors, clazz.packageName)
        return res + buildClass(clazz.clazz.name.toString(), clazz.constructors)
    }

    fun buildPackage(packageName: String): String {
        return "package $packageName\n\n"
    }

    fun buildImports(methodsInfo: List<PsiMethodInfo>, packageName: String): String {
        var res = "import com.github.javafaker.Faker;\n\n"
        if (methodsInfo.isNotEmpty()) {
            var aux = methodsInfo.get(0).args.filter { it.clazzInfo?.packageName ?: "" != packageName }
                .filter { it.clazzInfo?.clazz ?: "" != "" }
                .map { "import ${it.clazzInfo?.clazz?.qualifiedName}" }
                .joinToString(separator = ";\n")
            if (aux.isNotEmpty()) {
                res += "$aux;\n\n";
            }
        }
        return res
    }

    fun buildClass(className: String, constructors: List<PsiMethodInfo>): String {
        var res = "public class ${className}Mother{\n"
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
                    classesToTreat.add(clazzInfo)
                    "\n\t\t\t\trandom${clazzInfo.clazz.name}()"
                } else {
                    "new ${param.name}()"
                }
            }
        }
    }
}
