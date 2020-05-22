package hywt.music.touhou.savedata;

import hywt.music.touhou.Constants;
import hywt.music.touhou.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistData {
	public String name;
	public String author;
	public List<String> ids;

	@Override
	public String toString() {
		return name + " - " + author;
	}

	public Playlist getPlaylist(){
		List<Music> list= new ArrayList<>();
		for (String id : ids){
			list.add(Constants.bgmdata.getMusicbyId(id));
		}
		return new Playlist(list);
	}
}
