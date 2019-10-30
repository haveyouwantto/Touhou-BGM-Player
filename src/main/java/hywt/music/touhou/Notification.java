package hywt.music.touhou;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Notification {
	public JFrame frame;

	public Notification(JFrame frame) {
		this.frame = frame;
	}

	public void showError(String msg) {
		JOptionPane.showMessageDialog(frame, msg, "Error", 0);
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(frame, msg, "Info", 1);
	}

	public void showWarning(String msg) {
		JOptionPane.showMessageDialog(frame, msg, "Warning", 2);
	}

}
