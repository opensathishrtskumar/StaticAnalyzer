package com.analyzer.workers;

import java.util.List;

import com.analyzer.results.Result;
import com.analyzer.rules.Rules;
import com.github.javaparser.ast.CompilationUnit;

public class AnalyzeWorker extends BaseWorker<List<Result>> {
	
	public AnalyzeWorker(CompilationUnit unit) {
		super(unit);
	}
	
	@Override
	public List<Result> call() throws Exception {
		
		List<Rules> rules = getRules();
		
		List<Result> results = getResults();
		
		for(Rules rule : rules){
			
			CompilationUnit compilationUnit = getUnit();
			
			rule.preProcessing(compilationUnit);
			
			Result result = rule.apply(compilationUnit);
			results.add(result);
			
			rule.postProcessing(compilationUnit);
			
		}
		
		return results;
	}
	
}
