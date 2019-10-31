package hywt.music.touhou;

import java.util.Arrays;
import java.util.List;

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
}
