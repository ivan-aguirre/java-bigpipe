package org.ivanaguirre.bigpipe.servlet;

public class PageletContentService {

	public  String getContent(int pageletId) {
		// Fake Delay
		//for (long i = 0; i < 2000000000; i++);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "This is the content for Pagelet " + pageletId;
	}

}
