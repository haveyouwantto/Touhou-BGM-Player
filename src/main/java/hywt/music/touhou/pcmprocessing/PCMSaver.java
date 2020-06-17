package hywt.music.touhou.pcmprocessing;

import hywt.music.touhou.Logger;
import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.MusicSystem;
import hywt.music.touhou.io.tfpack.TFPack;
import hywt.music.touhou.io.tfpack.TFPackInputStream;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.GameFormat;
import hywt.music.touhou.savedata.Music;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PCMSaver {
    public static void save(File outFolder, String thbgm, Game game, Music music, boolean separate) {
        try {

            // 输出文件夹
            File outAlbum = new File(outFolder, game.toString());
            // new File(outFolder.getAbsolutePath() + "/" + game.toString() + "/");

            if (!outAlbum.exists()) {
                outAlbum.mkdirs();
            }

            if (GameFormat.isTFPack(game.format)) {
                File f = new File(outAlbum, (game.music.indexOf(music) + 1) + " - " + music.title + ".ogg");
                FileOutputStream fos = new FileOutputStream(f);
                TFPack tfp = new TFPack(game, music, new File(thbgm));
                TFPackInputStream tfs = tfp.getInputStream();
                byte[] buffer = new byte[256];
                int len;
                while ((len = tfs.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                tfs.close();
                setTag(game, music, f);
                //return;
            }

            // PCM 参数
            float sampleRate = music.sampleRate;
            int sampleSizeInBits = 16;
            int channels = 2;
            boolean signed = true;
            boolean bigEndian = false;

            if (separate) {
                MusicInputStream musicIn = game.format.getInputStream(game, music, new File(thbgm));

                long time1 = (MusicSystem.getPreludeLength(musicIn) / sampleSizeInBits * 8 / channels);
                long time2 = (music.loopLength / sampleSizeInBits * 8 / channels);

                // 创建音频流
                AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

                AudioInputStream ais1 = new AudioInputStream(musicIn, af, time1);
                AudioInputStream ais2 = new AudioInputStream(musicIn, af, time2);

                // wav 文件
                File outPath = new File(outAlbum, (game.music.indexOf(music) + 1) + " - " + music.title);

                if (!outPath.exists()) {
                    outPath.mkdirs();
                }

                File file1 = new File(outPath, (game.music.indexOf(music) + 1) + ".1 - " + music.title + ".flac");
                File file2 = new File(outPath, (game.music.indexOf(music) + 1) + ".2 - " + music.title + ".flac");

                AudioSystem.write(ais1, FLACFileWriter.FLAC, file1);
                AudioSystem.write(ais2, FLACFileWriter.FLAC, file2);
                ais1.close();
                ais2.close();
                PCMSaver.setTag(game, music, file1);
                PCMSaver.setTag(game, music, file2);
            } else {

                MusicInputStream musicIn = game.format.getInputStream(game, music, new File(thbgm));

                long time = (int) (MusicSystem.getLength(musicIn) / sampleSizeInBits * 8 / channels);

                // 创建音频流
                AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

                AudioInputStream ais = new AudioInputStream(musicIn, af, time);

                // 写文件
                File file = new File(outAlbum, (game.music.indexOf(music) + 1) + " - " + music.title + ".flac");

                AudioSystem.write(ais, FLACFileWriter.FLAC, file);
                ais.close();

                PCMSaver.setTag(game, music, file);
            }

        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static void setTag(Game game, Music music, File file) throws KeyNotFoundException, CannotReadException,
            IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, CannotWriteException {
        AudioFile f = AudioFileIO.read(file);
        Tag tag = f.getTag();
        for (String i : game.artist) {
            tag.addField(FieldKey.ALBUM_ARTIST, i);
            tag.addField(FieldKey.ARTIST, i);
        }
        tag.setField(FieldKey.ALBUM, game.title);
        tag.setField(FieldKey.TITLE, music.title);
        tag.setField(FieldKey.TRACK, String.valueOf(game.music.indexOf(music) + 1));
        try {
            tag.setField(Artwork.createArtworkFromFile(new File("Cover/" + game.no + ".jpg")));
        } catch (IOException e) {
            Logger.error(e);
        }
        f.commit();
    }
}
