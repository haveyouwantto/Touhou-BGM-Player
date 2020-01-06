package hywt.music.touhou.pcmprocessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

public class PCMSaver {
	public static void save(File outFolder, String thbgm, Game game, Music music, boolean separate)
			throws FileNotFoundException {
		try {

			// 输出文件夹
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

			if (separate) {
				FileInputStream fis1 = new FileInputStream(thbgm);
				fis1.skip(music.preludePos);

				FileInputStream fis2 = new FileInputStream(thbgm);
				fis2.skip(music.loopPos);

				long time1 = (int) (music.preludeLength / sampleSizeInBits * 8 / channels);
				long time2 = (int) (music.loopLength / sampleSizeInBits * 8 / channels);

				// 创建音频流
				AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

				AudioInputStream ais1 = new AudioInputStream(fis1, af, time1);
				AudioInputStream ais2 = new AudioInputStream(fis2, af, time2);

				// wav 文件
				File outPath = new File(
						outAlbum.getAbsolutePath() + "/" + (game.music.indexOf(music) + 1) + " - " + music.title + "/");

				if (!outPath.exists()) {
					outPath.mkdirs();
				}

				File file1 = new File(outPath.getAbsolutePath() + "/" + (game.music.indexOf(music) + 1) + ".1 - "
						+ music.title + ".wav");
				File file2 = new File(outPath.getAbsolutePath() + "/" + (game.music.indexOf(music) + 1) + ".2 - "
						+ music.title + ".wav");

				AudioSystem.write(ais1, Type.WAVE, file1);
				AudioSystem.write(ais2, Type.WAVE, file2);
				ais1.close();
				ais2.close();
			} else {

				FileInputStream fis = new FileInputStream(thbgm);
				fis.skip(music.preludePos);

				long time = (int) (music.getTotalLength() / sampleSizeInBits * 8 / channels);

				// 创建音频流
				AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

				AudioInputStream ais = new AudioInputStream(fis, af, time);

				// wav 文件
				File file = new File(outAlbum.getAbsolutePath() + "/" + (game.music.indexOf(music) + 1) + " - "
						+ music.title + ".wav");

				AudioSystem.write(ais, Type.WAVE, file);
				ais.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
