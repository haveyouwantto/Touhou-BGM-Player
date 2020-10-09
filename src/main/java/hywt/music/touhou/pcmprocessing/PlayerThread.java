package hywt.music.touhou.pcmprocessing;

import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.MusicSystem;
import hywt.music.touhou.savedata.BGMPath;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class PlayerThread extends Thread {
    private PCMPlayer player;

    private BGMPath bgmPath;
    private Game game;
    private Music music;

    private boolean playing;
    private boolean paused;

    private final int bufferSize = 1024;

    private SourceDataLine sdl;
    private FloatControl volumeControl;

    private final Object lock;

    public PlayerThread() {
        super();
        this.setDaemon(true);
        this.player = new PCMPlayer();
        lock = new Object();
    }

    public void setMG(Game game, Music music) {
        this.game = game;
        this.music = music;
    }

    public void play() throws LineUnavailableException {
        if (!playing) {
            bgmPath = BGMPath.load();
            playing = true;
            paused = false;

            // PCM 参数
            float sampleRate = music.sampleRate;
            int sampleSizeInBits = 16;
            int channels = 2;
            boolean signed = true;
            boolean bigEndian = false;

            // 创建音频流
            AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
            openSDL(af);
            synchronized (lock) {
                lock.notify();
            }
        } else if (paused) {
            paused = false;
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    public void pause() {
        paused = true;
    }

    public void terminate() {
        paused = false;
        playing = false;
        synchronized (lock) {
            lock.notify();
        }
    }

    private void openSDL(AudioFormat af) throws LineUnavailableException {
        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, af, bufferSize);
        sdl = (SourceDataLine) AudioSystem.getLine(info);

        sdl.open(af, music.sampleRate / 2);
        sdl.start();

        volumeControl = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
    }

    public void setVolume(float volume) {
        try {
            if (volume > 1) {
                volumeControl.setValue(0);
            } else if (volume < 0) {
                volumeControl.setValue(-80);
            } else {
                volumeControl.setValue((float) (Math.log10(Math.pow(volume, 4)) * 10));
            }
        } catch (NullPointerException e) {
            throw new NullPointerException("Music not loaded.");
        }
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (true) {
                try {
                    while (!playing) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String source = bgmPath.path.get(game.no);
                    MusicInputStream musicIn = game.format.getInputStream(game, music, new File(source));
                    int len;
                    long playback = 0;
                    while (playback < MusicSystem.getLength(musicIn)) {
                        while (paused) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!playing) break;
                        byte[] buffer = new byte[256];
                        len = musicIn.read(buffer);
                        if (len > -1) {
                            sdl.write(buffer, 0, len);
                            playback += len;
                        }
                    }
                    sdl.drain();
                    sdl.close();
                    break;
                    /*if (loop && playMode == MUSIC) {
                        long loopPos = MusicSystem.getLoopPos(musicIn);
                        playback = loopPos;
                        musicIn.seek(loopPos);
                    } else
                        break;*/
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    playing = false;
                } catch (UnsupportedAudioFileException unsupportedAudioFileException) {
                    unsupportedAudioFileException.printStackTrace();
                }
            }
        }

    }

    public PCMPlayer getPlayer() {
        return player;
    }

    public void setPlayer(PCMPlayer player) {
        this.player = player;
    }
}
