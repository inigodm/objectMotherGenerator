package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

class OMFile(private val inner: PsiFile) {
    fun getPackageNameOrVoid() : String {
        return JavaDirectoryService.getInstance().getPackage(inner.containingDirectory)?.qualifiedName ?: ""
    }

    fun getClasses(): List<OMClass> {
        return PsiTreeUtil.getChildrenOfType(inner, PsiClass::class.java)!!.map { OMClass(it) }
    }

}
