package com.analyzer.rules;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.analyzer.constants.Model;
import com.analyzer.dto.MethodTraceHolder;
import com.analyzer.results.Result;
import com.analyzer.util.AnalyzerUtil;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

public class NullArgumentRule implements Rules {

	private List<MethodDeclaration> allMethod = null;
	private String[] sourcePath = null, jarPath = null;

	private Map<MethodDeclaration, List<Model>> methodTrace = new LinkedHashMap<MethodDeclaration, List<Model>>();

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

		if (this.allMethod != null) {

			for (int i = 0; i < this.allMethod.size(); i++) {
				MethodDeclaration method = this.allMethod.get(i);
				JavaParserFacade facade = AnalyzerUtil.getJavaParserFacade(getSourcePath(), getJarPath());

				MethodTraceHolder traceHolder = new MethodTraceHolder().setJavaParserFacade(facade)
						.setMethodDeclaration(method).setCompilationUnit(unit).setDepth(0);

				List<Model> list = AnalyzerUtil.getMethodInvocationTrace(traceHolder);
				this.methodTrace.put(method, list);
			}
		}

		return checkNullParameter(unit);
	}

	private Result checkNullParameter(CompilationUnit unit){
		
		for(Entry<MethodDeclaration, List<Model>> entry : this.methodTrace.entrySet()){
			MethodDeclaration method = entry.getKey();
			
			Model model = new Model();
			model.setUnit(unit);
			model.setMethod(method);
			model.setInvocationList(entry.getValue());
			
			//System.out.println(method.getDeclarationAsString());
			AnalyzerUtil.leafNodeFirst(entry.getValue());
			
			//method.accept(new ParamNullCheckVisitor(model), null);
		}
		
		return null;
	}
	
	
	@Override
	public void postProcessing(CompilationUnit unit) throws Exception {
		
		for(Entry<MethodDeclaration, List<Model>> entry : this.methodTrace.entrySet()){
			System.out.println(entry.getKey().getDeclarationAsString());
			AnalyzerUtil.printMethodTrace(entry.getValue());
		}
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
