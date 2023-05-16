package contacts

import java.time.LocalDate

class Person: Contact() {
    override val type: ContactType = ContactType.Person

    enum class Field {NAME, SURNAME, BIRTH, GENDER, NUMBER}
    enum class Gender {MALE, FEMALE}
    private var surname: String? = null
    private val genderStr: String
        get() = gender?.toString()?:"[no data]"
    private var dateOfBirth: String? = null
        get() = field?.ifEmpty { "[no data]" }
    private var gender: Gender? = null
    fun toData(str: String): LocalDate? {
        return try {
            LocalDate.parse(str)
        } catch (_: Exception) {
            println("Bad birth date!")
            null
        }
    }
    fun create(): Person {
        enterName()
        enterSurname()
        enterDOB()
        enterGender()
        enterNumber()
        return this
    }
    fun enterName() {
        println("Enter the name:")
        name = readln()
    }
    fun enterSurname() {
        println("Enter the surname:")
        surname = readln()
    }
    fun enterDOB() {
        println("Enter the birth date:")
        dateOfBirth = readln()
    }
    fun enterGender() {
        println("Enter the gender (M, F):")
        gender = when(readln()) {
            "M" -> Gender.MALE
            "F" -> Gender.FEMALE
            else -> {
                println("Bad gender!")
                null
            }
        }
    }
    override fun search(query: String): Boolean = "$number$name$surname$dateOfBirth$genderStr$timeCreate$timeLastEdit"
        .uppercase()
        .contains(Regex(query.uppercase()))

    override fun toInfo(): String {
        return """
            Name: $name
            Surname: $surname
            Birth date: $dateOfBirth
            Gender: $genderStr
        """.trimIndent() + super.toInfo()
    }
    override fun edit() {
        println("Select a field (${Field.values().joinToString(", ") { it.name.lowercase() }}):")
        try {
            when (Field.valueOf(readln().uppercase())) {
                Field.NAME -> enterName()
                Field.SURNAME -> enterSurname()
                Field.BIRTH -> enterDOB()
                Field.GENDER -> enterGender()
                Field.NUMBER -> enterNumber()
            }
            println("The record updated!")
        } catch (_:IllegalArgumentException) {
            println("Unknown field")
        }
        super.edit()
    }
    override fun toString(): String = "$name $surname"
}