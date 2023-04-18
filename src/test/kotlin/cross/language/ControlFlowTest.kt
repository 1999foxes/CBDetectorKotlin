package cross.language

import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode
import cross.language.ast.*
import cross.language.utils.FileInfo
import cross.language.utils.getFilesInFolder
import java.io.File

fun main() {

    val patches: List<FileInfo> = getFilesInFolder("./data/patches/")
    val otherFiles: List<FileInfo> = getFilesInFolder("./data/lucenenet")

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
        val other = File(otherFiles.find {
            it.fileName == left.name.split("_").last().replace(".java", ".cs")
        }?.absolutePath ?: continue)

//        val leftAST = JavaControlFlow(left)
//        leftAST.cu.print()
//        leftAST.cu.dfs(
//            onVisit = {
//                if (it.type == JavaStructureNode.Type.METHOD || it.type == JavaStructureNode.Type.CONSTRUCTOR) {
//                    val methodBody = leftAST.createMethodBodyTree(it)
//                    methodBody.print()
//                    1
//                }
//            }
//        )
//        continue

        val otherAST = CSharpControlFlow(other)
        otherAST.cu.print()
        otherAST.cu.bfs(
            onVisit = {
                if (it.type == JavaStructureNode.Type.METHOD) {
                    println(it)
                    val body = otherAST.createMethodBodyTree(it)
                    body?.print()
                    1
                }
            }
        )

        println()
    }

}