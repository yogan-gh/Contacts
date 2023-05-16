package contacts


class Console(val phoneBook: MutableList<Contact>) {
    enum class CommandMainMenu {add, list, search, count, exit}
    enum class CommandSearchMenu {number, back, again}
    enum class CommandRecordMenu {edit, delete, menu}

    fun mainMenu(): Boolean {
        println("\n[menu] Enter action (${CommandMainMenu.values().joinToString()}):")
        when (CommandMainMenu.valueOf(readln())) {
            CommandMainMenu.add -> phoneBook.add()
            CommandMainMenu.list -> {
                if (phoneBook.showInfo())
                    listMenu()
            }
            CommandMainMenu.search -> {
                do {
                    var again = false
                    println("Enter search query:")
                    val query = readln()
                    val searchResult = phoneBook.filter { it.search(query) }.toMutableList()
                    if (searchResult.isEmpty()) {
                        println("Not found")
                        continue
                    }
                    else {
                        searchResult.showInfo()
                        again = searchMenu(searchResult)
                    }
                } while (again)
            }
            CommandMainMenu.count -> phoneBook.showCount()
            CommandMainMenu.exit -> return false
        }
        return true
    }
    fun listMenu() {
        println("\n[list] Enter action ([number], back):")
        when (val input = readln()) {
            "back" -> return
            else -> input.toIntOrNull()?.let {
                phoneBook.showFullInfo(it)
                recordMenu(it - 1)
            }?: println("Unknown command")
        }
    }
    fun recordMenu(recordIndex: Int) {
        while (true) {
            println("\n[record] Enter action (edit, delete, menu):")
            when (readln()) {
                "edit" -> phoneBook[recordIndex].edit()
                "delete" -> phoneBook.remove(recordIndex)
                "menu" -> return
                else -> println("Unknown command")
            }
        }
    }
    fun searchMenu(searchResult: MutableList<Contact>): Boolean {
        println("\n[search] Enter action ([number], back, again):")
        return when (val input = readln()) {
            "back" -> false
            "again" -> true
            else -> {
                input.toIntOrNull()?.let {
                    searchResult.showFullInfo(it)
                    recordMenu(it - 1)
                } ?: println("Unknown command")
                false
            }
        }
    }

}