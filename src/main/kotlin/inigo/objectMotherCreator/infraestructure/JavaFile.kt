package inigo.objectMotherCreator.infraestructure

import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil

class JavaFile(val inner: PsiFile) {
    fun getPackageNameOrVoid() : String {
        return JavaDirectoryService.getInstance().getPackage(inner.containingDirectory)?.qualifiedName ?: ""
    }

    fun getClasses(): List<JavaClass> {
        return PsiTreeUtil.getChildrenOfType(inner, PsiClass::class.java).map { JavaClass(it) }
    }

}

data class JavaClass(private val inner: PsiClass) {
    fun isPublic() = inner.modifierList!!.text.contains("public")
    fun getPackageName() = inner.qualifiedName!!.substringBeforeLast(".")
    fun getAllMethods() = inner.methods.map { JavaMethod(it) }
    fun getAllConstructors() = inner.constructors.map { JavaMethod(it) }
    fun getName() = inner.name
    fun getQualifiedName() = inner.qualifiedName
}

data class JavaMethod(private val inner: PsiMethod) {
    fun getName() = inner.name
    fun getParameters() = inner.parameterList.parameters.map { JavaParameter(it) }
}

data class JavaParameter(private val inner: PsiParameter) {
    fun getNameOrVoid() = inner.typeElement?.type?.getPresentableText() ?: ""
    fun getClassCanonicalName() = inner.type.getCanonicalText(true)
}

data class JavaDirectory(val inner: PsiDirectory) {
    fun findSubdirectory(name: String): JavaDirectory? {
        val subDirectory = inner.findSubdirectory(name) ?: return null
        return JavaDirectory(subDirectory)
    }

    fun createSubdirectory(name: String): JavaDirectory? {
        val createdSubdirectory = inner.createSubdirectory(name) ?: return null
        return JavaDirectory(createdSubdirectory)
    }

    fun getOMFile() = OMFile(inner.virtualFile)
}
