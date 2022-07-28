package inigo.objectMotherCreator.application


data class TypedClass(var className: String, var types: List<TypedClass> = mutableListOf()) {
    companion object {
        val regex = Regex("^([^<]*)<([^\$]*)>")

        fun findTypesFrom(canonicalText: String): List<TypedClass> {
            if (canonicalText.isEmpty()) {
                return mutableListOf()
            }
            var (type, value) = regex.find(canonicalText.trim())?.destructured
                ?: return mutableListOf(TypedClass(canonicalText.trim()))
            val types = mutableListOf<String>()
            var index: Int
            while (value.isNotEmpty()) {
                if (!value.contains("<")) { // A, B, C...
                    types.addAll(value.split(","))
                    value = ""
                } else if (value.contains(",")) { // A<B>, ...
                    if (value.indexOf(",") < value.indexOf("<")) {
                        types.add(value.substringBefore(","))
                        value = value.substringAfter(",")
                    } else {
                        index = indexOfTheFirstClosing(value)
                        types.add(value.substring(0, index))
                        value = value.substring(index)
                        if (value.isNotEmpty()) {
                            value = value.substringAfter(",")
                        }
                    }
                } else {
                    index = indexOfTheFirstClosing(value)
                    types.add(value.substring(0, index))
                    value = ""
                }
            }
            return mutableListOf(TypedClass(type.trim(), types.flatMap { findTypesFrom(it.trim()) }))
        }

        private fun indexOfTheFirstClosing(canonicalText: String): Int {
            var opening = 0
            var index = 0
            var i = 0
            canonicalText.chars().forEach {
                if (index != 0) {
                    return@forEach
                }
                // TODO change with '<'.code when it get removed
                if (it == '<'.toByte().toInt()) {
                    opening++
                } else {
                    if (it == '>'.toByte().toInt()) {
                        opening--
                        if (opening == 0) {
                            index = i + 1
                        }
                    }
                }
                i++
            }
            return index
        }
    }
}
