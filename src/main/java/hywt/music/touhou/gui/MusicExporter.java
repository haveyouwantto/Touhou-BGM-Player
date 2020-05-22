package hywt.music.touhou.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import hywt.music.touhou.Constants;
import hywt.music.touhou.StringFormatter;
import hywt.music.touhou.pcmprocessing.PCMSaver;
import hywt.music.touhou.savedata.BGMPath;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.GameFormat;
import hywt.music.touhou.savedata.Music;

public class MusicExporter {

	private final JFrame frmMusicExporter;
	private final List<List<JCheckBox>> musicCheckboxLists = new ArrayList<>();
	private final List<JCheckBox> gameCheckboxes = new ArrayList<>();
	private final List<JPanel> panels = new ArrayList<>();

	/**
	 * Create the frame.
	 */
	public MusicExporter() {
		frmMusicExporter = new BaseFrame();
		frmMusicExporter.setTitle(Messages.getString("MusicExporter.title")); //$NON-NLS-1$
		frmMusicExporter.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		frmMusicExporter.setBounds(100, 100, 801, 452);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frmMusicExporter.setContentPane(contentPane);

		JPanel selectionPanel = new JPanel();
		contentPane.add(selectionPanel, BorderLayout.CENTER);
		selectionPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JScrollPane gameScrollPane = new JScrollPane();
		JScrollBar gameScrollBar = gameScrollPane.getVerticalScrollBar();
		gameScrollBar.setUnitIncrement(10);
		selectionPanel.add(gameScrollPane);

		JPanel gamePanel = new JPanel();
		gameScrollPane.setViewportView(gamePanel);
		gamePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), //$NON-NLS-1$
				Messages.getString("MusicExporter.selectGame"), TitledBorder.CENTER, TitledBorder.TOP, null, //$NON-NLS-1$
				new Color(0, 0, 0)));

		gamePanel.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane musicScrollPane = new JScrollPane();
		JScrollBar musicScrollBar = musicScrollPane.getVerticalScrollBar();
		musicScrollBar.setUnitIncrement(10);
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
		selectAllButton.addActionListener(e -> {
			for (int i = 0; i < gameCheckboxes.size(); i++) {
				gameCheckboxes.get(i).setSelected(true);
				musicPanel.add(panels.get(i));
				for (int j = 0; j < musicCheckboxLists.get(i).size(); j++) {
					musicCheckboxLists.get(i).get(j).setSelected(true);
				}
			}
			musicPanel.updateUI();
		});
		exportPanel.add(selectAllButton);

		JButton deselectAllButton = new JButton(Messages.getString("MusicExporter.deselectAll.text")); //$NON-NLS-1$
		deselectAllButton.addActionListener(e -> {
			for (int i = 0; i < gameCheckboxes.size(); i++) {
				gameCheckboxes.get(i).setSelected(false);
				musicPanel.remove(panels.get(i));
				for (int j = 0; j < musicCheckboxLists.get(i).size(); j++) {
					musicCheckboxLists.get(i).get(j).setSelected(false);
				}
			}
			musicPanel.updateUI();
		});
		exportPanel.add(deselectAllButton);

		JCheckBox separateCheckBox = new JCheckBox(Messages.getString("MusicExporter.checkBox.text")); //$NON-NLS-1$
		exportPanel.add(separateCheckBox);

		JButton exportButton = new JButton(Messages.getString("MusicExporter.export.text")); //$NON-NLS-1$
		exportButton.addActionListener(e -> {

			JFileChooser filechooser = new JFileChooser(".");
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = filechooser.showSaveDialog(frmMusicExporter);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				BGMPath bgmpath = BGMPath.load();
				for (int i = 0; i < gameCheckboxes.size(); i++) {
					GameFormat format = Constants.bgmdata.games.get(i).format;
					for (int j = 0; j < musicCheckboxLists.get(i).size(); j++) {
						JCheckBox gc = gameCheckboxes.get(i);
						JCheckBox mc = musicCheckboxLists.get(i).get(j);

						if (mc.isSelected() && gc.isSelected()) {
							// 导出音乐
							Game g = Constants.bgmdata.games.get(i);
							Music m = Constants.bgmdata.games.get(i).music.get(j);
							boolean separate = separateCheckBox.isSelected();
							File path = filechooser.getSelectedFile();
							assert bgmpath != null;
							String source = bgmpath.path.get(Constants.bgmdata.order.get(i));
							try {
								if (format == GameFormat.BGM_FOLDER) {
									PCMSaver.save(path, source + "/" + StringFormatter.formatFileName(g.metadata, j), g, m, separate);
								} else{
									PCMSaver.save(path, source, g, m, separate);
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

		});
		exportPanel.add(exportButton);

		for (int i = 0; i < Constants.bgmdata.games.size(); i++) {
			JPanel innerMusicPanel = new JPanel();
			List<JCheckBox> checkboxes = new ArrayList<>();
			Game g = Constants.bgmdata.games.get(i);

			innerMusicPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), //$NON-NLS-1$
					g.toString(), TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
			innerMusicPanel.setLayout(new GridLayout(0, 1, 0, 0));
			JPanel fastSelect = new JPanel();
			fastSelect.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
			JButton btn = new JButton(Messages.getString("MusicExporter.selectAll")); //$NON-NLS-1$
			JButton btn2 = new JButton(Messages.getString("MusicExporter.deselectAll")); //$NON-NLS-1$

			btn.addActionListener(e -> {
				List<JCheckBox> jc = musicCheckboxLists.get(Constants.bgmdata.games.indexOf(g));
				for (JCheckBox jCheckBox : jc) {
					jCheckBox.setSelected(true);
				}
			});
			btn2.addActionListener(e -> {
				List<JCheckBox> jc = musicCheckboxLists.get(Constants.bgmdata.games.indexOf(g));
				for (JCheckBox jCheckBox : jc) {
					jCheckBox.setSelected(false);
				}
			});
			fastSelect.add(btn);
			fastSelect.add(btn2);
			innerMusicPanel.add(fastSelect);
			for (int j = 0; j < Constants.bgmdata.games.get(i).music.size(); j++) {
				JCheckBox chckbxMusic = new JCheckBox(Constants.bgmdata.games.get(i).music.get(j).toString()); // $NON-NLS-1$
				innerMusicPanel.add(chckbxMusic);
				checkboxes.add(chckbxMusic);
			}

			panels.add(innerMusicPanel);
			JCheckBox chckbxGame = new JCheckBox(Constants.bgmdata.games.get(i).toString()); // $NON-NLS-1$
			chckbxGame.addActionListener(e -> {
				if (chckbxGame.isSelected()) {
					musicPanel.add(innerMusicPanel);
				} else {
					musicPanel.remove(innerMusicPanel);
				}
				musicPanel.updateUI();
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
