package cross.language.utils

fun main() {
    val files = getFilesInFolder("src/test/")

    for (file in files) {
        println("File name: ${file.fileName}")
        println("Relative path: ${file.relativePath}")
        println("Absolute path: ${file.absolutePath}")
    }
}