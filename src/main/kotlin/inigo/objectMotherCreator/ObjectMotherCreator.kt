package inigo.objectMotherCreator

import inigo.objectMotherCreator.infraestructure.JavaDirectory

class ObjectMotherCreator(var fileCreator: JavaFileCreator, var template: ObjectMotherTemplate) {
    val objectMotherFileNames = mutableListOf<String>()

    fun createObjectMotherFor(fileInfo: FileInfo, baseDir: JavaDirectory) {
        objectMotherFileNames.clear()
        val classesToTreat = mutableListOf<ClassInfo>()
        classesToTreat.addAll(fileInfo.classesToTread())
        while (classesToTreat.isNotEmpty()) {
            val clazzInfo = classesToTreat.removeAt(0);
            fileCreator.buildFile(baseDir, clazzInfo, template.buildObjectMotherCode(clazzInfo))
            objectMotherFileNames.add(fileCreator.createdFilename)
            classesToTreat.addAll(template.getNeededObjectMothers())
        }
    }
}
