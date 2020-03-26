package hywt.music.touhou.tfpack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import hywt.music.touhou.savedata.Music;

// 绯想天/非想天则 ogg 提取
public class TFOggInputStream extends InputStream {
	private File file;
	private RandomAccessFile raf;
	private int xor;
	private long pointer;
	private long markpos;

	public TFOggInputStream(String path, Music m) throws IOException {
		file = new File(path);
		raf = new RandomAccessFile(file, "r");
		raf.seek(m.preludePos);
		int b = raf.read();
		xor = b ^ 0x4f;
		raf.seek(m.preludePos);
	}

	@Override
	public int read() throws IOException {
		pointer++;
		return raf.read() ^ xor;
	}
	
	public void seek(long pos) throws IOException {
		this.pointer = pos;
		raf.seek(pos);
	}
	
	@Override
	public void mark(int readlimit) {
		markpos = pointer;
	}

	@Override
	public void reset() throws IOException {
		seek(markpos);
	}

	@Override
	public void close() throws IOException {
		raf.close();
	}
}
