package hywt.music.touhou;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

public class BGMPath {
	public String[] path;
	public static BGMPath fromJSON(){
		Gson g = new Gson();
		String json;
		try {
			json = FileReader.read(new File("path.json"));
			return g.fromJson(json, BGMPath.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
