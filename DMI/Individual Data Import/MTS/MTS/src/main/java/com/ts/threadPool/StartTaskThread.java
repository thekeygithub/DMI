package com.ts.threadPool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ts.vo.mts.StandardizeData;

public class StartTaskThread implements Runnable {
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	private String batchNum;
	private StandardizeData data;
	public StartTaskThread(ThreadPoolTaskExecutor threadPoolTaskExecutor, String batchNum,StandardizeData data) {
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
		this.batchNum = batchNum;
		this.data = data;
	}

	@Override
	public synchronized void run() {
		String task = "task@ " + batchNum;
		StandardizeData standardizeData = data;
		System.out.println("创建任务并提交到线程池中：" + task);
		FutureTask<String> futureTask = new FutureTask<String>(new ThreadPoolTask(standardizeData));
		threadPoolTaskExecutor.execute(futureTask);
		// 在这里可以做别的任何事情
		String result = null;
		try {
			// 取得结果，同时设置超时执行时间为1秒。同样可以用future.get()，不设置执行超时时间取得结果
			result = futureTask.get(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			futureTask.cancel(true);
		} catch (ExecutionException e) {
			futureTask.cancel(true);
		} catch (Exception e) {
			futureTask.cancel(true);
			// 超时后，进行相应处理
		} finally {
			System.out.println("task@" + batchNum + ":result=" + result);
		}
	}
}
