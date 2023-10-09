package inigo.objectMotherCreator.application.values.mappings

class ClassMapping(val imports : List<String> = listOf(), val generator: List<String>, val className: String) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ClassMapping) return false
            if (className != other.className) return false
            return true
        }

        override fun hashCode(): Int {
            return className.hashCode()
        }

        fun toCollection() : Collection<String> {
            return listOf(className, imports.joinToString(separator = ", "), generator.joinToString(separator = ", "))
        }

        fun randomValue() : String = generator.random()

        companion object {
            fun fromList(row: Collection<String>) : ClassMapping {
                return ClassMapping(
                    (row as List)[0].split(","),
                    row[1].split(","),
                    row[2]
                )
            }
        }
}
