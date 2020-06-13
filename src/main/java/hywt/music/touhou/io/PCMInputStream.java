package hywt.music.touhou.io;

import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PCMInputStream extends MusicInputStream {
    private final RandomAccessFile raf;

    public PCMInputStream(Game game, Music music, File file) throws IOException {
        super(game, music, file);
        this.raf = new RandomAccessFile(file, "r");
        raf.seek(music.preludePos);
    }

    @Override
    public synchronized void seek(long pos) throws IOException {
        raf.seek(music.preludePos + pos);
    }

    @Override
    public int read() throws IOException {
        return raf.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return raf.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return raf.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        raf.close();
        super.close();
    }
}
