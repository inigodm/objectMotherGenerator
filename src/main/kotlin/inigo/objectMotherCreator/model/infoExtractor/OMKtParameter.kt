package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiParameter
import org.jetbrains.kotlin.idea.quickfix.expectactual.getTypeDescription
import org.jetbrains.kotlin.idea.refactoring.fqName.fqName
import org.jetbrains.kotlin.load.java.structure.impl.convertCanonicalNameToQName
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtTypeParameter
import org.jetbrains.kotlin.resolve.calls.callUtil.getType

data class OMKtParameter(private val inner: KtParameter) : OMParameter{
    override fun getNameOrVoid() = inner.name ?: ""
    override fun getClassCanonicalName() = inner.getTypeDescription().convertCanonicalNameToQName()
    override fun getTypes() : String? {
        return inner.getTypeDescription()
    }
}