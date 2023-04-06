package cross.language.ast

import ch.uzh.ifi.seal.changedistiller.model.classifiers.EntityType
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

// hack: CSharpAST converts (AST of Roslyn API) to (JavaStructureNode and Node of ChangeDistiller)
class CSharpAST(file: File) {
    private val src = file.readText()
    var cu: JavaStructureNode
    private val nodeMapper: MutableMap<JavaStructureNode, CSharpSyntaxNode> = mutableMapOf()

    init {
        val process =
            ProcessBuilder(listOf("libs/CBDetectorCSharp/CBDetectorCSharp.exe", file.absolutePath)).start()
        val jsonString = process.inputStream.bufferedReader().readText()
        val json = Json {
            ignoreUnknownKeys = true
        }
        val cSharpSyntaxNode: CSharpSyntaxNode = json.decodeFromString(CSharpSyntaxNode.serializer(), jsonString)
        cSharpSyntaxNode.print()
        cu = createStructureTree(cSharpSyntaxNode)
    }

    @Serializable
    data class CSharpSyntaxNode(
        var Label: String = "",
        var Start: Int = 0,
        var End: Int = 0,
        var Identifier: String = "",
        var Children: MutableList<CSharpSyntaxNode> = mutableListOf()
    )

    val CSharpSyntaxNode.Value: String
        get() {
            return src.substring(this.Start, this.End)
        }

    fun CSharpSyntaxNode.print(indent: String = "", isLast: Boolean = true) {
        val stringPrefix = "$indent${if (isLast) "└── " else "├── "}"
        val stringLabel = "${this.Label} ${this.Identifier} "
        val stringValue = this.Value
            .replace(
                "\n",
                "\n$indent${if (isLast) "    " else "│   "}${if (this.Children.isEmpty()) " " else "│"}${
                    " ".repeat(
                        this.Label.length
                    )
                }┆"
            )
        println("$stringPrefix$stringLabel$stringValue")
        for (i in this.Children.indices) {
            val childNode = this.Children[i]
            val childIndent = "$indent${if (isLast) "    " else "│   "}"
            childNode.print(childIndent, i == this.Children.lastIndex)
        }
    }

    fun CSharpSyntaxNode.bfs(callback: (CSharpSyntaxNode) -> Boolean): CSharpSyntaxNode? {
        val queue = LinkedList<CSharpSyntaxNode>()
        queue.add(this)
        while (queue.isNotEmpty()) {
            val node = queue.poll()
            if (callback(node)) {
                return node
            }
            for (child in node.Children) {
                queue.add(child)
            }
        }
        return null
    }

    fun CSharpSyntaxNode.dfs(callback: (CSharpSyntaxNode) -> Boolean): CSharpSyntaxNode? {
        if (callback(this)) {
            return this
        }
        for (child in this.Children) {
            val tmp = child.dfs(callback)
            if (tmp != null) {
                return tmp
            }
        }
        return null
    }

    var JavaStructureNode.cSharpSyntaxNode: CSharpSyntaxNode?
        get() {
            return nodeMapper[this]
        }
        set(value) {
            if (value != null) {
                nodeMapper[this] = value
            } else {
                nodeMapper.remove(this)
            }
        }

    private fun createStructureTree(cSharpSyntaxNode: CSharpSyntaxNode): JavaStructureNode {
        val cu = JavaStructureNode(JavaStructureNode.Type.CU, null, null, null)
        val namespaceNode: CSharpSyntaxNode = cSharpSyntaxNode.bfs { it.Label == LABEL.NamespaceDeclarationSyntax }!!
        val namespace: String = namespaceNode.bfs { it.Label == LABEL.QualifiedNameSyntax }!!.Value
        namespaceNode.Children.forEach {
            val childStructureNode = createStructureTreeIteration(it, namespace)
            if (childStructureNode != null) {
                cu.addChild(childStructureNode)
            }
        }
        return cu
    }

    private fun createStructureTreeIteration(
        cSharpSyntaxNode: CSharpSyntaxNode,
        qualifier: String = ""
    ): JavaStructureNode? {
        val thisNode: JavaStructureNode = when (cSharpSyntaxNode.Label) {
            LABEL.ClassDeclarationSyntax ->
                JavaStructureNode(JavaStructureNode.Type.CLASS, qualifier, cSharpSyntaxNode.Identifier, null)

            LABEL.FieldDeclarationSyntax ->
                JavaStructureNode(
                    JavaStructureNode.Type.FIELD,
                    qualifier,
                    cSharpSyntaxNode.bfs { it.Label == LABEL.IdentifierNameSyntax }!!.Identifier,
                    null
                )

            LABEL.ConstructorDeclarationSyntax ->
                JavaStructureNode(JavaStructureNode.Type.CONSTRUCTOR, qualifier, cSharpSyntaxNode.Identifier, null)

            LABEL.MethodDeclarationSyntax ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, cSharpSyntaxNode.Identifier, null)

            // TODO more types

            else -> return null
        }
        thisNode.cSharpSyntaxNode = cSharpSyntaxNode
        cSharpSyntaxNode.Children.forEach {
            createStructureTreeIteration(it, thisNode.fullyQualifiedName).let { childNode ->
                if (childNode != null) {
                    thisNode.addChild(childNode)
                }
            }
        }
        return thisNode
    }

    private fun createMethodBodyTree(cSharpSyntaxNode: CSharpSyntaxNode): Node {
        val node = Node(JavaEntityType.BODY, cSharpSyntaxNode.Value)
        // TODO control flow
        return node
    }

    private class LABEL {
        companion object {
            const val CompilationUnitSyntax = "CompilationUnitSyntax"
            const val UsingDirectiveSyntax = "UsingDirectiveSyntax"
            const val IdentifierNameSyntax = "IdentifierNameSyntax"
            const val QualifiedNameSyntax = "QualifiedNameSyntax"
            const val NamespaceDeclarationSyntax = "NamespaceDeclarationSyntax"
            const val ClassDeclarationSyntax = "ClassDeclarationSyntax"
            const val BaseListSyntax = "BaseListSyntax"
            const val SimpleBaseTypeSyntax = "SimpleBaseTypeSyntax"
            const val FieldDeclarationSyntax = "FieldDeclarationSyntax"
            const val VariableDeclarationSyntax = "VariableDeclarationSyntax"
            const val VariableDeclaratorSyntax = "VariableDeclaratorSyntax"
            const val ConstructorDeclarationSyntax = "ConstructorDeclarationSyntax"
            const val ParameterListSyntax = "ParameterListSyntax"
            const val ParameterSyntax = "ParameterSyntax"
            const val BlockSyntax = "BlockSyntax"
            const val ExpressionStatementSyntax = "ExpressionStatementSyntax"
            const val AssignmentExpressionSyntax = "AssignmentExpressionSyntax"
            const val MemberAccessExpressionSyntax = "MemberAccessExpressionSyntax"
            const val ThisExpressionSyntax = "ThisExpressionSyntax"
            const val MethodDeclarationSyntax = "MethodDeclarationSyntax"
            const val PredefinedTypeSyntax = "PredefinedTypeSyntax"
            const val IfStatementSyntax = "IfStatementSyntax"
            const val InvocationExpressionSyntax = "InvocationExpressionSyntax"
            const val ArgumentListSyntax = "ArgumentListSyntax"
            const val BaseExpressionSyntax = "BaseExpressionSyntax"
            const val ArgumentSyntax = "ArgumentSyntax"
            const val LocalDeclarationStatementSyntax = "LocalDeclarationStatementSyntax"
            const val EqualsValueClauseSyntax = "EqualsValueClauseSyntax"
            const val ReturnStatementSyntax = "ReturnStatementSyntax"
            const val ConditionalExpressionSyntax = "ConditionalExpressionSyntax"
            const val ParenthesizedExpressionSyntax = "ParenthesizedExpressionSyntax"
            const val IsPatternExpressionSyntax = "IsPatternExpressionSyntax"
            const val DeclarationPatternSyntax = "DeclarationPatternSyntax"
            const val SingleVariableDesignationSyntax = "SingleVariableDesignationSyntax"
            const val ArrayTypeSyntax = "ArrayTypeSyntax"
            const val ArrayRankSpecifierSyntax = "ArrayRankSpecifierSyntax"
            const val OmittedArraySizeExpressionSyntax = "OmittedArraySizeExpressionSyntax"
            const val ArrayCreationExpressionSyntax = "ArrayCreationExpressionSyntax"
            const val LiteralExpressionSyntax = "LiteralExpressionSyntax"
            const val BinaryExpressionSyntax = "BinaryExpressionSyntax"
            const val PrefixUnaryExpressionSyntax = "PrefixUnaryExpressionSyntax"
            const val ElementAccessExpressionSyntax = "ElementAccessExpressionSyntax"
            const val BracketedArgumentListSyntax = "BracketedArgumentListSyntax"
            const val ThrowStatementSyntax = "ThrowStatementSyntax"
            const val PropertyDeclarationSyntax = "PropertyDeclarationSyntax"
            const val ArrowExpressionClauseSyntax = "ArrowExpressionClauseSyntax"
            const val ObjectCreationExpressionSyntax = "ObjectCreationExpressionSyntax"
        }
    }
}
