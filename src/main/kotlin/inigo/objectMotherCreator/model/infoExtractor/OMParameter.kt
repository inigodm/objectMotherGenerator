package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiParameter

data class OMParameter(private val inner: PsiParameter) {
    fun getNameOrVoid() = inner.typeElement?.type?.presentableText ?: ""
    fun getClassCanonicalName() = inner.type.getCanonicalText(true)
    fun getTypes() : String? {
        return inner.type.canonicalText
    }
}
