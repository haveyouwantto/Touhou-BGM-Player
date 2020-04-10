package hywt.music.touhou.io.tfpack;

import hywt.music.touhou.savedata.Music;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

public abstract class TFPackInputStream extends InputStream {
    protected long pointer;
    protected RandomAccessFile raf;
    protected long markpos;
    protected Music music;
}
