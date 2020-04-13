package hywt.music.touhou.io.tfpack;

import hywt.music.touhou.savedata.Music;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TFV2OggInputStream extends TFPackInputStream {
    protected byte[] key;
    protected byte[] buffer;

    public TFV2OggInputStream(Music m, File file) throws IOException {
        this.raf = new RandomAccessFile(file, "r");
        this.key = new byte[16];
        this.buffer = new byte[16];
        this.music = m;

        raf.seek(m.preludePos);
        pointer = -1;

        String hex = music.metadata;
        for (int i = 0; i < 32; i += 2) {
            key[i >> 1] = (byte) (Integer.decode("0x" + hex.substring(i, i + 2)) % 256);
        }
    }

    @Override
    public int read() throws IOException {
        pointer++;
        if (pointer % 16 == 0) {
            raf.read(buffer);
            for (int i = 0; i < 16; i++) {
                buffer[i] = (byte) (buffer[i] ^ key[i]);
            }
        }
        if (pointer < music.preludeLength) {
            int data = buffer[(int) (pointer % 16)];
            if (data < 0)
                data = 256 + data;
            return data;
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
        super.close();
    }

    public long getPointer() {
        return pointer;
    }
}