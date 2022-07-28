package inigo.objectMotherCreator.model.infoExtractor.omkotlin

import inigo.objectMotherCreator.model.infoExtractor.om.OMMethod
import inigo.objectMotherCreator.model.infoExtractor.om.OMParameter
import org.jetbrains.kotlin.psi.KtConstructor

data class OMKtMethod<T : KtConstructor<T>>(private val inner: KtConstructor<T>): OMMethod {
    override fun getName() = inner.name ?: "NONAME"
    override fun getParameters(): List<OMParameter> {
        return inner.valueParameters.map {
            OMKtParameter(it)
        }
    }
}
