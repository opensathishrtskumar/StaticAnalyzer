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

		String sourceFolder = "/home/sathishrtskumar/Downloads/EMS_Windows/src/";

		Utility.loadSourceFiles(sourceFolder);

		String[] files2Scan = {
				"/home/sathishrtskumar/Downloads/EMS_Windows/src/com/ems/UI/internalframes/PingInternalFrame.java"/*,
				"/home/sathishrtskumar/Downloads/EMS_Windows/src/com/ems/UI/internalframes/PollingIFrame.java",
				"/home/sathishrtskumar/Downloads/EMS_Windows/src/com/ems/UI/swingworkers/ManageDeviceTask.java"*/	
			};

		
		List<Future<?>> tasks = new ArrayList<>();
		
		for(String scanMe : files2Scan){			
			CompilationUnit unitToAnalyze = Utility.createCompilationUnit(scanMe);
			AnalyzeWorker worker = new AnalyzeWorker(unitToAnalyze);			
			worker.setRules(getRequiredRules());
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
