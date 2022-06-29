package inigo.objectMotherCreator.application

import com.intellij.util.IncorrectOperationException
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.infoExtractor.OMDirectory

interface JavaFileCreator {
    @Throws(IncorrectOperationException::class)
    fun createFile(directory: OMDirectory, name: String, code: String)

    @Throws(IncorrectOperationException::class)
    fun findOrCreateDirectoryForPackage(packageName: String, srcDirectory: OMDirectory): OMDirectory?

    fun buildFile(baseDir: OMDirectory, clazzInfo: ClassInfo, javaCode: String, extension: String)

    fun createdFileName(): String
}
