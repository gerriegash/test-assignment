package org.mamta.garg.sc.test.assignment.task;

import static java.util.stream.Collectors.toList;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class JsIdentifierTask.
 */
public class IdentifyJsLibTask implements Callable<List<String>> {

	/** The item. */
	private JsonNode item;

	/**
	 * Instantiates a new javascript naming task.
	 *
	 * @param item
	 */
	public IdentifyJsLibTask(JsonNode item) {
		this.item = item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public List<String> call() throws Exception {
		try {
			if (Jsoup.connect(item.get("link").asText()).timeout(100000).execute().statusCode() == 200) {
				Document doc = Jsoup.connect(item.get("link").asText()).timeout(100000).get();
				Elements scriptTags = doc.select("script");
				return scriptTags.stream().filter(scriptTag -> scriptTag.hasAttr("src"))
						.map(scriptTag -> scriptTag.attr("src")).filter(scriptName -> !StringUtil.isBlank(scriptName))
						.collect(toList());
			}
		} catch (SocketException e) {
			System.out.println("Connection timed out while trying to reach " + item.get("link").asText());
		}
		return new ArrayList<>();
	}
}
