package hywt.music.touhou;

public class Main {
	public static void main(String[] args) {
		BGMData bgm=InfoReader.read();
		for (int i = 0; i < bgm.games.length; i++) {
			System.out.println(bgm.games[i].title);
			
		}
		System.out.println(bgm);
		//PCMPlayer.Play("E:\\正作\\[th11] 东方地灵殿 (汉化版+日文版)\\thbgm.dat", bgm.games[2].music[1],3);
	}
}
