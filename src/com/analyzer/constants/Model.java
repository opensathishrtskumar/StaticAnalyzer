package com.analyzer.constants;

import java.io.File;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class Model {

	private CompilationUnit unit;
	private File file;
	private MethodDeclaration method;
	
	public Model() {
		
	}
	
	public Model(CompilationUnit unit, File file) {
		this.unit = unit;
		this.file = file;
	}
	
	public CompilationUnit getUnit() {
		return unit;
	}
	public void setUnit(CompilationUnit unit) {
		this.unit = unit;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}

	public MethodDeclaration getMethod() {
		return method;
	}

	public void setMethod(MethodDeclaration method) {
		this.method = method;
	}
}
