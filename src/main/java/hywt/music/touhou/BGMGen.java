package hywt.music.touhou;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BGMGen {
    private RandomAccessFile raf;

    public BGMGen(File f) throws FileNotFoundException {
        raf = new RandomAccessFile(f, "r");
    }

    private int readInt() throws IOException {
        byte[] b = new byte[4];
        raf.read(b);
        return (b[0] & 0xff) |
                ((b[1] & 0xff) << 8) |
                ((b[2] & 0xff) << 16) |
                ((b[3] & 0xff) << 24);
    }

    private String readString() throws IOException {
        byte[] buf = new byte[16];
        raf.read(buf);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if (buf[i] != 0)
                builder.append((char) buf[i]);
            else break;
        }
        return builder.toString();
    }

    public String read(int num) throws IOException {
        raf.seek(num * 0x34);
        String title = readString();
        if (title.equals("")) {
            return null;
        }
        int[] ints = new int[3];
        for (int i = 0; i < 3; i++) {
            ints[i] = readInt();
        }
        long ppos = ints[0];
        long plen = ints[2];
        long lpos = ints[0] + plen;
        long llen = ints[1] - plen;
        return String.format("{\n" +
                "    \"title\": \"%s\",\n" +
                "    \"sampleRate\": 44100,\n" +
                "    \"preludePos\": %d,\n" +
                "    \"preludeLength\": %d,\n" +
                "    \"loopPos\": %d,\n" +
                "    \"loopLength\": %d\n" +
                "}", title, ppos, plen, lpos, llen);
    }

    public static void main(String[] args) throws IOException {
        BGMGen gen = new BGMGen(new File("D:\\Games\\Touhou 18 - Unconnected Marketeers\\thbgm.fmt"));
        String json;
        int i = 0;
        while ((json = gen.read(i)) != null) {
            System.out.println(json);
            i++;
        }
    }
}
