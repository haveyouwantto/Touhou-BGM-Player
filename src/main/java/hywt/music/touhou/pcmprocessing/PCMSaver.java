package hywt.music.touhou.pcmprocessing;

import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.MusicSystem;
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
                MusicInputStream musicIn = MusicSystem.getInputStream(game,music, new File(thbgm));

                long time1 = (int) (music.preludeLength / sampleSizeInBits * 8 / channels);
                long time2 = (int) (music.loopLength / sampleSizeInBits * 8 / channels);

                // 创建音频流
                AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

                AudioInputStream ais1 = new AudioInputStream(musicIn, af, time1);
                AudioInputStream ais2 = new AudioInputStream(musicIn, af, time2);

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

                MusicInputStream musicIn = MusicSystem.getInputStream(game,music, new File(thbgm));

                long time;
                if (game.format>1) time = (int) ((music.loopPos+music.loopLength) / sampleSizeInBits * 8 / channels);
                else time = (int) (MusicSystem.getLength(musicIn) / sampleSizeInBits * 8 / channels);

                // 创建音频流
                AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

                AudioInputStream ais = new AudioInputStream(musicIn, af, time);

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
