package hywt.music.touhou;

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
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.UIManager;

public class GUI {

	private JFrame frmTouhouBgmPlayer;
	private PCMPlayer pcmp = new PCMPlayer();
	private Notification nof;
	private int gameId = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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
		nof = new Notification(frmTouhouBgmPlayer);
		frmTouhouBgmPlayer.setIconImage(
				Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/assets/hywt/music/touhou/icon.png"))); //$NON-NLS-1$
		frmTouhouBgmPlayer.setTitle(Messages.getString("GUI.title")); //$NON-NLS-1$
		frmTouhouBgmPlayer.setBounds(100, 100, 450, 300);
		frmTouhouBgmPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		PathManager pathman = new PathManager();
		BGMData bgm = InfoReader.read();

		Notification not = new Notification(frmTouhouBgmPlayer);

		JPanel infoPanel = new JPanel();
		frmTouhouBgmPlayer.getContentPane().add(infoPanel, BorderLayout.SOUTH);

		JLabel lblNowPlaying = new JLabel(Messages.getString("GUI.lblNowPlaying.text")); //$NON-NLS-1$
		infoPanel.add(lblNowPlaying);

		JLabel lblplaying = new JLabel("-"); //$NON-NLS-1$
		infoPanel.add(lblplaying);

		JPanel controlPanel = new JPanel();
		frmTouhouBgmPlayer.getContentPane().add(controlPanel, BorderLayout.CENTER);

		JPanel musicSelectionPanel = new JPanel();
		musicSelectionPanel
				.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Messages.getString("GUI.musicSelection"), TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0))); //$NON-NLS-1$ //$NON-NLS-2$
		controlPanel.add(musicSelectionPanel);
		musicSelectionPanel.setLayout(new GridLayout(0, 1, 0, 4));

		JComboBox<Game> gameComboBox = new JComboBox<Game>();
		musicSelectionPanel.add(gameComboBox);

		JComboBox<Music> musicComboBox = new JComboBox<Music>();
		musicSelectionPanel.add(musicComboBox);
		gameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicComboBox.removeAllItems();
				gameId = gameComboBox.getSelectedIndex();
				for (int i = 0; i < bgm.games.get(gameId).music.size(); i++) {
					musicComboBox.addItem(bgm.games.get(gameId).music.get(i));
				}
			}
		});

		for (int i = 0; i < bgm.games.get(0).music.size(); i++) {
			musicComboBox.addItem(bgm.games.get(0).music.get(i));
		}
		for (int i = 0; i < bgm.games.size(); i++) {
			gameComboBox.addItem(bgm.games.get(i));
		}

		JPanel playbackControlPanel = new JPanel();
		playbackControlPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		controlPanel.add(playbackControlPanel);

		JButton btnStop = new JButton("\u2588"); //$NON-NLS-1$
		playbackControlPanel.add(btnStop);

		btnStop.setEnabled(false);
		JButton btnPlay = new JButton("\u25b6"); //$NON-NLS-1$
		playbackControlPanel.add(btnPlay);

		JToggleButton tglbtnLoop = new JToggleButton("L"); //$NON-NLS-1$
		tglbtnLoop.setSelected(true);
		playbackControlPanel.add(tglbtnLoop);

		JButton btnP = new JButton(Messages.getString("GUI.btnP.text")); //$NON-NLS-1$
		btnP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pathman.display();
			}
		});
		controlPanel.add(btnP);
		tglbtnLoop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tglbtnLoop.isSelected();
			}
		});

		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Play Music
				Runnable r = new Runnable() {
					@Override
					public void run() {
						try {
							BGMPath bgmpath = BGMPath.load();
							Game g = (Game) gameComboBox.getSelectedItem();
							Music m = (Music) musicComboBox.getSelectedItem();
							boolean loop = tglbtnLoop.isSelected();

							if (g.format == 0) {
								int index = g.music.indexOf(m);
								++index;

								String filename;
								if (index < 10) {
									filename = "th06_0" + index + ".wav"; //$NON-NLS-1$ //$NON-NLS-2$
								} else {
									filename = "th06_" + index + ".wav"; //$NON-NLS-1$ //$NON-NLS-2$
								}
								lblplaying.setText(m.title);
								pcmp.play(bgmpath.path.get(gameId).path + "/" + filename, m, loop); //$NON-NLS-1$
							} else if (g.format == 1) {
								lblplaying.setText(m.title);
								pcmp.play(bgmpath.path.get(gameId).path, m, loop);
							} else {
								nof.showWarning(Messages.getString("GUI.unsupported")); //$NON-NLS-1$
								btnStop.setEnabled(false);
								btnPlay.setEnabled(true);
							}
						} catch (FileNotFoundException e) {
							not.showError(Messages.getString("GUI.file_not_found_error")); //$NON-NLS-1$
							e.printStackTrace();
						}
					}
				};
				new Thread(r).start();

				btnStop.setEnabled(true);
				btnPlay.setEnabled(false);
			}
		});
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStop.setEnabled(false);
				btnPlay.setEnabled(true);
				lblplaying.setText("-"); //$NON-NLS-1$
				try {
					pcmp.stop();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

}
