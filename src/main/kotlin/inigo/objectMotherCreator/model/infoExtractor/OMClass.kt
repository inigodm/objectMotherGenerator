package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiClass

data class OMClass(private val inner: PsiClass) {
    fun isPublic() = inner.modifierList!!.text.contains("public")
    fun getPackageName() = inner.qualifiedName!!.substringBeforeLast(".")
    fun getAllConstructors() = inner.constructors.map { OMMethod(it) }
    fun getName() = inner.name
    fun getQualifiedName() = inner.qualifiedName
}
