package hywt.music.touhou.savedata;

import com.google.gson.Gson;
import hywt.music.touhou.Logger;
import hywt.music.touhou.io.FileReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BGMPath {
    public HashMap<String, String> path;

    public BGMPath() {
        this.path = new LinkedHashMap<>();
    }

    public static BGMPath load() {
        Gson g = new Gson();
        String json;
        try {
            json = FileReader.read(new File("path.json"));
            return g.fromJson(json, BGMPath.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logger.error(e);
        }
        return null;
    }

    public void save() {
        Gson g = new Gson();
        String s = g.toJson(this);
        try {
            FileReader.save(new File("path.json"), s);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logger.error(e);
        }
    }
}
