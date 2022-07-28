package inigo.objectMotherCreator.model.infoExtractor.omjava

import com.intellij.psi.PsiMethod
import inigo.objectMotherCreator.model.infoExtractor.om.OMMethod

data class OMJavaMethod(private val inner: PsiMethod): OMMethod {
    override fun getName() = inner.name
    override fun getParameters() = inner.parameterList.parameters.map { OMJavaParameter(it) }
}
