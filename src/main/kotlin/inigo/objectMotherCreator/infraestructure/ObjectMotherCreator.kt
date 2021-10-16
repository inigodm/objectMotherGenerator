package inigo.objectMotherCreator.infraestructure

import inigo.objectMotherCreator.ClassInfo
import inigo.objectMotherCreator.JavaFileCreator
import inigo.objectMotherCreator.application.ObjectMotherTemplate

class ObjectMotherCreator(var fileCreator: JavaFileCreator, var template: ObjectMotherTemplate) {
    val objectMotherFileNames = mutableListOf<String>()

    fun createObjectMotherFor(fileInfo: ClassInfo, baseDir: JavaDirectory, extension: String) {
        objectMotherFileNames.clear()
        val classesToTreat = mutableListOf<ClassInfo>()
        classesToTreat.add(fileInfo)
        while (classesToTreat.isNotEmpty()) {
            val clazzInfo = classesToTreat.removeAt(0);
            fileCreator.buildFile(baseDir, clazzInfo, template.buildObjectMotherCode(clazzInfo), extension)
            objectMotherFileNames.add(fileCreator.createdFilename)
            classesToTreat.addAll(template.getNeededObjectMothers())
        }
    }
}
