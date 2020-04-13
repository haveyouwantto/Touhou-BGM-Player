package hywt.music.touhou;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.MusicSystem;
import hywt.music.touhou.io.tfpack.TFPack;
import hywt.music.touhou.io.tfpack.TFPackInputStream;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

public class Test {
	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException {
		try {
			Constants.init();
			System.out.println(Constants.bgmdata.getGamebyId("th07"));
			Game g= Constants.bgmdata.getGamebyId("th14.5");
			Music m = g.music.get(0);
			TFPack tfp = new TFPack(g, m, new File("E:\\正作\\th145\\th145.pak"));
			TFPackInputStream tfs= tfp.getInputStream();
			byte[] b = new byte[256];
			int len;
			FileInputStream fis = new FileInputStream("E:\\正作\\th145\\th145.pak");
			FileOutputStream fos = new FileOutputStream("test.bin");
			int pointer=0;
			//tfs.read(b);
			System.out.println(Arrays.toString(b));
			
			while((len = tfs.read(b))!=-1){
				fos.write(b, 0, len);
			}
			tfs.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
