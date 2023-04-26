package xdevs.lib.projects.graph;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;


public class VentanaConfiguracion extends JFrame{
	private ManagerVista _manager;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentanaConfiguracion(ManagerVista manager) {
		this._manager=manager;
	}
	
	public void inicializar() {
		setSize(300,300);
		setTitle("Configuración");
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        JLabel frames = new JLabel("Frames por segundo", JLabel.CENTER);
        gridbag.setConstraints(frames, c);
        add(frames);
        c.weightx = 0.0;
		JComboBox combo = new JComboBox();
		for (int i=0;i<50;i++) {
			combo.addItem(i+1);
		}
		gridbag.setConstraints(combo, c);
		add(combo);
		setVisible(true);
	}
}
