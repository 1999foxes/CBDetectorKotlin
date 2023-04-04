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
    for (patchInfo in patches) {
        if (patchInfo.absolutePath.contains("\\from\\").not()) {
            continue
        }

        val left = File(patchInfo.absolutePath)
        val right = File(patchInfo.absolutePath.replace("\\from\\", "\\to\\"))
        if (right.exists().not()) {
            continue
        }

        if (count++ > 50) break  // debug
        val changes = distillerRun(left, right)

        for (change in changes) {
            println(change)
            println("old")
            getOldRoute(change).forEach {
                println(it.label)
                println("    " + it.value)
            }
            println("new")
            getNewRoute(change).forEach {
                println(it.label)
                println("    " + it.value)
            }
        }
    }
}

private fun getOldRoute(change: SourceCodeChange): List<Node> {
    if (change is Update) {
        println("change:::::" + change.changedEntity)
        println("changed node::::" + change.changedEntity.node)
    }
    val route = mutableListOf<Node>()
    var node = if (change is Delete || change is Move || change is Update) {
        change.changedEntity.node
    } else {
        null
    }
    while (node != null) {
        route.add(node)
        node = node.parent as Node?
    }
    return route.reversed()
}

private fun getNewRoute(change: SourceCodeChange): List<Node> {
    val route = mutableListOf<Node>()
    var node = when (change) {
        is Insert -> change.changedEntity.node
        is Move -> change.newEntity.node
        is Update -> change.newEntity.node
        else -> null
    }
    while (node != null) {
        route.add(node)
        node = node.parent as Node?
    }
    return route.reversed()
}

fun distillerRun(left: File, right: File): List<SourceCodeChange> {
    val injector = Guice.createInjector(JavaChangeDistillerModule())
    val distiller = injector.getInstance(FileDistiller::class.java)
    try {
        distiller.extractClassifiedSourceCodeChanges(left, right)
    } catch (e: Exception) {
        System.err.println("Warning: error while change distilling. " + e.message)
    }
    val changes = distiller.sourceCodeChanges ?: emptyList()
//    changes.forEach {
//        println(it.rootEntity)
//        println(it)
//    }
    return changes
}