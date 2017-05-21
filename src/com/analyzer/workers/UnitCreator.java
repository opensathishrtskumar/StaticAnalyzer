package com.analyzer.workers;

import java.io.File;
import java.util.concurrent.Callable;

import com.analyzer.util.Utility;
import com.github.javaparser.ast.CompilationUnit;

public class UnitCreator implements Callable<CompilationUnit> {
	
	private File file;
	
	public UnitCreator(File file) {
		this.file = file;
	}
	
	@Override
	public CompilationUnit call() throws Exception {
		
		CompilationUnit unit = Utility.createCompilationUnit(file);
		
		return unit;
	}

}
