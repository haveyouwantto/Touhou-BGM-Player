package hywt.music.touhou.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import hywt.music.touhou.Constants;
import hywt.music.touhou.Etc;
import hywt.music.touhou.pcmprocessing.PCMSaver;
import hywt.music.touhou.savedata.BGMPath;

import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JButton;

public class MusicExporter {

	private JFrame frmMusicExporter = new JFrame();
	private JPanel contentPane;
	private List<List<JCheckBox>> musicCheckboxLists = new ArrayList<List<JCheckBox>>();
	private List<JCheckBox> gameCheckboxes = new ArrayList<JCheckBox>();
	private List<JPanel> panels = new ArrayList<JPanel>();

	/**
	 * Create the frame.
	 */
	public MusicExporter() {
		frmMusicExporter.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MusicExporter.class.getResource("/assets/hywt/music/touhou/icon.png"))); //$NON-NLS-1$
		frmMusicExporter.setTitle(Messages.getString("MusicExporter.title")); //$NON-NLS-1$
		frmMusicExporter.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		frmMusicExporter.setBounds(100, 100, 801, 452);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frmMusicExporter.setContentPane(contentPane);

		BGMPath bgmpath = BGMPath.load();

		JPanel selectionPanel = new JPanel();
		contentPane.add(selectionPanel, BorderLayout.CENTER);
		selectionPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JScrollPane gameScrollPane = new JScrollPane();
		selectionPanel.add(gameScrollPane);

		JPanel gamePanel = new JPanel();
		gameScrollPane.setViewportView(gamePanel);
		gamePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), //$NON-NLS-1$
				Messages.getString("MusicExporter.selectGame"), TitledBorder.CENTER, TitledBorder.TOP, null, //$NON-NLS-1$
				new Color(0, 0, 0)));

		gamePanel.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane musicScrollPane = new JScrollPane();
		selectionPanel.add(musicScrollPane);

		JPanel musicPanel = new JPanel();
		musicScrollPane.setViewportView(musicPanel);
		musicPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), //$NON-NLS-1$
				Messages.getString("MusicExporter.selectMusic"), TitledBorder.CENTER, TitledBorder.TOP, null, //$NON-NLS-1$
				new Color(0, 0, 0)));
		musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));

		JPanel exportPanel = new JPanel();
		contentPane.add(exportPanel, BorderLayout.SOUTH);

		JButton selectAllButton = new JButton(Messages.getString("MusicExporter.selectAll.text")); //$NON-NLS-1$
		selectAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < gameCheckboxes.size(); i++) {
					gameCheckboxes.get(i).setSelected(true);
					musicPanel.add(panels.get(i));
					for (int j = 0; j < musicCheckboxLists.get(i).size(); j++) {
						musicCheckboxLists.get(i).get(j).setSelected(true);
					}
				}
				musicPanel.updateUI();
			}
		});
		exportPanel.add(selectAllButton);

		JButton deselectAllButton = new JButton(Messages.getString("MusicExporter.deselectAll.text")); //$NON-NLS-1$
		deselectAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < gameCheckboxes.size(); i++) {
					gameCheckboxes.get(i).setSelected(false);
					musicPanel.remove(panels.get(i));
					for (int j = 0; j < musicCheckboxLists.get(i).size(); j++) {
						musicCheckboxLists.get(i).get(j).setSelected(false);
					}
				}
				musicPanel.updateUI();
			}
		});
		exportPanel.add(deselectAllButton);
		
		JCheckBox separateCheckBox = new JCheckBox(Messages.getString("MusicExporter.checkBox.text")); //$NON-NLS-1$
		exportPanel.add(separateCheckBox);
		
				JButton exportButton = new JButton(Messages.getString("MusicExporter.export.text")); //$NON-NLS-1$
				exportButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						JFileChooser filechooser = new JFileChooser(".");
						filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int returnVal = filechooser.showSaveDialog(frmMusicExporter);

						if (returnVal == JFileChooser.APPROVE_OPTION) {
							for (int i = 0; i < gameCheckboxes.size(); i++) {
								int format = Constants.bgmdata.games.get(i).format;
								for (int j = 0; j < musicCheckboxLists.get(i).size(); j++) {

									if (musicCheckboxLists.get(i).get(j).isSelected() && gameCheckboxes.get(i).isSelected()) {
										// 导出音乐
										try {
											if (format == 0) {
												PCMSaver.save(filechooser.getSelectedFile(),
														bgmpath.path.get(i).path + "/" + Etc.getEoSDFilename(j),
														Constants.bgmdata.games.get(i),
														Constants.bgmdata.games.get(i).music.get(j), separateCheckBox.isSelected());
											} else if (format == 1) {
												PCMSaver.save(filechooser.getSelectedFile(), bgmpath.path.get(i).path,
														Constants.bgmdata.games.get(i),
														Constants.bgmdata.games.get(i).music.get(j), separateCheckBox.isSelected());
											} else if (format == 2) {
												// TODO 黄昏格式支持
											}
										} catch (FileNotFoundException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										System.out.println(Constants.bgmdata.games.get(i).music.get(j).toString());
									}
								}
							}
						}

					}
				});
				exportPanel.add(exportButton);

		for (int i = 0; i < Constants.bgmdata.games.size(); i++) {
			JPanel innerMusicPanel = new JPanel();
			List<JCheckBox> checkboxes = new ArrayList<JCheckBox>();

			innerMusicPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), //$NON-NLS-1$
					Constants.bgmdata.games.get(i).toString(), TitledBorder.CENTER, TitledBorder.TOP, null,
					new Color(0, 0, 0)));
			innerMusicPanel.setLayout(new GridLayout(0, 1, 0, 0));
			for (int j = 0; j < Constants.bgmdata.games.get(i).music.size(); j++) {
				JCheckBox chckbxMusic = new JCheckBox(Constants.bgmdata.games.get(i).music.get(j).toString()); // $NON-NLS-1$
				innerMusicPanel.add(chckbxMusic);
				checkboxes.add(chckbxMusic);
			}

			panels.add(innerMusicPanel);
			JCheckBox chckbxGame = new JCheckBox(Constants.bgmdata.games.get(i).toString()); // $NON-NLS-1$
			chckbxGame.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (chckbxGame.isSelected()) {
						musicPanel.add(innerMusicPanel);
					} else {
						musicPanel.remove(innerMusicPanel);
					}
					musicPanel.updateUI();
				}
			});
			gamePanel.add(chckbxGame);
			musicCheckboxLists.add(checkboxes);
			gameCheckboxes.add(chckbxGame);
		}
	}

	public void display() {
		frmMusicExporter.setVisible(true);
	}

}
