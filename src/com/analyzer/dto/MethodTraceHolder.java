package com.analyzer.dto;

import java.util.List;

import com.analyzer.constants.Model;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

public class MethodTraceHolder {
	private MethodDeclaration methodDeclaration;
	private JavaParserFacade javaParserFacade;
	private MethodCallExpr methodCallExpr;
	private CompilationUnit compilationUnit;
	private SymbolReference<com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration> referenceMethodDeclaration;
	private List<Model> methodCallList;
	private int depth;
	
	public MethodTraceHolder() {
	}

	public MethodDeclaration getMethodDeclaration() {
		return methodDeclaration;
	}

	public MethodTraceHolder setMethodDeclaration(MethodDeclaration methodDeclaration) {
		this.methodDeclaration = methodDeclaration;
		return this;
	}

	public JavaParserFacade getJavaParserFacade() {
		return javaParserFacade;
	}

	public MethodTraceHolder setJavaParserFacade(JavaParserFacade javaParserFacade) {
		this.javaParserFacade = javaParserFacade;
		return this;
	}

	public MethodCallExpr getMethodCallExpr() {
		return methodCallExpr;
	}

	public MethodTraceHolder setMethodCallExpr(MethodCallExpr methodCallExpr) {
		this.methodCallExpr = methodCallExpr;
		return this;
	}

	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}

	public MethodTraceHolder setCompilationUnit(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
		return this;
	}

	public SymbolReference<com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration> getReferenceMethodDeclaration() {
		return referenceMethodDeclaration;
	}

	public MethodTraceHolder setReferenceMethodDeclaration(
			SymbolReference<com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration> referenceMethodDeclaration) {
		this.referenceMethodDeclaration = referenceMethodDeclaration;
		return this;
	}

	public List<Model> getMethodCallList() {
		return methodCallList;
	}

	public MethodTraceHolder setMethodCallList(List<Model> methodCallList) {
		this.methodCallList = methodCallList;
		return this;
	}

	public int getDepth() {
		return depth;
	}

	public MethodTraceHolder setDepth(int depth) {
		this.depth = depth;
		return this;
	}
}
