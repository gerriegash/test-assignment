package org.mamta.garg.src.test.assignment.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.mamta.garg.sc.test.assignment.task.IdentifyJsLibTask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class HtmlParserService.
 */
public class HtmlParserService {

	/** The Constant API_KEY. */
	private static final String API_KEY = "AIzaSyAgKfJN9y2HfiU90iqxXrcUrQvUiT0OBdI";

	/** The Constant GOOGLE_ENGINE_KEY. */
	private static final String GOOGLE_ENGINE_KEY = "009799914807125141122:xrli-b5fgfm";

	/** The Constant GOOGLE_SEARCH_URL. */
	private static final String GOOGLE_SEARCH_URL = "https://www.googleapis.com/customsearch/v1";

	/** The Constant API_KEY_QUERY_PARAM. */
	private static final String API_KEY_QUERY_PARAM = "?key=%s";

	/** The Constant ENGINE_QUERY_PARAM. */
	private static final String ENGINE_QUERY_PARAM = "&cx=%s";

	/** The Constant SEARCH_QUERY_PARAM. */
	private static final String SEARCH_QUERY_PARAM = "&q=%s";

	/** The Constant SEARCH_URL. */
	private static final String SEARCH_URL = GOOGLE_SEARCH_URL + API_KEY_QUERY_PARAM + ENGINE_QUERY_PARAM
			+ SEARCH_QUERY_PARAM;

	private ExecutorService executor = Executors.newFixedThreadPool(10);

	/**
	 * Gets the final script list.
	 *
	 * @param searchKeyword
	 * @return the final script list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public List<Map.Entry<String, Long>> getFinalScriptList(String searchKeyword) throws IOException {
		String googleResponseJson = getGoogleSearchJson(searchKeyword);
		return getScriptsMap(googleResponseJson);
	}

	/**
	 * Gets the google search json.
	 *
	 * @param searchKeyword
	 *            the search keyword
	 * @return the google search json
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getGoogleSearchJson(String searchKeyword) throws IOException {
		InputStream doc = new URL(String.format(SEARCH_URL, API_KEY, GOOGLE_ENGINE_KEY, searchKeyword)).openStream();
		return readAll(new InputStreamReader(doc));
	}

	/**
	 * Read all.
	 *
	 * @param rd
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	/**
	 * Gets the scripts map.
	 *
	 * @param googleResponseJson
	 *            the google response json
	 * @return the scripts map
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private List<Map.Entry<String, Long>> getScriptsMap(String googleResponseJson) throws IOException {
		JsonNode node = new ObjectMapper().readTree(googleResponseJson);
		List<String> scriptsList = new ArrayList<>();
		for (JsonNode item : node.get("items")) {
			IdentifyJsLibTask identifyJsLibTask = new IdentifyJsLibTask(item);
			Future<List<String>> future = executor.submit(identifyJsLibTask);
			try {
				scriptsList.addAll(future.get());
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			} catch (ExecutionException e) {
				System.out.println(e.getMessage());
			}
		}

		return scriptsList.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting())).entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())).limit(5)
				.collect(Collectors.toList());
	}

}
