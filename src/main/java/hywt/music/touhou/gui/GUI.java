package hywt.music.touhou.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.UIManager;
import java.awt.FlowLayout;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import hywt.music.touhou.gui.Messages;
import hywt.music.touhou.pcmprocessing.PCMPlayer;
import hywt.music.touhou.savedata.BGMPath;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;
import hywt.music.touhou.Constants;
import hywt.music.touhou.Etc;

import javax.swing.event.ChangeEvent;
import javax.swing.BoxLayout;

public class GUI {

	private JFrame frmTouhouBgmPlayer;
	private PCMPlayer pcmp = new PCMPlayer();
	private int gameId = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GUI window = new GUI();
					window.frmTouhouBgmPlayer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		frmTouhouBgmPlayer = new JFrame();
		final Notification not = new Notification(frmTouhouBgmPlayer);
		frmTouhouBgmPlayer.setIconImage(
				Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/assets/hywt/music/touhou/icon.png"))); //$NON-NLS-1$
		frmTouhouBgmPlayer.setTitle(Messages.getString("GUI.title")); //$NON-NLS-1$
		frmTouhouBgmPlayer.setBounds(100, 100, 450, 300);
		frmTouhouBgmPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 获取BGM信息

		// 初始化工具
		PathManager pathman = new PathManager();
		MusicExporter mus = new MusicExporter();

		JPanel infoPanel = new JPanel();
		frmTouhouBgmPlayer.getContentPane().add(infoPanel, BorderLayout.SOUTH);

		// 正在播放
		JLabel lblNowPlaying = new JLabel(Messages.getString("GUI.lblNowPlaying.text")); //$NON-NLS-1$
		infoPanel.add(lblNowPlaying);

		JLabel lblplaying = new JLabel("-"); //$NON-NLS-1$
		infoPanel.add(lblplaying);

		JPanel controlPanel = new JPanel();
		frmTouhouBgmPlayer.getContentPane().add(controlPanel, BorderLayout.CENTER);
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// 选择音乐的panel
		JPanel musicSelectionPanel = new JPanel();
		musicSelectionPanel.setBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"), Messages.getString("GUI.musicSelection"), //$NON-NLS-1$ //$NON-NLS-2$
						TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		controlPanel.add(musicSelectionPanel);
		musicSelectionPanel.setLayout(new GridLayout(0, 1, 0, 4));

		// 游戏下拉菜单
		JComboBox<Game> gameComboBox = new JComboBox<Game>();
		musicSelectionPanel.add(gameComboBox);

		// 音乐下拉菜单
		JComboBox<Music> musicComboBox = new JComboBox<Music>();
		musicSelectionPanel.add(musicComboBox);
		gameComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				musicComboBox.removeAllItems();
				gameId = gameComboBox.getSelectedIndex();
				for (int i = 0; i < Constants.bgmdata.games.get(gameId).music.size(); i++) {
					musicComboBox.addItem(Constants.bgmdata.games.get(gameId).music.get(i));
				}
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
		playbackControlPanel.add(panel_1);

		// 停止按钮
		JButton btnStop = new JButton("\u2588");
		panel_1.add(btnStop);
		


		// 播放按钮
		JButton btnPlay = new JButton("\u25b6");
		panel_1.add(btnPlay);

		// 循环按钮
		JToggleButton tglbtnLoop = new JToggleButton("L"); //$NON-NLS-1$
		panel_1.add(tglbtnLoop);
		tglbtnLoop.setSelected(true);

		JToggleButton btnPause = new JToggleButton(Messages.getString("GUI.btnP_1.text"));
		panel_1.add(btnPause);
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pcmp.isPaused()) {
					pcmp.resume();
				} else {
					pcmp.pause();
				}
			}
		});
		btnPause.setEnabled(false);
		tglbtnLoop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tglbtnLoop.isSelected();
			}
		});

		// 线程代码
		Runnable musicPlaying = new Runnable() {
			@Override
			public void run() {
				try {
					// 读取音乐信息
					BGMPath bgmpath = BGMPath.load();
					Game g = (Game) gameComboBox.getSelectedItem();
					Music m = (Music) musicComboBox.getSelectedItem();
					boolean loop = tglbtnLoop.isSelected();

					if (g.format == 0) {
						// 东方红魔乡格式 = 0
						int index = g.music.indexOf(m);

						lblplaying.setText(m.title);
						pcmp.play(bgmpath.path.get(gameId).path + "/" + Etc.getEoSDFilename(index), m, loop); //$NON-NLS-1$
					} else if (g.format == 1) {

						// 一般格式 = 1
						lblplaying.setText(m.title);
						pcmp.play(bgmpath.path.get(gameId).path, m, loop);
					} else {
						// 不支持的格式
						// TODO 支持黄昏格式（绯想天和非想天则）
						not.showWarning(Messages.getString("GUI.unsupported")); //$NON-NLS-1$
						btnStop.setEnabled(false);
						btnPlay.setEnabled(true);
					}
				} catch (FileNotFoundException e1) {
					not.showError(Messages.getString("GUI.fileNotFoundError")); //$NON-NLS-1$
					e1.printStackTrace();
				}
			}
		};

		btnStop.setEnabled(false);

		// 播放按钮的事件
		btnPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				new Thread(musicPlaying).start();

				// 设置播放和停止按钮状态
				btnStop.setEnabled(true);
				btnPlay.setEnabled(false);
				btnPause.setEnabled(true);
			}
		});

		// 停止按钮的事件
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// 将正在播放的曲目设置为无
				lblplaying.setText("-"); //$NON-NLS-1$
				try {
					// 尝试停止音乐
					pcmp.resume();
					pcmp.stop();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				// 设置播放和停止按钮状态
				btnStop.setEnabled(false);
				btnPlay.setEnabled(true);
				btnPause.setSelected(false);
				btnPause.setEnabled(false);
			}
		});

		JPanel panel = new JPanel();
		playbackControlPanel.add(panel);

		// 音量数字显示
		JLabel volumeLabel = new JLabel(Messages.getString("GUI.volumeLabel.text"));
		panel.add(volumeLabel);

		// 音量滑块
		JSlider slider = new JSlider();
		panel.add(slider);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				float volume = slider.getValue() / 100f;
				volumeLabel.setText(String.valueOf(slider.getValue()));
				pcmp.setVolume(volume);
			}
		});
		slider.setValue(100);
		slider.setPaintLabels(true);
		
		JPanel panel_2 = new JPanel();
		controlPanel.add(panel_2);
		
				// 显示路径管理器按钮
				JButton btnP = new JButton(Messages.getString("GUI.PathManager.text")); //$NON-NLS-1$
				panel_2.add(btnP);
				
						// 音乐导出按钮
						JButton btnExporter = new JButton(Messages.getString("GUI.exportMusic.text")); //$NON-NLS-1$
						panel_2.add(btnExporter);
						btnExporter.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								mus.display();
							}
						});
				btnP.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// 显示路径管理器
						pathman.display();
					}
				});

	}

}
