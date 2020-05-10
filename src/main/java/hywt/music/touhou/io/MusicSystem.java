package hywt.music.touhou.io;

import hywt.music.touhou.io.tfpack.TFPack;
import hywt.music.touhou.io.tfpack.TFPackInputStream;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.GameFormats;
import hywt.music.touhou.savedata.Music;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class MusicSystem {

	public static MusicInputStream getInputStream(Game g, Music m, File f)
			throws IOException, UnsupportedAudioFileException {
		if (g.format == GameFormats.WAVE_FILE) {
			return new PCMInputStream(g, m, new File(f, m.metadata));
		} else if (g.format == GameFormats.BGM_FOLDER) {
			return new PCMInputStream(g, m, f);
		} else if (g.format == GameFormats.RAW_PCM) {
			return new PCMInputStream(g, m, f);
		} else if (GameFormats.isTFPack(g.format)) {
			TFPack tfp = new TFPack(g, m, f);
			TFPackInputStream tfs = tfp.getInputStream();
			return new OggInputStream(g, m, f, tfs);
		}
		throw new UnsupportedAudioFileException();
	}

	public static int getPreludeLength(MusicInputStream musicIn) {
		int format = musicIn.game.format;
		Music music = musicIn.music;
		if (format == GameFormats.BGM_FOLDER || format == GameFormats.RAW_PCM) {
			return music.preludeLength;
		} else {
			return (int) music.loopPos;
		}
	}

	public static long getLength(MusicInputStream musicIn) {
		int format = musicIn.game.format;
		Music music = musicIn.music;
		if (format == GameFormats.BGM_FOLDER || format == GameFormats.RAW_PCM) {
			return music.preludeLength + music.loopLength;
		} else {
			return music.loopPos + music.loopLength;
		}
	}

	public static long getLoopPos(MusicInputStream musicIn) {
		int format = musicIn.game.format;
		Music music = musicIn.music;
		if (format == GameFormats.BGM_FOLDER || format == GameFormats.RAW_PCM) {
			return music.preludeLength;
		} else {
			return music.loopPos;
		}
	}

	public static long roundByte(long src, int minBytes) {
		return src / minBytes * minBytes;
	}

}
