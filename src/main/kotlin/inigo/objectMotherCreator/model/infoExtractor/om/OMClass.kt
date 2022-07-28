package inigo.objectMotherCreator.model.infoExtractor.om

interface OMClass {
    fun isPublic(): Boolean
    fun getPackageName(): String
    fun getAllConstructors(): List<OMMethod>
    fun getName(): String?
    fun getQualifiedName(): String?
}
