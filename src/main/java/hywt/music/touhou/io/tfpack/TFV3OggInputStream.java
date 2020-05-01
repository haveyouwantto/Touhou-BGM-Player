package hywt.music.touhou.io.tfpack;

import java.io.File;
import java.io.IOException;

import hywt.music.touhou.savedata.Music;

// TH14.5 | TH15.5
public class TFV3OggInputStream extends TFV2OggInputStream {

    private byte[] aux;

    public TFV3OggInputStream(Music m, File file) throws IOException {
        super(m, file);
        aux = new byte[4];
        System.arraycopy(key, 0, aux, 0, 4);
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
            byte data = buffer[(int) (pointer % 16)];
            return data & 0xff;
        } else
            return -1;
    }
}