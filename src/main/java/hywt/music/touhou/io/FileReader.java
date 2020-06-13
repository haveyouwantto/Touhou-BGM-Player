package hywt.music.touhou.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileReader {
    public static String read(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        return new String(data, StandardCharsets.UTF_8);
    }

    public static void save(File file, String content) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        osw.write(content);
        osw.close();
    }
}
