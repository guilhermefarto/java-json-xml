package farto.cleva.guilherme.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public abstract class FileUtil {

	private static final String UTF_8 = "UTF-8";

	public static File createTempFile(String filename, String data) throws IOException {
		File temporaryFile = File.createTempFile(filename.split("\\.")[0], filename.split("\\.")[1]);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temporaryFile), UTF_8));

		bw.write(data);

		bw.close();

		return temporaryFile;
	}

}
