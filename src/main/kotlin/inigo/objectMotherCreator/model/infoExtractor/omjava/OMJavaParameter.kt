package inigo.objectMotherCreator.model.infoExtractor.omjava

import com.intellij.psi.PsiParameter
import inigo.objectMotherCreator.model.infoExtractor.om.OMParameter

data class OMJavaParameter(private val inner: PsiParameter) : OMParameter {
    override fun getNameOrVoid() : String {
        //TODO first try should be deleted?¿
        return inner.typeElement?.type?.presentableText ?: inner.type.presentableText
    }
    override fun getClassCanonicalName() = inner.type.getCanonicalText(true)
    override fun getTypes() : String {
        return inner.type.canonicalText
    }
}
