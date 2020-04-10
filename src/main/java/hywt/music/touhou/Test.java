package hywt.music.touhou;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Test {
	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException {
		try {
			Constants.init();
			System.out.println(Constants.bgmdata.getGamebyId("th07"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
