package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiParameter

interface OMParameter {
    fun getNameOrVoid(): String
    fun getClassCanonicalName(): String
    fun getTypes() : String?
}
