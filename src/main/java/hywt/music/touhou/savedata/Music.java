package hywt.music.touhou.savedata;

public class Music {
	public String title;
	public int sampleRate;
	public long preludePos;
	public int preludeLength;
	public long loopPos;
	public int loopLength;
	public String metadata;

	public Music(String title, int sampleRate, long preludePos, int preludeLength, long loopPos, int loopLength,String metadata) {
		super();
		this.title = title;
		this.sampleRate = sampleRate;
		this.preludePos = preludePos;
		this.preludeLength = preludeLength;
		this.loopPos = loopPos;
		this.loopLength = loopLength;
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return title;
	}
}
