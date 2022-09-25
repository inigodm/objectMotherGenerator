package inigo.objectMotherCreator.application

import inigo.objectMotherCreator.application.infoholders.ClassInfo
import inigo.objectMotherCreator.application.template.ObjectMotherTemplate
import inigo.objectMotherCreator.application.values.FakerGenerator
import inigo.objectMotherCreator.model.infoExtractor.om.OMDirectory

class ObjectMotherCreator(var fileCreator: JavaFileCreator, var template: ObjectMotherTemplate) {
    val objectMotherFileNames = mutableListOf<String>()

    fun createObjectMotherFor(
        classInfo: ClassInfo,
        baseDir: OMDirectory,
        extension: String,
        fakerValuesGenerator: FakerGenerator
    ) {
        fakerValuesGenerator.neededObjectMotherClasses.clear()
        val classesToTreat = mutableListOf(classInfo)
        while (classesToTreat.isNotEmpty()) {
            val clazzInfo = classesToTreat.removeAt(0)
            val objectMotherSourceCode = template.createObjectMotherSourceCode(clazzInfo)
            createObjectMotherFile(baseDir, clazzInfo, objectMotherSourceCode, extension)
            classesToTreat.addAll(fakerValuesGenerator.neededObjectMotherClasses)
        }
    }

    private fun createObjectMotherFile(baseDir: OMDirectory, clazzInfo: ClassInfo, objectMotherSourceCode: String,
                                       extension: String) {
        fileCreator.buildFile(baseDir, clazzInfo, objectMotherSourceCode, extension)
        objectMotherFileNames.add(fileCreator.createdFileName())
    }
}
