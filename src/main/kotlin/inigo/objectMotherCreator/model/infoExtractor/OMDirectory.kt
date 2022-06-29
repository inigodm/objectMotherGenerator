package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.PsiDirectory

data class OMDirectory(val inner: PsiDirectory) {
    fun findSubdirectory(name: String): OMDirectory? {
        val subDirectory = inner.findSubdirectory(name) ?: return null
        return OMDirectory(subDirectory)
    }

    fun createSubdirectory(name: String): OMDirectory? {
        val createdSubdirectory = inner.createSubdirectory(name)
        return OMDirectory(createdSubdirectory)
    }

    fun getOMFile() = OMVirtualFile(inner.virtualFile)
}
