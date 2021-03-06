package hywt.music.touhou.pcmprocessing;

import hywt.music.touhou.Constants;
import hywt.music.touhou.Logger;
import hywt.music.touhou.Playlist;
import hywt.music.touhou.StringFormatter;
import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.MusicSystem;
import hywt.music.touhou.savedata.BGMPath;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.GameFormat;
import hywt.music.touhou.savedata.Music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class PCMPlayer {

    /* fields for playing */
    private MusicInputStream musicIn;
    private AudioFormat af;
    private SourceDataLine sdl;
    private FloatControl volumeControl;

    private boolean playing = false;
    private boolean pause;
    private float volume = 1;
    private int playMode;
    private boolean loop;

    private Music music;
    private Game game;

    private boolean gameList;

    private long playback;
    private final int bufferSize = 1024;
    private final byte[] buffer;

    /* constants*/
    public static final int MUSIC = 0;
    public static final int LIST = 1;


    public PCMPlayer() {
        loop = true;
        playMode = MUSIC;
        buffer = new byte[bufferSize];
    }

    public void playList(Playlist ply, int start)
            throws IOException, LineUnavailableException, InterruptedException, UnsupportedAudioFileException {
        ply.setPos(start);
        do {
            Music m = ply.next();
            System.out.println(m);
            Game g = Constants.bgmdata.getGamebyMusic(m);
            play(g, m);
        } while (loop && playing);
    }

    public void play(Game g, Music m)
            throws IOException, LineUnavailableException, InterruptedException, UnsupportedAudioFileException {

        BGMPath bgmpath = BGMPath.load();
        playing = true;
        pause = false;
        game = g;
        music = m;
        String source = bgmpath.path.get(g.no);
        if (g.format == GameFormat.BGM_FOLDER) {
            int index = g.music.indexOf(m);
            musicIn = g.format.getInputStream(g, m,
                    new File(source + "/" + StringFormatter.formatFileName(g.metadata, index)));
        } else
            musicIn = g.format.getInputStream(g, m, new File(source));

        // PCM 参数
        float sampleRate = music.sampleRate;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = false;

        // 创建音频流
        af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        openSDL(af);
        play();
    }

    private void openSDL(AudioFormat af) throws LineUnavailableException {
        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, af, bufferSize);
        sdl = (SourceDataLine) AudioSystem.getLine(info);

        sdl.open(af, music.sampleRate / 2);
        sdl.start();

        volumeControl = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
        this.setVolume(this.volume);

        setGame();
    }

    private void play() throws IOException, InterruptedException {
        playback = 0;
        // musicIn.seek(0);

        int len;
        while (true) {
            while (playback < MusicSystem.getLength(musicIn)) {
                if (!playing) {
                    musicIn.close();
                    return;
                }
                if (pause) {
                    Thread.sleep(50);
                    continue;
                }
                len = musicIn.read(buffer);
                if (len > -1) {
                    sdl.write(buffer, 0, len);
                    playback += len;
                }
            }
            if (loop && playMode == MUSIC) {
                long loopPos = MusicSystem.getLoopPos(musicIn);
                playback = loopPos;
                musicIn.seek(loopPos);
            } else
                break;
        }
    }

    public void seek(int pos) throws IOException {
        try {
            long pos2 = MusicSystem.roundByte(pos, af.getFrameSize());
            playback = pos2;
            musicIn.seek(pos2);
        } catch (RuntimeException ex) {
            Logger.log("Seeking not supported for " + musicIn.getClass());
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
        } catch (NullPointerException ignored) {

        }
    }

    public long getPlayback() {
        return playing ? playback : 0;
    }

    public int getPreludeLength() {
        return playing ? MusicSystem.getPreludeLength(musicIn) : 0;
    }

    public long getLength() {
        return (music == null || !playing) ? 0 : MusicSystem.getLength(musicIn);
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

    public byte[] getBuffer() {
        return buffer;
    }

    public float getProgress() {
        return playing ? playback * 1f / MusicSystem.getLength(musicIn) : 0;
    }

}
