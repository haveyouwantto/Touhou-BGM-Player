package hywt.music.touhou.io;

import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class MusicInputStream extends InputStream {
    protected Game game;
    protected Music music;
    protected File file;

    public MusicInputStream(Game game, Music music,File file) {
        this.game = game;
        this.music = music;
        this.file = file;
    }

    public abstract void seek(long pos) throws IOException;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

}
