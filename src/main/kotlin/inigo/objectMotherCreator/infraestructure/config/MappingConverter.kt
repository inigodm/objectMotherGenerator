package inigo.objectMotherCreator.infraestructure.config

import com.google.gson.Gson
import com.intellij.util.xmlb.Converter
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.*


internal class MappingConverter : Converter<Vector<Vector<String>>>() {


    override fun fromString(value: String): Vector<Vector<String>> {
        val type: Type = object : TypeToken<Vector<Vector<String>>>() {}.getType()
        return Gson().fromJson(value, type)
    }

    override fun toString(value: Vector<Vector<String>>): String {
        return Gson().toJson(value)
    }
}
