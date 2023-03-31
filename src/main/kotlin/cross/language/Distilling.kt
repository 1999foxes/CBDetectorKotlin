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

        if (count++ > 0) break  // debug
        val changes = distillerRun(left, right)

        for (change in changes) {
            println(change)
            println(getOldRoute(change))
            println(getNewRoute(change))
        }
    }
}

private val nodePublisherList: MutableList<MutableList<Node>> = mutableListOf()
fun getNodePublisher(): MutableList<Node> {
    val publisher = mutableListOf<Node>()
    nodePublisherList.add(publisher)
    return publisher
}

private fun getOldRoute(change: SourceCodeChange): List<Node> {
    val route = mutableListOf<Node>()
    if (change is Delete || change is Move || change is Update) {
        nodePublisherList[0].forEach { node ->
            if (nodeDFSWithRoute(node, route) {
                    it.entity == change.changedEntity
                }) {
                return route
            }
        }
    }
    return route
}

private fun getNewRoute(change: SourceCodeChange): List<Node> {
    val route = mutableListOf<Node>()
    if (change is Insert || change is Move || change is Update) {
        nodePublisherList[1].forEach { node ->
            if (nodeDFSWithRoute(node, route) {
                    it.entity == change.changedEntity
                }) {
                return route
            }
        }
    }
    return route
}

private fun nodeDFSWithRoute(node: Node, route: MutableList<Node>, callback: (Node) -> Boolean): Boolean {
    route.add(node)
    if (callback(node)) {
        return true
    } else {
        for (child in node.children()) {
            if (nodeDFSWithRoute(child as Node, route, callback)) {
                return true
            }
        }
        route.removeLast()
        return false
    }
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