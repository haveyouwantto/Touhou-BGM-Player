package hywt.music.touhou.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import hywt.music.touhou.Constants;
import hywt.music.touhou.savedata.Music;
import hywt.music.touhou.savedata.Playlist;
import hywt.music.touhou.savedata.PlaylistList;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import javax.swing.DefaultListModel;

import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class PlaylistPlayer {

	private JFrame frame;
	private GUI parent;
	private PlaylistList list;
	private JButton btnPlay;
	private Notification not = new Notification(frame);
	private JComboBox<Playlist> comboBox;
	private JList<Music> jlist;
	private JScrollPane scrollPane;
	private JButton btnStop;
	private JButton btnPause;

	/**
	 * Create the application.
	 */
	public PlaylistPlayer(GUI parent) {
		this.parent = parent;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(PlaylistPlayer.class.getResource("/assets/hywt/music/touhou/icon.png")));
		frame.setTitle("列表播放器");
		frame.setBounds(100, 100, 391, 260);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		list = new PlaylistList();

		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		jlist = new JList<Music>();
		scrollPane.setViewportView(jlist);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		btnStop = new JButton("\u2588");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.getBtnStop().doClick();
			}
		});
		panel_1.add(btnStop);

		btnPlay = new JButton("\u25b6");
		panel_1.add(btnPlay);

		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, BorderLayout.NORTH);
		panel_2.setBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u9009\u62E9\u64AD\u653E\u5217\u8868",
						TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));

		comboBox = new JComboBox<Playlist>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Playlist p = (Playlist) comboBox.getSelectedItem();
				if (p != null) {
					DefaultListModel<Music> dlm = new DefaultListModel<Music>();

					for (int i = 0; i < p.ids.size(); i++) {
						String[] ids = p.ids.get(i);
						dlm.addElement(Constants.bgmdata.getMusicbyId(ids[0], ids[1]));
					}
					jlist.setModel(dlm);
					jlist.setSelectedIndex(0);
				}
			}
		});
		panel_2.add(comboBox);

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					Playlist ply = (Playlist) comboBox.getSelectedItem();
					parent.pcmp.setLoop(parent.getBtnLoop().isSelected());
					parent.pcmp.setGameList(false);
					parent.pcmp.playList(ply, jlist.getSelectedIndex());
				} catch (FileNotFoundException e1) {
					not.showError(Messages.getString("GUI.fileNotFoundError")); //$NON-NLS-1$
					parent.setStop();
					e1.printStackTrace();
					// btnStop.doClick();
				} catch (IOException e) {
					not.showError(Messages.getString("GUI.IOException") + e.getLocalizedMessage()); //$NON-NLS-1$
					e.printStackTrace();
					// btnStop.doClick();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(r).start();
				parent.setStart();
			}
		});
	}

	public void show() {
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
		frame.setVisible(true);
	}

	public JButton getBtnPlay() {
		return btnPlay;
	}

	protected JComboBox<Playlist> getComboBox() {
		return comboBox;
	}
	public JButton getBtnStop() {
		return btnStop;
	}
	public JButton getBtnPause() {
		return btnPause;
	}
}
