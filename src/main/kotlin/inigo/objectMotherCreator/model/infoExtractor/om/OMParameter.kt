package inigo.objectMotherCreator.model.infoExtractor.om

import com.intellij.psi.PsiParameter

interface OMParameter {
    fun getNameOrVoid(): String
    fun getClassCanonicalName(): String
    fun getTypes() : String?
}
