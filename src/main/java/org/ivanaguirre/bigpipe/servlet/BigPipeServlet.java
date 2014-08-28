package org.ivanaguirre.bigpipe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 'BigPiped' version of nobigpipe.html.
 *
 * @author ivandeaguirre
 *
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/bigpipe.html" })
public class BigPipeServlet extends HttpServlet {

	private static final int MAX_PAGELETS = 10;

	private ExecutorService executorService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// The thread pool size has a huge impact on BigPipe performance
		// Not sure how to handle this pool for multiple Servlets
		this.executorService = Executors.newFixedThreadPool(10);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.executorService.shutdown();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html");
		resp.setCharacterEncoding("utf-8");

		PrintWriter writer = resp.getWriter();
		writer.write("<!DOCTYPE>");
		writer.write("<html>");

		writeHead(writer);
		
		writer.write("<body>");

		writePageletsHeader(writer);

		writePagelets(writer);

		writer.write("</body>");

		writer.write("</html>");
		writer.flush();

		Collection<Callable<Void>> tasks = new ArrayList<>();
		for (int pageletId = 0; pageletId < MAX_PAGELETS; pageletId++) {
			tasks.add(new PageletTask(pageletId, writer));
		}

		try {
			executorService.invokeAll(tasks);
		} catch (InterruptedException e) {
			throw new ServletException(e);
		}
	}

	private void writePagelets(PrintWriter writer) {
		for (int pageletId = 0; pageletId < 10; pageletId++) {
			writer.write("<h1 id='pagelet" + pageletId + "'>Waiting...<h1/>");
		}
	}

	private void writePageletsHeader(PrintWriter writer) {
		writer.write("<script>");
		writer.write("document.write('<h1>' + new Date().toLocaleTimeString() + '</h1>');");
		writer.write("</script>");
		writer.write("<h1>Some Pagelets:</h1>");
	}

	private void writeHead(PrintWriter writer) {
		writer.write("<head>");
		writer.write("<script src='js/jquery-2.1.1.min.js'></script>");
		writer.write("</head>");
	}
}