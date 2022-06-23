package hywt.music.touhou.gui;

import hywt.music.touhou.Logger;
import hywt.music.touhou.Playlist;
import hywt.music.touhou.savedata.Music;
import hywt.music.touhou.savedata.PlaylistData;
import hywt.music.touhou.savedata.PlaylistList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class PlaylistPlayer implements LazyLoader {

    private BaseFrame frame;
    private final GUI parent;
    private PlaylistList list;
    private JButton btnPlay;
    private final Notification not = new Notification(frame);
    private JComboBox<PlaylistData> comboBox;
    private JList<Music> jlist;
    private JScrollPane scrollPane;
    private JButton btnStop;
    private JButton btnPause;

    /**
     * Create the application.
     */
    public PlaylistPlayer(GUI parent) {
        this.parent = parent;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new BaseFrame();
        frame.setTitle("列表播放器");
        frame.setBounds(100, 100, 391, 260);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        list = new PlaylistList();

        scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        jlist = new JList<>();
        scrollPane.setViewportView(jlist);

        JPanel panel_1 = new JPanel();
        frame.getContentPane().add(panel_1, BorderLayout.SOUTH);

        btnStop = new JButton("\u2588");
        btnStop.setEnabled(false);
        btnStop.addActionListener(e -> parent.getBtnStop().doClick());
        panel_1.add(btnStop);

        btnPlay = new JButton("\u25b6");
        panel_1.add(btnPlay);

        JPanel panel_2 = new JPanel();
        frame.getContentPane().add(panel_2, BorderLayout.NORTH);
        panel_2.setBorder(
                new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u9009\u62E9\u64AD\u653E\u5217\u8868",
                        TitledBorder.CENTER, TitledBorder.TOP, null, null));

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            PlaylistData p = (PlaylistData) comboBox.getSelectedItem();
            if (p != null) {
                DefaultListModel<Music> dlm = new DefaultListModel<>();
                Playlist playlist = p.getPlaylist();
                Iterator<Music> iterator = playlist.getIterator();
                while (iterator.hasNext()) {
                    Music m = iterator.next();
                    dlm.addElement(m);
                }
                jlist.setModel(dlm);
                jlist.setSelectedIndex(0);
            }
        });
        panel_2.add(comboBox);

        Runnable r = () -> {
            try {
                PlaylistData ply = (PlaylistData) comboBox.getSelectedItem();
                parent.pcmp.setLoop(parent.getBtnLoop().isSelected());
                parent.pcmp.setGameList(false);
                parent.pcmp.playList(ply.getPlaylist(), jlist.getSelectedIndex());
            } catch (FileNotFoundException e1) {
                not.showError(Messages.getString("GUI.fileNotFoundError")); //$NON-NLS-1$
                parent.setStop();
                Logger.error(e1);
                // btnStop.doClick();
            } catch (IOException e) {
                not.showError(Messages.getString("GUI.IOException") + e.getLocalizedMessage()); //$NON-NLS-1$
                Logger.error(e);
                // btnStop.doClick();
            } catch (LineUnavailableException e) {
                // TODO Auto-generated catch block
                Logger.error(e);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                Logger.error(e);
            } catch (UnsupportedAudioFileException e) {
                // TODO Auto-generated catch block
                Logger.error(e);
            }
        };
        btnPlay.addActionListener(e -> {
            new Thread(r).start();
            parent.setStart();
        });
    }

    public JButton getBtnPlay() {
        return btnPlay;
    }

    protected JComboBox<PlaylistData> getComboBox() {
        return comboBox;
    }

    public JButton getBtnStop() {
        return btnStop;
    }

    public JButton getBtnPause() {
        return btnPause;
    }

    @Override
    public void load() {
        initialize();
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            File f = new File("playlists/");
            try {
                list.load(f);
            } catch (NullPointerException e) {
                f.mkdirs();
                list.load(f);
            }
            comboBox.removeAllItems();
            for (int i = 0; i < list.list.size(); i++) {
                comboBox.addItem(list.list.get(i));
            }
        }
        frame.setVisible(b);
    }
}
