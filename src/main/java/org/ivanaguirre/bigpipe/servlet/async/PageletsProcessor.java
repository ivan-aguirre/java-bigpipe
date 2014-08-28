package org.ivanaguirre.bigpipe.servlet.async;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ivanaguirre.bigpipe.servlet.PageletTask;

public class PageletsProcessor {

	// The thread pool size has a huge impact on BigPipe performance
	// Not sure how to handle this pool for multiple Servlets
	private ExecutorService executorService= Executors.newFixedThreadPool(10);

	private Collection<PageletTask> tasks = new ArrayList<>();

	private PrintWriter writer;
	
	public PageletsProcessor(PrintWriter writer) {
		this.writer = writer;
	}

	public void execute() {
		try {
			executorService.invokeAll(tasks);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void add(int pageletId) {
		tasks.add(new PageletTask(pageletId, writer));
	}
}
