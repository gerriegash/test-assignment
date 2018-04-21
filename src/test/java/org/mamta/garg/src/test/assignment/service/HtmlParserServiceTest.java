package org.mamta.garg.src.test.assignment.service;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * The Class HtmlParserServiceTest.
 */
public class HtmlParserServiceTest extends TestCase {
	
	/** The parser service. */
	private HtmlParserService parserService = new HtmlParserService();
	
	/**
	 * Gets the google search json.
	 *
	 * @return the google search json
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void getGoogleSearchJson() throws IOException {
		parserService.getGoogleSearchJson("lectures");
	}
}
