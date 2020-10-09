package hywt.music.touhou.io;

import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

// TODO
public abstract class MusicFile {
    public Game game;
    public Music music;

    public abstract MusicInputStream getMusicInputStream();
}
