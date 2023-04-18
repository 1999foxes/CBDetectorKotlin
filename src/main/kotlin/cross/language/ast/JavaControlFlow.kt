package cross.language.ast

import ch.uzh.ifi.seal.changedistiller.JavaChangeDistillerModule
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node
import com.google.inject.Guice
import java.io.File

val injector = Guice.createInjector(JavaChangeDistillerModule())!!
val distiller = injector.getInstance(FileDistiller::class.java)!!

class JavaControlFlow(val file: File) {
    private val astHelper = distiller.getASTHelper(file)
    val cu: JavaStructureNode = astHelper.createStructureTree() as JavaStructureNode

    fun createMethodBodyTree(javaStructureNode: JavaStructureNode): Node {
        return astHelper.createMethodBodyTree(javaStructureNode).toControlFlow()
    }

    private fun Node.toControlFlow(): Node {
        val simpleLabel = if (this.label in listOf(
                JavaEntityType.BREAK_STATEMENT,
                JavaEntityType.RETURN_STATEMENT,
                JavaEntityType.IF_STATEMENT,
                JavaEntityType.THEN_STATEMENT,
                JavaEntityType.ELSE_STATEMENT,
                JavaEntityType.SWITCH_STATEMENT,
                JavaEntityType.SWITCH_CASE,
                JavaEntityType.FOR_STATEMENT,
                JavaEntityType.FOR_INCR,
                JavaEntityType.FOR_INIT,
                JavaEntityType.FOREACH_STATEMENT,
                JavaEntityType.WHILE_STATEMENT,
                JavaEntityType.DO_STATEMENT,
                JavaEntityType.TRY_STATEMENT,
                JavaEntityType.CATCH_CLAUSES,
                JavaEntityType.CATCH_CLAUSE,
                JavaEntityType.BLOCK,
                JavaEntityType.BODY,
                JavaEntityType.METHOD,
            )
        ) {
            this.label
        } else {
            JavaEntityType.LEAF_STATEMENT
        }

        return Node(simpleLabel, this.value).let {controlFlow ->
            this.children().toList().forEach { child ->
                controlFlow.add((child as Node).toControlFlow())
            }
            controlFlow
        }
    }
}