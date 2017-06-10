package com.analyzer.visitor;

import java.io.File;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.typesystem.Type;
import com.github.javaparser.symbolsolver.model.typesystem.VoidType;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class GeneralVisitor  extends Visitor {

	public GeneralVisitor(CompilationUnit unit) {
		super(unit);
	}
	
	
	private Type getFacade(String sourceFolder, Node node){
		Type type = null;
		
		try {
			CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(
					new ReflectionTypeSolver(),
					new JavaParserTypeSolver(new File(sourceFolder)),
					new JarTypeSolver("E:\\lib\\")
					);
			
			JavaParserFacade facade = JavaParserFacade.get(combinedTypeSolver);
			
			type = facade.getType(node);
			
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
		
		return type;
	}
	
	
	@Override
	public void visit(ObjectCreationExpr n, Void arg) {
		
		Optional<Expression> expression = n.getScope();
		
		expression.ifPresent(instance -> {
			instance.getParentNode();
			
			
			//unit.getClassByName();
			
		});
		
		
		super.visit(n, arg);
	}
	
	@Override
	public void visit(MethodCallExpr n, Void arg) {
		
		//n.getScope().ifPresent(scope -> System.out.println(scope.getParentNode()));
		String sourceFolder = "C:/Users/Gokul.m/workspace/Git/EMS/src/";
		
		Optional<Expression> scope = n.getScope();
		
		if(scope.isPresent()){
			
			Expression expression = scope.get();
			
			//System.out.println("Scope : " + scope.get() + " " + n.getName());
			
			getFacade(sourceFolder, expression.getParentNode().get());
			
		} else {
			System.out.println("No Scope : " + n.toString());
			
			Type type = getFacade(sourceFolder, n.getParentNodeForChildren());
			
			if(type instanceof VoidType){
				VoidType vType = (VoidType)type;
				System.out.println(vType.describe());
			}
			
			System.out.println("No Scope Type : " + type);
		}
		
		super.visit(n, arg);
	}
}
