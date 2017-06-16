package com.analyzer.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.analyzer.visitor.GeneralVisitor;
import com.analyzer.visitor.MethodVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public abstract class AnalyzerUtil {

	public static List<MethodDeclaration> getMethods(CompilationUnit unit,  String[] sourcePath,
			String[] jarPath) {
		MethodVisitor<JavaParserFacade> visitor = new MethodVisitor<JavaParserFacade>(unit);
		JavaParserFacade facade = getJavaParserFacade(sourcePath, jarPath);
		visitor.visit(unit, facade);
		List<MethodDeclaration> methodList = visitor.getMethodList();
		return methodList;
	}

	public static void getMethodInvocationTrace(MethodDeclaration method, CompilationUnit unit, String[] sourcePath,
			String[] jarPath) {

		GeneralVisitor generalVisitor = new GeneralVisitor(unit);
		
		//System.out.println("==================" + method.getName() + "====================");

		JavaParserFacade facade = getJavaParserFacade(sourcePath, jarPath);

		method.accept(generalVisitor, facade);
	}
	
	public static void getMethodInvocationTrace(MethodDeclaration method, CompilationUnit unit, JavaParserFacade facade) {

		GeneralVisitor generalVisitor = new GeneralVisitor(unit);
		
		//System.out.println("==================" + method.getName() + "====================");

		method.accept(generalVisitor, facade);
	}

	public static TypeSolver getTypeResolver(String[] sourcePath, String[] jarPath) {

		CombinedTypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());

		if (sourcePath != null) {
			for (String source : sourcePath) {
				typeSolver.add(new JavaParserTypeSolver(new File(source)));
			}
		}

		if (jarPath != null) {
			for (String source : jarPath) {
				try {
					typeSolver.add(new JarTypeSolver(source));
				} catch (IOException e) {
					System.err.println(" Jar Type solver failed :  " + source);
				}
			}
		}

		return typeSolver;
	}

	public static JavaParserFacade getJavaParserFacade(String[] sourcePath, String[] jarPath){
		TypeSolver typeSolver = getTypeResolver(sourcePath, jarPath);		
		JavaParserFacade facade = JavaParserFacade.get(typeSolver);
		return facade;
	}
	
	public static String getNameWithPackage(com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration md){
		StringBuilder builder = new StringBuilder();
		
		if(md != null){
			builder.append(md.getPackageName());
			if(!builder.toString().isEmpty())
				builder.append(".");
			builder.append(md.getClassName());
		}
		
		return builder.toString();
	}
}
