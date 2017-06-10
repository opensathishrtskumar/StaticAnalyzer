package com.analyzer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.analyzer.util.ConcurrencyUtils;
import com.analyzer.util.Utility;
import com.analyzer.workers.AnalyzeWorker;
import com.github.javaparser.ast.CompilationUnit;

import static com.analyzer.util.Utility.*;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		String sourceFolder = "C:/Users/Gokul.m/workspace/Git/EMS/src";

		Utility.loadSourceFiles(sourceFolder);

		String[] files2Scan = {
				"C:/Users/Gokul.m/workspace/Git/EMS/src/com/ems/UI/swingworkers/DBPollingWorker.java",
				//"/home/sathishrtskumar/Downloads/EMS_Windows/src/com/ems/UI/internalframes/PollingIFrame.java",
				//"/home/sathishrtskumar/Downloads/sEMS_Windows/src/com/ems/UI/swingworkers/ManageDeviceTask.java",
				//sourceFolder + "/com/ems/util/ConfigHelper.java",
				//sourceFolder + "/com/ems/UI/swingworkers/ManageDeviceTask.java"
			};

		
		List<Future<?>> tasks = new ArrayList<>();
		
		for(String scanMe : files2Scan){			
			CompilationUnit unitToAnalyze = Utility.createCompilationUnit(scanMe);
			AnalyzeWorker worker = new AnalyzeWorker(unitToAnalyze);			
			worker.setRules(getRequiredRules(new String[]{sourceFolder},new String[]{}));
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
