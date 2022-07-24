package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiParameter

data class OMJavaParameter(private val inner: PsiParameter) : OMParameter{
    override fun getNameOrVoid() = inner.typeElement?.type?.presentableText ?: ""
    override fun getClassCanonicalName() = inner.type.getCanonicalText(true)
    override fun getTypes() : String? {
        return inner.type.canonicalText
    }
}
