package hywt.music.touhou;

import hywt.music.touhou.savedata.BGMData;

public class Main {
	public static void main(String[] args) {
		BGMData bgm = BGMData.read();
		for (int i = 0; i < bgm.games.size(); i++) {
			System.out.println(bgm.games.get(i).title);

		}
		System.out.println(bgm);
		// PCMPlayer.Play("E:\\正作\\[th11] 东方地灵殿 (汉化版+日文版)\\thbgm.dat",
		// bgm.games[2].music[1],3);
	}
}
