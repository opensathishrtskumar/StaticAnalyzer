package com.analyzer.rules;

import java.util.List;

import com.analyzer.results.Result;
import com.analyzer.util.AnalyzerUtil;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class NullArgumentRule implements Rules {
	
	private List<MethodDeclaration> allMethod = null;
	private String[] sourcePath = null, jarPath = null;
	
	public NullArgumentRule() {
	}
	
	public NullArgumentRule(String[] sourcePath, String[] jarPath) {
		this.sourcePath = sourcePath;
		this.jarPath = jarPath;
	}
	
	@Override
	public void preProcessing(CompilationUnit unit) throws Exception {
		this.allMethod = AnalyzerUtil.getMethods(unit, getSourcePath(), getJarPath());
	}

	@Override
	public Result apply(CompilationUnit unit) throws Exception {
		
		if(this.allMethod != null){
			this.allMethod.forEach(method -> {
				System.out.println("============Starts " + method.getNameAsString() + "=====================");
				AnalyzerUtil.getMethodInvocationTrace(method, unit, getSourcePath(), getJarPath());
				System.out.println("============Ends " + method.getNameAsString() + "=====================");
			});
		}
		
		return null;
	}

	@Override
	public void postProcessing(CompilationUnit unit) throws Exception {
		//System.out.println("Postprocessing...");
	}

	@Override
	public Result getResult() throws Exception {
		return result;
	}


	public String[] getSourcePath() {
		return sourcePath;
	}


	public void setSourcePath(String[] sourcePath) {
		this.sourcePath = sourcePath;
	}


	public String[] getJarPath() {
		return jarPath;
	}


	public void setJarPath(String[] jarPath) {
		this.jarPath = jarPath;
	}

	public List<MethodDeclaration> getAllMethod() {
		return allMethod;
	}

	public void setAllMethod(List<MethodDeclaration> allMethod) {
		this.allMethod = allMethod;
	}
}
