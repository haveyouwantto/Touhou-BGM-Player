package hywt.music.touhou.io.tfpack;

import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.GameFormats;
import hywt.music.touhou.savedata.Music;

import java.io.File;
import java.io.IOException;

public class TFPack {
    private File pack;
    private Game game;
    private Music music;

    public TFPack(Game g, Music m, File file) {
        this.game = g;
        this.music = m;
        this.pack = file;
    }

    public TFPackInputStream getInputStream() throws IOException {
        switch (game.format) {
            case GameFormats.TFPACK_1:
                return new TFOggInputStream(music, pack);
            case GameFormats.TFPACK_2:
                return new TF2OggInputStream(music, pack);
        }
        return null;
    }
}
