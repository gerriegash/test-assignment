package org.mamta.garg.sc.test.assignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.mamta.garg.src.test.assignment.service.HtmlParserService;

/**
 * The Class WebCrawler.
 */
public class WebCrawler {

	/**Parses the google search results. */
	private static HtmlParserService htmlParserService = new HtmlParserService();
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String... args) throws IOException {
		try {
			String searchKeyword = args[0];
			displayTheWinners(searchKeyword);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Please supply an argument");
		}
	}

	private static void displayTheWinners(String searchKeyword) throws IOException {
		List<Map.Entry<String, Long>> finalScriptList = htmlParserService.getFinalScriptList(searchKeyword);
		System.out.println("**************************************************************************************");
		System.out.println("");
		System.out.println("Here is the order of javascript libraries being used :-");
		System.out.println("");
		for (int i = 0; i < finalScriptList.size(); i++)
			System.out.println(String.format("Number %s: With %s occurrences { %s }.", i + 1,
					finalScriptList.get(i).getValue(), finalScriptList.get(i).getKey()));
		System.out.println("");
		System.out.println("**************************************************************************************");
	}

}
