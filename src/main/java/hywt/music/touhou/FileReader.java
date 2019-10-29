package hywt.music.touhou;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReader {
	 public static String read(File file) throws IOException {
		  FileInputStream fis = new FileInputStream(file);
		  byte[] data = new byte[(int) file.length()];
		  fis.read(data);
		  fis.close();

		  String str = new String(data, "UTF-8");
		  return str;
	  }
}
