package cross.language.ast

import ch.uzh.ifi.seal.changedistiller.JavaChangeDistillerModule
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode
import com.google.inject.Guice
import java.io.File

fun getJavaAST(file: File) {
    val injector = Guice.createInjector(JavaChangeDistillerModule())
    val distiller = injector.getInstance(FileDistiller::class.java)
    val astHelper = distiller.getAST(file)
    val cu = astHelper.createStructureTree() as JavaStructureNode
//    cu.print()
//    println()
    // TODO control flow
}

fun JavaStructureNode.print(indent: String = "", isLast: Boolean = true) {
    val stringPrefix = "$indent${if (isLast) "└── " else "├── "}"
    val stringLabel = "${this.type}  "
    val stringValue = this.toString()
    println("$stringPrefix$stringLabel$stringValue")
    for (i in this.children.indices) {
        val childNode = this.children[i]
        val childIndent = "$indent${if (isLast) "    " else "│   "}"
        childNode.print(childIndent, i == this.children.lastIndex)
    }
}