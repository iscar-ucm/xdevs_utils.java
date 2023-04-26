package xdevs.lib.projects.graph;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class Ventana2D extends JFrame {
	private static final long serialVersionUID = 1L;
	private ManagerVista _manager;
	private ManagerGL2D _visual;
	
	
	public Ventana2D(ManagerVista manager) {
		setTitle("Interfaz de simulaci�n 2D");
		this._manager = manager;
	}
	
	public void inicializar() {
		setSize(800,600);
		configurarBarraHerramientas();
		setVisible(false);
		setVisible(true);
	}
	
	private void configurarBarraHerramientas() {
        JToolBar toolBar = new JToolBar("Herramientas");
        configurarBotonesZoom(toolBar);
        toolBar.add(new JToolBar.Separator());
        configurarBotonesDesplazamiento(toolBar);
        toolBar.setRollover(true);
        add(toolBar, BorderLayout.PAGE_START);
	}
	
	private void configurarBotonesZoom(JToolBar toolBar) {
		JButton button = crearBoton("ZoomIn","ZoomIn","Zoom In","Zoom In");
		button.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						zoomIn();
					}
				}
		);	
		toolBar.add(button);
		button = crearBoton("ZoomOut","ZoomOut","Zoom Out","Zoom Out");
		button.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						zoomOut();
					}
				}
		);	
		toolBar.add(button);
	}
	
	private void configurarBotonesDesplazamiento(JToolBar toolBar) {
		JButton button = crearBoton("Izquierda","desplazar","Desplazar a la izquierda","Izquierda");
		button.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						desplazar(-10,0);
					}
				}
		);	
		toolBar.add(button);
		button = crearBoton("Arriba","desplazar","Desplazar arriba","Arriba");
		button.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						desplazar(0,10);
					}
				}
		);	
		toolBar.add(button);
		button = crearBoton("Abajo","desplazar","Desplazar abajo","Abajo");
		button.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						desplazar(0,-10);
					}
				}
		);	
		toolBar.add(button);
		button = crearBoton("Derecha","desplazar","Desplazar a la derecha","Derecha");
		button.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						desplazar(10,0);
					}
				}
		);	
		toolBar.add(button);
	}
	
	private JButton crearBoton (String imageName, String actionCommand, String toolTipText, String altText) {		
		//Create and initialize the button.
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.setIcon(new ImageIcon("./Imagenes/"+imageName+".gif", altText));
		
		return button;
	}
	
	private void zoomIn() {
		_manager.zoom2D(0.5f);
	}
	
	private void zoomOut() {
		_manager.zoom2D(2f);
	}
	
	private void desplazar(float x, float y) {
		_manager.desplazar2D(x, y);
	}
	
	public void ponerVisual(ManagerGL2D visual) {
		this._visual = visual;
	}
}

