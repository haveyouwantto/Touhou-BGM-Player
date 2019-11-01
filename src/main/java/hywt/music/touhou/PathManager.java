package hywt.music.touhou;

import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Insets;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class PathManager {

	private JFrame frmBgmPathManager;
	private BGMData bgm;
	private JTextField textField;
	private JPanel panel;
	private List<JTextField> fields = new ArrayList<JTextField>();
	private List<JButton> buttons = new ArrayList<JButton>();
	private BGMPath bgmpath;
	private JButton btnSave;
	private Notification nof = new Notification(frmBgmPathManager);

	/**
	 * Create the application.
	 */
	public PathManager() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBgmPathManager = new JFrame();
		frmBgmPathManager.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(PathManager.class.getResource("/assets/hywt/music/touhou/icon.png")));
		frmBgmPathManager.setTitle(Messages.getString("PathManager.title")); //$NON-NLS-1$
		frmBgmPathManager.setBounds(100, 100, 711, 407);
		frmBgmPathManager.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		frmBgmPathManager.getContentPane().add(scrollPane, BorderLayout.CENTER);

		panel = new JPanel();
		scrollPane.setViewportView(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 270, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		btnSave = new JButton(Messages.getString("PathManager.btnSave.text")); //$NON-NLS-1$
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BGMPath b = new BGMPath();
				for (int i = 0; i < bgm.games.size(); i++) {
					DataPath d = new DataPath(bgm.games.get(i).no, fields.get(i).getText());
					b.path.add(d);
				}
				b.save();
				nof.showMessage(Messages.getString("PathManager.save_dialog")); //$NON-NLS-1$
			}
		});
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.gridwidth = 3;
		gbc_btnSave.insets = new Insets(0, 0, 0, 5);
		gbc_btnSave.gridx = 0;
		gbc_btnSave.gridy = 0;
		panel.add(btnSave, gbc_btnSave);

		bgm = InfoReader.read();

		for (int i = 0; i < bgm.games.size(); i++) {
			JLabel labelGameTitle = new JLabel(bgm.games.get(i).toString());
			labelGameTitle.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_labelGameTitle = new GridBagConstraints();
			gbc_labelGameTitle.insets = new Insets(0, 0, 0, 5);
			gbc_labelGameTitle.anchor = GridBagConstraints.EAST;
			gbc_labelGameTitle.gridx = 0;
			gbc_labelGameTitle.gridy = i + 1;
			panel.add(labelGameTitle, gbc_labelGameTitle);

			textField = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 1;
			gbc_textField.gridy = i + 1;
			panel.add(textField, gbc_textField);
			textField.setColumns(10);
			fields.add(textField);

			JButton button = new JButton(Messages.getString("PathManager.browse")); //$NON-NLS-1$
			GridBagConstraints gbc_btnNewButton1 = new GridBagConstraints();
			gbc_btnNewButton1.gridx = 2;
			gbc_btnNewButton1.gridy = i + 1;

			final int i2 = i;

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					JFileChooser filechooser = new JFileChooser("."); //$NON-NLS-1$

					String filename;

					if (i2 == 0) { // TH06
						filename = "BGM"; //$NON-NLS-1$
						filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					} else if (i2 == 2) { // TH07.5
						filename = "th075bgm.dat"; //$NON-NLS-1$
					} else if (i2 == 7) { // TH10.5
						filename = "th105a.dat"; //$NON-NLS-1$
					} else if (i2 == 10) { // TH12.3
						filename = "th123a.dat"; //$NON-NLS-1$
					} else {
						filename = "thbgm.dat"; //$NON-NLS-1$
					}

					FileNameExtensionFilter Filter = new FileNameExtensionFilter(filename, "dat"); //$NON-NLS-1$ //$NON-NLS-2$

					filechooser.setFileFilter(Filter);
					filechooser.setAcceptAllFileFilterUsed(false);

					int returnVal = filechooser.showOpenDialog(frmBgmPathManager);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						// 设置field文本
						fields.get(i2).setText(filechooser.getSelectedFile().getPath());
					}
				}
			});
			panel.add(button, gbc_btnNewButton1);
			buttons.add(button);
		}
	}

	public void display() {
		try {
			bgmpath = BGMPath.load();
			for (int i = 0; i < bgm.games.size(); i++) {
				// 将 path.json 里的路径信息显示至 textField
				String path = bgmpath.path.get(i).path;
				fields.get(i).setText(path);
			}
		} catch (Exception e) {

		}
		frmBgmPathManager.setVisible(true);
	}

}
