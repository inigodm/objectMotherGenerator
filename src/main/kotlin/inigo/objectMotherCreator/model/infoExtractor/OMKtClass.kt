package inigo.objectMotherCreator.model.infoExtractor

import org.jetbrains.kotlin.psi.KtClass

class OMKtClass(private val inner: KtClass, private val packageName: String) : OMClass {
    override fun isPublic() = inner.modifierList?.text?.contains("public") ?: true
    override fun getPackageName() = packageName
    override fun getAllConstructors() = mutableListOf<OMMethod>()
    override fun getName() = inner.name
    override fun getQualifiedName() = "$packageName.${inner.name}"
}
