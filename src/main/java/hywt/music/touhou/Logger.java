package hywt.music.touhou;

import java.util.Date;

public class Logger {
    public static void log(String message) {
        System.out.printf("[%s] [LOG] %s\n", new Date().toString(), message);
    }

    public static void error(Throwable t) {
        System.out.printf("\033[31m[%s] [ERROR] %s\n", new Date().toString(), t);
        for (StackTraceElement element : t.getStackTrace()) {
            System.out.println("-> " + element);
        }
        System.out.println("\033[0m");
    }
}
