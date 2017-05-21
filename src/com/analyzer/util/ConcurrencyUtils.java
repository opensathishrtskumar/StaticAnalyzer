package com.analyzer.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConcurrencyUtils {
	
	private static ThreadPoolExecutor workers = new ThreadPoolExecutor(10, 30, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	public static ThreadPoolExecutor getWorkers(){
		return workers;
	}
	
	public static Future<?> execute(Callable<?> work){
		Future<?> result = workers.submit(work);
		return result;
	}
	
	public static void shutDown(){
		workers.shutdown();
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		shutDown();
	}
}
