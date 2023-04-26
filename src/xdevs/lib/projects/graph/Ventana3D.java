package xdevs.lib.projects.graph;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class Ventana3D extends JFrame implements TreeExpansionListener,TreeSelectionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ManagerVista _manager;
	private JSlider _sliderRoll;
	private JSlider _sliderYaw;
	private JSlider _sliderPitch;
	private JSlider _sliderVelocidad;
	private JTextField _anguloRoll;
	private JTextField _anguloYaw;
	private JTextField _anguloPitch;
	private JTextField _desplazamientoX;
	private JTextField _desplazamientoY;
	private JTextField _desplazamientoZ;
	private JTextField _goToX;
	private JTextField _goToY;
	private JTextField _goToZ;
	private JTextField _factorZoom;
	private JButton _aceptarRoll;
	private JButton _aceptarYaw;
	private JButton _aceptarPitch;
	private JButton _aceptarX;
	private JButton _aceptarY;
	private JButton _aceptarZ;
	private JButton _aceptarGoTo;
	private JButton _aceptarZoom;
	private JTree _tree;
	private int _numAviones;
	private int _numBarcos;
	private JLabel _velocidad;
	
	public Ventana3D (ManagerVista manager, int numAviones, int numBarcos) {
		this._manager = manager;
		this._numAviones=numAviones;
		this._numBarcos=numBarcos;
		setTitle("Interfaz de simulaci�n 3D");
		setLayout(new BorderLayout());
	}
	
	public void inicializar() {
		setSize(800,600);
		setLayout(new BorderLayout());
		configurarMenu();
		configurarPanelSur();
		configurarPanelEste();
		setVisible(true);
	}
	
	private void configurarMenu() {
		JMenuBar barraMenu = new JMenuBar();
		JMenuItem configuracion = new JMenuItem("Configuraci�n");
		barraMenu.add(configuracion);
		this.setJMenuBar(barraMenu);
		establecerOyenteConfiguracion(configuracion);
	}
	
	private void establecerOyenteConfiguracion(AbstractButton configuracion) {
		configuracion.addActionListener(
			new ActionListener () {
				public void actionPerformed(ActionEvent arg0) {
					VentanaConfiguracion ventana = new VentanaConfiguracion(_manager);
					ventana.inicializar();
				}
			}
		);
	}
	
	private void configurarPanelSur() {
	/*	JPanel panelSur = new JPanel(new GridLayout(1,3));
		panelSur.add(configurarPanel1());
		panelSur.add(configurarPanel2());
		panelSur.add(configurarPanel3());
		this.add(panelSur, BorderLayout.SOUTH); */
		_sliderVelocidad = new JSlider(1,100);
		establecerOyenteSliderVelocidad();
		JPanel panelSur = new JPanel();
		panelSur.add(new JLabel("Velocidad de simulaci�n"));
		panelSur.add(_sliderVelocidad);
		_velocidad = new JLabel();
		_sliderVelocidad.setValue(1);
		panelSur.add(_velocidad);
		this.add(panelSur, BorderLayout.SOUTH);
	}
	
	private void establecerOyenteSliderVelocidad() {
		_sliderVelocidad.addChangeListener(
				new ChangeListener () {
					public void stateChanged(ChangeEvent evento) {
						cambioSliderVelocidad();
					}
				}
		);
	}
	
	private void cambioSliderVelocidad() {
		int velocidad = _sliderVelocidad.getValue();
		_velocidad.setText(((Integer)velocidad).toString());
		_manager.setVelocidad(velocidad);
	}
	
	private JPanel configurarPanel1() {
		GridBagLayout gridbag = new GridBagLayout();
		JPanel panel1 = new JPanel(gridbag);
		_aceptarRoll = new JButton("Introducir");
		_anguloRoll = new JTextField("0.0", 4);
		_sliderRoll = new JSlider(-1800,1800);
		configurarSlider(panel1,gridbag, "Cámara: Roll", _anguloRoll, _sliderRoll, _aceptarRoll);
		establecerOyenteAceptarRoll();
		establecerOyenteSliderRoll();
		_aceptarYaw = new JButton("Introducir");
		_anguloYaw = new JTextField("0.0", 4);
		_sliderYaw = new JSlider(-1800,1800);
		configurarSlider(panel1,gridbag, "Cámara: Yaw", _anguloYaw, _sliderYaw, _aceptarYaw);
		establecerOyenteAceptarYaw();
		establecerOyenteSliderYaw();
		_aceptarPitch = new JButton("Introducir");
		_anguloPitch = new JTextField("0.0", 4);
		_sliderPitch = new JSlider(-1800,1800);
		configurarSlider(panel1,gridbag, "Cámara: Pitch", _anguloPitch, _sliderPitch, _aceptarPitch);
		establecerOyenteAceptarPitch();
		establecerOyenteSliderPitch();
		return panel1;
	}

	private void configurarSlider(JPanel panel, GridBagLayout gridbag, String text, JTextField angulo, JSlider slider, JButton aceptar) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        JLabel etiqueta = new JLabel(text, JLabel.CENTER);
        gridbag.setConstraints(etiqueta, c);
        panel.add(etiqueta);
        c.weightx = 0.0;
		angulo.setHorizontalAlignment(JTextField.RIGHT);
		gridbag.setConstraints(angulo, c);
		panel.add(angulo);
		c.weightx = 0.0;
		JLabel aux = new JLabel("  ");
		gridbag.setConstraints(aux, c);
		panel.add(aux);
		c.weightx = 0.0;		
		gridbag.setConstraints(aceptar, c);
		panel.add(aceptar);
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		aux = new JLabel("   ");
		gridbag.setConstraints(aux, c);
		panel.add(aux);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(slider, c);
		panel.add(slider);		
	}
	
	private void establecerOyenteSliderRoll() {
		_sliderRoll.addChangeListener(
				new ChangeListener () {
					public void stateChanged(ChangeEvent evento) {
						cambioSliderRoll();
					}
				}
		);
	}
	
	private void cambioSliderRoll() {
		float angulo = (float)(_sliderRoll.getValue())/10f;
		_anguloRoll.setText(""+angulo);
		_manager.rollTo(angulo);
	}
	
	private void establecerOyenteAceptarRoll() {
		_aceptarRoll.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						aceptarRoll();
					}
				}
		);	
	}
	
	private void aceptarRoll() {
		try {
			float angulo = Float.parseFloat(_anguloRoll.getText());
			if ((angulo<=180)&&(angulo>=-180)) {
				_sliderRoll.setValue(((Float)(angulo*10)).intValue());
				_manager.rollTo(angulo);
			}
		}
		catch (Exception e) {
			float anguloAux = (float)(_sliderRoll.getValue())/10f;
			_anguloRoll.setText(""+anguloAux);			
		}
	}
	
	private void establecerOyenteSliderYaw() {
		_sliderYaw.addChangeListener(
				new ChangeListener () {
					public void stateChanged(ChangeEvent evento) {
						cambioSliderYaw();
					}
				}
		);
	}
	
	private void cambioSliderYaw() {
		float angulo = (float)(_sliderYaw.getValue())/10f;
		_anguloYaw.setText(""+angulo);
		_manager.yawTo(angulo);
	}
	
	private void establecerOyenteAceptarYaw() {
		_aceptarYaw.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						aceptarYaw();
					}
				}
		);	
	}
	
	private void aceptarYaw() {
		try {
			float angulo = Float.parseFloat(_anguloYaw.getText());
			if ((angulo<=180)&&(angulo>=-180)) {
				_sliderYaw.setValue(((Float)(angulo*10)).intValue());
				_manager.yawTo(angulo);
			}
		}
		catch (Exception e) {
			float anguloAux = (float)(_sliderYaw.getValue())/10f;
			_anguloYaw.setText(""+anguloAux);			
		}
	}
	
	private void establecerOyenteSliderPitch() {
		_sliderPitch.addChangeListener(
				new ChangeListener () {
					public void stateChanged(ChangeEvent evento) {
						cambioSliderPitch();
					}
				}
		);
	}
	
	private void cambioSliderPitch() {
		float angulo = (float)(_sliderPitch.getValue())/10f;
		_anguloPitch.setText(""+angulo);
		_manager.pitchTo(angulo);
	}
	
	private void establecerOyenteAceptarPitch() {
		_aceptarPitch.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						aceptarPitch();
					}
				}
		);	
	}
	
	private void aceptarPitch() {
		try {
			float angulo = Float.parseFloat(_anguloPitch.getText());
			if ((angulo<=180)&&(angulo>=-180)) {
				_sliderPitch.setValue(((Float)(angulo*10)).intValue());
				_manager.pitchTo(angulo);
			}
		}
		catch (Exception e) {
			float anguloAux = (float)(_sliderPitch.getValue())/10f;
			_anguloPitch.setText(""+anguloAux);			
		}
	}
	
	
	private JPanel configurarPanel2() {
		JPanel panel2 = new JPanel(new GridLayout(4,1));
		_aceptarX = new JButton("Introducir");
		_desplazamientoX = new JTextField("0.0", 4);
		JPanel panel21 = new JPanel();
		configurarSolicitud(panel21, "C�mara: Desp. eje X", _desplazamientoX, _aceptarX);
		establecerOyenteAceptarX();
		_aceptarY = new JButton("Introducir");
		_desplazamientoY = new JTextField("0.0", 4);
		JPanel panel22 = new JPanel();
		configurarSolicitud(panel22, "C�mara: Desp. eje Y", _desplazamientoY, _aceptarY);
		establecerOyenteAceptarY();
		_aceptarZ = new JButton("Introducir");
		_desplazamientoZ = new JTextField("0.0", 4);
		JPanel panel23 = new JPanel();
		configurarSolicitud(panel23, "C�mara: Desp. eje Z", _desplazamientoZ, _aceptarZ);
		establecerOyenteAceptarZ();
		_aceptarZoom = new JButton("Introducir");
		_factorZoom = new JTextField("0.0",4);
		JPanel panel24 = new JPanel();
		configurarSolicitud(panel24, "C�mara: Zoom (x)", _factorZoom, _aceptarZoom);
		panel2.add(panel21);
		panel2.add(panel22);
		panel2.add(panel23);
		panel2.add(panel24);
		return panel2;
	}
	
	private void establecerOyenteAceptarX() {
		_aceptarX.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						aceptarX();
					}
				}
		);			
	}
	
	private void aceptarX() {
		try {
			float desp = Float.parseFloat(_desplazamientoX.getText());
			_manager.desplazar3D(desp,0,0);
		}
		catch (Exception e) {
			_desplazamientoX.setText("0.0");
		}		
	}
	
	private void establecerOyenteAceptarY() {
		_aceptarY.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						aceptarY();
					}
				}
		);				
	}
	
	private void aceptarY() {
		try {
			float desp = Float.parseFloat(_desplazamientoY.getText());
			_manager.desplazar3D(0,desp,0);
		}
		catch (Exception e) {
			_desplazamientoY.setText("0.0");
		}		
	}
	
	private void establecerOyenteAceptarZ() {
		_aceptarZ.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						aceptarZ();
					}
				}
		);				
	}
	
	private void aceptarZ() {
		try {
			float desp = Float.parseFloat(_desplazamientoZ.getText());
			_manager.desplazar3D(0,0,desp);
		}
		catch (Exception e) {
			_desplazamientoZ.setText("0.0");
		}		
	}
	
	private void configurarSolicitud(JPanel panel, String text, JTextField desplazamiento, JButton aceptar) {
		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        JLabel etiqueta = new JLabel(text, JLabel.CENTER);
        gridbag.setConstraints(etiqueta, c);
        panel.add(etiqueta);
        c.weightx = 0.0;
		desplazamiento.setHorizontalAlignment(JTextField.RIGHT);
		gridbag.setConstraints(desplazamiento, c);
		panel.add(desplazamiento);
		c.weightx = 0.0;
		JLabel aux = new JLabel(" ");
		gridbag.setConstraints(aux, c);
		panel.add(aux);
		c.weightx = 0.0;	
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(aceptar, c);
		panel.add(aceptar);
	}
	
	private void establecerOyenteAceptarGoTo() {
		_aceptarGoTo.addActionListener(
				new ActionListener () {
					public void actionPerformed (ActionEvent evento) {
						goTo();
					}
				}
		);			
	}
	
	private void goTo() {
		try {
			float x = (Float.parseFloat(_goToX.getText()));
			float y = (Float.parseFloat(_goToY.getText()));
			float z = (Float.parseFloat(_goToZ.getText()));
			_manager.goTo3D(x,y,z);
		}
		catch (Exception e) {
			_goToX.setText("0.0");
			_goToY.setText("0.0");
			_goToZ.setText("0.0");
		}			
	}	
	
	private void configurarGoTo(JPanel panel, String text, JTextField x, JTextField y, JTextField z, JButton aceptar) {
		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        JLabel etiqueta = new JLabel(text, JLabel.CENTER);
        gridbag.setConstraints(etiqueta, c);
        panel.add(etiqueta);
        c.weightx = 0.0;
		JLabel aux = new JLabel(" X:", JLabel.RIGHT);
		gridbag.setConstraints(aux, c);
		panel.add(aux);
        c.weightx = 0.0;
		x.setHorizontalAlignment(JTextField.RIGHT);
		gridbag.setConstraints(x, c);
		panel.add(x);
		c.weightx = 0.0;
		aux = new JLabel(" Y:", JLabel.RIGHT);
		gridbag.setConstraints(aux, c);
		panel.add(aux);
		c.weightx = 0.0;	
		y.setHorizontalAlignment(JTextField.RIGHT);
		gridbag.setConstraints(y, c);
		panel.add(y);
		c.weightx = 0.0;
		aux = new JLabel(" Z:", JLabel.RIGHT);
		gridbag.setConstraints(aux, c);
		panel.add(aux);
		c.weightx = 0.0;
		z.setHorizontalAlignment(JTextField.RIGHT);
		gridbag.setConstraints(z, c);
		panel.add(z);
		c.weightx = 0.0;
		aux = new JLabel("  ");
		gridbag.setConstraints(aux, c);
		panel.add(aux);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(aceptar, c);
		panel.add(aceptar);
	}
	
	private JPanel configurarPanel3() {
		return (new JPanel());
	}
	
	private void configurarPanelEste() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(new TreeNode(TreeNode.ELEMENTOS,-1));
		crearNodos(top);
	    _tree = new JTree(top);
	    _tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    _tree.addTreeExpansionListener(this);
	    _tree.addTreeSelectionListener(this);
	    _tree.setCellRenderer(new TreeRender());
	    JScrollPane treeView = new JScrollPane(_tree);
		add(treeView,BorderLayout.EAST);
	}
	
	private void crearNodos(DefaultMutableTreeNode top) {
	    DefaultMutableTreeNode aviones = null;
	    DefaultMutableTreeNode barcos = null;  
	    aviones = new DefaultMutableTreeNode(new TreeNode(TreeNode.AVIONES,-1));
	    top.add(aviones);
	    DefaultMutableTreeNode elemento;
	    for (int i=0; i<_numAviones; i++) {
	    	elemento = new DefaultMutableTreeNode(new TreeNode(TreeNode.AVION,i));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.CAMARA_CENITAL_AVION,i)));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.CAMARA_FRONTAL_AVION,i)));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.CAMARA_LATERAL_AVION,i)));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.CAMARA_POSTERIOR_AVION,i)));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.OPERACIONES_AVION,i)));
	    	aviones.add(elemento);
	    	
	    }
	    barcos = new DefaultMutableTreeNode(new TreeNode(TreeNode.BARCOS,-1));
	    top.add(barcos);
	    for (int i=0; i<_numAviones; i++) {
	    	elemento = new DefaultMutableTreeNode(new TreeNode(TreeNode.BARCO,i));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.CAMARA_CENITAL_BARCO,i)));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.CAMARA_FRONTAL_BARCO,i)));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.CAMARA_LATERAL_BARCO,i)));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.CAMARA_POSTERIOR_BARCO,i)));
	    	elemento.add(new DefaultMutableTreeNode(new TreeNode(TreeNode.OPERACIONES_BARCO,i)));
	    	barcos.add(elemento);
	    }
	}

	public void valueChanged(TreeSelectionEvent arg0) {
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        _tree.getLastSelectedPathComponent();

	    if (node == null)
			// Nothing is selected.
			return;
	
		TreeNode nodeInfo = (TreeNode) node.getUserObject();
		if (node.isLeaf()) {
			_manager.anclar(nodeInfo.getTipo(),nodeInfo.getNumero());
		}
		else {
			_manager.desanclar();
		}
	}

	public void treeCollapsed(TreeExpansionEvent arg0) {
		this.setVisible(true);
	}

	public void treeExpanded(TreeExpansionEvent arg0) {
		this.setVisible(true);
	}
}
