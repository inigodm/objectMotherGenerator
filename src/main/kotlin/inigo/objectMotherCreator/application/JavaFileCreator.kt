package inigo.objectMotherCreator.application

import com.intellij.util.IncorrectOperationException
import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.infoExtractor.OMDirectory

interface JavaFileCreator {
    fun buildFile(baseDir: OMDirectory, clazzInfo: ClassInfo, javaCode: String, extension: String)

    fun createdFileName(): String
}
