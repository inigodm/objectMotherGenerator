package inigo.objectMotherCreator.model.infoExtractor.om

interface OMMethod {
    fun getName(): String
    fun getParameters(): List<OMParameter>
}
