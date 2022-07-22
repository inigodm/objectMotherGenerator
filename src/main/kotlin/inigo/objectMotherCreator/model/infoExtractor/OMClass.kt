package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiClass

interface OMClass {
    fun isPublic(): Boolean
    fun getPackageName(): String
    fun getAllConstructors(): List<OMMethod>
    fun getName(): String?
    fun getQualifiedName(): String?
}
