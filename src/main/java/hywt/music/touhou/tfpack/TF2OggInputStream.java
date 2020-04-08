package hywt.music.touhou.tfpack;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import hywt.music.touhou.savedata.Music;

public class TF2OggInputStream extends InputStream {
    private RandomAccessFile raf;
    private byte[] key;
    private byte[] buffer;
    private long pos;
    private long markpos;
    private Music music;

    public TF2OggInputStream(String file, Music m) throws IOException {
        raf = new RandomAccessFile(file, "r");
        key = new byte[16];
        buffer = new byte[16];
        this.music = m;

        raf.seek(m.preludePos);
        pos = -1;

        String hex = music.metadata;
        for (int i = 0; i < 32; i += 2) {
            key[i >> 1] = (byte) (Integer.decode("0x" + hex.substring(i, i + 2)) - 128);
        }
    }

    @Override
    public int read() throws IOException {
        pos++;
        if (pos % 16 == 0) {
            raf.read(buffer);
            for (int i = 0; i < 16; i++) {
                buffer[i] = (byte) ((buffer[i] ^ key[i]) + 128);
            }
        }
        if (pos < music.preludeLength) {
            int data = buffer[(int) (pos % 16)];
            if (data < 0)
                data = 256 + data;
            return data;
        } else
            return -1;
    }

    public void seek(long pos) throws IOException {
        this.pos = pos;
        raf.seek(pos);
    }

    @Override
    public void mark(int readlimit) {
        markpos = pos;
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

    public long getPos(){
        return pos;
    }
}