package inigo.objectMotherCreator.model.infoExtractor.omkotlin

import inigo.objectMotherCreator.model.infoExtractor.om.OMParameter
import org.jetbrains.kotlin.idea.refactoring.fqName.fqName
import org.jetbrains.kotlin.nj2k.postProcessing.type
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtUserType

data class OMKtParameter(private val inner: KtParameter) : OMParameter {
    override fun getNameOrVoid() : String {
        return (inner.typeReference!!.typeElement as KtUserType).referencedName!!
    }
    override fun getClassCanonicalName() : String {
      return inner.type()!!.fqName!!.asString()
    }
    override fun getTypes() : String? {
        return (inner.typeReference!!.typeElement as KtUserType).referenceExpression!!.getIdentifier()!!.text
    }
}
