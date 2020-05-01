package hywt.music.touhou.io.tfpack;

import java.io.InputStream;
import java.io.RandomAccessFile;

import hywt.music.touhou.savedata.Music;

public abstract class TFPackInputStream extends InputStream {
    protected long pointer;
    protected RandomAccessFile raf;
    protected long markpos;
    protected Music music;
    protected byte[] buffer;
}
