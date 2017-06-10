package com.analyzer.visitor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodVisitor<T> extends Visitor<T> {

	public MethodVisitor(CompilationUnit unit) {
		super(unit);
	}
	
	@Override
	public void visit(MethodDeclaration method, T arg) {

		if(!method.isNative())
			getMethodList().add(method);

		super.visit(method, arg);
	}

}
