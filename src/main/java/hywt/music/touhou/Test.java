package hywt.music.touhou;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import hywt.music.touhou.savedata.Music;
import hywt.music.touhou.tfpack.TF2OggInputStream;

public class Test {
	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException {
		try {
			Constants.init();
			System.out.println(Constants.bgmdata.getGamebyId("th07"));
			Music m = new Music("chuboss1", 44100, 130371826, 3274200, 0, 0, "0DB0D96027F59092D0826278D2AA25BD");
			TF2OggInputStream tfpack = new TF2OggInputStream("Y:/正作/[th135] 东方心绮楼1.33汉化版+1.34b日文版/th135.pak", m);
			byte[] b = new byte[256];
			FileOutputStream fos = new FileOutputStream("raw.ogg");
			int read = 0;
			/*for (int i = 0; i <64;i++) {
				System.out.print(tfpack.read()+", ");
			}*/
			while (tfpack.read(b) > 0) {
				fos.write(b);
			}
			tfpack.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
