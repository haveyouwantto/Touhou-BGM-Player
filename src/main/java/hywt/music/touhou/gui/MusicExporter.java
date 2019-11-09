package hywt.music.touhou.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import hywt.music.touhou.savedata.BGMData;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.JCheckBox;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

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

		BGMData bgm = BGMData.read();

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

		JLabel label = new JLabel(Messages.getString("MusicExporter.loopCount.text")); //$NON-NLS-1$
		exportPanel.add(label);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 99, 1));
		exportPanel.add(spinner);

		JButton exportButton = new JButton(Messages.getString("MusicExporter.export.text")); //$NON-NLS-1$
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < gameCheckboxes.size(); i++) {
					for (int j = 0; j < musicCheckboxLists.get(i).size(); j++) {
						if (musicCheckboxLists.get(i).get(j).isSelected()) {
							//导出音乐
							System.out.println(bgm.games.get(i).music.get(j).toString());
						}
					}
				}
			}
		});
		exportPanel.add(exportButton);

		for (int i = 0; i < bgm.games.size(); i++) {
			JPanel innerMusicPanel = new JPanel();
			List<JCheckBox> checkboxes = new ArrayList<JCheckBox>();

			innerMusicPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), //$NON-NLS-1$
					bgm.games.get(i).toString(), TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
			innerMusicPanel.setLayout(new GridLayout(0, 1, 0, 0));
			for (int j = 0; j < bgm.games.get(i).music.size(); j++) {
				JCheckBox chckbxMusic = new JCheckBox(bgm.games.get(i).music.get(j).toString()); // $NON-NLS-1$
				innerMusicPanel.add(chckbxMusic);
				checkboxes.add(chckbxMusic);
			}

			panels.add(innerMusicPanel);
			JCheckBox chckbxGame = new JCheckBox(bgm.games.get(i).toString()); // $NON-NLS-1$
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
		System.out.println(musicCheckboxLists);
	}

}
