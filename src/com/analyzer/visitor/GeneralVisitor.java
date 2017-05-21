package com.analyzer.visitor;

import java.io.File;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

public class GeneralVisitor  extends Visitor {

	public GeneralVisitor(CompilationUnit unit) {
		super(unit);
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
		
		Optional<Expression> scope = n.getScope();
		
		if(scope.isPresent()){
		
			//ObjectCreationExpr expression = new ObjectCreationExpr(scope, type, arguments);
			
			/*JavaParserFacade facade = JavaParserFacade.get(new JavaParserTypeSolver(new File(
					"/home/sathishrtskumar/Downloads/EMS_Windows/src/com/ems/UI/internalframes/PingInternalFrame.java")));
			SymbolSolver solver = facade.getSymbolSolver();*/
			
		}
		
		
		//System.out.println("MethodCall " + n.toString() + n.getScope());
		super.visit(n, arg);
	}
}
