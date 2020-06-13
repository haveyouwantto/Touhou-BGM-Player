package hywt.music.touhou.gui.test;

import hywt.music.touhou.gui.KeyboardListener;
import hywt.music.touhou.gui.WaveGraph;
import hywt.music.touhou.io.MusicSystem;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class LoopEditor extends WaveGraph {
    /**
     *
     */

    private static final long serialVersionUID = -4343137822143577401L;
    private final RandomAccessFile raf;
    protected long start;
    protected long end;
    private byte[] b;
    protected byte[] b2;
    protected int[] ints2;
    private final KeyboardListener keyboardListener;

    public LoopEditor(File file, KeyboardListener keyboardListener) throws IOException {
        super(1024);
        raf = new RandomAccessFile(file, "r");
        start = 1024;
        end = 1048576;
        b = new byte[1024];
        b2 = new byte[1024];
        ints2 = new int[512];
        initialize();
        this.keyboardListener = keyboardListener;
    }

    private void setData() throws IOException {
        raf.seek(start - 512);
        this.setMarker1((float) (raf.getFilePointer()) / raf.length());
        raf.read(b);
        this.setData(b, 0.0001f);
        this.repaint();
    }

    private void setData2() throws IOException {
        raf.seek(end - 512);
        this.setMarker2((float) (raf.getFilePointer()) / raf.length());
        raf.read(b2);
        this.setData2(b2, 0.0001f);
        this.repaint();
    }

    @Override
    public void setData(final byte[] b, final float progress) {
        this.progress = progress;
        for (int i = 0; i < b.length - 1; i += 2) {
            ints[i >> 1] = ((short) (Byte.toUnsignedInt(b[i]) | Byte.toUnsignedInt(b[i + 1]) << 8) >> 7);
        }
    }

    public void setData2(final byte[] b, final float progress) {
        this.progress = progress;
        for (int i = 0; i < b.length - 1; i += 2) {
            ints2[i >> 1] = ((short) (Byte.toUnsignedInt(b[i]) | Byte.toUnsignedInt(b[i + 1]) << 8) >> 7);
        }
    }

    private void initialize() throws IOException {
        final long len = raf.length();
        final Insets ins = this.getInsets();
        this.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseMoved(final MouseEvent e) {
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                try {
                    if (e.getButton() == 1) {
                        long pos = (long) ((e.getX() - ins.left) / 512.0f * len);
                        start = MusicSystem.roundByte(pos, 4);
                        setData();
                    } else if (e.getButton() == 3) {
                        long pos = (long) ((e.getX() - ins.left) / 512.0f * len);
                        end = MusicSystem.roundByte(pos, 4);
                        setData2();
                    }
                    repaint();
                } catch (final IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
            }
        });
        this.addMouseWheelListener(e -> {
            try {
                int i = e.getWheelRotation();
                int base = 4;
                System.out.println(keyboardListener.isKeyDown(KeyEvent.VK_SHIFT));
                if (keyboardListener.isKeyDown(KeyEvent.VK_CONTROL))
                    if (keyboardListener.isKeyDown(KeyEvent.VK_SHIFT))
                        base = 256;
                    else
                        base = 16;
                else if (keyboardListener.isKeyDown(KeyEvent.VK_SHIFT))
                    base = 1;
                int delta = i * 4 * base;
                if (end + delta < raf.length() - 512 && end + delta > 512) {
                    end += delta;
                    setData2();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 512, 517);
        g.setColor(Color.lightGray);
        g.drawLine(256, 0, 256, 517);
        if (progress > 0) {
            g.setColor(Color.red);
            int i;
            for (i = 0; i < ints.length - 3; i++) {
                int offset = i % 2 == 0 ? 128 : 384;
                g.drawLine(i, ints[i] / 2 + offset, i + 2, ints[i + 2] / 2 + offset);
            }
            g.setColor(Color.green);
            for (i = 0; i < ints.length - 3; i++) {
                int offset = i % 2 == 0 ? 128 : 384;
                g.drawLine(i, ints2[i] / 2 + offset, i + 2, ints2[i + 2] / 2 + offset);
            }
            g.setColor(Color.white);
            g.fillRect(0, 512, (int) (progress * 512), 5);
            g.setColor(Color.red);
            g.drawLine((int) (marker1 * 512), 512, (int) (marker1 * 512), 517);
            g.setColor(Color.green);
            g.drawLine((int) (marker2 * 512), 512, (int) (marker2 * 512), 517);
        }
        String end1 = String.valueOf(end);
        g.drawString(end1, 512 - end1.length() * 7, 512);
        g.setColor(Color.red);
        g.drawString(String.valueOf(start), 0, 512);
        g.setColor(Color.white);
        int delta1 = ints[256] - ints2[256];
        int delta2 = ints[257] - ints2[257];
        String s1 = String.valueOf(delta1);
        g.drawString(s1, 256 - s1.length() * 7, 512);
        g.drawString(String.valueOf(delta2), 258, 512);
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) throws IOException {
        this.start = start;
        this.setData();
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) throws IOException {
        this.end = end;
        this.setData2();
    }
}
