package contacts

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.FileReader
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class ContactsType {person, organization}

fun LocalDateTime.toStringDateTime(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
}
fun moshiAdapter(): JsonAdapter<List<Contact>> {
    val moshi = Moshi.Builder()
        .add(PolymorphicJsonAdapterFactory.of(
            Contact::class.java, "type")
            .withSubtype(
                Organization::class.java, ContactType.Organization.name)
            .withSubtype(
                Person::class.java, ContactType.Person.name)
        )
        .add(KotlinJsonAdapterFactory())
        .build()
    val type = Types.newParameterizedType(List::class.java, Contact::class.java)
    return moshi.adapter(type)?: throw Exception("Json error, moshi adapter not create")
}
fun MutableList<Contact>.save(fileName: String) {
    FileWriter(fileName).use {
        it.write(moshiAdapter().toJson(this))
    }
}
fun MutableList<Contact>.load(fileName: String) {
    val jsonText = FileReader(fileName).use {
        it.readText()
    }
    this.clear()
    this.addAll(moshiAdapter().fromJson(jsonText)?.toMutableList() ?: throw Exception("Json error, load failed"))
}
fun MutableList<Contact>.select(): Int? {
    println("Select a record:")
    readln().toIntOrNull()?.let {
        if ((it - 1) in this.indices) {
            return (it - 1)
        } else {
            println("Out of showInfo")
            return null
        }
    }?:run {
        println("Wrong index")
        return null
    }
}
fun MutableList<Contact>.add() {
    println("Enter the type (${ContactsType.values().joinToString(", ")}):")
    try {
        when (ContactsType.valueOf(readln())) {
            ContactsType.person -> this.add(Person().create())
            ContactsType.organization -> this.add(Organization().create())
        }
        println("The record added.")
    } catch (_:IllegalArgumentException) {
        println("Unknown type.")
    }
}
fun MutableList<Contact>.showInfo(): Boolean {
    if (this.isEmpty()) println("No records.").also { return false }
    else this.forEachIndexed { index, contact ->
        println("${index + 1}. $contact")
    }.also { return true }
}
fun MutableList<Contact>.showFullInfo(number: Int): Boolean {
    if (this.isEmpty())
        println("No records.").also { return false }
    else {
        if ((number - 1) in this.indices)
            println(this[number - 1].toInfo()).also { return true }
        else println("Wrong index").also { return false }
    }
}
fun MutableList<Contact>.showCount() {
    println("The Phone Book has ${this.size} records.")
}
fun MutableList<Contact>.remove(number: Int) {
    if (this.isEmpty()) println("No records to remove!")
    else {
        if ((number - 1) in this.indices) {
            this.removeAt(number - 1)
            println("The record removed!")
        }
        else println("Wrong index")
    }
}
fun MutableList<Contact>.edit(number: Int) {
    if (this.isEmpty()) println("No records to edit!")
    else {
        if ((number - 1) in this.indices) {
            this[number - 1].edit()
            println("Saved")
            this.showFullInfo(number - 1)
        }
        else println("Wrong index")
    }
}