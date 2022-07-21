package inigo.objectMotherCreator.model.infoExtractor

import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile

class OMFile(private val inner: PsiFile) {
    fun getPackageNameOrVoid() : String {
        return JavaDirectoryService.getInstance().getPackage(inner.containingDirectory)?.qualifiedName ?: ""
    }

    fun getClasses(): List<OMClass> {
        inner.fileType
        return PsiTreeUtil.getChildrenOfTypeAsList(inner, KtClass::class.java).map { OMKtClass(it, (inner as KtFile).packageFqName?.asString() ?: "") }
            .plus(PsiTreeUtil.getChildrenOfTypeAsList(inner, PsiClass::class.java).map { OMJavaClass(it) })
    }
}
