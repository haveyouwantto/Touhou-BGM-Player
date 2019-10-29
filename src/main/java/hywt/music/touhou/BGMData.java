package hywt.music.touhou;

import java.util.Arrays;

public class BGMData {
	public int[] date;
	public Game[] games;
	public String[] comments;
	public BGMData(int[] date, Game[] games) {
		super();
		this.date = date;
		this.games = games;
	}
	@Override
	public String toString() {
		return "BGMData [date=" + date + ", games=" + Arrays.toString(games) + "]";
	}
}
