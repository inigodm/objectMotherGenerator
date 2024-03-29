package inigo.objectMotherCreator.model.infoExtractor.om

import com.intellij.openapi.vfs.VirtualFile

class OMVirtualFile(val inner: VirtualFile?) {
    override fun toString() = inner.toString()
    fun getCanonicalPath() = inner?.canonicalPath
}

