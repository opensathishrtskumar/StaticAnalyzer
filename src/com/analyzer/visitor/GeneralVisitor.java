package com.analyzer.visitor;

import java.util.Map;

import com.analyzer.constants.Model;
import com.analyzer.util.AnalyzerUtil;
import com.analyzer.util.Utility;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionMethodDeclaration;

public class GeneralVisitor extends Visitor<JavaParserFacade> {
	
	public GeneralVisitor(CompilationUnit unit) {
		super(unit);
	}

	@Override
	public void visit(MethodCallExpr call, JavaParserFacade javaParserFacade) {

		try {
			
			SymbolReference<MethodDeclaration> declaration = javaParserFacade.solve(call);
			
			Model model = new Model();
			model.setUnit(unit);
			
			getMethodCallList().add(model);
			
			invokeAgain(declaration, javaParserFacade, call);

		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
		
		super.visit(call, javaParserFacade);
	}

	public void invokeAgain(SymbolReference<MethodDeclaration> declaration, JavaParserFacade javaParserFacade,
			MethodCallExpr call) {
		if (declaration.isSolved()) {
			String instanceName = AnalyzerUtil.getNameWithPackage(declaration.getCorrespondingDeclaration());

			if (declaration.getCorrespondingDeclaration() instanceof JavaParserMethodDeclaration) {
				JavaParserMethodDeclaration md = (JavaParserMethodDeclaration) declaration
						.getCorrespondingDeclaration();
				
				com.github.javaparser.ast.body.MethodDeclaration method = md.getWrappedNode();
				
				Map<String, Object> map = Utility.getCompilationUnitMap();
				Object object = map.get(instanceName);
				
				if (object != null) {
					Model model = (Model) object;
					
					NodeList<Parameter> params = method.getParameters();

					String paramString = AnalyzerUtil.getParameters(params);

					if (params != null && params.size() > 0) {
						AnalyzerUtil.getMethodInvocationTrace(method, model.getUnit(), javaParserFacade);

						System.out.println(instanceName + " -> " + method.getNameAsString() + "(" + paramString + ")");
					}

				} else {
					System.err.println("Class not availale in sourcefolder : " + instanceName);
				}
			} else if (declaration.getCorrespondingDeclaration() instanceof ReflectionMethodDeclaration) {
				
				ReflectionMethodDeclaration reflectionMethod = (ReflectionMethodDeclaration)declaration.getCorrespondingDeclaration();
				
			} else {
				System.out.println("Unknow method declaration " + declaration.getCorrespondingDeclaration());
			}

		} else {
			System.out.println("Symbol not solved " + call.getNameAsString());
		}
	}
}
