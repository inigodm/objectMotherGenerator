package inigo.objectMotherCreator

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiJavaFile
import inigo.objectMotherCreator.infraestructure.IdeaShits
import java.io.File

class PluginLauncher {

    fun doObjectMotherCreation(ideaShits: IdeaShits) {
        val dir = ideaShits.obtainTestSourceDirectory() ?: return
        objectMotherCreation(ideaShits, dir)
    }

    private fun objectMotherCreation(ideaShits: IdeaShits, testSrcDir: PsiDirectory?) {
        val creator = ObjectMotherCreator(JavaFileCreator(ideaShits), JavaObjectMotherTemplate())
        val fileInfoExtractor = JavaFileInfo(ideaShits.getCurrentPSIFile() as PsiJavaFile, ideaShits)
        creator.createObjectMotherFor(fileInfoExtractor, testSrcDir)
        openFilesInEditor(ideaShits, creator.objectMotherFileNames)
    }

    private fun openFilesInEditor(ideaShits: IdeaShits, createdFiles : List<String>) {
        createdFiles.forEach {
            val file = File(it)
            if (file.exists()) {
                ideaShits.openFileInNewTab(file)
            }
        }
    }
}
