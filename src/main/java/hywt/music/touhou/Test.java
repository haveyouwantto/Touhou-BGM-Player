package hywt.music.touhou;

import hywt.music.touhou.pcmprocessing.PCMPlayer;
import hywt.music.touhou.savedata.Music;
import hywt.music.touhou.tfpack.TFOggInputStream;
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.jcraft.jorbis.JOrbisException;
import com.jcraft.jorbis.VorbisFile;

public class Test {
	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException {
		try {
			Constants.init();
			/*
			 * FileOutputStream fos = new FileOutputStream("test.ogg"); byte[] b = new
			 * byte[256]; int pointer = 0; while (pointer < m.preludeLength - 256) {
			 * tfois.read(b); fos.write(b); pointer += 256; } byte[] b2 = new
			 * byte[m.preludeLength - pointer]; tfois.read(b2); fos.write(b2);
			 * 
			 * tfois.close(); fos.close();
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
