package hywt.music.touhou;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class PCMPlayer {
	private AudioFormat af;
	private SourceDataLine.Info info;
	private SourceDataLine sdl;
	private FloatControl volumeControl;
	private boolean playing = false;
	private float volume = 1;
	long playback;

	public void play(String thbgm, Music music, boolean loop) throws FileNotFoundException {
		try {

			// 打开 thbgm.dat
			RandomAccessFile raf = new RandomAccessFile(thbgm, "r");

			int bufferSize = 0x100;

			// PCM 参数
			float sampleRate = music.sampleRate;
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

			volumeControl = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
			this.setVolume(this.volume);

			playing = true;

			// 播放前奏
			raf.seek(music.preludePos);
			byte[] b = new byte[bufferSize];

			playback = 0;
			while (playback < music.preludeLength) {
				raf.read(b, 0, bufferSize);
				sdl.write(b, 0, b.length);
				playback += bufferSize;
			}

			// 循环
			playback = music.loopPos;
			while (true) {
				playback = 0;
				raf.seek(music.loopPos);
				while (playback < music.loopLength) {
					raf.read(b, 0, bufferSize);
					sdl.write(b, 0, b.length);
					if (!loop || !playing) {
						break;
					}
					playback += bufferSize;
				}
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
		playing = false;
		sdl.close();
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		try {
			if (volume > 1) {
				this.volume = 1f;
				volumeControl.setValue(0);
			} else if (volume < 0) {
				this.volume = 0f;
				volumeControl.setValue(-100);
			} else {
				this.volume = volume;
				volumeControl.setValue((float) (Math.log10(Math.pow(this.volume, 2)) * 10));
			}
		} catch (NullPointerException e) {

		}
	}

}
