package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiMethod

data class OMJavaMethod(private val inner: PsiMethod): OMMethod {
    override fun getName() = inner.name
    override fun getParameters() = inner.parameterList.parameters.map { OMJavaParameter(it) }
}
