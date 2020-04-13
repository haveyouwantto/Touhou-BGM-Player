package hywt.music.touhou.io.tfpack;

import java.io.File;
import java.io.IOException;

import hywt.music.touhou.savedata.Music;

public class TFV3OggInputStream extends TFV2OggInputStream {

    private byte[] aux;

    public TFV3OggInputStream(Music m, File file) throws IOException {
        super(m, file);
        aux = new byte[4];
        for (int i = 0; i < 4; ++i)
            aux[i] = key[i];
    }

    @Override
    public int read() throws IOException {
        pointer++;
        if (pointer % 16 == 0) {
            raf.read(buffer);
            for (int i = 0; i < 16; ++i) {
                byte k = (byte) (buffer[i] ^ key[i] ^ aux[i % 4]);
                aux[i % 4] = buffer[i];
                buffer[i] = k;
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
}