package inigo.objectMotherCreator.infraestructure.config

import com.google.gson.Gson
import com.intellij.util.xmlb.Converter
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


internal class MappingConverter : Converter<MutableList<Collection<String>>>() {


    override fun fromString(value: String): MutableList<Collection<String>> {
        val type: Type = object : TypeToken<List<Collection<String>>>() {}.getType()
        return Gson().fromJson(value, type)
    }

    override fun toString(value: MutableList<Collection<String>>): String {
        return Gson().toJson(value)
    }
}
