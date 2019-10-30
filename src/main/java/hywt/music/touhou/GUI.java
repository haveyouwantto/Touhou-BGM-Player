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

public class GUI {

	private JFrame frmTouhouBgmPlayer;
	private PCMPlayer pcmp = new PCMPlayer();
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
		frmTouhouBgmPlayer.setIconImage(
				Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/assets/hywt/music/touhou/icon.png")));
		frmTouhouBgmPlayer.setTitle(Messages.getString("GUI.title")); //$NON-NLS-1$
		frmTouhouBgmPlayer.setBounds(100, 100, 450, 300);
		frmTouhouBgmPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		PathManager pathman = new PathManager();

		Notification not = new Notification(frmTouhouBgmPlayer);

		JPanel panel = new JPanel();
		frmTouhouBgmPlayer.getContentPane().add(panel, BorderLayout.CENTER);

		JComboBox<Music> musicComboBox = new JComboBox<Music>();

		// TODO: Add music to combo box
		BGMData bgm = InfoReader.read();

		for (int i = 0; i < bgm.games[0].music.length; i++) {
			musicComboBox.addItem(bgm.games[0].music[i]);
		}

		JComboBox<Game> gameComboBox = new JComboBox<Game>();
		gameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicComboBox.removeAllItems();
				gameId = gameComboBox.getSelectedIndex();
				for (int i = 0; i < bgm.games[gameId].music.length; i++) {
					musicComboBox.addItem(bgm.games[gameId].music[i]);
				}
			}
		});
		for (int i = 0; i < bgm.games.length; i++) {
			gameComboBox.addItem(bgm.games[i]);
		}
		panel.add(gameComboBox);

		panel.add(musicComboBox);

		JPanel playbackControlPanel = new JPanel();
		playbackControlPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.add(playbackControlPanel);

		JButton btnStop = new JButton("O"); //$NON-NLS-1$
		playbackControlPanel.add(btnStop);

		btnStop.setEnabled(false);
		JButton btnPlay = new JButton(">"); //$NON-NLS-1$
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
		panel.add(btnP);
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
							if (g.format == 0) {

							} else if (g.format == 1) {
								pcmp.play(bgmpath.path.get(gameId).path, (Music) musicComboBox.getSelectedItem(),
										tglbtnLoop.isSelected());
							} else {

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
