package org.ivanaguirre.bigpipe.servlet;

import java.io.PrintWriter;
import java.util.concurrent.Callable;

public class PageletTask implements Callable<Void> {
	private PageletContentService pageletContentService = new PageletContentService();

	private int id;
	private PrintWriter writer;
	
	public PageletTask(int id, PrintWriter writer) {
		this.id = id;
		this.writer = writer;
	}

	@Override
	public Void call() throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<script>");

		buffer.append("$('#pagelet" + this.id + "').html('"
				+ pageletContentService.getContent(this.id)
				+ " @ ' +  new Date().toLocaleTimeString() )");

		buffer.append("</script>");
		writer.write(buffer.toString());
		writer.flush();
		return null;
	}
}