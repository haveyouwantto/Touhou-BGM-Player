package hywt.music.touhou;

import hywt.music.touhou.savedata.Music;

import java.util.Iterator;
import java.util.List;

public class Playlist {
    private final List<Music> musicList;
    private int pos;

    public Playlist(List<Music> musicList) {
        this.musicList = musicList;
        pos = -1;
    }

    public Music next() {
        pos++;
        if (pos >= musicList.size()) pos = 0;
        return musicList.get(pos);
    }

    public Music previous() {
        pos--;
        if (pos < 0) pos = musicList.size() - 1;
        return musicList.get(pos);
    }

    public Iterator<Music> getIterator() {
        return musicList.iterator();
    }

    public void setPos(Music music) {
        int index = musicList.indexOf(music);
        if (index != -1) pos = index - 1;
    }

    public void setPos(int index) {
        pos = index - 1;
    }
}
