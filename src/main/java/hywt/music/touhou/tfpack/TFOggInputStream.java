package hywt.music.touhou.tfpack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import hywt.music.touhou.savedata.Music;

// 绯想天/非想天则 ogg 提取
public class TFOggInputStream extends InputStream {
	private Music m;
	private File file;
	private RandomAccessFile raf;
	private int xor;
	private long markpos;
	private long pointer;
	
	public TFOggInputStream(String path, Music m) throws IOException {
		this.m = m;
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

	@Override
	public int read(byte b[], int off, int len) throws IOException {
		if (pointer >= m.getTotalLength())
			return -1;
		pointer += b.length;
		int re = raf.read(b, off, len);
		// System.out.println(Arrays.toString(b2));
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) (b[i] ^ xor);
		}
		return re;
	}

	@Override
	public synchronized void mark(int readlimit) {
		markpos = pointer;
	}

	@Override
	public synchronized void reset() throws IOException {
		raf.seek(markpos);
	}

	@Override
	public void close() throws IOException {
		raf.close();
	}
}
