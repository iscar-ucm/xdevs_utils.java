package xdevs.lib.projects.graph;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ssii2007.modelo.ControladorAplicacion;

public class VentanaI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ControladorAplicacion _controlador;
	
	public VentanaI(ControladorAplicacion controlador) {
		this._controlador = controlador;
	}
	
	public void inicializar() {
		setLayout(new GridLayout(1,2));
		add(new JScrollPane(crearPanelIzquierdo()));
		add(crearPanelDerecho());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,400);
		setTitle("Configuración de la simulación");
		setVisible(true);
	}
	
	private JPanel crearPanelIzquierdo() {
		JPanel panel = new JPanel();
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(gridBag);
		//Etiqueta de misión
		c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0.0;
		JLabel labelMision = new JLabel("Selecciona la misión: ");
		gridBag.setConstraints(labelMision, c);
		panel.add(labelMision);
		//Lista de misiones
		JComboBox listaMisiones = new JComboBox();
		listaMisiones.addItem("Rescate maritimo coordinado entre avión y barco");
		gridBag.setConstraints(listaMisiones, c);
		panel.add(listaMisiones);
		//Final
		c.gridheight = GridBagConstraints.REMAINDER;
		JLabel fin = new JLabel("s");
		gridBag.setConstraints(fin, c);
		panel.add(fin);
		
		return panel;
	}
	
	private JPanel crearPanelDerecho() {
		JPanel panel = new JPanel();
		return panel;
	}
	
	public static void main(String[] args) {
		VentanaI ventana = new VentanaI(null);
		ventana.inicializar();		
	}
}
