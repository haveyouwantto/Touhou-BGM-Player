package hywt.music.touhou.pcmprocessing;

import java.io.BufferedInputStream;
import java.io.File;
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
import hywt.music.touhou.Etc;
import hywt.music.touhou.savedata.BGMPath;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;
import hywt.music.touhou.savedata.Playlist;
import hywt.music.touhou.tfpack.TF2OggInputStream;
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
	private Game game;
	private String source;
	private boolean gameList;
	int playback;
	int bufferSize = 256;
	public static final int MUSIC = 0;
	public static final int LIST = 1;

	public PCMPlayer() {
		loop = true;
		playMode = MUSIC;
	}

	public void playList(Playlist ply, int start)
			throws IOException, LineUnavailableException, InterruptedException, UnsupportedAudioFileException {
		while (true) {
			for (int i = start; i < ply.ids.size(); i++) {
				Music m = Constants.bgmdata.getMusicbyId(ply.ids.get(i)[0], ply.ids.get(i)[1]);
				Game g = Constants.bgmdata.getGamebyMusic(m);
				play(g, m);
				if (!playing)
					break;
			}
			if (!loop || !playing)
				break;
		}
	}

	public void play(Game g, Music m)
			throws IOException, LineUnavailableException, InterruptedException, UnsupportedAudioFileException {

		BGMPath bgmpath = BGMPath.load();
		pause = false;
		music = m;
		source = bgmpath.path.get(g.no);

		if (g.format == 0) {
			// 东方红魔乡格式 = 0
			int index = g.music.indexOf(m);
			play(source + "/" + Etc.getEoSDFilename(index), m); //$NON-NLS-1$
		} else if (g.format == 1) {
			// 一般格式 = 1
			play(source, m);
		} else if (g.format == 2) {
			// TH10.5/12.3格式
			playTFMusic(source, m);
		} else if (g.format == 3) {
			// TH10.5/12.3格式
			playTF2Music(source, m);
		}
	}

	private void play(String thbgm, Music m) throws IOException, LineUnavailableException, InterruptedException {
		source = thbgm;
		pause = false;
		music = m;

		// 打开 thbgm.dat
		raf = new RandomAccessFile(source, "r");

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
		source = thbgm;
		pause = false;
		music = m;

		TFOggInputStream tfois = new TFOggInputStream(source, m);

		final AudioInputStream in = AudioSystem.getAudioInputStream(tfois);

		final AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, music.sampleRate, 16, 2, 4,
				music.sampleRate, false);
		final AudioInputStream ais = AudioSystem.getAudioInputStream(outFormat, in);
		BufferedInputStream bufferedIn = new BufferedInputStream(ais);
		openSDL(outFormat);
		final byte[] b = new byte[bufferSize];
		int len;

		bufferedIn.mark(0x7fffffff);

		playback = 0;
		while (true) {
			while (playback < music.loopPos + music.loopLength) {
				if (!playing) {
					return;
				}
				if (pause) {
					Thread.sleep(50);
					continue;
				}
				len = bufferedIn.read(b);
				if (len > 0) {
					sdl.write(b, 0, len);
					playback += len;
				}
			}
			if (loop) {
				playback = (int) music.loopPos;
				bufferedIn.reset();
				bufferedIn.skip(music.loopPos);
			} else {
				bufferedIn.close();
				ais.close();
				in.close();
				tfois.close();
				break;
			}
		}
	}

	public void playTF2Music(String thbgm, Music m)
			throws LineUnavailableException, IOException, InterruptedException, UnsupportedAudioFileException {
		source = thbgm;
		pause = false;
		music = m;

		TF2OggInputStream tfois = new TF2OggInputStream(source, m);

		final AudioInputStream in = AudioSystem.getAudioInputStream(tfois);

		final AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, music.sampleRate, 16, 2, 4,
				music.sampleRate, false);
		final AudioInputStream ais = AudioSystem.getAudioInputStream(outFormat, in);
		BufferedInputStream bufferedIn = new BufferedInputStream(ais);
		openSDL(outFormat);
		final byte[] b = new byte[bufferSize];
		int len;

		bufferedIn.mark(0x7fffffff);

		playback = 0;
		while (true) {
			while (playback < music.loopPos + music.loopLength) {
				if (!playing) {
					return;
				}
				if (pause) {
					Thread.sleep(50);
					continue;
				}
				len = bufferedIn.read(b);
				if (len > 0) {
					sdl.write(b, 0, len);
					playback += len;
				}
			}
			if (loop) {
				playback = (int) music.loopPos;
				bufferedIn.reset();
				bufferedIn.skip(music.loopPos);
			} else {
				bufferedIn.close();
				ais.close();
				in.close();
				tfois.close();
				break;
			}
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
		setGame();

	}

	// 循环
	private void playFile() throws IOException, InterruptedException {
		byte[] b = new byte[bufferSize];
		playback = 0;
		raf.seek(music.preludePos);
		while (true) {
			while (playback < music.getTotalLength()) {
				if (!playing) {
					raf.close();
					return;
				}
				if (pause) {
					Thread.sleep(50);
					continue;
				}
				raf.read(b);
				sdl.write(b, 0, b.length);
				playback += bufferSize;
			}
			if (playMode == MUSIC) {
				playback = music.preludeLength;
				raf.seek(music.loopPos);
			} else if (playMode == LIST) {
				if (gameList) {
					int[] index = Constants.bgmdata.indexOf(music);
					try {
						music = Constants.bgmdata.games.get(index[0]).music.get(index[1] + 1);
						if (Constants.bgmdata.games.get(index[0]).no.equals("TH06")) {
							source = new File(source).getParent() + "/" + Etc.getEoSDFilename(index[1] + 1);
							raf = new RandomAccessFile(source, "r");
						}
					} catch (IndexOutOfBoundsException e) {
						if (!loop)
							break;
						music = Constants.bgmdata.games.get(index[0]).music.get(0);
						if (Constants.bgmdata.games.get(index[0]).no.equals("TH06")) {
							source = new File(source).getParent() + "/" + Etc.getEoSDFilename(0);
							raf = new RandomAccessFile(source, "r");
						}
					}
					playback = 0;
					raf.seek(music.preludePos);
					continue;
				} else
					break;
			}
			if (!loop && playMode == LIST) {
				break;
			}
		}
	}

	public void seek(int pos) throws IOException {
		if (game.format == 0 || game.format == 1) {
			if (pos % 2 == 1)
				pos++;
			playback = pos;
			raf.seek(music.preludePos + pos);
		} else if (game.format == 2 || game.format == 3) {
			// TODO: support ogg seeking
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
		if (sdl != null)
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

	public int getPreludeLength() {
		if (!playing)
			return 0;
		else {
			if (game.format == 0 || game.format == 1) {
				return music.preludeLength;
			} else if (game.format == 2 || game.format == 3) {
				return (int) music.loopPos;
			}
		}
		return -1;
	}

	public long getLength() {
		if (music == null)
			return 0;
		if (!playing)
			return 0;
		if (game.format == 2 || game.format == 3)
			return music.loopPos + music.loopLength;
		else
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

	public boolean isGameList() {
		return gameList;
	}

	public void setGameList(boolean gameList) {
		this.gameList = gameList;
	}

	private void setGame() {
		game = Constants.bgmdata.getGamebyMusic(music);
	}

}
