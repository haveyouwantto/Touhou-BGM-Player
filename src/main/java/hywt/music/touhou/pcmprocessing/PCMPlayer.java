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

import hywt.music.touhou.Constants;
import hywt.music.touhou.savedata.Music;
import hywt.music.touhou.tfpack.TFOggInputStream;

public class PCMPlayer {
	private AudioFormat af;
	private SourceDataLine.Info info;
	private SourceDataLine sdl;
	private RandomAccessFile raf;
	private FloatControl volumeControl;
	private boolean playing = false;
	private boolean pause;
	private float volume = 1;
	private int playMode;
	private boolean loop;
	private Music music;
	int playback;
	int bufferSize = 256;

	public PCMPlayer() {
		loop = true;
		playMode = 1;
	}

	public void play(String thbgm, Music m) throws IOException, LineUnavailableException, InterruptedException {
		pause = false;
		music = m;

		// 打开 thbgm.dat
		raf = new RandomAccessFile(thbgm, "r");

		// PCM 参数
		float sampleRate = music.sampleRate;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = false;

		// 创建音频流
		af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		openSDL(af);
		playFile();
	}

	public void playMusic(AudioInputStream f)
			throws LineUnavailableException, IOException, InterruptedException, UnsupportedAudioFileException {
		final AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
		final AudioInputStream ais = AudioSystem.getAudioInputStream(outFormat, f);
		openSDL(outFormat);
		byte[] b = new byte[bufferSize];
		playback = 0;
		while (f.available() > 0) {
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

	public void playTFMusic(String thbgm, Music m)
			throws LineUnavailableException, IOException, InterruptedException, UnsupportedAudioFileException {

		pause = false;
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

	// 循环
	private void playFile() throws IOException, InterruptedException {
		byte[] b = new byte[bufferSize];
		playback = 0;
		raf.seek(music.preludePos);
		while (true) {
			while (playback < music.getTotalLength()) {
				if (pause) {
					Thread.sleep(50);
					continue;
				}
				if (!playing) {
					raf.close();
					return;
				}
				raf.read(b);
				sdl.write(b, 0, b.length);
				playback += bufferSize;
			}
			if (playMode == 0) {
				playback = music.preludeLength;
				raf.seek(music.loopPos);
			} else if (playMode == 1) {
				int[] index = Constants.bgmdata.indexOf(music);
				try {
					music = Constants.bgmdata.games.get(index[0]).music.get(index[1] + 1);
				} catch (IndexOutOfBoundsException e) {
					if (!loop)
						break;
					music = Constants.bgmdata.games.get(index[0]).music.get(0);
				}
				playback = 0;
				raf.seek(music.preludePos);
				continue;
			}
			if (!loop && playMode == 0) {
				break;
			}
		}
	}

	public void seek(int pos) throws IOException {
		int format = Constants.bgmdata.getGamebyMusic(music).format;
		if (format == 0 || format == 1) {
			if (pos % 2 == 1)
				pos++;
			playback = pos;
			raf.seek(music.preludePos + pos);
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

	public void stop() throws IOException {
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
		return playback;
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

	public int getPlayMode() {
		return playMode;
	}

	public void setPlayMode(int playMode) {
		this.playMode = playMode;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

}
