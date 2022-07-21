package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiClass

open class OMJavaClass(private val inner: PsiClass): OMClass {
    override fun isPublic() = inner.modifierList!!.text.contains("public")
    override fun getPackageName() = inner.qualifiedName!!.substringBeforeLast(".")
    override fun getAllConstructors() = inner.constructors.map { OMMethod(it) }
    override fun getName() = inner.name
    override fun getQualifiedName() = inner.qualifiedName
}
