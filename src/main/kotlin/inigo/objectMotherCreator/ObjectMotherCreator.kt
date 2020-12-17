package inigo.objectMotherCreator

import inigo.objectMotherCreator.infraestructure.JavaDirectory

class ObjectMotherCreator(var fileCreator: JavaFileCreator, var template: ObjectMotherTemplate) {
    val objectMotherFileNames = mutableListOf<String>()

    fun createObjectMotherFor(fileInfoExtractor: FileInfo, baseDir: JavaDirectory) {
        objectMotherFileNames.clear()
        val classesToTreat = mutableListOf<ClassInfo>()
        classesToTreat.addAll(fileInfoExtractor.classesToTread())
        while (classesToTreat.isNotEmpty()) {
            val clazzInfo = classesToTreat.removeAt(0);
            val filename = fileCreator.createFile(baseDir,
                    clazzInfo,
                    template.buildObjectMotherCode(clazzInfo))
            objectMotherFileNames.add(filename)
            classesToTreat.addAll(template.getNeededObjectMothers())
        }
    }
}
