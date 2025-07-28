package org.example

import java.io.File

fun main() {
    val file = File("dictionary")
    file.createNewFile()

    for (i in file.readLines()) {
        print("$i\n")
    }
}