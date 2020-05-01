package hywt.music.touhou.io.tfpack;

import hywt.music.touhou.savedata.Music;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

// TH10.5 | TH12.3
public class TFV1OggInputStream extends TFPackInputStream {
    protected int key;

    public TFV1OggInputStream(Music m, File file) throws IOException {
        this.music = m;
        this.raf = new RandomAccessFile(file, "r");

        buffer = new byte[16];
        pointer = -1;

        raf.seek(m.preludePos);
        int b = raf.read();
        key = b ^ 0x4f;
        raf.seek(m.preludePos);
    }

    @Override
    public int read() throws IOException {
        pointer++;
        if (pointer % 16 == 0) {
            raf.read(buffer);
            for (int i = 0; i < 16; i++) {
                buffer[i] = (byte) (buffer[i] ^ key);
            }
        }
        if (pointer < music.preludeLength) {
            byte data = buffer[(int) (pointer % 16)];
            return data & 0xff;
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
