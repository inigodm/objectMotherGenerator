package inigo.objectMotherCreator.infraestructure

import com.intellij.openapi.vfs.VirtualFile

class OMFile(val inner: VirtualFile?) {
    override fun toString() = inner.toString()

    fun isNull() = inner == null
}
