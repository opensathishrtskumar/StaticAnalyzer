package com.analyzer;

import static com.analyzer.util.Utility.getRequiredRules;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.analyzer.constants.Model;
import com.analyzer.util.ConcurrencyUtils;
import com.analyzer.util.Utility;
import com.analyzer.workers.AnalyzeWorker;
import com.github.javaparser.ast.CompilationUnit;

public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {

		String sourceFolder = "C:/Users/Gokul.m/workspace/Git/EMS/src";

		Map<String, Object> sourceUnits = Utility.loadSourceFiles(sourceFolder);

		String[] files2Scan = {
				//"com.ems.UI.swingworkers.DBPollingWorker",
				//"com.ems.tmp.datamngr.TempDataManager",
				//"com.ems.UI.Main",
				//"com.ems.util.AnalyzeTest",
				//"com.ems.UI.internalframes.DashboardFrame",
				//"com.ems.response.handlers.DashboardResponseHandler",
				"com.ems.UI.internalframes.ReportsIFrame",
			};

		
		List<Future<?>> tasks = new ArrayList<>();
		
		for(String scanMe : files2Scan){			
			Model model = (Model)sourceUnits.get(scanMe);
			CompilationUnit unitToAnalyze = model.getUnit();
			
			if(unitToAnalyze == null){
				continue;
			}
			
			AnalyzeWorker worker = new AnalyzeWorker(unitToAnalyze);			
			worker.setRules(getRequiredRules(new String[]{sourceFolder},new String[]{/*pass here jar(s)*/}));
			tasks.add(ConcurrencyUtils.execute(worker));
		}										
		
		for(Future<?> task : tasks){
			try {
				task.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {				
				e.printStackTrace();
			}
		}
		
		
		ConcurrencyUtils.shutDown();
	}

}
