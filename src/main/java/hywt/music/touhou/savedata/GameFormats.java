package hywt.music.touhou.savedata;

public class GameFormats {
    public static final int BGM_FOLDER = 0;
    public static final int RAW_PCM = 1;
    public static final int TFPACK_1 = 2;
    public static final int TFPACK_2 = 3;
    public static final int TFPACK_3 = 4;
    
    public static boolean isTFPack (int format) {
        return format >= 2;
    }
}
