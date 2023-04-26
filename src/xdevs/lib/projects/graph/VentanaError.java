package xdevs.lib.projects.graph;

import java.awt.Component;
import javax.swing.JOptionPane;

public class VentanaError {
	public VentanaError (Component parent, String error) {
		JOptionPane.showMessageDialog(parent, error, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
