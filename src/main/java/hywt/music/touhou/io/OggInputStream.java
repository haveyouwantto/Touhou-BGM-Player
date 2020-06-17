package hywt.music.touhou.io;

import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;
import javazoom.spi.vorbis.sampled.convert.DecodedVorbisAudioInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class OggInputStream extends MusicInputStream {
    private int pointer;
    private final ByteBuffer buffer;

    public OggInputStream(Game g, Music m, File f, InputStream is) throws IOException, UnsupportedAudioFileException {
        super(g, m, f);
        final AudioInputStream in = AudioSystem.getAudioInputStream(is);

        final AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, m.sampleRate, 16, 2, 4,
                m.sampleRate, false);
        DecodedVorbisAudioInputStream ais = new DecodedVorbisAudioInputStream(outFormat, in);

        int len = (int) MusicSystem.getLength(g, m);
        byte[] b = new byte[len];
        BufferedInputStream bufferedIn = new BufferedInputStream(ais);
        int i = 0;
        while (i < len) {
            try {
                int read = bufferedIn.read(b, i, 256);
                i += Math.max(read, 0);
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        bufferedIn.close();
        buffer = ByteBuffer.wrap(b);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();
    }

    @Override
    public void seek(long pos) {
        buffer.position((int) pos);
    }

    @Override
    public void mark(int readlimit) {
        buffer.mark();
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    @Override
    public synchronized int read() {
        if (pointer < music.preludeLength)
            return buffer.get();
        return -1;
    }


    @Override
    public synchronized int read(byte[] b) {
        if (buffer.remaining() < b.length) return read(b, 0, buffer.remaining());
        buffer.get(b);
        return b.length;
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) {
        buffer.get(b, off, len);
        return len;
    }

    @Override
    public void close() throws IOException {
        buffer.clear();
        super.close();
    }
}
