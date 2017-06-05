package farto.cleva.guilherme.main;

import java.io.File;
import farto.cleva.guilherme.bo.JsonXmlParser;

public class MainAsFile {

	private static final File DATA = new File("src/main/resources/data/data.json");

	public static void main(String[] args) {

		try {
			// String xml = new JsonXmlParser().extract(DATA);
			// String xml = new JsonXmlParser("data", "data-item").extract(DATA);
			// String xml = new JsonXmlParser(true).extract(DATA);
			// String xml = new JsonXmlParser("data", "data-item", true).extract(DATA);

			String xml = new JsonXmlParser("data", "data-item", true).replaceTag("resultado/registros/colunasVO", "colunas").extract(DATA);

			System.out.println(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
