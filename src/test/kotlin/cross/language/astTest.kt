package cross.language

import cross.language.ast.CSharpAST
import cross.language.ast.getJavaAST
import cross.language.ast.print
import java.io.File

fun main() {

    val cs = CSharpAST(File("D:\\code\\CBDetectorKotlin\\data\\lucenenet\\src\\Lucene.Net\\Analysis\\CharFilter.cs"))
    cs.cu.print()

//    getJavaAST(File("D:\\code\\CBDetectorKotlin\\data\\lucene\\lucene\\core\\src\\java\\org\\apache\\lucene\\analysis\\CharFilter.java"))
}