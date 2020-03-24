package hywt.music.touhou;

import java.io.IOException;

import hywt.music.touhou.savedata.BGMData;

public class Constants {
	public static BGMData bgmdata;
	public static void init() throws IOException {
		bgmdata = JSONLoader.readBGMData();
	}
}
