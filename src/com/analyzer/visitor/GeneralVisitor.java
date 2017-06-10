package com.analyzer.visitor;

import java.util.Optional;

import com.analyzer.constants.Model;
import com.analyzer.util.Utility;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.ValueDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

public class GeneralVisitor  extends Visitor<JavaParserFacade> {

	public GeneralVisitor(CompilationUnit unit) {
		super(unit);
	}
	
	@Override
	public void visit(ObjectCreationExpr n, JavaParserFacade arg) {
		
		Optional<Expression> expression = n.getScope();
		
		expression.ifPresent(instance -> {
			instance.getParentNode();
		});
		
		
		super.visit(n, arg);
	}
	
	
	@Override
	public void visit(MethodCallExpr n, JavaParserFacade javaParserFacade) {
		super.visit(n, javaParserFacade);
		
		try {
			
			if(n.getScope().isPresent()){
				System.out.println("Presennt " + n.getScope().get());
				System.out.println(n.toString() + " has type " + javaParserFacade.getType(n.getScope().get()).describe());
				
				SymbolReference<? extends ValueDeclaration> referennce = javaParserFacade.solve(n.getScope().get());
				
				SymbolReference<MethodDeclaration> declaration = javaParserFacade.solve(n);
				
				System.out.println(declaration);
				if(referennce.isSolved()){
					System.out.println("Solved : " + referennce.getCorrespondingDeclaration());
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		System.out.println("=========================================");
	}
}
