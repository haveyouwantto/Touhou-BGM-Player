package hywt.music.touhou;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Test {
	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException {
		try {
			Constants.init();
			System.out.println(Constants.bgmdata.getGamebyId("th07"));
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
