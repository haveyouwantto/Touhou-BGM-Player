package hywt.music.touhou;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

import hywt.music.touhou.savedata.BGMData;

public class JSONLoader {

	public static BGMData readBGMData() throws IOException {
		Gson g = new Gson();
		String json = FileReader.read(new File("BGM.json"));
		return g.fromJson(json, BGMData.class);
	}
}
