package com.analyzer.visitor;

import java.util.List;

import com.analyzer.constants.Model;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;

public class ParamNullCheckVisitor extends Visitor<Object>{

	private Model model;
	
	public ParamNullCheckVisitor(Model model) {
		super(model.getUnit());
		this.model = model;
	}
	
	@Override
	public void visit(ExpressionStmt n, Object arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}
	
	@Override
	public void visit(IfStmt n, Object arg) {
		
		List<ExpressionStmt> list = n.getChildNodesByType(ExpressionStmt.class);
		list.forEach(stmt -> {
			//System.out.println(stmt.getExpression());
		});
		
		super.visit(n, arg);
	}
	
	@Override
	public void visit(InitializerDeclaration n, Object arg) {
		//System.out.println("InitializerDeclaration " + n.toString());
		super.visit(n, arg);
	}
	
	@Override
	public void visit(VariableDeclarator n, Object arg) {
		//System.out.println("VariableDeclarator "+n);
		super.visit(n, arg);
	}
	
	@Override
	public void visit(Parameter n, Object arg) {
		//System.out.println("Parameter " + n); 
		super.visit(n, arg);
	}
	
	public Model getModel() {
		return model;
	}
}
