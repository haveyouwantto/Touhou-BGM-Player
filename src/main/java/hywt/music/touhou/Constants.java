package hywt.music.touhou;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;

import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.PCMInputStream;
import hywt.music.touhou.savedata.BGMData;

public class Constants {
    public static BGMData bgmdata;
    public static Hashtable<String, Class<? extends MusicInputStream>> formatMap;

    public static void init() throws IOException {
        bgmdata = JSONLoader.readBGMData();
        formatMap = new Hashtable<>();
        formatMap.put("touhou_bgm_player:wave_files", PCMInputStream.class);
    }

}
