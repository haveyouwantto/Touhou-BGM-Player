package hywt.music.touhou.io;

import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class OggInputStream extends MusicInputStream {
    private BufferedInputStream bufferedIn;
	private long pointer;

	public OggInputStream(Game g, Music m, File f, InputStream is) throws IOException, UnsupportedAudioFileException {
		super(g, m, f);
		final AudioInputStream in = AudioSystem.getAudioInputStream(is);

		final AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, m.sampleRate, 16, 2, 4,
				m.sampleRate, false);
		final AudioInputStream ais = AudioSystem.getAudioInputStream(outFormat, in);

		bufferedIn = new BufferedInputStream(ais);
		bufferedIn.mark(0x7fffffff);
	}

	@Override
	public void seek(long pos) throws IOException {
		bufferedIn.reset();
		bufferedIn.skip(pos);
	}

	@Override
	public int read() throws IOException {
		if (pointer < music.preludeLength)
			return bufferedIn.read();
		return -1;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return bufferedIn.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return bufferedIn.read(b, off, len);
	}

	@Override
	public void close() throws IOException {
		bufferedIn.close();
		super.close();
	}
}
