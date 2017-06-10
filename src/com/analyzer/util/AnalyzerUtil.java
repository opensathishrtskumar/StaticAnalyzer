package com.analyzer.util;

import java.util.List;

import com.analyzer.visitor.GeneralVisitor;
import com.analyzer.visitor.MethodVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public abstract class AnalyzerUtil {
	
	public static List<MethodDeclaration> getMethods(CompilationUnit unit,
			boolean privateRequired){
		MethodVisitor visitor = new MethodVisitor(unit);
		visitor.setPrivateRequired(privateRequired);
		visitor.visit(unit, null);
		List<MethodDeclaration> methodList = visitor.getMethodList();
		return methodList;
	}
	
	public static void getMethodInvocationTrace(MethodDeclaration method, CompilationUnit unit){

		GeneralVisitor generalVisitor = new GeneralVisitor(unit);
		System.out.println("==================" + method.getName() +  "====================");
		method.accept(generalVisitor, null);
	}
	
}
