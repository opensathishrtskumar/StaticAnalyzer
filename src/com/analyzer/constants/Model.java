package com.analyzer.constants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.analyzer.util.Utility;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class Model {

	private CompilationUnit unit;
	private File file;
	private MethodDeclaration method;
	private List<Model> invocationList;
	private int depth;
	
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

	public List<Model> getInvocationList() {
		return invocationList;
	}

	public void setInvocationList(List<Model> invocationList) {
		this.invocationList = invocationList;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public String toString() {
		return  Utility.getQualifiedName(unit) + " -> " + method.getDeclarationAsString() + Arrays.toString(invocationList.toArray());
	}
	
}
