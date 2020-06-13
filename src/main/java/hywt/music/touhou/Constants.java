package hywt.music.touhou;

import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.PCMInputStream;
import hywt.music.touhou.savedata.BGMData;

import java.io.IOException;
import java.util.Hashtable;

public class Constants {
    public static BGMData bgmdata;
    public static Hashtable<String, Class<? extends MusicInputStream>> formatMap;

    public static void init() throws IOException {
        bgmdata = JSONLoader.readBGMData();
        formatMap = new Hashtable<>();
        formatMap.put("touhou_bgm_player:wave_files", PCMInputStream.class);
    }

}
