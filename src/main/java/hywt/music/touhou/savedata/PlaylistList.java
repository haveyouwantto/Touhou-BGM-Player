package hywt.music.touhou.savedata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import hywt.music.touhou.FileReader;

public class PlaylistList {
	public List<Playlist> list;
	
	public PlaylistList() {
	}

	public void load(File path) {
		list = new ArrayList<Playlist>();
		Gson g = new Gson();
		try {
			File[] listFiles = path.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				if (listFiles[i].getName().endsWith(".json")) {
					list.add(g.fromJson(FileReader.read(listFiles[i]), Playlist.class));
				}
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlaylistList [list=");
		builder.append(list);
		builder.append("]");
		return builder.toString();
	}
}
