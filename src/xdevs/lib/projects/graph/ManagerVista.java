package xdevs.lib.projects.graph;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;
import xdevs.lib.projects.graph.models.ControladorAplicacion;
import xdevs.lib.projects.graph.models.CoupledSimulacion;
import xdevs.lib.projects.graph.structs.Camara;
import xdevs.lib.projects.graph.structs.PV3D;
import xdevs.lib.projects.graph.structs.terrain.Terreno;

public class ManagerVista extends AtomicState {
	public Port<Number> inAnimacion = new Port<>("INAnimacion");
	
	public Port<Number> outAnimador = new Port<>("OUTAnimador");

	private final String fps = "FPS";
	
	private CoupledSimulacion _simulacion;
	private Camara _camara;
	private Ventana3D _ventanaPrincipal;
	private Ventana2D _ventana2D;
	private ManagerGL3D _managerGL3D;
	private ManagerGL2D _managerGL2D;
		
	public ManagerVista(ControladorAplicacion control, Terreno terreno, CoupledSimulacion simulacion, int numeroAviones, int numeroBarcos) {
		super("ManagerVista");
	    addInPort(inAnimacion);
	    addOutPort(outAnimador);
	    addState(fps);
	    setStateValue(fps,0);
	    super.passivate();
	    PV3D eye = new PV3D (-100,100,-100,1);
	    PV3D look = new PV3D (1000.0f,1000.0f,1000.0f,1);
	    PV3D up = new PV3D (0,1,0,0);
	    _camara = new Camara(eye,look,up);
	    Dibujante dibujante = new Dibujante(terreno,simulacion,_camara);
	    _simulacion = simulacion;
	    _ventana2D = new Ventana2D(this);
		_ventana2D.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		}); 
	    _ventana2D.inicializar();
	    _ventana2D.setVisible(false);
	    _ventana2D.setVisible(true);
	    int largo = terreno.getLongitud();
	    int ancho = terreno.getAnchura();
	    _managerGL2D = new ManagerGL2D(_ventana2D,0,largo,0,ancho,dibujante);
	    _ventana2D.ponerVisual(_managerGL2D);
	    
	    _ventanaPrincipal = new Ventana3D(this, numeroAviones, numeroBarcos);
		_ventanaPrincipal.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		}); 
	    _ventanaPrincipal.inicializar();
	    _ventanaPrincipal.setVisible(false);
	    _ventanaPrincipal.setVisible(true);
	    _managerGL3D = new ManagerGL3D (this, -10,10,-10,10,1,100000,_ventanaPrincipal,dibujante,_camara);
	}
	
	public void setFPS(int FPS) {
		setStateValue(this.fps,FPS);
	}

	@Override
	public void deltext(double e) {
		Iterator iteradorInAnimacion = inAnimacion.getValues().iterator();
		if (iteradorInAnimacion.hasNext()) {
			_managerGL3D.actualizar();
			_managerGL2D.actualizar();
			setSigma(0);
		}
	}

	@Override
	public void deltint() {
		super.passivate();
	}

	@Override
	public void lambda() {
		if (getStateValue(fps).intValue()!=0) {
			outAnimador.addValue(getStateValue(fps));
			setStateValue(fps,0);
		}
	}
	
	public void rollTo(float angulo) {
		_managerGL3D.rollTo(angulo);
	}
	
	public void yawTo(float angulo) {
		_managerGL3D.yawTo(angulo);
	}
	
	public void pitchTo(float angulo) {
		_managerGL3D.pitchTo(angulo);
	}
	
	public void desplazar3D(float x, float y, float z) {
		_managerGL3D.desplazar(x,y,z);
	}
	
	public void goTo3D(float x, float y, float z) {
		_managerGL3D.goTo(x,y,z);
	}
	
	public void movimientoCamara(PV3D camara) {
		_managerGL2D.actualizar();
	}
	
	public void zoom2D(float zoom) {
		_managerGL2D.zoom(zoom);
	}
	
	public void desplazar2D(float x, float y) {
		if (x<0) _managerGL2D.desplazarProporcionadoIzquierda();
		else if (x>0) _managerGL2D.desplazarProporcionadoDerecha();
		if (y<0) _managerGL2D.desplazarProporcionadoAbajo();
		else if (y>0) _managerGL2D.desplazarProporcionadoArriba();
	}
	
	public void anclar(int tipo, int numero) {
		Seguible elemento = null;
		int tipoCamara = -1;
		switch(tipo) {
		case TreeNode.CAMARA_CENITAL_AVION: {
			tipoCamara = Camara.CENITAL;
			elemento = _simulacion.dameAviones().get(numero).getAvion();
		}; break;
		case TreeNode.CAMARA_FRONTAL_AVION: {
			tipoCamara = Camara.FRONTAL;
			elemento = _simulacion.dameAviones().get(numero).getAvion();
		}; break;
		case TreeNode.CAMARA_LATERAL_AVION: {
			tipoCamara = Camara.LATERAL;
			elemento = _simulacion.dameAviones().get(numero).getAvion();
		}; break;
		case TreeNode.CAMARA_POSTERIOR_AVION: {
			tipoCamara = Camara.POSTERIOR;
			elemento = _simulacion.dameAviones().get(numero).getAvion();
		}; break;
		case TreeNode.CAMARA_CENITAL_BARCO: {
			tipoCamara = Camara.CENITAL;
			elemento = _simulacion.dameBarcos().get(numero).getBarco();
		}; break;
		case TreeNode.CAMARA_FRONTAL_BARCO: {
			tipoCamara = Camara.FRONTAL;
			elemento = _simulacion.dameBarcos().get(numero).getBarco();
		}; break;
		case TreeNode.CAMARA_LATERAL_BARCO: {
			tipoCamara = Camara.LATERAL;
			elemento = _simulacion.dameBarcos().get(numero).getBarco();
		}; break;
		case TreeNode.CAMARA_POSTERIOR_BARCO: {
			tipoCamara = Camara.POSTERIOR;
			elemento = _simulacion.dameBarcos().get(numero).getBarco();
		}; break;
		} 
		_camara.anclar(elemento, tipoCamara);
	}
	
	public void desanclar() {
		_camara.desanclar();
	}
	
	public void setVelocidad(int velocidad) {
		_simulacion.ponVelocidad(velocidad);	
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}
}

