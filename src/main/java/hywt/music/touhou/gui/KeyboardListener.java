package hywt.music.touhou.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class KeyboardListener implements KeyListener {

    private final HashMap<Integer, Boolean> keys;
    public final static int KEY_COUNTS = 300;

    public KeyboardListener() {
        System.out.println("init");
        keys = new HashMap<>(KEY_COUNTS);
        for (int i = 0; i < KEY_COUNTS; i++) {
            keys.put(i, false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.put(e.getKeyCode(), false);
    }

    public boolean isKeyDown(int keyCode) {
        return keys.get(keyCode);
    }

}