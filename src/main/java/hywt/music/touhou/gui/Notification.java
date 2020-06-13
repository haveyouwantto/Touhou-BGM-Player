package hywt.music.touhou.gui;

import javax.swing.*;

public class Notification {
    public JFrame frame;

    public Notification(JFrame frame) {
        this.frame = frame;
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, Messages.getString("Notification.error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(frame, msg, Messages.getString("Notification.info"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
    }

    public void showWarning(String msg) {
        JOptionPane.showMessageDialog(frame, msg, Messages.getString("Notification.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
    }

}
