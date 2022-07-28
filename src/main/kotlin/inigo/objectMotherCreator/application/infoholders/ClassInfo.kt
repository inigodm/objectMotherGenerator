package inigo.objectMotherCreator.application.infoholders

import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.model.infoExtractor.om.OMClass
import inigo.objectMotherCreator.model.infoExtractor.om.OMFile

class ClassInfo(
    var clazz: OMClass? = null,
    var packageStr: String = "",
    var root: OMFile? = null,
    var ideaShits: IdeaShits
) {
    var constructors: List<MethodInfo>

    init {
        if (clazz == null) {
            extractInfo()
        }
        constructors = clazz!!.getAllConstructors().map { MethodInfo(it, ideaShits) }.toList()
    }

    fun extractInfo(){
        val javaClasses = root!!.getClasses()
        clazz =  mainClass(javaClasses)
        packageStr = root!!.getPackageNameOrVoid()
    }

    fun mainClass(OMClasses: List<OMClass>): OMClass {
        return OMClasses.filter {
            it.isPublic()
        }.firstOrNull() ?: OMClasses[0]
    }
}
