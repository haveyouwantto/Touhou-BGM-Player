package hywt.music.touhou.savedata;

import java.util.List;

public class Game {
	public String no;
	public String title;
	public int format;
	public List<Music> music;

	public Game(String no, String titie, int format, List<Music> music) {
		super();
		this.no = no;
		this.title = titie;
		this.format = format;
		this.music = music;
	}

	@Override
	public String toString() {
		return no + " - " + title;
	}
	
	public int indexOf(Music m) {
		return music.indexOf(m);
	}
}
