package hywt.music.touhou.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hywt.music.touhou.gui.Messages;

public class Notification {
	public JFrame frame;

	public Notification(JFrame frame) {
		this.frame = frame;
	}

	public void showError(String msg) {
		JOptionPane.showMessageDialog(frame, msg, Messages.getString("Notification.error"), 0); //$NON-NLS-1$
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(frame, msg, Messages.getString("Notification.info"), 1); //$NON-NLS-1$
	}

	public void showWarning(String msg) {
		JOptionPane.showMessageDialog(frame, msg, Messages.getString("Notification.warning"), 2); //$NON-NLS-1$
	}

}
