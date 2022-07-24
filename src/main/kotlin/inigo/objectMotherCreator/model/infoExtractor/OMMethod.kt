package inigo.objectMotherCreator.model.infoExtractor

interface OMMethod {
    fun getName(): String
    fun getParameters(): List<OMParameter>
}
