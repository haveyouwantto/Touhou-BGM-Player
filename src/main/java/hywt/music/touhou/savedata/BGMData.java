package hywt.music.touhou.savedata;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import hywt.music.touhou.FileReader;

public class BGMData {
	public int[] date;
	public List<Game> games;
	public List<String> comments;

	public BGMData(int[] date, List<Game> games) {
		super();
		this.date = date;
		this.games = games;
	}

	@Override
	public String toString() {
		return "BGMData [date=" + Arrays.toString(date) + ", games=" + games + ", comments=" + comments
				+ "]";
	}

	public List<String> getComments() {
		return comments;
	}
	
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
