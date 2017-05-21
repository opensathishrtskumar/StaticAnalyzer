package com.analyzer.rules;

import com.analyzer.results.Result;
import com.github.javaparser.ast.CompilationUnit;

public interface Rules {
	
	public Result result = null;
	
	public void preProcessing(CompilationUnit unit) throws Exception;
	
	public Result apply(CompilationUnit unit) throws Exception;	
	
	public void postProcessing(CompilationUnit unit) throws Exception;
	
	public Result getResult()throws Exception;
}
