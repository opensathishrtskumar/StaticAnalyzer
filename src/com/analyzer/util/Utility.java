package com.analyzer.util;

import static com.analyzer.constants.LangConstants.JAVA_EXTN;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.analyzer.constants.Model;
import com.analyzer.rules.NullArgumentRule;
import com.analyzer.rules.Rules;
import com.analyzer.workers.UnitCreator;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public final class Utility {
	
	private static final Map<String,Object> compilationUnitMap = new ConcurrentHashMap<>(500);
	
	private Utility(){
	}

	public static Map<String, Object> getCompilationUnitMap(){
		return compilationUnitMap;
	}
	
	public static CompilationUnit createCompilationUnit(File source) throws FileNotFoundException{
		
		CompilationUnit compilationUnit = JavaParser.parse(source);
        
		addCompilationUnit2Map(source, compilationUnit);
		
		return compilationUnit;
	}
	
	public static CompilationUnit createCompilationUnit(String sourceFile) throws FileNotFoundException{
		
		File file = new File(sourceFile);
		
		return createCompilationUnit(file);
	}
	

	public static String getPackageName(CompilationUnit unit){
		
		final StringBuilder builder  = new StringBuilder();
		
		unit.getPackageDeclaration().ifPresent(p -> {builder.append(p.getChildNodes().get(0).toString());});
		
		return builder.toString();
	}
	
	private static void addCompilationUnit2Map(File file, CompilationUnit compilationUnit){
		String packageName = getPackageName(compilationUnit);
		
		StringBuilder builder = new StringBuilder();
		builder.append(packageName);
		if(!packageName.isEmpty())
			builder.append(".");
		
		NodeList<TypeDeclaration<?>> list = compilationUnit.getTypes();
		
		list.forEach(node -> {
			if(node instanceof ClassOrInterfaceDeclaration){
				ClassOrInterfaceDeclaration classDec = (ClassOrInterfaceDeclaration) node;
                    if (!classDec.isInterface()) {
                    	builder.append(classDec.getName());
                    }
			}
		});
		
		Model model = new Model(compilationUnit, file);
		
		getCompilationUnitMap().put(builder.toString(), model);
	}
	
	public static List<File> listJavaFiles(String directoryName) {
		File directory = new File(directoryName);

		List<File> resultList = new ArrayList<File>();

		File[] fList = directory.listFiles();
		
		for (File file : fList) {
			if (file.isFile() && file.getName().endsWith(JAVA_EXTN)) {
				resultList.add(file);
			} else if (file.isDirectory()) {
				resultList.addAll(listJavaFiles(file.getAbsolutePath()));
			}
		}
		return resultList;
	}
	
	public static Map<String,Object> loadSourceFiles(String sourceFolder){
		
		List<File> sourceFiles = listJavaFiles(sourceFolder);
		
		System.out.println("Total java files available : " + sourceFiles.size());
		
		List<Future<?>> future = new ArrayList<Future<?>>();
		
		for(File sourceFile : sourceFiles){
			try {
				
				UnitCreator creator = new UnitCreator(sourceFile);
				Future<?> task = ConcurrencyUtils.execute(creator);
				future.add(task);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Compilation unit creation failed : " 
						+ sourceFile.getAbsolutePath());
			}
		}
		
		for(Future<?> task : future){
			try {
				task.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Number of compilation units created : " 
				+ getCompilationUnitMap().size());
		
		return getCompilationUnitMap();
	}
	
	
	
	/**
	 * Add Rules required to execute against input sources
	 * 
	 */
	public static List<Rules> getRequiredRules(String[] sourcePath, String[] jarPath){
		List<Rules> rules = new ArrayList<Rules>();
		
		rules.add(new NullArgumentRule(sourcePath, jarPath));
		
		return rules;
	}
	
}
