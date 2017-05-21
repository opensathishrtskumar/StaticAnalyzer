package com.analyzer.rules;

import java.util.List;

import com.analyzer.results.Result;
import com.analyzer.util.AnalyzerUtil;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class NullArgumentRule implements Rules {
	
	private List<MethodDeclaration> allMethod = null;
	
	@Override
	public void preProcessing(CompilationUnit unit) throws Exception {
		
		this.allMethod = AnalyzerUtil.getMethods(unit, true);
		
		allMethod.forEach(method -> {
			
			AnalyzerUtil.getMethodInvocationTrace(method, unit);
			//System.out.println(Thread.currentThread().getName() + " : " + method.getNameAsString());
		});
		
	}

	@Override
	public Result apply(CompilationUnit unit) throws Exception {
		//System.out.println("Excecute Logic for null argument " + Thread.currentThread().getName());
		
		
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

}
