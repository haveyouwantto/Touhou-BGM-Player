package hywt.music.touhou;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

public class InfoReader {
	public static BGMData read() {
		Gson g = new Gson();
		try {
			String json = FileReader.read(new File("BGM.json"));
			return g.fromJson(json, BGMData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
