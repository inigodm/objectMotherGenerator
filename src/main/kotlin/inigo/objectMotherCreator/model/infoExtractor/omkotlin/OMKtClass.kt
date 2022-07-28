package inigo.objectMotherCreator.model.infoExtractor.omkotlin

import com.intellij.psi.PsiElement
import inigo.objectMotherCreator.model.infoExtractor.om.OMClass
import inigo.objectMotherCreator.model.infoExtractor.om.OMMethod
import org.jetbrains.kotlin.psi.*

class OMKtClass(private val inner: KtClass, private val packageName: String) : OMClass {
    override fun isPublic() = inner.modifierList?.text?.contains("public") ?: true
    override fun getPackageName() = packageName
    override fun getAllConstructors() : List<OMMethod> {
        return getAllConstructorsOf(inner)
    }

    private fun getAllConstructorsOf(element: PsiElement) : List<OMMethod> {
        val result = mutableListOf<OMMethod>()
        var child: PsiElement? = element.firstChild
        while (child != null) {
            result.addAll(checkForConstructors(child))
            child = child.nextSibling
        }
        return result
    }

    private fun checkForConstructors(child: PsiElement?): List<OMMethod>  {
        if (child is KtConstructor<*>) {
            return listOf(OMKtMethod(child))
        }
        if (child is KtClassBody) {
            return getAllConstructorsOf(child)
        }
        return emptyList()
    }

    override fun getName() = inner.name
    override fun getQualifiedName() = "$packageName.${inner.name}"
}
