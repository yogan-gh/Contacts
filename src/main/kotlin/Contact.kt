package contacts

import java.time.LocalDateTime

enum class ContactType {
    Organization,
    Person
}

abstract class Contact {
    abstract val type: ContactType
    protected var name: String? = null
    protected var number: String? = null
        get() = field?:"[no number]"
        set(value) {
            field = value?.let {
                if ("""\+?\w*(\d[ -])?(\(\w{2,}\))?(\w{2,}[ -]\(\w{2,}\))?(\b\w{2,})?([ -]\w{2,})*""".toRegex().matches(value))
                    value
                else {
                    println("Wrong number format!")
                    null
                }
            }
        }
    protected val timeCreate: String = LocalDateTime.now().toStringDateTime()
    protected var timeLastEdit: String = LocalDateTime.now().toStringDateTime()
    fun enterNumber() {
        println("Enter the number:")
        number = readln()
    }
    abstract fun search(query: String): Boolean
    open fun edit() {
        timeLastEdit = LocalDateTime.now().toStringDateTime()
    }
    open fun toInfo(): String {
        return """
            
            Number: $number
            Time created: $timeCreate
            Time last edit: $timeLastEdit
        """.trimIndent()
    }
}