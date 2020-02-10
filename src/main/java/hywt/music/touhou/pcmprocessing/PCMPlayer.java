package hywt.music.touhou.pcmprocessing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import hywt.music.touhou.savedata.Music;

public class PCMPlayer {
	private AudioFormat af;
	private SourceDataLine.Info info;
	private SourceDataLine sdl;
	private FloatControl volumeControl;
	private boolean playing = false;
	private boolean inLoop;
	private boolean pause;
	private float volume = 1;
	private Music music;
	int playback;

	public void play(String thbgm, Music m, boolean loop) throws IOException {
		try {
			pause = false;
			inLoop = false;
			music = m;

			// 打开 thbgm.dat
			RandomAccessFile raf = new RandomAccessFile(thbgm, "r");

			int bufferSize = 256;

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
				if (pause) {
					Thread.sleep(50);
					continue;
				}
				raf.read(b, 0, bufferSize);
				sdl.write(b, 0, b.length);
				playback += bufferSize;
				if (!playing) {
					raf.close();
					return;
				}
			}

			// 循环
			inLoop = true;
			while (true) {
				playback = 0;
				raf.seek(music.loopPos);
				while (playback < music.loopLength) {
					if (pause) {
						Thread.sleep(50);
						continue;
					}
					raf.read(b, 0, bufferSize);
					sdl.write(b, 0, b.length);
					playback += bufferSize;
					if (!playing) {
						raf.close();
						return;
					}
				}
				if (!loop) {
					break;
				}
			}

			raf.close();

		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pause() {
		pause = true;
	}

	public void resume() {
		pause = false;
	}

	public boolean isPaused() {
		return this.pause;
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
				volumeControl.setValue(-80);
			} else {
				this.volume = volume;
				volumeControl.setValue((float) (Math.log10(Math.pow(this.volume, 4)) * 10));
			}
		} catch (NullPointerException e) {

		}
	}

	public int getPlayback() {
		if (!playing)
			return 0;
		if (inLoop) {
			return playback + music.preludeLength;
		} else {
			return playback;
		}
	}
	
	// TODO 设置播放进度
	public void setPlayback(int value) {
		
	}

	public int getLength() {
		if (music == null)
			return 0;
		if (!playing)
			return 0;
		return music.getTotalLength();
	}

	public Music getMusic() {
		return music;
	}

}
