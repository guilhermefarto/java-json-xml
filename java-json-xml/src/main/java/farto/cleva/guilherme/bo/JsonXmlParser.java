package farto.cleva.guilherme.bo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import farto.cleva.guilherme.utils.StringUtil;

public class JsonXmlParser {

	private static final String SEPARATOR = "/";
	public static final String ROOT = "root";
	private static final String COLLECTION_ITEM = "item";

	private static final String TAG_START = "<{0}>";
	private static final String TAG_END = "</{0}>";
	private static final String CDATA = "<![CDATA[{0}]]>";

	private String rootTag = JsonXmlParser.ROOT;
	private String collectionItemTag = JsonXmlParser.COLLECTION_ITEM;
	private boolean usesCData = false;

	private Map<String, String> tags = null;

	public JsonXmlParser() {
		super();
	}

	public JsonXmlParser(boolean usesCData) {
		this(JsonXmlParser.ROOT, JsonXmlParser.COLLECTION_ITEM, usesCData);
	}

	public JsonXmlParser(String rootTag, String collectionItemTag) {
		this(rootTag, collectionItemTag, false);
	}

	public JsonXmlParser(String rootTag, String collectionItemTag, boolean usesCData) {
		super();

		this.rootTag = rootTag;
		this.collectionItemTag = collectionItemTag;
		this.usesCData = usesCData;
	}

	public JsonXmlParser replaceTags(Map<String, String> tags) {
		this.tags = tags;

		return this;
	}

	public JsonXmlParser replaceTag(String originalTag, String newTag) {
		if (this.tags == null) {
			this.tags = new LinkedHashMap<String, String>();
		}

		this.tags.put(originalTag, newTag);

		return this;
	}

	public String extract(File jsonFile) throws FileNotFoundException, IOException, ParseException {
		JSONObject root = (JSONObject) new JSONParser().parse(new FileReader(jsonFile));

		StringBuilder xml = new StringBuilder("");

		if (StringUtil.isNotEmptyOrNull(rootTag)) {
			xml.append(MessageFormat.format(TAG_START, this.rootTag));
		}

		this.extract(root, xml, "");

		if (StringUtil.isNotEmptyOrNull(rootTag)) {
			xml.append(MessageFormat.format(TAG_END, this.rootTag));
		}

		return xml.toString();
	}

	private void extract(JSONObject root, StringBuilder xml, String path) {
		if (root != null) {
			for (Iterator<?> iterator = root.keySet().iterator(); iterator.hasNext();) {
				Object key = iterator.next();

				String newPath = (StringUtil.isNotEmptyOrNull(path) ? path + SEPARATOR : path) + key;

				String newTag = this.verifyTag(newPath, String.valueOf(key));

				if (root.get(key) instanceof org.json.simple.JSONObject) {
					xml.append(MessageFormat.format(TAG_START, newTag));

					extract((JSONObject) root.get(key), xml, newPath);

					xml.append(MessageFormat.format(TAG_END, newTag));
				} else if (root.get(key) instanceof org.json.simple.JSONArray) {
					JSONArray jsonArray = (JSONArray) root.get(key);

					xml.append(MessageFormat.format(TAG_START, newTag));

					for (Object item : jsonArray) {
						xml.append(MessageFormat.format(TAG_START, this.collectionItemTag));

						extract((JSONObject) item, xml, newPath);

						xml.append(MessageFormat.format(TAG_END, this.collectionItemTag));
					}

					xml.append(MessageFormat.format(TAG_END, newTag));
				} else {
					xml.append(MessageFormat.format(TAG_START, newTag)).append(this.usesCData ? MessageFormat.format(CDATA, root.get(key)) : root.get(key)).append(MessageFormat.format(TAG_END, newTag));
				}
			}
		}
	}

	private String verifyTag(String key, String defaultValue) {
		return this.tags != null && this.tags.containsKey(key) ? String.valueOf(this.tags.get(key)) : defaultValue;
	}

}
