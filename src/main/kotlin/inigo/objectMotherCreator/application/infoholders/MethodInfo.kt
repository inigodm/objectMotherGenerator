package inigo.objectMotherCreator.application.infoholders

import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.model.infoExtractor.OMMethod

class MethodInfo(var method: OMMethod, var ideaShits: IdeaShits){
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
