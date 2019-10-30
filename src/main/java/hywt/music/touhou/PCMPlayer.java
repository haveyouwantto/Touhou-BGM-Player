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
	private boolean playing=false;

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
			/*
			sdl.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					System.out.println(event.getFramePosition());
				}
			});
			*/
			playing=true;
			
			// 播放前奏
			raf.seek(music.preludePos);
			byte[] b = new byte[music.preludeLength];
			raf.read(b, 0, music.preludeLength);
			sdl.write(b, 0, b.length);

			// 循环
			b = new byte[music.loopLength];
			while (true) {
				raf.seek(music.loopPos);
				raf.read(b, 0, music.loopLength);
				sdl.write(b, 0, b.length);
				if (!loop || !playing) {
					break;
				}
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
		playing=false;
		sdl.close();
	}
}
