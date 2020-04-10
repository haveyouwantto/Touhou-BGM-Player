package hywt.music.touhou.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileReader {
	public static String read(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();

		String str = new String(data, "UTF-8");
		return str;
	}

	public static void save(File file, String content) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		osw.write(content);
		osw.close();
	}
}
