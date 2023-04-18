package cross.language.ast

import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node
import java.io.File
import java.util.*

// hack: CSharpAST converts (AST of Roslyn API) to (JavaStructureNode and Node of ChangeDistiller)
class CSharpControlFlow(file: File) {
    var cu: JavaStructureNode

    private val nodeMapper: WeakHashMap<JavaStructureNode, CSharpSyntaxNode?> = WeakHashMap()
    private var JavaStructureNode.cSharpSyntaxNode: CSharpSyntaxNode?
        get() {
            return nodeMapper[this]
        }
        set(value) {
            println(value)
            nodeMapper[this] = value
        }

    init {
        val cSharpSyntaxNode = CSharpSyntaxNode.fromFile(file)
        cu = createStructureTree(cSharpSyntaxNode)!!
    }

    private fun createStructureTree(cSharpSyntaxNode: CSharpSyntaxNode): JavaStructureNode? {
        val namespaceNode: CSharpSyntaxNode =
            cSharpSyntaxNode.bfs { it.kind == KindText.NamespaceDeclaration || it.kind == KindText.FileScopedNamespaceDeclaration }
                ?: return null
        val namespace: String = namespaceNode.bfs { it.kind == KindText.QualifiedName }?.src ?: return null
        val cu = JavaStructureNode(JavaStructureNode.Type.CU, null, namespace, null)
        cu.cSharpSyntaxNode = cSharpSyntaxNode

        val stack = Stack<JavaStructureNode>()
        stack.push(cu)
        namespaceNode.dfs(
            onVisit = {
                val structureNode = createStructureNode(it, stack.peek().fullyQualifiedName)
                if (structureNode != null) {
                    structureNode.cSharpSyntaxNode = it
                    stack.peek().addChild(structureNode)
                    stack.push(structureNode)
                }
            },
            onEndVisit = {
                if (createStructureNode(it) != null) {
                    stack.pop()
                }
            }
        )

        return cu
    }


    /**
     * convert CSharpSyntaxNode to corresponding JavaStructureNode.
     *
     *  related syntax node definition, referring to Rosyln API:
     *
     *  CSharpSyntaxNode
     *     AccessorDeclarationSyntax: GetAccessorDeclaration, SetAccessorDeclaration, InitAccessorDeclaration, AddAccessorDeclaration, RemoveAccessorDeclaration, UnknownAccessorDeclaration
     *     MemberDeclarationSyntax
     *         BasePropertyDeclarationSyntax
     *             EventDeclarationSyntax: EventDeclaration
     *             IndexerDeclarationSyntax: IndexerDeclaration
     *             PropertyDeclarationSyntax: PropertyDeclaration
     *         BaseFieldDeclarationSyntax
     *             EventFieldDeclarationSyntax: EventFieldDeclaration
     *             FieldDeclarationSyntax: FieldDeclaration
     *         BaseMethodDeclarationSyntax
     *             ConstructorDeclarationSyntax: ConstructorDeclaration
     *             ConversionOperatorDeclarationSyntax: ConversionOperatorDeclaration
     *             DestructorDeclarationSyntax: DestructorDeclaration
     *             MethodDeclarationSyntax: MethodDeclaration
     *             OperatorDeclarationSyntax: OperatorDeclaration
     *         BaseTypeDeclarationSyntax
     *             TypeDeclarationSyntax
     *                 ClassDeclarationSyntax: ClassDeclaration
     *                 StructDeclarationSyntax: StructDeclaration
     *                 InterfaceDeclarationSyntax: InterfaceDeclaration
     *                 RecordDeclarationSyntax: RecordDeclaration
     *             EnumDeclarationSyntax: EnumDeclaration
     *  ...others are ignored
     */
    private fun createStructureNode(
        cSharpSyntaxNode: CSharpSyntaxNode,
        qualifier: String = ""
    ): JavaStructureNode? {
        val thisNode: JavaStructureNode = when (cSharpSyntaxNode.kind) {

            // type

            KindText.ClassDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.CLASS, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.InterfaceDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.INTERFACE, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.RecordDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.CLASS, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.StructDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.CLASS, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.EnumDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.CLASS, qualifier, cSharpSyntaxNode.identifierSafe, null)

            // field

            KindText.EventFieldDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.FIELD, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.FieldDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.FIELD, qualifier, cSharpSyntaxNode.identifierSafe, null)

            // property

            KindText.EventDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.FIELD, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.PropertyDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.FIELD, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.IndexerDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.FIELD, qualifier, "Indexer", null)

            // method

            KindText.ConstructorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.CONSTRUCTOR, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.ConversionOperatorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.DestructorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.MethodDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, cSharpSyntaxNode.identifierSafe, null)

            KindText.OperatorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, cSharpSyntaxNode.identifierSafe, null)

            // accessor

            KindText.GetAccessorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, "Get", null)

            KindText.SetAccessorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, "Set", null)

            KindText.InitAccessorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, "Init", null)

            KindText.AddAccessorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, "Add", null)

            KindText.RemoveAccessorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, "Remove", null)

            KindText.UnknownAccessorDeclaration ->
                JavaStructureNode(JavaStructureNode.Type.METHOD, qualifier, "Accessor", null)

            else -> return null
        }
        return thisNode
    }

    public fun createMethodBodyTree(javaStructureNode: JavaStructureNode): Node? {
        javaStructureNode.cSharpSyntaxNode?.print()
        return createMethodBodyTree(javaStructureNode.cSharpSyntaxNode ?: return null)
    }

    private fun createMethodBodyTree(cSharpSyntaxNode: CSharpSyntaxNode): Node? {
        val bodySyntaxNode =
            cSharpSyntaxNode.bfs { it.kind == KindText.Block || it.kind.endsWith("Statement") }
                ?: return null
        return Node(JavaEntityType.BODY, bodySyntaxNode.src).run {
            add(bodySyntaxNode.children.mapNotNull { createNode(it) })
            this
        }

//
//        bodySyntaxNode.dfs(
//            onVisit = {
//                println(it.kind + it.src.split("\n")[0])
//                if ((body.label as JavaEntityType).isBlock().not()) {
//                    return@dfs
//                }
//                val node = createNode(it)
//                if (node != null) {
//                    body.add(node)
//                    body = node
//                }
//            },
//            onEndVisit = {
//                println(it.kind + it.src.split("\n")[0])
//                if ((body.label as JavaEntityType).isBlock().not()) {
//                    return@dfs
//                }
//                if (createNode(it) != null) {
//                    body = body.parent as Node
//                }
//            }
//        )

//        return body
    }

    private fun createNode(cSharpSyntaxNode: CSharpSyntaxNode): Node? {
        return when (cSharpSyntaxNode.kind) {
//            KindText.LocalDeclarationStatement ->
//                Node(JavaEntityType.VARIABLE_DECLARATION_STATEMENT, "${cSharpSyntaxNode.kind},${cSharpSyntaxNode.src}")

//            KindText.ExpressionStatement ->
//                Node(JavaEntityType.METHOD_INVOCATION, "${cSharpSyntaxNode.kind},${cSharpSyntaxNode.src}")

            KindText.BreakStatement ->
                Node(JavaEntityType.BREAK_STATEMENT, "")

            KindText.ReturnStatement ->
                Node(JavaEntityType.RETURN_STATEMENT, cSharpSyntaxNode.src)

            KindText.IfStatement ->
                createNodeIfStatement(cSharpSyntaxNode)

            KindText.SwitchStatement ->
                createNodeSwitchStatement(cSharpSyntaxNode)

            KindText.CaseSwitchLabel ->
                Node(JavaEntityType.SWITCH_CASE, cSharpSyntaxNode.src)

            KindText.DefaultSwitchLabel ->
                Node(JavaEntityType.SWITCH_CASE, cSharpSyntaxNode.src)

            KindText.ForStatement ->
                createNodeForStatement(cSharpSyntaxNode)

            KindText.ForEachStatement ->
                createNodeForEachStatement(cSharpSyntaxNode)

            KindText.WhileStatement ->
                createNodeWhileStatement(cSharpSyntaxNode)

            KindText.DoStatement ->
                createNodeDoStatement(cSharpSyntaxNode)

            KindText.TryStatement ->
                createNodeTryStatement(cSharpSyntaxNode)

            KindText.Block ->
                Node(JavaEntityType.BLOCK, cSharpSyntaxNode.src).run {
                    add(cSharpSyntaxNode.children.mapNotNull { createNode(it) })
                    this
                }

            else ->
//                Node(JavaEntityType.BLOCK, "${cSharpSyntaxNode.kind},${cSharpSyntaxNode.src}").run {
//                    add(cSharpSyntaxNode.children.mapNotNull { createNode(it) })
//                    this
//                }
                Node(JavaEntityType.LEAF_STATEMENT, cSharpSyntaxNode.src)
        }
    }

    /**
     *  create an if-statement node.
     *  src:
     *  IF_STATEMENT
     *      a condition
     *      a statement
     *      ELSE_CLAUSE
     *          a statement
     *
     *   tgt:
     *   IF_STATEMENT
     *       THEN_STATEMENT
     *           statements
     *       ELSE_STATEMENT
     *           statements
     */
    private fun createNodeIfStatement(cSharpSyntaxNode: CSharpSyntaxNode): Node {
        val conditionSyntaxNode = cSharpSyntaxNode.children[0]
        val thenSyntaxNode = cSharpSyntaxNode.children[1]
        val elseSyntaxNode = if (cSharpSyntaxNode.children.size == 3) cSharpSyntaxNode.children[2] else null

        val ifNode = Node(
            JavaEntityType.IF_STATEMENT,
            matchFirstParentheses(conditionSyntaxNode.src)
        )

        val thenNode = Node(JavaEntityType.THEN_STATEMENT, ifNode.value).apply {
            thenSyntaxNode.flatten().forEach {
                this.add(createNode(it))
            }
        }
        ifNode.add(thenNode)

        if (elseSyntaxNode != null) {
            val elseNode = Node(JavaEntityType.ELSE_STATEMENT, elseSyntaxNode.src).apply {
                elseSyntaxNode.children[0].flatten().forEach {
                    this.add(createNode(it))
                }
            }
            ifNode.add(elseNode)
        }
        return ifNode
    }

    private fun matchFirstParentheses(inputString: String): String {
        val stack = Stack<Int>()
        inputString.forEachIndexed { i, it ->
            if (it == '(') {
                stack.push(i)
            } else if (it == ')') {
                val left = stack.pop()
                if (stack.empty()) {
                    return inputString.substring(left + 1, i)
                }
            }
        }
        return inputString
    }

    private fun CSharpSyntaxNode.flatten(): List<CSharpSyntaxNode> {
        return if (kind == KindText.Block) {
            children.toList()
        } else {
            listOf(this)
        }
    }

    /**
     *  create a for-statement node
     *
     *  src:
     *  FOR_STATEMENT
     *      0~3 statements
     *      a for-block
     *
     *  tgt:
     *  FOR_STATEMENT
     *      statements
     *      FOR_INIT
     *      FOR_INCR
     */
    private fun createNodeForStatement(cSharpSyntaxNode: CSharpSyntaxNode): Node {
        // A regular expression to match the three components of a for loop
        val regex = """for\s*\(([^\;]*);\s*([^\;]*);\s*([^\)]*)\)(?s).*""".toRegex()

        val (_, strForInit, strForStatement, strForIncr) = regex.find(cSharpSyntaxNode.src)!!.groupValues

        return Node(JavaEntityType.FOR_STATEMENT, "($strForStatement)").run {
            var i = 0
            if (strForInit.isNotEmpty()) {
                val nodeForInit = Node(JavaEntityType.FOR_INIT, strForInit)
                nodeForInit.add(createNode(cSharpSyntaxNode.children[i]))
                this.add(nodeForInit)
                i++
            }
            if (strForStatement.isNotEmpty()) {
                i++
            }
            if (strForIncr.isNotEmpty()) {
                val nodeForIncr = Node(JavaEntityType.FOR_INCR, strForIncr)
                nodeForIncr.add(createNode(cSharpSyntaxNode.children[i]))
                this.add(nodeForIncr)
                i++
            }
            this.add(cSharpSyntaxNode.children[i].flatten().mapNotNull { createNode(it) })
            assert(i == cSharpSyntaxNode.children.lastIndex)
            this
        }
    }

    /**
     *  create a foreach-statement node
     *
     *  src:
     *  ForEachStatement
     *      IdentifierName // type name
     *      IdentifierName // variable name
     *      a statement
     *
     *  tgt:
     *  FOREACH_STATEMENT
     *      statements
     */
    private fun createNodeForEachStatement(cSharpSyntaxNode: CSharpSyntaxNode): Node {
        return Node(JavaEntityType.FOREACH_STATEMENT, matchFirstParentheses(cSharpSyntaxNode.src)).run {
            add(cSharpSyntaxNode.children.last().flatten().mapNotNull { createNode(it) })
            this
        }
    }

    /**
     *  create a while-statement node
     *
     *  src:
     *  WhileStatement
     *      a condition
     *      a statement
     *
     *  tgt:
     *  WHILE_STATEMENT
     *      statements
     */
    private fun createNodeWhileStatement(cSharpSyntaxNode: CSharpSyntaxNode): Node {
        return Node(JavaEntityType.WHILE_STATEMENT, "(${cSharpSyntaxNode.children[0].src})").run {
            this.add(cSharpSyntaxNode.children[1].flatten().mapNotNull { createNode(it) })
            this
        }
    }

    /**
     *  create a switch-statement node
     *
     *  src:
     *  SwitchStatement
     *      IdentifierName
     *      SwitchSection
     *          CaseSwitchLabel
     *          statements
     *          BreakStatement
     *      SwitchSection
     *      ...
     *
     *  tgt:
     *  SWITCH_STATEMENT
     *      SWITCH_CASE
     *      statements
     *      BREAK_STATEMENT
     *      SWITCH_CASE
     *      ...
     */
    private fun createNodeSwitchStatement(cSharpSyntaxNode: CSharpSyntaxNode): Node {
        return Node(JavaEntityType.SWITCH_STATEMENT, cSharpSyntaxNode.children[0].src).run {
            add(cSharpSyntaxNode.children
                .filter { it.kind == KindText.SwitchSection }
                .flatMap { it.children.mapNotNull { child -> createNode(child) } }
            )
            this
        }
    }

    /**
     *  create a do-statement node
     *
     *  src:
     *  DoStatement
     *      a statement
     *      a condition
     *
     *  tgt:
     *  DO_STATEMENT
     *      statements
     */
    private fun createNodeDoStatement(cSharpSyntaxNode: CSharpSyntaxNode): Node {
        return Node(JavaEntityType.DO_STATEMENT, cSharpSyntaxNode.children[1].src).run {
            add(cSharpSyntaxNode.children[0].flatten().mapNotNull { createNode(it) })
            this
        }
    }

    /**
     *  create a try-statement node
     *
     *  src:
     *  TryStatement
     *      Block
     *      CatchClause
     *          CatchDeclaration
     *          CatchFilterClause?
     *          Block
     *
     *  tgt:
     *  TRY_STATEMENT
     *      BODY
     *      CATCH_CLAUSES
     *          CATCH_CLAUSE
     *              statements
     */
    private fun createNodeTryStatement(cSharpSyntaxNode: CSharpSyntaxNode): Node {
        val tryStatementNode = Node(JavaEntityType.TRY_STATEMENT, "try")

        tryStatementNode.add(createNode(cSharpSyntaxNode.children[0]))  // try block
        val catchClausesNode = Node(JavaEntityType.CATCH_CLAUSES, "")
        tryStatementNode.add(catchClausesNode)

        for (i in 1 until cSharpSyntaxNode.children.size) {
            val catchBlock = cSharpSyntaxNode.children[i]
            val catchClause = Node(JavaEntityType.CATCH_CLAUSE, catchBlock.children[0].src)
            for (j in 1 until catchBlock.children.size) {
                catchClause.add(createNode(catchBlock.children[j]))
            }
            catchClausesNode.add(catchClause)
        }

        return tryStatementNode
    }

}
