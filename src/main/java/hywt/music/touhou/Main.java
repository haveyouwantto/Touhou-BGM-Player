package hywt.music.touhou;

import hywt.music.touhou.savedata.Music;
import hywt.music.touhou.tfpack.TFOggInputStream;

import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main {
	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException {
		try {
			Constants.init();
			Music m = Constants.bgmdata.getMusicbyId("th12.3", "1");
			TFOggInputStream tfois = new TFOggInputStream("E:\\正作\\[th123] 东方非想天则 (汉化版)\\th123b.dat", m);
			byte[] b = new byte[4096];
			tfois.read(b);
			System.out.println(Arrays.toString(b));
			tfois.close();
			// PCMPlayer pcmp=new PCMPlayer();
			// pcmp.playAudioStream(Etc.toPCM(tfois));
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
