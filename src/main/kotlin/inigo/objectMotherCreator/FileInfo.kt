package inigo.objectMotherCreator

import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.infraestructure.JavaClass
import inigo.objectMotherCreator.infraestructure.JavaFile

interface FileInfo{
    fun classesToTread(): List<ClassInfo>
    fun packageName(): String
}

class JavaFileInfo(var root: JavaFile, var ideaShits: IdeaShits): FileInfo{

    lateinit var packageStr: String
    lateinit var javaClasses: List<JavaClass>
    lateinit var mainClass: ClassInfo

    init{
        extractFileInfo()
    }

    fun extractFileInfo(){
        packageStr = root.getPackageNameOrVoid()
        javaClasses = root.getClasses()
        val main =  mainClass(javaClasses)
        mainClass = ClassInfo(main, packageStr, ideaShits)
    }

    override fun classesToTread(): List<ClassInfo>{
        return listOf(mainClass)
    }

    override fun packageName(): String {
        return packageStr
    }

    fun mainClass(javaClasses: List<JavaClass>): JavaClass {
        return javaClasses.filter {
            it.isPublic()
        }.first()
    }
}
