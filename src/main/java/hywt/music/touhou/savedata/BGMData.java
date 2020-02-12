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

	public Game getGamebyId(String id) {
		for (int i = 0; i < games.size(); i++) {
			if (games.get(i).no.equals(id))
				return games.get(i);
		}
		return null;
	}

	public Music getMusicbyId(String gameId, String musicId) {
		return getGamebyId(gameId.toUpperCase()).music.get(Integer.parseInt(musicId) - 1);
	}

	@Override
	public String toString() {
		return "BGMData [date=" + Arrays.toString(date) + ", games=" + games + ", comments=" + comments + "]";
	}

	public List<String> getComments() {
		return comments;
	}

	public static BGMData read() throws IOException {
		Gson g = new Gson();
		String json = FileReader.read(new File("BGM.json"));
		return g.fromJson(json, BGMData.class);
	}

	public int[] indexOf(Music m) {
		for (int i = 0; i < games.size(); i++) {
			int index = games.get(i).indexOf(m);
			if (index != -1) {
				int[] out = { i, index };
				return out;
			}
		}
		return null;
	}
	
	public Game getGamebyMusic(Music m) {
		for (int i = 0; i < games.size(); i++) {
			int index = games.get(i).indexOf(m);
			if (index != -1) return games.get(i);
		}
		return null;
	}
}
