package xdevs.lib.projects.graph;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ssii2007.modelo.ControladorAplicacion;


public class VentanaInicial extends JFrame {

	private ControladorAplicacion _controlador;
	
	private JTextField _numeroAviones;
	private JTextField _numeroBarcos;
	private JTextField _numeroNaufragos;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentanaInicial(ControladorAplicacion controlador) {
		this._controlador=controlador;
	}
	
	public void inicializar() {
		setSize(300,300);
		setLayout(new GridLayout(5,1,10,10));
		_numeroAviones = new JTextField("1",3);
		_numeroBarcos = new JTextField("1",3);
		_numeroNaufragos = new JTextField("100",3);
		setTitle("Configuración de la simulación");
		configurarPeticion(_numeroAviones, "Número de aviones");
		configurarPeticion(_numeroBarcos, "Número de barcos");
		configurarPeticion(_numeroNaufragos,"Número de náufragos");
		add(new JPanel());
		configurarBotones();
	}
	
	private void configurarPeticion(JTextField campoTexto, String texto) {
		GridLayout grid = new GridLayout(1,1);
		JPanel panelAviones = new JPanel(grid);
		GridBagLayout gridbag = new GridBagLayout();
		JPanel panelInterior = new JPanel(gridbag);
		panelAviones.add(panelInterior);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        JLabel etiqueta = new JLabel(texto, JLabel.CENTER);
        gridbag.setConstraints(etiqueta, c);
        panelInterior.add(etiqueta);
        c.weightx = 0.0;
		campoTexto.setHorizontalAlignment(JTextField.RIGHT);
		gridbag.setConstraints(campoTexto, c);
		panelInterior.add(campoTexto);
		add(panelAviones);
	}
	
	private void configurarBotones() {
		JPanel panelBotones = new JPanel(new GridLayout(1,4,10,10));
		panelBotones.add(new JLabel());
		JButton botonAceptar = new JButton("Aceptar");
		establecerOyenteAceptar(botonAceptar);
		panelBotones.add(botonAceptar);
		JButton botonSalir = new JButton("Salir");
		establecerOyenteSalir(botonSalir);
		panelBotones.add(botonSalir);
		panelBotones.add(new JLabel());
		add(panelBotones);
	}
	
	private void establecerOyenteAceptar(AbstractButton aceptar) {
		aceptar.addActionListener(
			new ActionListener () {
				public void actionPerformed(ActionEvent arg0) {
					int aviones=0;
					int barcos=0;
					int naufragos=0;
					try {
						aviones = Integer.parseInt(_numeroAviones.getText());
						barcos = Integer.parseInt(_numeroBarcos.getText());
						naufragos = Integer.parseInt(_numeroNaufragos.getText());
						//orden -- aviones,barcos,naufragos,tiempo,escribirFicheo,XmlOSerializable,tiempoge,velocidad avion,
						//posicionnavion,posicioneavion,distanciaavion,posicionnbarco,posicionebarco,distanciabarco, rescatanaviones
						_controlador.inicializar(aviones,barcos,naufragos,3600*7,true,false,2000,100,100000,100000,100000,100000,100000,10000,false);
						cerrar(); 
					}
					catch (Exception e) {
						e.printStackTrace();
						error();
					}
				}
			}
		);
	}
	
	private void cerrar() {
		this.dispose();
	}
	
	private void error() {
		JOptionPane.showMessageDialog(this, "Error en los campos", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void establecerOyenteSalir(AbstractButton salir) {
		salir.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				}
		);
	}
}
