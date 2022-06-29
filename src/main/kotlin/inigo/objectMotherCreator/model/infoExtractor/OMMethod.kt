package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiMethod

data class OMMethod(private val inner: PsiMethod) {
    fun getName() = inner.name
    fun getParameters() = inner.parameterList.parameters.map { OMParameter(it) }
}
