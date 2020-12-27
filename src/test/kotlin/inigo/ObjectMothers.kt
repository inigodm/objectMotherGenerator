import inigo.objectMotherCreator.ClassInfo
import inigo.objectMotherCreator.MethodInfo
import inigo.objectMotherCreator.ParametersInfo
import io.mockk.every
import io.mockk.mockk

fun fixedMethodInfo() : MethodInfo  {
    val methodInfo = mockk<MethodInfo>()
    val parametersInfo = mockk<ParametersInfo>()
    every { parametersInfo.clazzInfo?.packageStr } returns "packagename"
    every { parametersInfo.name } returns "parameterName"
    every { parametersInfo.clazzInfo?.clazz?.getName() } returns "clazzName"
    every { parametersInfo.clazzInfo?.clazz?.getQualifiedName() } returns "qualified.clazzName"
    every { methodInfo.args } returns mutableListOf(parametersInfo)
    return methodInfo
}

fun fixedMethodInfo(type: String) : MethodInfo  {
    val methodInfo = mockk<MethodInfo>()
    val parametersInfo = mockk<ParametersInfo>()
    every { parametersInfo.clazzInfo?.packageStr } returns "packagename"
    every { parametersInfo.name } returns type
    every { parametersInfo.clazzInfo?.clazz?.getName() } returns type
    every { parametersInfo.clazzInfo?.clazz?.getQualifiedName() } returns "qualified.$type"
    every { methodInfo.args } returns mutableListOf(parametersInfo)
    return methodInfo
}

fun fixedClassInfo(): ClassInfo {
    val classInfo = mockk<ClassInfo>()
    every { classInfo.packageStr } returns "packagename"
    every { classInfo.clazz!!.getName() } returns "clazzname"
    every { classInfo.constructors } returns listOf()
    return classInfo

}
