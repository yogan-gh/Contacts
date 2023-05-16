package contacts

class Organization() : Contact() {
    override val type: ContactType = ContactType.Organization
    enum class Field {ADDRESS, NUMBER}
    private var address: String? = null

    fun create(): Contact {
        enterName()
        enterAddress()
        enterNumber()
        return this
    }
    fun enterName() {
        println("Enter the organization name:")
        name = readln()
    }
    fun enterAddress() {
        println("Enter the address:")
        address = readln()
    }
    override fun search(query: String): Boolean = "$number$name$address$timeCreate$timeLastEdit"
        .uppercase()
        .contains(Regex(query.uppercase()))
    override fun edit() {
        println("Select a field (${Organization.Field.values().joinToString(", ") { it.name.lowercase() }}):")
        try {
            when (Field.valueOf(readln().uppercase())) {
                Field.ADDRESS -> enterAddress()
                Field.NUMBER -> enterNumber()
            }
            println("The record updated!")
        } catch (_:IllegalArgumentException) {
            println("Unknown field")
        }
        super.edit()
    }
    override fun toInfo(): String {
        return """
            Organization name: $name
            Address: $address
        """.trimIndent() + super.toInfo()
    }
    override fun toString(): String = "$name"
}