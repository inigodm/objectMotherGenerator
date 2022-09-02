package inigo.objectMotherCreator.application.infoholders

import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.model.infoExtractor.om.OMParameter

class ParametersInfo(var param: OMParameter, var ideaShits: IdeaShits) {
    lateinit var name: String
    var clazzInfo: ClassInfo? = null

    init {
        extractParamInfo()
    }

    fun extractParamInfo() {
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
