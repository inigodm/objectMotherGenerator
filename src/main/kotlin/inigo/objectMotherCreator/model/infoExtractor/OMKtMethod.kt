package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.asJava.elements.KtLightMethod

data class OMKtMethod(private val inner: KtLightMethod) {
    fun getName() = inner.name
    fun getParameters() = inner.parameterList.parameters.map { OMParameter(it) }
}
