package cross.language

import ch.uzh.ifi.seal.changedistiller.JavaChangeDistillerModule
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller
import ch.uzh.ifi.seal.changedistiller.model.entities.*
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node
import com.google.inject.Guice
import cross.language.utils.FileInfo
import cross.language.utils.getFilesInFolder
import java.io.File


fun distill() {
    val patches: List<FileInfo> = getFilesInFolder("./data/patches/")
    var count = 0
    var countChangedFile = 0
    var countChange = 0
    for (patchInfo in patches) {
        val path = patchInfo.absolutePath
        if (path.contains("\\from\\").not() || path.endsWith(".java").not()) {
            continue
        }

        val left = File(path)
        val right = File(path.replace("\\from\\", "\\to\\"))
        if (right.exists().not()) {
            continue
        }

        val changes = changeDistillerRun(left, right)

//        for (change in changes) {
//            println(change)
//            if (change.changedEntity.node == null) {
//                continue
//            }
//            printTreeNode(change.changedEntity.node.root as Node)
//        }
        count++
        println(count)
        if (changes.isNotEmpty()) {
            countChangedFile++
            countChange += changes.size
        }
//        if (count > 50) {
//            break
//        }
    }
    println("changed files: $countChangedFile")
    println("change: $countChange")
    println("total: $count")
}

fun printTreeNode(node: Node, indent: String = "", isLast: Boolean = true) {
    val stringPrefix = "$indent${if (isLast) "└── " else "├── "}"
    val stringLabel = "${(node.label)}  "
    val stringValue = node.value.replace(
        "\n",
        "\n${indent}${if (isLast) "    " else "│   "}${if (node.children().toList().isEmpty()) " " else "│"}${" ".repeat(node.label.toString().length)}┆"
    )
    println(stringPrefix + stringLabel + stringValue)
    val children = node.children()
    while (children.hasMoreElements()) {
        val childNode = children.nextElement()
        val isLastChild = !children.hasMoreElements()
        val childIndent = "$indent${if (isLast) "    " else "│   "}"
        printTreeNode(childNode as Node, childIndent, isLastChild)
    }
}

fun changeDistillerRun(left: File, right: File): List<SourceCodeChange> {
    val injector = Guice.createInjector(JavaChangeDistillerModule())
    val distiller = injector.getInstance(FileDistiller::class.java)
    try {
        distiller.extractClassifiedSourceCodeChanges(left, right)
    } catch (e: Exception) {
        System.err.println("Warning: error while change distilling. " + e.message)
    }
    val changes = distiller.sourceCodeChanges ?: emptyList()
    return changes
}