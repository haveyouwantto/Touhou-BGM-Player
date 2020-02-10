package hywt.music.touhou;

import hywt.music.touhou.savedata.BGMData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import hywt.music.touhou.pcmprocessing.PCMSaver;

public class Main {
	public static void main(String[] args) {
		BGMData bgm;
		try {
			bgm = BGMData.read();
			for (int i = 0; i < bgm.games.size(); i++) {
				System.out.println(bgm.games.get(i).title);

			}
			System.out.println(bgm);
			try {
				PCMSaver.save(new File("d:\\"), "E:/正作/[th07] 东方妖妖梦 (汉化版)/thbgm.dat", Constants.bgmdata.games.get(0),bgm.games.get(1).music.get(0), false);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// PCMPlayer.Play("E:\\正作\\[th11] 东方地灵殿 (汉化版+日文版)\\thbgm.dat",
			// bgm.games[2].music[1],3);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
