package hywt.music.touhou.savedata;

import java.util.List;

public class Playlist {
	public String name;
	public String author;
	public List<String[]> ids;
	@Override
	public String toString() {
		return name + " - " + author;
	}
}
