package hywt.music.touhou;

public class Game {
	public String no;
	public String title;
	public int format;
	public Music[] music;

	public Game(String no, String titie, int format, Music[] music) {
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
}
