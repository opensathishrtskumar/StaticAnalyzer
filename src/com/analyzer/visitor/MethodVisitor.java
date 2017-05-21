package com.analyzer.visitor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodVisitor extends Visitor {
	private boolean privateRequired = true;

	public boolean isPrivateRequired() {
		return privateRequired;
	}

	public void setPrivateRequired(boolean privateRequired) {
		this.privateRequired = privateRequired;
	}

	public MethodVisitor(CompilationUnit unit) {
		super(unit);
	}
	
	@Override
	public void visit(MethodDeclaration method, Void arg) {

		boolean add = true;

		if((method.isPrivate() && !isPrivateRequired()) 
				|| method.isNative()){
			add = false;
		}

		if(add)
			getMethodList().add(method);

		super.visit(method, arg);
	}

}
