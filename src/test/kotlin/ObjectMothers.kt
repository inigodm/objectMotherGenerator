import inigo.objectMotherCreator.ClassInfo
import inigo.objectMotherCreator.MethodInfo
import inigo.objectMotherCreator.ParametersInfo
import io.mockk.every
import io.mockk.mockk

fun fixedMethodInfo() : MethodInfo  {
    var methodInfo = mockk<MethodInfo>()
    var parametersInfo = mockk<ParametersInfo>()
    every { parametersInfo.clazzInfo?.packageName } returns "packagename"
    every { parametersInfo.name } returns "parameterName"
    every { parametersInfo.clazzInfo?.clazz?.name } returns "clazzName"
    every { parametersInfo.clazzInfo?.clazz?.qualifiedName } returns "qualified.clazzName"
    every { methodInfo.args } returns mutableListOf(parametersInfo)
    return methodInfo
}

fun fixedMethodInfo(type: String) : MethodInfo  {
    var methodInfo = mockk<MethodInfo>()
    var parametersInfo = mockk<ParametersInfo>()
    every { parametersInfo.clazzInfo?.packageName } returns "packagename"
    every { parametersInfo.name } returns type
    every { parametersInfo.clazzInfo?.clazz?.name } returns type
    every { parametersInfo.clazzInfo?.clazz?.qualifiedName } returns "qualified.$type"
    every { methodInfo.args } returns mutableListOf(parametersInfo)
    return methodInfo
}

fun fixedClassInfo(): ClassInfo {
    var classInfo = mockk<ClassInfo>()
    every { classInfo.packageName } returns "packagename"
    every { classInfo.clazz.name } returns "clazzname"
    every { classInfo.constructors } returns listOf()
    return classInfo

}
