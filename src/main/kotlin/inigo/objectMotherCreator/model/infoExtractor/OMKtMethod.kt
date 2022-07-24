package inigo.objectMotherCreator.model.infoExtractor

import org.jetbrains.kotlin.psi.KtConstructor
import org.jetbrains.kotlin.psi.KtParameter

data class OMKtMethod<T : KtConstructor<T>>(private val inner: KtConstructor<T>): OMMethod {
    override fun getName() = inner.name ?: "NONAME"
    override fun getParameters(): List<OMParameter> {
        return inner.valueParameters.map {
            OMKtParameter(it)
        }
    }
}
