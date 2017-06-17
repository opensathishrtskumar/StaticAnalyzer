package com.analyzer.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.analyzer.constants.Model;
import com.analyzer.dto.MethodTraceHolder;
import com.analyzer.visitor.GeneralVisitor;
import com.analyzer.visitor.MethodVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public abstract class AnalyzerUtil {

	public static List<MethodDeclaration> getMethods(CompilationUnit unit, String[] sourcePath, String[] jarPath) {
		MethodVisitor<JavaParserFacade> visitor = new MethodVisitor<JavaParserFacade>(unit);
		JavaParserFacade facade = getJavaParserFacade(sourcePath, jarPath);
		visitor.visit(unit, facade);
		List<MethodDeclaration> methodList = visitor.getMethodList();
		return methodList;
	}

	public static List<Model> getMethodInvocationTrace(MethodTraceHolder traceHolder) {

		GeneralVisitor generalVisitor = new GeneralVisitor(traceHolder);

		traceHolder.getMethodDeclaration().accept(generalVisitor, traceHolder.getJavaParserFacade());

		return generalVisitor.getMethodCallList();
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

	public static JavaParserFacade getJavaParserFacade(String[] sourcePath, String[] jarPath) {
		TypeSolver typeSolver = getTypeResolver(sourcePath, jarPath);
		JavaParserFacade facade = JavaParserFacade.get(typeSolver);
		return facade;
	}

	public static String getNameWithPackage(
			com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration md) {
		StringBuilder builder = new StringBuilder();

		if (md != null) {
			builder.append(md.getPackageName());
			if (!builder.toString().isEmpty())
				builder.append(".");
			builder.append(md.getClassName());
		}

		return builder.toString();
	}

	public static String getParameters(NodeList<Parameter> params) {
		StringBuilder builder = new StringBuilder();

		if (params != null) {
			for (int count = 0; count < params.size(); count++) {
				if (count > 0 && count <= params.size() - 1)
					builder.append(",");
				builder.append(params.get(count).getType());
			}
		}

		return builder.toString();
	}

	public static void printMethodTrace(List<Model> calls) {

		for (Model model : calls) {
			
			printTab(model.getDepth());
			
			System.out.println(
					model.getMethod().getDeclarationAsString() + "[" + Utility.getQualifiedName(model.getUnit()) + "]");

			printMethodTrace(model.getInvocationList());
		}
	}

	public static void printTab(int count) {
		for (int i = 0; i < count; i++) {
			System.out.print("\t");
		}
	}
}
