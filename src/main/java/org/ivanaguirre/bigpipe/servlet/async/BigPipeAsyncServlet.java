package org.ivanaguirre.bigpipe.servlet.async;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 'BigPiped' version of nobigpipe.html with Async Servlet.
 *
 * @author ivandeaguirre
 *
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/bigpipeasync.html" }, asyncSupported=true)
public class BigPipeAsyncServlet extends HttpServlet {
	private static final int MAX_PAGELETS = 10;

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

		final PageletsProcessor processor = new PageletsProcessor(writer);

		for (int pageletId = 0; pageletId < MAX_PAGELETS; pageletId++) {
			processor.add(pageletId);
		}

		final AsyncContext asyncContext = req.startAsync();
		System.out.println(Thread.currentThread().getName() + ": Going Async!");
		asyncContext.start(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + ": Running in Background!");
				processor.execute();
				asyncContext.complete();
				System.out.println(Thread.currentThread().getName() + ": Background Tasks are done!");
			}
		});
		System.out.println(Thread.currentThread().getName() + ": Servlet Thread done!");
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