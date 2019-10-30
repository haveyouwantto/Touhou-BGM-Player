package hywt.music.touhou;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class BGMPath {
	public List<DataPath> path;

	public BGMPath() {
		this.path = new ArrayList<DataPath>();
	}

	public static BGMPath load() {
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

	public void save() {
		Gson g = new Gson();
		String s = g.toJson(this);
		try {
			FileReader.save(new File("path.json"), s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
