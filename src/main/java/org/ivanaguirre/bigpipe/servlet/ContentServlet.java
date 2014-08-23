package org.ivanaguirre.bigpipe.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web Service to generate content for Pagelet.
 *
 * @author ivandeaguirre
 *
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/content/*" })
public class ContentServlet extends HttpServlet {

	private PageletContentService pageletContentService = new PageletContentService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("plain/text");
		resp.setCharacterEncoding("utf-8");

		int pageletId = getPageletId(req);

		resp.getWriter().write(pageletContentService.getContent(pageletId));
	}

	private int getPageletId(HttpServletRequest req) {
		String uri = req.getRequestURI();
		return Integer.valueOf(uri.substring(uri.lastIndexOf('/') + 1));
	}
}
