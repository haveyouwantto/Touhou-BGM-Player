package hywt.music.touhou.savedata;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import hywt.music.touhou.io.FileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistList {
    public List<PlaylistData> list;

    public PlaylistList() {
    }

    public void load(File path) {
        list = new ArrayList<>();
        Gson g = new Gson();
        try {
            File[] listFiles = path.listFiles();
            for (File listFile : listFiles) {
                if (listFile.getName().endsWith(".json")) {
                    try {
                        list.add(g.fromJson(FileReader.read(listFile), PlaylistData.class));
                    } catch (JsonSyntaxException ex) {
                        ex.printStackTrace();
                    }
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
        String builder = "PlaylistList [list=" +
                list +
                "]";
        return builder;
    }
}
