package cross.language.ast

import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node
import java.util.ArrayDeque

fun JavaStructureNode.print(indent: String = "", isLast: Boolean = true) {
    val stringPrefix = "$indent${if (isLast) "└── " else "├── "}"
    val stringLabel = "${this.type}  "
    val stringValue = this.fullyQualifiedName
    println("$stringPrefix$stringLabel$stringValue")
    for (i in this.children.indices) {
        val childNode = this.children[i]
        val childIndent = "$indent${if (isLast) "    " else "│   "}"
        childNode.print(childIndent, i == this.children.lastIndex)
    }
}

fun JavaStructureNode.bfs(
    onVisit: (JavaStructureNode) -> Unit = {},
    onEndVisit: (JavaStructureNode) -> Unit = {},
    breakCondition: (JavaStructureNode) -> Boolean = { false }
): JavaStructureNode? {
    val queue = ArrayDeque<JavaStructureNode>()
    queue.addLast(this)
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        onVisit(node)
        if (breakCondition(node)) {
            return node
        }
        for (child in node.children) {
            queue.addLast(child)
        }
        onEndVisit(node)
    }
    return null
}

fun JavaStructureNode.dfs(
    onVisit: (JavaStructureNode) -> Unit = {},
    onEndVisit: (JavaStructureNode) -> Unit = {},
    breakCondition: (JavaStructureNode) -> Boolean = { false }
): JavaStructureNode? {
    onVisit(this)
    if (breakCondition(this)) {
        return this
    }
    for (child in this.children) {
        val result = child.dfs(onVisit, onEndVisit, breakCondition)
        if (result != null) {
            return result
        }
    }
    onEndVisit(this)
    return null
}

fun Node.print(indent: String = "", isLast: Boolean = true) {

    if (this.label == JavaEntityType.WHILE_STATEMENT) {
        1
    }
    if (this.label == JavaEntityType.TRY_STATEMENT) {
        3
    }


    val children = this.children().toList()
    val stringPrefix = "$indent${if (isLast) "└── " else "├── "}"
    val stringLabel = "${this.label}  "
    val stringValue = this.value
        .replace(
            "\n",
            "\n$indent${if (isLast) "    " else "│   "}${if (children.isEmpty()) " " else "│"}${
                " ".repeat(
                    stringLabel.length
                )
            }┆"
        )
    println("$stringPrefix$stringLabel$stringValue")
    for (i in children.indices) {
        val childNode = children[i] as Node
        val childIndent = "$indent${if (isLast) "    " else "│   "}"
        childNode.print(childIndent, i == children.lastIndex)
    }
}

fun Node.add(nodes: List<Node>) {
    nodes.forEach {
        this.add(it)
    }
}

@Suppress("UNCHECKED_CAST")
private fun Node.flatten(): List<Node> {
    return if (label == JavaEntityType.BLOCK) {
        children().toList() as List<Node>
    } else {
        listOf(this)
    }
}
