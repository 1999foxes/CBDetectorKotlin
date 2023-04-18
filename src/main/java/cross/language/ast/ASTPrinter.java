package cross.language.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.AnnotationMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.AssertStatement;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.BreakStatement;
import org.eclipse.jdt.internal.compiler.ast.CaseStatement;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.CharLiteral;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.Clinit;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ContinueStatement;
import org.eclipse.jdt.internal.compiler.ast.DoStatement;
import org.eclipse.jdt.internal.compiler.ast.DoubleLiteral;
import org.eclipse.jdt.internal.compiler.ast.EmptyStatement;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.ExtendedStringLiteral;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.FloatLiteral;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.IntersectionCastTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Javadoc;
import org.eclipse.jdt.internal.compiler.ast.JavadocAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.JavadocArgumentExpression;
import org.eclipse.jdt.internal.compiler.ast.JavadocArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocArraySingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocFieldReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocImplicitTypeReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocMessageSend;
import org.eclipse.jdt.internal.compiler.ast.JavadocQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.JavadocSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.LabeledStatement;
import org.eclipse.jdt.internal.compiler.ast.LambdaExpression;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.LongLiteral;
import org.eclipse.jdt.internal.compiler.ast.MarkerAnnotation;
import org.eclipse.jdt.internal.compiler.ast.MemberValuePair;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NormalAnnotation;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.PostfixExpression;
import org.eclipse.jdt.internal.compiler.ast.PrefixExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedSuperReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedThisReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReferenceExpression;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleMemberAnnotation;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.StringLiteralConcatenation;
import org.eclipse.jdt.internal.compiler.ast.SuperReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.SynchronizedStatement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeParameter;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.ast.UnionTypeReference;
import org.eclipse.jdt.internal.compiler.ast.WhileStatement;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;
import java.util.Stack;


public class ASTPrinter extends ASTVisitor {

    private class Node extends DefaultMutableTreeNode {
        public String label;
        public String value;
        public Node(String l, String v) {
            label = l;
            value = v;
        }
    }

    public static String generateEmptyString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void printTreeNode(Node node, String indent, boolean isLast) {
        String stringPrefix = indent + (isLast ? "└── " : "├── ");
        String stringLabel = node.label + "  ";
        String stringValue = node.value.replace(
                "\n",
                "\n" + indent + (isLast ? "      " : "│     ") + generateEmptyString(node.label.length()) + "┆"
        );
        System.out.println(stringPrefix + stringLabel + stringValue);
        Enumeration<Node> children = node.children();
        while (children.hasMoreElements()) {
            Node childNode = children.nextElement();
            boolean isLastChild = !children.hasMoreElements();
            String childIndent = indent + (isLast ? "    " : "│   ");
            printTreeNode(childNode, childIndent, isLastChild);
        }
    }

    private Stack<Node> stack = new Stack<>();
    private Node root;

    public void preVisit(ASTNode aSTNode) {
        stack.push(new Node(aSTNode.getClass().getSimpleName(), aSTNode.toString()));
    }

    public void postVisit(ASTNode aSTNode) {
        Node node = stack.pop();
        if (stack.empty()) {
            root = node;
            printTreeNode(root, "", true);
        } else {
            stack.peek().add(node);
        }
    }

/*
JavaScript to generate visit and endVisit methods

const oldCode = "base methods copied from ASTVisitor";

const regex = /(public\s+boolean\s+visit\(\w+ (\w+), \w+ \w+\)\s+\{)/g;
const replacement = "$1\n        preVisit($2);";
const newCode = oldCode.replace(regex, replacement);
console.log(newCode);

const regex = /(public\s+void\s+endVisit\(\w+ (\w+), \w+ \w+\)\s+\{)\s*\}/g;
const replacement = "$1\n        postVisit($2);\n    }";
const newCode = oldCode.replace(regex, replacement);
console.log(newCode);
*/
    public void endVisit(AllocationExpression allocationExpression, BlockScope scope) {
        postVisit(allocationExpression);
    }
    public void endVisit(AND_AND_Expression and_and_Expression, BlockScope scope) {
        postVisit(and_and_Expression);
    }

    public void endVisit(AnnotationMethodDeclaration annotationTypeDeclaration, ClassScope classScope) {
        postVisit(annotationTypeDeclaration);
    }

    public void endVisit(Argument argument, BlockScope scope) {
        postVisit(argument);
    }

    public void endVisit(Argument argument, ClassScope scope) {
        postVisit(argument);
    }

    public void endVisit(ArrayAllocationExpression arrayAllocationExpression, BlockScope scope) {
        postVisit(arrayAllocationExpression);
    }

    public void endVisit(ArrayInitializer arrayInitializer, BlockScope scope) {
        postVisit(arrayInitializer);
    }

    public void endVisit(ArrayInitializer arrayInitializer, ClassScope scope) {
        postVisit(arrayInitializer);
    }

    public void endVisit(ArrayQualifiedTypeReference arrayQualifiedTypeReference, BlockScope scope) {
        postVisit(arrayQualifiedTypeReference);
    }

    public void endVisit(ArrayQualifiedTypeReference arrayQualifiedTypeReference, ClassScope scope) {
        postVisit(arrayQualifiedTypeReference);
    }

    public void endVisit(ArrayReference arrayReference, BlockScope scope) {
        postVisit(arrayReference);
    }

    public void endVisit(ArrayTypeReference arrayTypeReference, BlockScope scope) {
        postVisit(arrayTypeReference);
    }

    public void endVisit(ArrayTypeReference arrayTypeReference, ClassScope scope) {
        postVisit(arrayTypeReference);
    }

    public void endVisit(AssertStatement assertStatement, BlockScope scope) {
        postVisit(assertStatement);
    }

    public void endVisit(Assignment assignment, BlockScope scope) {
        postVisit(assignment);
    }

    public void endVisit(BinaryExpression binaryExpression, BlockScope scope) {
        postVisit(binaryExpression);
    }

    public void endVisit(Block block, BlockScope scope) {
        postVisit(block);
    }

    public void endVisit(BreakStatement breakStatement, BlockScope scope) {
        postVisit(breakStatement);
    }

    public void endVisit(CaseStatement caseStatement, BlockScope scope) {
        postVisit(caseStatement);
    }

    public void endVisit(CastExpression castExpression, BlockScope scope) {
        postVisit(castExpression);
    }

    public void endVisit(CharLiteral charLiteral, BlockScope scope) {
        postVisit(charLiteral);
    }

    public void endVisit(ClassLiteralAccess classLiteral, BlockScope scope) {
        postVisit(classLiteral);
    }

    public void endVisit(Clinit clinit, ClassScope scope) {
        postVisit(clinit);
    }

    public void endVisit(CompilationUnitDeclaration compilationUnitDeclaration, CompilationUnitScope scope) {
        postVisit(compilationUnitDeclaration);
    }

    public void endVisit(CompoundAssignment compoundAssignment, BlockScope scope) {
        postVisit(compoundAssignment);
    }

    public void endVisit(ConditionalExpression conditionalExpression, BlockScope scope) {
        postVisit(conditionalExpression);
    }

    public void endVisit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
        postVisit(constructorDeclaration);
    }

    public void endVisit(ContinueStatement continueStatement, BlockScope scope) {
        postVisit(continueStatement);
    }

    public void endVisit(DoStatement doStatement, BlockScope scope) {
        postVisit(doStatement);
    }

    public void endVisit(DoubleLiteral doubleLiteral, BlockScope scope) {
        postVisit(doubleLiteral);
    }

    public void endVisit(EmptyStatement emptyStatement, BlockScope scope) {
        postVisit(emptyStatement);
    }

    public void endVisit(EqualExpression equalExpression, BlockScope scope) {
        postVisit(equalExpression);
    }

    public void endVisit(ExplicitConstructorCall explicitConstructor, BlockScope scope) {
        postVisit(explicitConstructor);
    }

    public void endVisit(ExtendedStringLiteral extendedStringLiteral, BlockScope scope) {
        postVisit(extendedStringLiteral);
    }

    public void endVisit(FalseLiteral falseLiteral, BlockScope scope) {
        postVisit(falseLiteral);
    }

    public void endVisit(FieldDeclaration fieldDeclaration, MethodScope scope) {
        postVisit(fieldDeclaration);
    }

    public void endVisit(FieldReference fieldReference, BlockScope scope) {
        postVisit(fieldReference);
    }

    public void endVisit(FieldReference fieldReference, ClassScope scope) {
        postVisit(fieldReference);
    }

    public void endVisit(FloatLiteral floatLiteral, BlockScope scope) {
        postVisit(floatLiteral);
    }

    public void endVisit(ForeachStatement forStatement, BlockScope scope) {
        postVisit(forStatement);
    }

    public void endVisit(ForStatement forStatement, BlockScope scope) {
        postVisit(forStatement);
    }

    public void endVisit(IfStatement ifStatement, BlockScope scope) {
        postVisit(ifStatement);
    }

    public void endVisit(ImportReference importRef, CompilationUnitScope scope) {
        postVisit(importRef);
    }

    public void endVisit(Initializer initializer, MethodScope scope) {
        postVisit(initializer);
    }

    public void endVisit(InstanceOfExpression instanceOfExpression, BlockScope scope) {
        postVisit(instanceOfExpression);
    }

    public void endVisit(IntLiteral intLiteral, BlockScope scope) {
        postVisit(intLiteral);
    }

    public void endVisit(Javadoc javadoc, BlockScope scope) {
        postVisit(javadoc);
    }

    public void endVisit(Javadoc javadoc, ClassScope scope) {
        postVisit(javadoc);
    }

    public void endVisit(JavadocAllocationExpression expression, BlockScope scope) {
        postVisit(expression);
    }

    public void endVisit(JavadocAllocationExpression expression, ClassScope scope) {
        postVisit(expression);
    }

    public void endVisit(JavadocArgumentExpression expression, BlockScope scope) {
        postVisit(expression);
    }

    public void endVisit(JavadocArgumentExpression expression, ClassScope scope) {
        postVisit(expression);
    }

    public void endVisit(JavadocArrayQualifiedTypeReference typeRef, BlockScope scope) {
        postVisit(typeRef);
    }

    public void endVisit(JavadocArrayQualifiedTypeReference typeRef, ClassScope scope) {
        postVisit(typeRef);
    }

    public void endVisit(JavadocArraySingleTypeReference typeRef, BlockScope scope) {
        postVisit(typeRef);
    }

    public void endVisit(JavadocArraySingleTypeReference typeRef, ClassScope scope) {
        postVisit(typeRef);
    }

    public void endVisit(JavadocFieldReference fieldRef, BlockScope scope) {
        postVisit(fieldRef);
    }

    public void endVisit(JavadocFieldReference fieldRef, ClassScope scope) {
        postVisit(fieldRef);
    }

    public void endVisit(JavadocImplicitTypeReference implicitTypeReference, BlockScope scope) {
        postVisit(implicitTypeReference);
    }

    public void endVisit(JavadocImplicitTypeReference implicitTypeReference, ClassScope scope) {
        postVisit(implicitTypeReference);
    }

    public void endVisit(JavadocMessageSend messageSend, BlockScope scope) {
        postVisit(messageSend);
    }

    public void endVisit(JavadocMessageSend messageSend, ClassScope scope) {
        postVisit(messageSend);
    }

    public void endVisit(JavadocQualifiedTypeReference typeRef, BlockScope scope) {
        postVisit(typeRef);
    }

    public void endVisit(JavadocQualifiedTypeReference typeRef, ClassScope scope) {
        postVisit(typeRef);
    }

    public void endVisit(JavadocReturnStatement statement, BlockScope scope) {
        postVisit(statement);
    }

    public void endVisit(JavadocReturnStatement statement, ClassScope scope) {
        postVisit(statement);
    }

    public void endVisit(JavadocSingleNameReference argument, BlockScope scope) {
        postVisit(argument);
    }

    public void endVisit(JavadocSingleNameReference argument, ClassScope scope) {
        postVisit(argument);
    }

    public void endVisit(JavadocSingleTypeReference typeRef, BlockScope scope) {
        postVisit(typeRef);
    }

    public void endVisit(JavadocSingleTypeReference typeRef, ClassScope scope) {
        postVisit(typeRef);
    }

    public void endVisit(LabeledStatement labeledStatement, BlockScope scope) {
        postVisit(labeledStatement);
    }

    public void endVisit(LocalDeclaration localDeclaration, BlockScope scope) {
        postVisit(localDeclaration);
    }

    public void endVisit(LongLiteral longLiteral, BlockScope scope) {
        postVisit(longLiteral);
    }

    public void endVisit(MarkerAnnotation annotation, BlockScope scope) {
        postVisit(annotation);
    }

    public void endVisit(MarkerAnnotation annotation, ClassScope scope) {
        postVisit(annotation);
    }

    public void endVisit(MemberValuePair pair, BlockScope scope) {
        postVisit(pair);
    }

    public void endVisit(MemberValuePair pair, ClassScope scope) {
        postVisit(pair);
    }

    public void endVisit(MessageSend messageSend, BlockScope scope) {
        postVisit(messageSend);
    }

    public void endVisit(MethodDeclaration methodDeclaration, ClassScope scope) {
        postVisit(methodDeclaration);
    }

    public void endVisit(StringLiteralConcatenation literal, BlockScope scope) {
        postVisit(literal);
    }

    public void endVisit(NormalAnnotation annotation, BlockScope scope) {
        postVisit(annotation);
    }

    public void endVisit(NormalAnnotation annotation, ClassScope scope) {
        postVisit(annotation);
    }

    public void endVisit(NullLiteral nullLiteral, BlockScope scope) {
        postVisit(nullLiteral);
    }

    public void endVisit(OR_OR_Expression or_or_Expression, BlockScope scope) {
        postVisit(or_or_Expression);
    }

    public void endVisit(ParameterizedQualifiedTypeReference parameterizedQualifiedTypeReference, BlockScope scope) {
        postVisit(parameterizedQualifiedTypeReference);
    }

    public void endVisit(ParameterizedQualifiedTypeReference parameterizedQualifiedTypeReference, ClassScope scope) {
        postVisit(parameterizedQualifiedTypeReference);
    }

    public void endVisit(ParameterizedSingleTypeReference parameterizedSingleTypeReference, BlockScope scope) {
        postVisit(parameterizedSingleTypeReference);
    }

    public void endVisit(ParameterizedSingleTypeReference parameterizedSingleTypeReference, ClassScope scope) {
        postVisit(parameterizedSingleTypeReference);
    }

    public void endVisit(PostfixExpression postfixExpression, BlockScope scope) {
        postVisit(postfixExpression);
    }

    public void endVisit(PrefixExpression prefixExpression, BlockScope scope) {
        postVisit(prefixExpression);
    }

    public void endVisit(QualifiedAllocationExpression qualifiedAllocationExpression, BlockScope scope) {
        postVisit(qualifiedAllocationExpression);
    }

    public void endVisit(QualifiedNameReference qualifiedNameReference, BlockScope scope) {
        postVisit(qualifiedNameReference);
    }

    public void endVisit(QualifiedNameReference qualifiedNameReference, ClassScope scope) {
        postVisit(qualifiedNameReference);
    }

    public void endVisit(QualifiedSuperReference qualifiedSuperReference, BlockScope scope) {
        postVisit(qualifiedSuperReference);
    }

    public void endVisit(QualifiedSuperReference qualifiedSuperReference, ClassScope scope) {
        postVisit(qualifiedSuperReference);
    }

    public void endVisit(QualifiedThisReference qualifiedThisReference, BlockScope scope) {
        postVisit(qualifiedThisReference);
    }

    public void endVisit(QualifiedThisReference qualifiedThisReference, ClassScope scope) {
        postVisit(qualifiedThisReference);
    }

    public void endVisit(QualifiedTypeReference qualifiedTypeReference, BlockScope scope) {
        postVisit(qualifiedTypeReference);
    }

    public void endVisit(QualifiedTypeReference qualifiedTypeReference, ClassScope scope) {
        postVisit(qualifiedTypeReference);
    }

    public void endVisit(ReturnStatement returnStatement, BlockScope scope) {
        postVisit(returnStatement);
    }

    public void endVisit(SingleMemberAnnotation annotation, BlockScope scope) {
        postVisit(annotation);
    }

    public void endVisit(SingleMemberAnnotation annotation, ClassScope scope) {
        postVisit(annotation);
    }

    public void endVisit(SingleNameReference singleNameReference, BlockScope scope) {
        postVisit(singleNameReference);
    }

    public void endVisit(SingleNameReference singleNameReference, ClassScope scope) {
        postVisit(singleNameReference);
    }

    public void endVisit(SingleTypeReference singleTypeReference, BlockScope scope) {
        postVisit(singleTypeReference);
    }

    public void endVisit(SingleTypeReference singleTypeReference, ClassScope scope) {
        postVisit(singleTypeReference);
    }

    public void endVisit(StringLiteral stringLiteral, BlockScope scope) {
        postVisit(stringLiteral);
    }

    public void endVisit(SuperReference superReference, BlockScope scope) {
        postVisit(superReference);
    }

    public void endVisit(SwitchStatement switchStatement, BlockScope scope) {
        postVisit(switchStatement);
    }

    public void endVisit(SynchronizedStatement synchronizedStatement, BlockScope scope) {
        postVisit(synchronizedStatement);
    }

    public void endVisit(ThisReference thisReference, BlockScope scope) {
        postVisit(thisReference);
    }

    public void endVisit(ThisReference thisReference, ClassScope scope) {
        postVisit(thisReference);
    }

    public void endVisit(ThrowStatement throwStatement, BlockScope scope) {
        postVisit(throwStatement);
    }

    public void endVisit(TrueLiteral trueLiteral, BlockScope scope) {
        postVisit(trueLiteral);
    }

    public void endVisit(TryStatement tryStatement, BlockScope scope) {
        postVisit(tryStatement);
    }

    public void endVisit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
        postVisit(localTypeDeclaration);
    }

    public void endVisit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
        postVisit(memberTypeDeclaration);
    }

    public void endVisit(TypeDeclaration typeDeclaration, CompilationUnitScope scope) {
        postVisit(typeDeclaration);
    }

    public void endVisit(TypeParameter typeParameter, BlockScope scope) {
        postVisit(typeParameter);
    }

    public void endVisit(TypeParameter typeParameter, ClassScope scope) {
        postVisit(typeParameter);
    }

    public void endVisit(UnaryExpression unaryExpression, BlockScope scope) {
        postVisit(unaryExpression);
    }

    public void endVisit(UnionTypeReference unionTypeReference, BlockScope scope) {
        postVisit(unionTypeReference);
    }

    public void endVisit(UnionTypeReference unionTypeReference, ClassScope scope) {
        postVisit(unionTypeReference);
    }

    public void endVisit(WhileStatement whileStatement, BlockScope scope) {
        postVisit(whileStatement);
    }

    public void endVisit(Wildcard wildcard, BlockScope scope) {
        postVisit(wildcard);
    }

    public void endVisit(Wildcard wildcard, ClassScope scope) {
        postVisit(wildcard);
    }

    public void endVisit(LambdaExpression lambdaExpression, BlockScope blockScope) {
        postVisit(lambdaExpression);
    }

    public void endVisit(ReferenceExpression referenceExpression, BlockScope blockScope) {
        postVisit(referenceExpression);
    }

    public void endVisit(IntersectionCastTypeReference intersectionCastTypeReference, ClassScope scope) {
        postVisit(intersectionCastTypeReference);
    }

    public void endVisit(IntersectionCastTypeReference intersectionCastTypeReference, BlockScope scope) {
        postVisit(intersectionCastTypeReference);
    }
    public boolean visit(AllocationExpression allocationExpression, BlockScope scope) {
        preVisit(allocationExpression);
        return true;
    }

    public boolean visit(AND_AND_Expression and_and_Expression, BlockScope scope) {
        preVisit(and_and_Expression);
        return true;
    }

    public boolean visit(AnnotationMethodDeclaration annotationTypeDeclaration, ClassScope classScope) {
        preVisit(annotationTypeDeclaration);
        return true;
    }

    public boolean visit(Argument argument, BlockScope scope) {
        preVisit(argument);
        return true;
    }

    public boolean visit(Argument argument, ClassScope scope) {
        preVisit(argument);
        return true;
    }

    public boolean visit(ArrayAllocationExpression arrayAllocationExpression, BlockScope scope) {
        preVisit(arrayAllocationExpression);
        return true;
    }

    public boolean visit(ArrayInitializer arrayInitializer, BlockScope scope) {
        preVisit(arrayInitializer);
        return true;
    }

    public boolean visit(ArrayInitializer arrayInitializer, ClassScope scope) {
        preVisit(arrayInitializer);
        return true;
    }

    public boolean visit(ArrayQualifiedTypeReference arrayQualifiedTypeReference, BlockScope scope) {
        preVisit(arrayQualifiedTypeReference);
        return true;
    }

    public boolean visit(ArrayQualifiedTypeReference arrayQualifiedTypeReference, ClassScope scope) {
        preVisit(arrayQualifiedTypeReference);
        return true;
    }

    public boolean visit(ArrayReference arrayReference, BlockScope scope) {
        preVisit(arrayReference);
        return true;
    }

    public boolean visit(ArrayTypeReference arrayTypeReference, BlockScope scope) {
        preVisit(arrayTypeReference);
        return true;
    }

    public boolean visit(ArrayTypeReference arrayTypeReference, ClassScope scope) {
        preVisit(arrayTypeReference);
        return true;
    }

    public boolean visit(AssertStatement assertStatement, BlockScope scope) {
        preVisit(assertStatement);
        return true;
    }

    public boolean visit(Assignment assignment, BlockScope scope) {
        preVisit(assignment);
        return true;
    }

    public boolean visit(BinaryExpression binaryExpression, BlockScope scope) {
        preVisit(binaryExpression);
        return true;
    }

    public boolean visit(Block block, BlockScope scope) {
        preVisit(block);
        return true;
    }

    public boolean visit(BreakStatement breakStatement, BlockScope scope) {
        preVisit(breakStatement);
        return true;
    }

    public boolean visit(CaseStatement caseStatement, BlockScope scope) {
        preVisit(caseStatement);
        return true;
    }

    public boolean visit(CastExpression castExpression, BlockScope scope) {
        preVisit(castExpression);
        return true;
    }

    public boolean visit(CharLiteral charLiteral, BlockScope scope) {
        preVisit(charLiteral);
        return true;
    }

    public boolean visit(ClassLiteralAccess classLiteral, BlockScope scope) {
        preVisit(classLiteral);
        return true;
    }

    public boolean visit(Clinit clinit, ClassScope scope) {
        preVisit(clinit);
        return true;
    }

    public boolean visit(CompilationUnitDeclaration compilationUnitDeclaration, CompilationUnitScope scope) {
        preVisit(compilationUnitDeclaration);
        return true;
    }

    public boolean visit(CompoundAssignment compoundAssignment, BlockScope scope) {
        preVisit(compoundAssignment);
        return true;
    }

    public boolean visit(ConditionalExpression conditionalExpression, BlockScope scope) {
        preVisit(conditionalExpression);
        return true;
    }

    public boolean visit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
        preVisit(constructorDeclaration);
        return true;
    }

    public boolean visit(ContinueStatement continueStatement, BlockScope scope) {
        preVisit(continueStatement);
        return true;
    }

    public boolean visit(DoStatement doStatement, BlockScope scope) {
        preVisit(doStatement);
        return true;
    }

    public boolean visit(DoubleLiteral doubleLiteral, BlockScope scope) {
        preVisit(doubleLiteral);
        return true;
    }

    public boolean visit(EmptyStatement emptyStatement, BlockScope scope) {
        preVisit(emptyStatement);
        return true;
    }

    public boolean visit(EqualExpression equalExpression, BlockScope scope) {
        preVisit(equalExpression);
        return true;
    }

    public boolean visit(ExplicitConstructorCall explicitConstructor, BlockScope scope) {
        preVisit(explicitConstructor);
        return true;
    }

    public boolean visit(ExtendedStringLiteral extendedStringLiteral, BlockScope scope) {
        preVisit(extendedStringLiteral);
        return true;
    }

    public boolean visit(FalseLiteral falseLiteral, BlockScope scope) {
        preVisit(falseLiteral);
        return true;
    }

    public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
        preVisit(fieldDeclaration);
        return true;
    }

    public boolean visit(FieldReference fieldReference, BlockScope scope) {
        preVisit(fieldReference);
        return true;
    }

    public boolean visit(FieldReference fieldReference, ClassScope scope) {
        preVisit(fieldReference);
        return true;
    }

    public boolean visit(FloatLiteral floatLiteral, BlockScope scope) {
        preVisit(floatLiteral);
        return true;
    }

    public boolean visit(ForeachStatement forStatement, BlockScope scope) {
        preVisit(forStatement);
        return true;
    }

    public boolean visit(ForStatement forStatement, BlockScope scope) {
        preVisit(forStatement);
        return true;
    }

    public boolean visit(IfStatement ifStatement, BlockScope scope) {
        preVisit(ifStatement);
        return true;
    }

    public boolean visit(ImportReference importRef, CompilationUnitScope scope) {
        preVisit(importRef);
        return true;
    }

    public boolean visit(Initializer initializer, MethodScope scope) {
        preVisit(initializer);
        return true;
    }

    public boolean visit(InstanceOfExpression instanceOfExpression, BlockScope scope) {
        preVisit(instanceOfExpression);
        return true;
    }

    public boolean visit(IntLiteral intLiteral, BlockScope scope) {
        preVisit(intLiteral);
        return true;
    }

    public boolean visit(Javadoc javadoc, BlockScope scope) {
        preVisit(javadoc);
        return true;
    }

    public boolean visit(Javadoc javadoc, ClassScope scope) {
        preVisit(javadoc);
        return true;
    }

    public boolean visit(JavadocAllocationExpression expression, BlockScope scope) {
        preVisit(expression);
        return true;
    }

    public boolean visit(JavadocAllocationExpression expression, ClassScope scope) {
        preVisit(expression);
        return true;
    }

    public boolean visit(JavadocArgumentExpression expression, BlockScope scope) {
        preVisit(expression);
        return true;
    }

    public boolean visit(JavadocArgumentExpression expression, ClassScope scope) {
        preVisit(expression);
        return true;
    }

    public boolean visit(JavadocArrayQualifiedTypeReference typeRef, BlockScope scope) {
        preVisit(typeRef);
        return true;
    }

    public boolean visit(JavadocArrayQualifiedTypeReference typeRef, ClassScope scope) {
        preVisit(typeRef);
        return true;
    }

    public boolean visit(JavadocArraySingleTypeReference typeRef, BlockScope scope) {
        preVisit(typeRef);
        return true;
    }

    public boolean visit(JavadocArraySingleTypeReference typeRef, ClassScope scope) {
        preVisit(typeRef);
        return true;
    }

    public boolean visit(JavadocFieldReference fieldRef, BlockScope scope) {
        preVisit(fieldRef);
        return true;
    }

    public boolean visit(JavadocFieldReference fieldRef, ClassScope scope) {
        preVisit(fieldRef);
        return true;
    }

    public boolean visit(JavadocImplicitTypeReference implicitTypeReference, BlockScope scope) {
        preVisit(implicitTypeReference);
        return true;
    }

    public boolean visit(JavadocImplicitTypeReference implicitTypeReference, ClassScope scope) {
        preVisit(implicitTypeReference);
        return true;
    }

    public boolean visit(JavadocMessageSend messageSend, BlockScope scope) {
        preVisit(messageSend);
        return true;
    }

    public boolean visit(JavadocMessageSend messageSend, ClassScope scope) {
        preVisit(messageSend);
        return true;
    }

    public boolean visit(JavadocQualifiedTypeReference typeRef, BlockScope scope) {
        preVisit(typeRef);
        return true;
    }

    public boolean visit(JavadocQualifiedTypeReference typeRef, ClassScope scope) {
        preVisit(typeRef);
        return true;
    }

    public boolean visit(JavadocReturnStatement statement, BlockScope scope) {
        preVisit(statement);
        return true;
    }

    public boolean visit(JavadocReturnStatement statement, ClassScope scope) {
        preVisit(statement);
        return true;
    }

    public boolean visit(JavadocSingleNameReference argument, BlockScope scope) {
        preVisit(argument);
        return true;
    }

    public boolean visit(JavadocSingleNameReference argument, ClassScope scope) {
        preVisit(argument);
        return true;
    }

    public boolean visit(JavadocSingleTypeReference typeRef, BlockScope scope) {
        preVisit(typeRef);
        return true;
    }

    public boolean visit(JavadocSingleTypeReference typeRef, ClassScope scope) {
        preVisit(typeRef);
        return true;
    }

    public boolean visit(LabeledStatement labeledStatement, BlockScope scope) {
        preVisit(labeledStatement);
        return true;
    }

    public boolean visit(LocalDeclaration localDeclaration, BlockScope scope) {
        preVisit(localDeclaration);
        return true;
    }

    public boolean visit(LongLiteral longLiteral, BlockScope scope) {
        preVisit(longLiteral);
        return true;
    }

    public boolean visit(MarkerAnnotation annotation, BlockScope scope) {
        preVisit(annotation);
        return true;
    }

    public boolean visit(MarkerAnnotation annotation, ClassScope scope) {
        preVisit(annotation);
        return true;
    }

    public boolean visit(MemberValuePair pair, BlockScope scope) {
        preVisit(pair);
        return true;
    }

    public boolean visit(MemberValuePair pair, ClassScope scope) {
        preVisit(pair);
        return true;
    }

    public boolean visit(MessageSend messageSend, BlockScope scope) {
        preVisit(messageSend);
        return true;
    }

    public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
        preVisit(methodDeclaration);
        return true;
    }

    public boolean visit(StringLiteralConcatenation literal, BlockScope scope) {
        preVisit(literal);
        return true;
    }

    public boolean visit(NormalAnnotation annotation, BlockScope scope) {
        preVisit(annotation);
        return true;
    }

    public boolean visit(NormalAnnotation annotation, ClassScope scope) {
        preVisit(annotation);
        return true;
    }

    public boolean visit(NullLiteral nullLiteral, BlockScope scope) {
        preVisit(nullLiteral);
        return true;
    }

    public boolean visit(OR_OR_Expression or_or_Expression, BlockScope scope) {
        preVisit(or_or_Expression);
        return true;
    }

    public boolean visit(ParameterizedQualifiedTypeReference parameterizedQualifiedTypeReference, BlockScope scope) {
        preVisit(parameterizedQualifiedTypeReference);
        return true;
    }

    public boolean visit(ParameterizedQualifiedTypeReference parameterizedQualifiedTypeReference, ClassScope scope) {
        preVisit(parameterizedQualifiedTypeReference);
        return true;
    }

    public boolean visit(ParameterizedSingleTypeReference parameterizedSingleTypeReference, BlockScope scope) {
        preVisit(parameterizedSingleTypeReference);
        return true;
    }

    public boolean visit(ParameterizedSingleTypeReference parameterizedSingleTypeReference, ClassScope scope) {
        preVisit(parameterizedSingleTypeReference);
        return true;
    }

    public boolean visit(PostfixExpression postfixExpression, BlockScope scope) {
        preVisit(postfixExpression);
        return true;
    }

    public boolean visit(PrefixExpression prefixExpression, BlockScope scope) {
        preVisit(prefixExpression);
        return true;
    }

    public boolean visit(QualifiedAllocationExpression qualifiedAllocationExpression, BlockScope scope) {
        preVisit(qualifiedAllocationExpression);
        return true;
    }

    public boolean visit(QualifiedNameReference qualifiedNameReference, BlockScope scope) {
        preVisit(qualifiedNameReference);
        return true;
    }

    public boolean visit(QualifiedNameReference qualifiedNameReference, ClassScope scope) {
        preVisit(qualifiedNameReference);
        return true;
    }

    public boolean visit(QualifiedSuperReference qualifiedSuperReference, BlockScope scope) {
        preVisit(qualifiedSuperReference);
        return true;
    }

    public boolean visit(QualifiedSuperReference qualifiedSuperReference, ClassScope scope) {
        preVisit(qualifiedSuperReference);
        return true;
    }

    public boolean visit(QualifiedThisReference qualifiedThisReference, BlockScope scope) {
        preVisit(qualifiedThisReference);
        return true;
    }

    public boolean visit(QualifiedThisReference qualifiedThisReference, ClassScope scope) {
        preVisit(qualifiedThisReference);
        return true;
    }

    public boolean visit(QualifiedTypeReference qualifiedTypeReference, BlockScope scope) {
        preVisit(qualifiedTypeReference);
        return true;
    }

    public boolean visit(QualifiedTypeReference qualifiedTypeReference, ClassScope scope) {
        preVisit(qualifiedTypeReference);
        return true;
    }

    public boolean visit(ReturnStatement returnStatement, BlockScope scope) {
        preVisit(returnStatement);
        return true;
    }

    public boolean visit(SingleMemberAnnotation annotation, BlockScope scope) {
        preVisit(annotation);
        return true;
    }

    public boolean visit(SingleMemberAnnotation annotation, ClassScope scope) {
        preVisit(annotation);
        return true;
    }

    public boolean visit(SingleNameReference singleNameReference, BlockScope scope) {
        preVisit(singleNameReference);
        return true;
    }

    public boolean visit(SingleNameReference singleNameReference, ClassScope scope) {
        preVisit(singleNameReference);
        return true;
    }

    public boolean visit(SingleTypeReference singleTypeReference, BlockScope scope) {
        preVisit(singleTypeReference);
        return true;
    }

    public boolean visit(SingleTypeReference singleTypeReference, ClassScope scope) {
        preVisit(singleTypeReference);
        return true;
    }

    public boolean visit(StringLiteral stringLiteral, BlockScope scope) {
        preVisit(stringLiteral);
        return true;
    }

    public boolean visit(SuperReference superReference, BlockScope scope) {
        preVisit(superReference);
        return true;
    }

    public boolean visit(SwitchStatement switchStatement, BlockScope scope) {
        preVisit(switchStatement);
        return true;
    }

    public boolean visit(SynchronizedStatement synchronizedStatement, BlockScope scope) {
        preVisit(synchronizedStatement);
        return true;
    }

    public boolean visit(ThisReference thisReference, BlockScope scope) {
        preVisit(thisReference);
        return true;
    }

    public boolean visit(ThisReference thisReference, ClassScope scope) {
        preVisit(thisReference);
        return true;
    }

    public boolean visit(ThrowStatement throwStatement, BlockScope scope) {
        preVisit(throwStatement);
        return true;
    }

    public boolean visit(TrueLiteral trueLiteral, BlockScope scope) {
        preVisit(trueLiteral);
        return true;
    }

    public boolean visit(TryStatement tryStatement, BlockScope scope) {
        preVisit(tryStatement);
        return true;
    }

    public boolean visit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
        preVisit(localTypeDeclaration);
        return true;
    }

    public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
        preVisit(memberTypeDeclaration);
        return true;
    }

    public boolean visit(TypeDeclaration typeDeclaration, CompilationUnitScope scope) {
        preVisit(typeDeclaration);
        return true;
    }

    public boolean visit(TypeParameter typeParameter, BlockScope scope) {
        preVisit(typeParameter);
        return true;
    }

    public boolean visit(TypeParameter typeParameter, ClassScope scope) {
        preVisit(typeParameter);
        return true;
    }

    public boolean visit(UnaryExpression unaryExpression, BlockScope scope) {
        preVisit(unaryExpression);
        return true;
    }

    public boolean visit(UnionTypeReference unionTypeReference, BlockScope scope) {
        preVisit(unionTypeReference);
        return true;
    }

    public boolean visit(UnionTypeReference unionTypeReference, ClassScope scope) {
        preVisit(unionTypeReference);
        return true;
    }

    public boolean visit(WhileStatement whileStatement, BlockScope scope) {
        preVisit(whileStatement);
        return true;
    }

    public boolean visit(Wildcard wildcard, BlockScope scope) {
        preVisit(wildcard);
        return true;
    }

    public boolean visit(Wildcard wildcard, ClassScope scope) {
        preVisit(wildcard);
        return true;
    }

    public boolean visit(LambdaExpression lambdaExpression, BlockScope blockScope) {
        preVisit(lambdaExpression);
        return true;
    }

    public boolean visit(ReferenceExpression referenceExpression, BlockScope blockScope) {
        preVisit(referenceExpression);
        return true;
    }

    public boolean visit(IntersectionCastTypeReference intersectionCastTypeReference, ClassScope scope) {
        preVisit(intersectionCastTypeReference);
        return true;
    }

    public boolean visit(IntersectionCastTypeReference intersectionCastTypeReference, BlockScope scope) {
        preVisit(intersectionCastTypeReference);
        return true;
    }
}