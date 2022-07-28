package inigo.objectMotherCreator.application

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.model.infoExtractor.om.OMDirectory

interface JavaFileCreator {
    fun buildFile(baseDir: OMDirectory, clazzInfo: ClassInfo, javaCode: String, extension: String)

    fun createdFileName(): String
}
