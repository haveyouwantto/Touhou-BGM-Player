package hywt.music.touhou.savedata;

import java.util.List;

public class Game {
    public String no;
    public String title;
    public GameFormat format;
    public List<Music> music;
    public String fileName;
    public List<String> artist;
    public String metadata;

    public Game(String no, String titie, GameFormat format, String fileName, List<Music> music, String metadata, List<String> artist) {
        super();
        this.no = no;
        this.title = titie;
        this.format = format;
        this.music = music;
        this.fileName = fileName;
        this.artist = artist;
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return no + " - " + title;
    }

    public int indexOf(Music m) {
        return music.indexOf(m);
    }
}
