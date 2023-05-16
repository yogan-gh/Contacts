package contacts

import java.io.File

const val FILE_NAME = "phonebook.db"


fun loadPhonebook(fileName: String): MutableList<Contact> {
    val phoneBook = mutableListOf<Contact>()
    fileName.let {
        if (File(it).exists()) phoneBook.load(it)
        println("open $it")
    }
    return phoneBook
}
fun main(arg: Array<String>) {
    val fileName: String? = arg.getOrNull(0)
    val phoneBook = fileName?.let { loadPhonebook(fileName) }?: mutableListOf<Contact>()
    val program = Console(phoneBook)
    while (program.mainMenu()) {
        fileName?.let { phoneBook.save(it) }
    }
}