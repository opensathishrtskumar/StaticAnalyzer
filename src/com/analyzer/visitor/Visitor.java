package com.analyzer.visitor;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public abstract class Visitor extends VoidVisitorAdapter<Void>{
	
	protected List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();
	
	protected CompilationUnit unit;
	
	public Visitor(CompilationUnit unit) {
		this.unit = unit;
	}
	
	public List<MethodDeclaration> getMethodList() {
		return methodList;
	}
}
