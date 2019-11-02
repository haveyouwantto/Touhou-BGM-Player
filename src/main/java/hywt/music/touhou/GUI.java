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
		nof=new Notification(frmTouhouBgmPlayer);
		frmTouhouBgmPlayer.setIconImage(
				Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/assets/hywt/music/touhou/icon.png"))); //$NON-NLS-1$
		frmTouhouBgmPlayer.setTitle(Messages.getString("GUI.title")); //$NON-NLS-1$
		frmTouhouBgmPlayer.setBounds(100, 100, 450, 300);
		frmTouhouBgmPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		PathManager pathman = new PathManager();

		Notification not = new Notification(frmTouhouBgmPlayer);
		
		JPanel infoPanel = new JPanel();
		frmTouhouBgmPlayer.getContentPane().add(infoPanel, BorderLayout.SOUTH);
		
		JLabel lblNowPlaying = new JLabel(Messages.getString("GUI.lblNowPlaying.text")); //$NON-NLS-1$
		infoPanel.add(lblNowPlaying);
		
		JLabel lblplaying = new JLabel(Messages.getString("GUI.lblNewLabel.text")); //$NON-NLS-1$
		infoPanel.add(lblplaying);

		JPanel controlPanel = new JPanel();
		frmTouhouBgmPlayer.getContentPane().add(controlPanel, BorderLayout.CENTER);

		JComboBox<Music> musicComboBox = new JComboBox<Music>();

		// TODO: Add music to combo box
		BGMData bgm = InfoReader.read();

		for (int i = 0; i < bgm.games.get(0).music.size(); i++) {
			musicComboBox.addItem(bgm.games.get(0).music.get(i));
		}

		JComboBox<Game> gameComboBox = new JComboBox<Game>();
		gameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicComboBox.removeAllItems();
				gameId = gameComboBox.getSelectedIndex();
				for (int i = 0; i < bgm.games.get(gameId).music.size(); i++) {
					musicComboBox.addItem(bgm.games.get(gameId).music.get(i));
				}
			}
		});
		for (int i = 0; i < bgm.games.size(); i++) {
			gameComboBox.addItem(bgm.games.get(i));
		}
		controlPanel.add(gameComboBox);

		controlPanel.add(musicComboBox);

		JPanel playbackControlPanel = new JPanel();
		playbackControlPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		controlPanel.add(playbackControlPanel);

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
							Music m=(Music) musicComboBox.getSelectedItem();
							boolean loop=tglbtnLoop.isSelected();
							
							if (g.format == 0) {
								int index=g.music.indexOf(m);
								++index;
								
								String filename;
								if(index<10) {
									filename="th06_0"+index+".wav"; //$NON-NLS-1$ //$NON-NLS-2$
								}else {
									filename="th06_"+index+".wav"; //$NON-NLS-1$ //$NON-NLS-2$
								}
								lblplaying.setText(m.title);
								pcmp.play(bgmpath.path.get(gameId).path+"/"+filename, m, loop); //$NON-NLS-1$
							} else if (g.format == 1) {
								lblplaying.setText(m.title);
								pcmp.play(bgmpath.path.get(gameId).path, m,
										loop);
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
				lblplaying.setText("-");
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
