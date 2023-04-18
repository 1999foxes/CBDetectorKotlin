package cross.language.ast

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.util.ArrayDeque

@Serializable
data class CSharpSyntaxNodeData(
    var Kind: String = "",
    var Identifier: String = "",
    var Start: Int = 0,
    var End: Int = 0,
    var Children: MutableList<CSharpSyntaxNodeData> = mutableListOf()
)

class CSharpSyntaxNode(data: CSharpSyntaxNodeData, val parent: CSharpSyntaxNode? = null, rootSrc: String? = null) {
    companion object {
        fun fromFile(file: File): CSharpSyntaxNode {
            val process =
                ProcessBuilder(listOf("libs/CBDetectorCSharp/CBDetectorCSharp.exe", file.absolutePath)).start()
            val jsonString = process.inputStream.bufferedReader().readText()
            val json = Json {
                ignoreUnknownKeys = true
            }
            val data = json.decodeFromString(CSharpSyntaxNodeData.serializer(), jsonString)
            val rootSrc = file.readText()
            return CSharpSyntaxNode(
                data,
                rootSrc = if (rootSrc.length - data.End + 1 != 0) {  // fix bug
                    "${" ".repeat(data.End - rootSrc.length + 1)}$rootSrc"
                } else {
                    rootSrc
                }
            )
        }
    }

    val kind: String = data.Kind
    val identifier: String = data.Identifier
    val start: Int = data.Start
    val end: Int = data.End
    val children: MutableList<CSharpSyntaxNode> = data.Children.map { CSharpSyntaxNode(it, this) }.toMutableList()
    val identifierSafe: String
        get() {
            return this.identifier.ifEmpty {
                this.bfs { it.kind == KindText.IdentifierName }?.identifier ?: "_"
            }
        }

    val root: CSharpSyntaxNode
        get() {
            var ancestor = this
            while (true) {
                ancestor = ancestor.parent ?: return ancestor
            }
        }

    val src: String = rootSrc ?: ""
        get() {
            return field.ifEmpty {
                root.src.substring(start + 1, end + 1)
            }
        }

    fun print(indent: String = "", isLast: Boolean = true) {

        if (this.kind == KindText.TryStatement) {
            1
        }
        if (this.kind == KindText.ElseClause) {
            2
        }

        val stringPrefix = "$indent${if (isLast) "└── " else "├── "}"
        val stringLabel = "${this.kind},${this.identifier},"
        val stringValue = this.src
            .replace(
                "\n",
                "\n$indent${if (isLast) "    " else "│   "}${if (this.children.isEmpty()) " " else "│"}${
                    " ".repeat(
                        stringLabel.length
                    )
                }┆"
            )
        println("$stringPrefix$stringLabel$stringValue")
        for (i in this.children.indices) {
            val childNode = this.children[i]
            val childIndent = "$indent${if (isLast) "    " else "│   "}"
            childNode.print(childIndent, i == this.children.lastIndex)
        }
    }

    fun bfs(
        onVisit: (CSharpSyntaxNode) -> Unit = {},
        onEndVisit: (CSharpSyntaxNode) -> Unit = {},
        breakCondition: (CSharpSyntaxNode) -> Boolean = { false }
    ): CSharpSyntaxNode? {
        val queue = ArrayDeque<CSharpSyntaxNode>()
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

    fun dfs(
        onVisit: (CSharpSyntaxNode) -> Unit = {},
        onEndVisit: (CSharpSyntaxNode) -> Unit = {},
        breakCondition: (CSharpSyntaxNode) -> Boolean = { false }
    ): CSharpSyntaxNode? {
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
}