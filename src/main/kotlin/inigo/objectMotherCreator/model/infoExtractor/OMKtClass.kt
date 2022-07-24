package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.*

class OMKtClass(private val inner: KtClass, private val packageName: String) : OMClass {
    override fun isPublic() = inner.modifierList?.text?.contains("public") ?: true
    override fun getPackageName() = packageName
    override fun getAllConstructors() : List<OMMethod> {
        val result = mutableListOf<OMMethod>()
        var child: PsiElement? = inner.getFirstChild()
            while (child != null) {
                if (child is KtConstructor<*>) {
                    result.add(OMKtMethod(child))
                }
                child = child.nextSibling
            }
        return result
    }
    override fun getName() = inner.name
    override fun getQualifiedName() = "$packageName.${inner.name}"
}
