package hywt.music.touhou.pcmprocessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import hywt.music.touhou.Constants;
import hywt.music.touhou.savedata.BGMData;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

public class PCMSaver {
	public static void save(File outFolder, String thbgm, Game game, Music music, int loopcount)
			throws FileNotFoundException {
		// TODO 导出文件
		try {
			FileInputStream fis = new FileInputStream(thbgm);
			fis.skip(music.preludePos);

			File outAlbum = new File(outFolder.getAbsolutePath() + "/" + game.toString() + "/");

			if (!outAlbum.exists()) {
				outAlbum.mkdirs();
			}

			// PCM 参数
			float sampleRate = music.sampleRate;
			int sampleSizeInBits = 16;
			int channels = 2;
			boolean signed = true;
			boolean bigEndian = false;

			long time = (int) (music.getTotalLength() / sampleSizeInBits * 8 / channels);

			// 创建音频流
			AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

			AudioInputStream ais = new AudioInputStream(fis, af, time);

			File file = new File(
					outAlbum.getAbsolutePath() + "/" + (game.music.indexOf(music) + 1) + " - " + music.title + ".wav");

			AudioSystem.write(ais, Type.WAVE, file);
			ais.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
