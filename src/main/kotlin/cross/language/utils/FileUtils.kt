package cross.language.utils

import java.io.File

data class FileInfo(val fileName: String, val relativePath: String, val absolutePath: String)

fun getFilesInFolder(folderPath: String): List<FileInfo> {
    val files = mutableListOf<FileInfo>()
    val folder = File(folderPath)

    folder.walkTopDown().forEach {
        if (it.isFile) {
            val fileName = it.name
            val relativePath = it.relativeTo(folder).path
            val absolutePath = it.absolutePath
            files.add(FileInfo(fileName, relativePath, absolutePath))
        }
    }

    return files
}