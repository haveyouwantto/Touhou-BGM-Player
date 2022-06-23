package hywt.music.touhou;

import com.google.gson.Gson;
import hywt.music.touhou.io.FileReader;
import hywt.music.touhou.savedata.BGMData;
import hywt.music.touhou.savedata.Game;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class JSONLoader {

    public static BGMData readBGMData() {
        Gson g = new Gson();
        Reader json = new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("BGM.json")), StandardCharsets.UTF_8);
        BGMData bgmData = g.fromJson(json, BGMData.class);
        bgmData.order = new ArrayList<>();
        for (int i = 0; i < bgmData.games.size(); i++) {
            Game game = bgmData.games.get(i);
            bgmData.order.add(game.no);
        }
        return bgmData;
    }
}
