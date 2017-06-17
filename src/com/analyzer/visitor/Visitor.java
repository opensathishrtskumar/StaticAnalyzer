package com.analyzer.visitor;

import java.util.ArrayList;
import java.util.List;

import com.analyzer.constants.Model;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public abstract class Visitor<T> extends VoidVisitorAdapter<T>{
	
	protected List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>(30);
	protected List<Model> methodCallList = new ArrayList<Model>(30); 
	protected CompilationUnit unit;
	
	public Visitor(CompilationUnit unit) {
		this.unit = unit;
	}
	
	public List<MethodDeclaration> getMethodList() {
		return methodList;
	}

	public List<Model> getMethodCallList() {
		return methodCallList;
	}
	
}
