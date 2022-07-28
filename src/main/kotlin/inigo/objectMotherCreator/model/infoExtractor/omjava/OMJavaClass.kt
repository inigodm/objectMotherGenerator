package inigo.objectMotherCreator.model.infoExtractor.omjava

import com.intellij.psi.PsiClass
import inigo.objectMotherCreator.model.infoExtractor.om.OMClass

open class OMJavaClass(private val inner: PsiClass): OMClass {
    override fun isPublic() = inner.modifierList!!.text.contains("public")
    override fun getPackageName() = inner.qualifiedName!!.substringBeforeLast(".")
    override fun getAllConstructors() = inner.constructors.map { OMJavaMethod(it) }
    override fun getName() = inner.name
    override fun getQualifiedName() = inner.qualifiedName
}
