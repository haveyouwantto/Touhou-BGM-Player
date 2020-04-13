package hywt.music.touhou.io.tfpack;

import hywt.music.touhou.savedata.Music;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

// 绯想天/非想天则 ogg 提取
public class TFV1OggInputStream extends TFPackInputStream {
    protected int key;

    public TFV1OggInputStream(Music m, File file) throws IOException {
        this.music = m;
        this.raf = new RandomAccessFile(file, "r");

        raf.seek(m.preludePos);
        int b = raf.read();
        key = b ^ 0x4f;
        raf.seek(m.preludePos);
    }

    @Override
    public int read() throws IOException {
        pointer++;
        if (pointer < music.preludeLength) {
            return raf.read() ^ key;
        } else
            return -1;
    }

    public void seek(long pos) throws IOException {
        this.pointer = pos;
        raf.seek(pos);
    }

    @Override
    public void mark(int readlimit) {
        markpos = pointer;
    }

    @Override
    public void reset() throws IOException {
        seek(markpos);
    }

    @Override
    public void close() throws IOException {
        raf.close();
    }

    @Override
    public int available(){
        return (int) (music.preludeLength - pointer);
    }
}
