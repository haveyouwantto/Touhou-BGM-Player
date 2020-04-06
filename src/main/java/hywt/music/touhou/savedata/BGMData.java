package hywt.music.touhou.savedata;

import java.util.Arrays;
import java.util.List;

public class BGMData {
	public int[] date;
	public List<Game> games;
	public List<String> comments;
	public List<String> order;

	public BGMData(int[] date, List<Game> games) {
		super();
		this.date = date;
		this.games = games;
	}

	public Game getGamebyId(String id) {
		return games.get(order.indexOf(id.toUpperCase()));
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
