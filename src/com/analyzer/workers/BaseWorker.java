package com.analyzer.workers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.analyzer.results.Result;
import com.analyzer.rules.Rules;
import com.github.javaparser.ast.CompilationUnit;

public abstract class BaseWorker<T> implements Callable<T>{

	protected CompilationUnit unit;
	protected List<Rules> rules;
	protected List<Result> results = new ArrayList<Result>();
	
	public BaseWorker(CompilationUnit unit) {
		this.unit = unit;
	}
	
	public CompilationUnit getUnit() {
		return unit;
	}

	public void setUnit(CompilationUnit unit) {
		this.unit = unit;
	}

	public List<Rules> getRules() {
		return rules;
	}

	public void setRules(List<Rules> rules) {
		this.rules = rules;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}
}
