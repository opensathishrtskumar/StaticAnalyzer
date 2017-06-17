package com.analyzer.visitor;

import java.util.Map;

import com.analyzer.constants.Model;
import com.analyzer.dto.MethodTraceHolder;
import com.analyzer.util.AnalyzerUtil;
import com.analyzer.util.Utility;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionMethodDeclaration;

public class GeneralVisitor extends Visitor<JavaParserFacade> {

	public GeneralVisitor(CompilationUnit unit, com.github.javaparser.ast.body.MethodDeclaration rootMethodCall) {
		super(unit);
		this.rootMethodCall = rootMethodCall;
	}

	public GeneralVisitor(MethodTraceHolder traceHolder) {
		super(traceHolder.getCompilationUnit());
		this.traceHolder = traceHolder;
		this.rootMethodCall = traceHolder.getMethodDeclaration();
		this.methodCallList = traceHolder.getMethodCallList();
	}

	@Override
	public void visit(MethodCallExpr call, JavaParserFacade javaParserFacade) {

		try {

			SymbolReference<MethodDeclaration> declaration = javaParserFacade.solve(call, true);

			MethodTraceHolder traceHolder = new MethodTraceHolder().setJavaParserFacade(javaParserFacade)
					.setMethodCallExpr(call).setReferenceMethodDeclaration(declaration);

			invokeAgain(traceHolder);

		} catch (Exception e) {
			// System.err.println(e.getMessage());
		}

		super.visit(call, javaParserFacade);
	}

	public void invokeAgain(MethodTraceHolder traceHolders) {

		SymbolReference<MethodDeclaration> declaration = traceHolders.getReferenceMethodDeclaration();
		JavaParserFacade javaParserFacade = traceHolders.getJavaParserFacade();
		MethodCallExpr call = traceHolders.getMethodCallExpr();

		if (declaration.isSolved()) {
			String instanceName = AnalyzerUtil.getNameWithPackage(declaration.getCorrespondingDeclaration());

			if (declaration.getCorrespondingDeclaration() instanceof JavaParserMethodDeclaration) {
				JavaParserMethodDeclaration md = (JavaParserMethodDeclaration) declaration
						.getCorrespondingDeclaration();

				com.github.javaparser.ast.body.MethodDeclaration method = md.getWrappedNode();

				Map<String, Object> map = Utility.getCompilationUnitMap();
				Model model = (Model) map.get(instanceName);

				Model invocation = new Model();
				invocation.setMethod(method);
				invocation.setUnit(model.getUnit());
				getMethodCallList().add(invocation);

				MethodTraceHolder traceHolder = new MethodTraceHolder().setJavaParserFacade(javaParserFacade)
						.setMethodCallExpr(call).setReferenceMethodDeclaration(declaration)
						.setCompilationUnit(model.getUnit()).setMethodDeclaration(method)
						.setMethodCallList(getMethodCallList());//Set same list to get result

				AnalyzerUtil.getMethodInvocationTrace(traceHolder);
			} else if (declaration.getCorrespondingDeclaration() instanceof ReflectionMethodDeclaration) {

				/*
				 * ReflectionMethodDeclaration reflectionMethod =
				 * (ReflectionMethodDeclaration) declaration
				 * .getCorrespondingDeclaration();
				 */
				// Do nothing here - jar content

			} else {
				System.out.println("Unknow method declaration " + declaration.getCorrespondingDeclaration());
			}

		} else {
			System.out.println("Symbol not solved " + call.getNameAsString());
		}
	}
}
