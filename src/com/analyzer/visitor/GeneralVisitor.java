package com.analyzer.visitor;

import java.util.Map;
import java.util.Optional;

import com.analyzer.constants.Model;
import com.analyzer.util.AnalyzerUtil;
import com.analyzer.util.Utility;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionMethodDeclaration;

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
			
			SymbolReference<MethodDeclaration> declaration = javaParserFacade.solve(n);
			
			invokeAgain(declaration , javaParserFacade);
			
		} catch (Exception e) {
			//System.err.println(e.getLocalizedMessage());
		}
	}
	
	public void invokeAgain(SymbolReference<MethodDeclaration> declaration, JavaParserFacade javaParserFacade){
		if(declaration.isSolved()){
			String instanceName = AnalyzerUtil.getNameWithPackage(declaration.getCorrespondingDeclaration());
			
			if(declaration.getCorrespondingDeclaration() instanceof JavaParserMethodDeclaration){
				//System.out.println("JavaParserMethodDeclaration " + instanceName);
				JavaParserMethodDeclaration md = (JavaParserMethodDeclaration)declaration.getCorrespondingDeclaration();
				Map<String, Object> map = Utility.getCompilationUnitMap();
				
				if(map.get(instanceName) != null){
					Model model = (Model)map.get(instanceName);
					AnalyzerUtil.getMethodInvocationTrace(md.getWrappedNode(), model.getUnit(), javaParserFacade);
					System.out.println(instanceName + " -> " + md.getWrappedNode().getNameAsString());
				} else {
					System.err.println("Class not availale in sourcefolder : " + instanceName);
				}
			} else if(declaration.getCorrespondingDeclaration() instanceof ReflectionMethodDeclaration) {
				//System.out.println("ReflectionMethodDeclaration " + instanceName);
			} else {
				System.out.println(declaration.getCorrespondingDeclaration());
			}
				
		} else {
			System.out.println("Symbol not solved " + declaration);
		}
	}
}
