package hywt.music.touhou.gui;

import hywt.music.touhou.Constants;
import hywt.music.touhou.Logger;
import hywt.music.touhou.StringFormatter;
import hywt.music.touhou.io.MusicSystem;
import hywt.music.touhou.pcmprocessing.PCMPlayer;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

public class GUI {

    private BaseFrame frmTouhouBgmPlayer;
    PCMPlayer pcmp = new PCMPlayer();
    private int gameId = 0;
    private boolean updateProgressBar = true;
    private int playMode = 0;
    private boolean playing;
    private PlaylistPlayer ply;
    private Hashtable<Integer, JLabel> ht;
    /**
     * @wbp.nonvisual location=479,34
     */
    private final JLabel label = new JLabel("\u25b2");
    private JLabel lblplaying;
    private JButton btnPlay;
    private JButton btnStop;
    private JToggleButton btnPause;
    private JSlider progressBar;
    private JLabel lblLength;
    private JToggleButton btnLoop;
    private JButton btnPlayMode;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GUI window = new GUI();
                window.frmTouhouBgmPlayer.setVisible(true);
            } catch (Exception e) {
                Logger.error(e);
            }
        });
    }

    private HashMap<LazyLoader, Boolean> loaderStatus;

    /**
     * Create the application.
     */
    public GUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        frmTouhouBgmPlayer = new BaseFrame();
        frmTouhouBgmPlayer.setTitle(Messages.getString("GUI.title")); //$NON-NLS-1$
        frmTouhouBgmPlayer.setBounds(100, 100, 440, 320);
        frmTouhouBgmPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 初始化
        final Notification not = new Notification(frmTouhouBgmPlayer);

        try {
            // 获取BGM信息
            Constants.init();
        } catch (IOException e3) {
            not.showError(Messages.getString("GUI.bgmDataNotFound")); //$NON-NLS-1$
            Logger.error(e3);
            Thread.currentThread().interrupt();
        }

        PathManager pathman = new PathManager();
        MusicExporter mus = new MusicExporter();
        Visualizer vis = new Visualizer(pcmp.getBuffer().length);
        ply = new PlaylistPlayer(this);
        Extra extra = new Extra();

        loaderStatus = new HashMap<>();
        loaderStatus.put(pathman, false);
        loaderStatus.put(mus, false);
        loaderStatus.put(vis, false);
        loaderStatus.put(extra, false);
        loaderStatus.put(ply, false);

        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
        frmTouhouBgmPlayer.getContentPane().add(infoPanel, BorderLayout.SOUTH);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JPanel panel_3 = new JPanel();
        infoPanel.add(panel_3);

        // 正在播放
        JLabel lblNowPlaying = new JLabel(Messages.getString("GUI.lblNowPlaying.text")); //$NON-NLS-1$
        panel_3.add(lblNowPlaying);

        lblNowPlaying.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                show(vis);
            }
        });

        lblplaying = new JLabel("-"); //$NON-NLS-1$
        panel_3.add(lblplaying);

        JPanel panel_4 = new JPanel();
        infoPanel.add(panel_4);
        panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));

        JLabel lblTime = new JLabel(Messages.getString("GUI.lblNewLabel.text"));
        panel_4.add(lblTime);

        progressBar = new JSlider();
        progressBar.setEnabled(false);
        progressBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (progressBar.isEnabled())
                    lblTime.setText(
                            StringFormatter.getMusicLengthTime(pcmp.getMusic().sampleRate, progressBar.getValue()));
            }
        });
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (progressBar.isEnabled()) {
                    int pos = progressBar.getValue();
                    try {
                        pcmp.seek(pos);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        Logger.error(e1);
                    }
                }
                updateProgressBar = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                updateProgressBar = false;
            }
        });
        panel_4.add(progressBar);
        progressBar.setValue(0);

        ht = new Hashtable<>();
        ht.put(0, label);
        progressBar.setLabelTable(ht);
        progressBar.setPaintLabels(true);

        lblLength = new JLabel(Messages.getString("GUI.label.text")); //$NON-NLS-1$
        panel_4.add(lblLength);

        // 播放状态更新线程
        Timer timer = new Timer(16, listener -> {
            try {
                if (updateProgressBar) {
                    Music m = pcmp.getMusic();
                    progressBar.setValue((int) pcmp.getPlayback());
                    lblTime.setText(StringFormatter.getMusicLengthTime(m.sampleRate, pcmp.getPlayback()));
                    lblLength.setText(StringFormatter.getMusicLengthTime(m.sampleRate, pcmp.getLength()));
                    lblplaying.setText(m.title);
                    progressBar.setMaximum((int) pcmp.getLength());
                    ht.clear();
                    ht.put(pcmp.getPreludeLength(), label);
                    if (vis.isVisible()) {
                        vis.update(pcmp.getBuffer(), pcmp.getProgress());
                    }
                }
                if (!playing) {
                    lblplaying.setText("-"); //$NON-NLS-1$
                }
            } catch (NullPointerException ignored) {
            }
        });

        timer.start();

        JPanel controlPanel = new JPanel();
        frmTouhouBgmPlayer.getContentPane().add(controlPanel, BorderLayout.CENTER);
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));

        // 选择音乐的panel
        JPanel musicSelectionPanel = new JPanel();
        musicSelectionPanel.setBorder(
                new TitledBorder(UIManager.getBorder("TitledBorder.border"), Messages.getString("GUI.musicSelection"), //$NON-NLS-1$ //$NON-NLS-2$
                        TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
        controlPanel.add(musicSelectionPanel);
        musicSelectionPanel.setLayout(new GridLayout(0, 1, 0, 4));

        // 游戏下拉菜单
        JComboBox<Game> gameComboBox = new JComboBox<>();
        musicSelectionPanel.add(gameComboBox);

        // 音乐下拉菜单
        JComboBox<Music> musicComboBox = new JComboBox<>();
        musicSelectionPanel.add(musicComboBox);
        gameComboBox.addActionListener(e -> {
            musicComboBox.removeAllItems();
            gameId = gameComboBox.getSelectedIndex();
            for (int i = 0; i < Constants.bgmdata.games.get(gameId).music.size(); i++) {
                musicComboBox.addItem(Constants.bgmdata.games.get(gameId).music.get(i));
            }
        });

        // 将音乐信息添加至下拉菜单
        for (int i = 0; i < Constants.bgmdata.games.get(0).music.size(); i++) {
            musicComboBox.addItem(Constants.bgmdata.games.get(0).music.get(i));
        }
        for (int i = 0; i < Constants.bgmdata.games.size(); i++) {
            gameComboBox.addItem(Constants.bgmdata.games.get(i));
        }

        // 播放控制的panel
        JPanel playbackControlPanel = new JPanel();
        playbackControlPanel.setBorder(new TitledBorder(null, Messages.getString("GUI.playControl"), //$NON-NLS-1$
                TitledBorder.CENTER, TitledBorder.TOP, null, null));
        controlPanel.add(playbackControlPanel);
        playbackControlPanel.setLayout(new BoxLayout(playbackControlPanel, BoxLayout.Y_AXIS));

        JPanel panel_1 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
        flowLayout.setVgap(0);
        playbackControlPanel.add(panel_1);

        // 停止按钮
        btnStop = new JButton("\u2588"); //$NON-NLS-1$
        panel_1.add(btnStop);

        // 播放按钮
        btnPlay = new JButton("\u25b6"); //$NON-NLS-1$
        panel_1.add(btnPlay);

        btnPause = new JToggleButton(Messages.getString("GUI.btnP_1.text")); //$NON-NLS-1$
        panel_1.add(btnPause);
        btnPause.addActionListener(e -> {
            if (pcmp.isPaused()) {
                pcmp.resume();
            } else {
                pcmp.pause();
            }
        });
        btnPause.setEnabled(false);

        // 循环按钮
        btnPlayMode = new JButton(Messages.getString("GUI.toggleBtnLoop.text")); //$NON-NLS-1$
        btnPlayMode.addActionListener(e -> toggleModeBtn());

        btnLoop = new JToggleButton(Messages.getString("GUI.btnLoop.text")); //$NON-NLS-1$
        btnLoop.addActionListener(e -> pcmp.setLoop(btnLoop.isSelected()));
        btnLoop.setSelected(true);
        panel_1.add(btnLoop);
        panel_1.add(btnPlayMode);

        // 播放线程代码
        Runnable musicPlaying = () -> {
            try {
                // 读取音乐信息
                Game g = (Game) gameComboBox.getSelectedItem();
                Music m = (Music) musicComboBox.getSelectedItem();
                vis.getWaveGraph().marker1 = MusicSystem.getLoopPercentage(g, m);
                vis.getWaveGraph().marker2 = 1;
                pcmp.setLoop(btnLoop.isSelected());
                pcmp.setPlayMode(playMode);
                pcmp.play(g, m);
            } catch (FileNotFoundException e1) {
                not.showError(Messages.getString("GUI.fileNotFoundError")); //$NON-NLS-1$
                Logger.error(e1);
            } catch (IOException e) {
                not.showError(Messages.getString("GUI.IOException") + e.getLocalizedMessage()); //$NON-NLS-1$
                Logger.error(e);
            } catch (UnsupportedAudioFileException e) {
                not.showError(Messages.getString("GUI.unsupported")); //$NON-NLS-1$
                Logger.error(e);
            } catch (AssertionError e) {
                not.showError(Messages.getString("GUI.nullSelection"));
            } catch (Exception e) {
                Logger.error(e);
                not.showError(e.toString());
            }
            btnStop.doClick();
        };

        btnStop.setEnabled(false);

        // 播放按钮的事件
        btnPlay.addActionListener(e -> {
            pcmp.setGameList(true);

            load(vis);
            load(ply);

            Thread t = new Thread(musicPlaying);
            t.setName("Player Thread");
            t.start();
            setStart();
        });

        // 停止按钮的事件
        btnStop.addActionListener(e -> setStop());

        JPanel panel = new JPanel();
        playbackControlPanel.add(panel);

        // 音量数字显示
        JLabel volumeLabel = new JLabel(Messages.getString("GUI.volumeLabel.text")); //$NON-NLS-1$
        panel.add(volumeLabel);

        // 音量滑块
        JSlider slider = new JSlider();
        panel.add(slider);
        slider.addChangeListener(e -> {
            float volume = slider.getValue() / 100f;
            volumeLabel.setText(String.valueOf(slider.getValue()));
            pcmp.setVolume(volume);
        });
        slider.setValue(100);
        slider.setPaintLabels(true);

        JPanel panel_2 = new JPanel();
        FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
        flowLayout_2.setVgap(0);
        controlPanel.add(panel_2);

        JButton btnNewButton = new JButton(Messages.getString("GUI.btnNewButton.text")); //$NON-NLS-1$
        btnNewButton.addActionListener(e -> show(ply));
        panel_2.add(btnNewButton);

        // 显示路径管理器按钮
        JButton btnP = new JButton(Messages.getString("GUI.PathManager.text")); //$NON-NLS-1$
        panel_2.add(btnP);

        // 音乐导出按钮
        JButton btnExporter = new JButton(Messages.getString("GUI.exportMusic.text")); //$NON-NLS-1$
        panel_2.add(btnExporter);
        btnExporter.addActionListener(e -> show(mus));
        btnP.addActionListener(e -> {
            // 显示路径管理器
            show(pathman);
        });

    }

    private void show(LazyLoader loader) {
        load(loader);
        loader.setVisible(true);
    }

    private void load(LazyLoader loader) {
        if (!loaderStatus.get(loader)) {
            loader.load();
            loaderStatus.put(loader, true);
        }
    }

    protected JLabel getLblplaying() {
        return lblplaying;
    }

    public int getPlayMode() {
        return playMode;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
        setModeBtn();
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public JButton getBtnPlay() {
        return btnPlay;
    }

    public JButton getBtnStop() {
        return btnStop;
    }

    public JToggleButton getBtnPause() {
        return btnPause;
    }

    public void setStart() {
        // 设置GUI
        btnStop.setEnabled(true);
        btnPlay.setEnabled(false);
        ply.getBtnPlay().setEnabled(false);
        ply.getBtnStop().setEnabled(true);
        btnPause.setEnabled(true);
        progressBar.setEnabled(true);
        playing = true;
    }

    public void setStop() {
        // 设置GUI
        btnStop.setEnabled(false);
        btnPlay.setEnabled(true);
        ply.getBtnPlay().setEnabled(true);
        ply.getBtnStop().setEnabled(false);
        btnPause.setSelected(false);
        btnPause.setEnabled(false);
        progressBar.setEnabled(false);
        pcmp.stop();
        lblLength.setText(Messages.getString("GUI.time")); //$NON-NLS-1$
        playing = false;
        ht.clear();
        ht.put(0, label);
    }

    public JSlider getProgressBar() {
        return progressBar;
    }

    public JLabel getLblLength() {
        return lblLength;
    }

    public JToggleButton getBtnLoop() {
        return btnLoop;
    }

    protected void setModeBtn() {
        switch (playMode) {
            case PCMPlayer.MUSIC:
                btnPlayMode.setText("单曲");
                break;
            case PCMPlayer.LIST:
                btnPlayMode.setText("列表");
                break;
        }
        pcmp.setPlayMode(playMode);
    }

    protected void toggleModeBtn() {
        switch (playMode) {
            case PCMPlayer.MUSIC:
                playMode = PCMPlayer.LIST;
                btnPlayMode.setText("列表");
                break;
            case PCMPlayer.LIST:
                playMode = PCMPlayer.MUSIC;
                btnPlayMode.setText("单曲");
                break;
        }
        pcmp.setPlayMode(playMode);
    }
}
