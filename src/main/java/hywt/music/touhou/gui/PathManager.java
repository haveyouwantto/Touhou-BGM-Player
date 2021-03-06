package hywt.music.touhou.gui;

import hywt.music.touhou.Constants;
import hywt.music.touhou.Logger;
import hywt.music.touhou.savedata.BGMPath;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.GameFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PathManager implements LazyLoader {

    private BaseFrame frmBgmPathManager;
    private final List<JTextField> fields = new ArrayList<>();
    private final List<JButton> buttons = new ArrayList<>();
    private final Notification nof = new Notification(frmBgmPathManager);

    /**
     * Create the application.
     */
    public PathManager() {

    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmBgmPathManager = new BaseFrame();
        frmBgmPathManager.setTitle(Messages.getString("PathManager.title")); //$NON-NLS-1$
        frmBgmPathManager.setBounds(100, 100, 711, 407);
        frmBgmPathManager.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane();
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setUnitIncrement(10);
        frmBgmPathManager.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        scrollPane.setViewportView(panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 270, 0, 0};
        gbl_panel.rowHeights = new int[]{0, 0};
        gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);

        JPanel panel_1 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
        flowLayout.setHgap(0);
        frmBgmPathManager.getContentPane().add(panel_1, BorderLayout.NORTH);

        JButton btnSave = new JButton(Messages.getString("PathManager.btnSave.text"));
        panel_1.add(btnSave);
        btnSave.addActionListener(e -> {
            BGMPath b = new BGMPath();
            for (int i = 0; i < Constants.bgmdata.games.size(); i++) {
                b.path.put(Constants.bgmdata.games.get(i).no, fields.get(i).getText());
            }
            b.save();
            nof.showMessage(Messages.getString("PathManager.save_dialog")); //$NON-NLS-1$
        });

        for (int i = 0; i < Constants.bgmdata.games.size(); i++) {
            JLabel labelGameTitle = new JLabel(Constants.bgmdata.games.get(i).toString());
            labelGameTitle.setHorizontalAlignment(SwingConstants.LEFT);
            GridBagConstraints gbc_labelGameTitle = new GridBagConstraints();
            gbc_labelGameTitle.insets = new Insets(0, 0, 0, 5);
            gbc_labelGameTitle.anchor = GridBagConstraints.WEST;
            gbc_labelGameTitle.gridx = 0;
            gbc_labelGameTitle.gridy = i + 1;
            panel.add(labelGameTitle, gbc_labelGameTitle);

            JTextField textField = new JTextField();
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

            button.addActionListener(e -> {

                JFileChooser filechooser = new JFileChooser("."); //$NON-NLS-1$

                Game g = Constants.bgmdata.games.get(i2);
                String[] names = g.fileName.split("\\.");

                if (g.format == GameFormat.BGM_FOLDER || g.format == GameFormat.WAVE_FILE) { // TH06
                    filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                }

                FileNameExtensionFilter Filter = new FileNameExtensionFilter(g.fileName, names[names.length - 1]); //$NON-NLS-1$

                filechooser.setFileFilter(Filter);
                filechooser.setAcceptAllFileFilterUsed(false);

                int returnVal = filechooser.showOpenDialog(frmBgmPathManager);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    // 将选择的文件路径显示至textField
                    fields.get(i2).setText(filechooser.getSelectedFile().getPath());
                }
            });
            panel.add(button, gbc_btnNewButton1);
            buttons.add(button);
        }
    }

    public void display() {
    }

    @Override
    public void load() {
        initialize();
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            try {
                BGMPath bgmpath = BGMPath.load();
                for (int i = 0; i < Constants.bgmdata.games.size(); i++) {
                    // 将 path.json 里的路径信息显示至 textField
                    String path = bgmpath.path.get(Constants.bgmdata.order.get(i));
                    fields.get(i).setText(path);
                }
            } catch (Exception e) {
                Logger.error(e);
            }
        }
        frmBgmPathManager.setVisible(b);
    }
}
