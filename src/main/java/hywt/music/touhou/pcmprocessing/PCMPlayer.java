package hywt.music.touhou.pcmprocessing;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import hywt.music.touhou.savedata.Music;
import hywt.music.touhou.tfpack.TFOggInputStream;

public class PCMPlayer {
	private AudioFormat af;
	private SourceDataLine.Info info;
	private SourceDataLine sdl;
	private FloatControl volumeControl;
	private boolean playing = false;
	private boolean inLoop;
	private boolean pause;
	private float volume = 1;
	private boolean loop;
	private Music music;
	int playback;
	int bufferSize = 256;

	public void play(String thbgm, Music m, boolean loop)
			throws IOException, LineUnavailableException, InterruptedException {
		pause = false;
		inLoop = false;
		this.loop = loop;
		music = m;

		// 打开 thbgm.dat
		RandomAccessFile raf = new RandomAccessFile(thbgm, "r");

		// PCM 参数
		float sampleRate = music.sampleRate;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = false;

		// 创建音频流
		af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		openSDL(af);
		playPreleude(raf);
		playLoop(raf);
		raf.close();
	}
	
	public void playMusic(AudioInputStream f)
			throws LineUnavailableException, IOException, InterruptedException, UnsupportedAudioFileException {
		final AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4,
				44100, false);
		final AudioInputStream ais = AudioSystem.getAudioInputStream(outFormat, f);
		openSDL(outFormat);
		byte[] b = new byte[bufferSize];
		playback = 0;
		while (f.available()>0) {
			if (pause) {
				Thread.sleep(50);
				continue;
			}
			if (!playing) {
				return;
			}
			ais.read(b);
			sdl.write(b, 0, b.length);
			playback += bufferSize;
		}
	}

	public void playTFMusic(String thbgm, Music m, boolean loop)
			throws LineUnavailableException, IOException, InterruptedException, UnsupportedAudioFileException {

		pause = false;
		inLoop = false;
		this.loop = loop;
		music = m;

		TFOggInputStream tfois = new TFOggInputStream(thbgm, m);

		final AudioInputStream in = AudioSystem.getAudioInputStream(new BufferedInputStream(tfois));
		final AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, music.sampleRate, 16, 2, 4,
				music.sampleRate, false);
		final AudioInputStream ais = AudioSystem.getAudioInputStream(outFormat, in);
		openSDL(outFormat);
		byte[] b = new byte[256];
		playback = 0;
		while (playback < music.getTotalLength()) {
			if (pause) {
				Thread.sleep(50);
				continue;
			}
			if (!playing) {
				return;
			}
			ais.read(b);
			sdl.write(b, 0, b.length);
			playback += bufferSize;
		}
	}

	private void openSDL(AudioFormat af) throws LineUnavailableException {
		info = new DataLine.Info(SourceDataLine.class, af, bufferSize);
		sdl = (SourceDataLine) AudioSystem.getLine(info);

		sdl.open(af);
		sdl.start();

		volumeControl = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
		this.setVolume(this.volume);

		playing = true;

	}

	// 播放前奏
	private void playPreleude(RandomAccessFile raf) throws IOException, InterruptedException {
		raf.seek(music.preludePos);
		byte[] b = new byte[bufferSize];

		playback = 0;
		while (playback < music.preludeLength) {
			if (pause) {
				Thread.sleep(50);
				continue;
			}
			if (!playing) {
				return;
			}
			raf.read(b);
			sdl.write(b, 0, b.length);
			playback += bufferSize;
		}
	}

	// 循环
	private void playLoop(RandomAccessFile raf) throws IOException, InterruptedException {
		inLoop = true;
		byte[] b = new byte[bufferSize];
		while (loop) {
			playback = 0;
			raf.seek(music.loopPos);
			while (playback < music.loopLength) {
				if (pause) {
					Thread.sleep(50);
					continue;
				}
				if (!playing) {
					return;
				}
				raf.read(b);
				sdl.write(b, 0, b.length);
				playback += bufferSize;
			}
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
		if (!playing)
			return;
		if (value < music.preludeLength) {

		} else {

		}
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
