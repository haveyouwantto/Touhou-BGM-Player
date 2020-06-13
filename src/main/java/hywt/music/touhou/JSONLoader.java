package hywt.music.touhou;

import com.google.gson.Gson;
import hywt.music.touhou.io.FileReader;
import hywt.music.touhou.savedata.BGMData;
import hywt.music.touhou.savedata.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JSONLoader {

    public static BGMData readBGMData() throws IOException {
        Gson g = new Gson();
        String json = FileReader.read(new File("BGM.json"));
        BGMData bgmData = g.fromJson(json, BGMData.class);
        bgmData.order = new ArrayList<>();
        for (int i = 0; i < bgmData.games.size(); i++) {
            Game game = bgmData.games.get(i);
            bgmData.order.add(game.no);
        }
        return bgmData;
    }
}
