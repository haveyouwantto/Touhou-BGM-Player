package hywt.music.touhou;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class PCMPlayer {
	private AudioFormat af;
	private SourceDataLine.Info info;
	private SourceDataLine sdl;
	
	
	public void play(String thbgm, Music music, boolean loop) throws FileNotFoundException {
		try {

			// 打开 thbgm.dat
			RandomAccessFile raf = new RandomAccessFile(thbgm, "r");

			int bufferSize = 0x100000;

			// PCM 参数
			float sampleRate = 44100;
			int sampleSizeInBits = 16;
			int channels = 2;
			boolean signed = true;
			boolean bigEndian = false;

			// 创建音频流
			af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
			info = new DataLine.Info(SourceDataLine.class, af, bufferSize);
			sdl = (SourceDataLine) AudioSystem.getLine(info);
			sdl.open(af);
			sdl.start();

			// 播放第一遍
			raf.seek(music.preludePos);
			byte[] b = new byte[music.getTotalLength()];
			raf.read(b, 0, music.getTotalLength());
			sdl.write(b, 0, b.length);

			// 循环
			while (loop) {
				raf.seek(music.loopPos);
				byte[] b2 = new byte[music.loopLength];
				raf.read(b2, 0, music.loopLength);
				sdl.write(b2, 0, b2.length);
			}

			raf.close();

		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void stop() {
		sdl.close();
	}
}
