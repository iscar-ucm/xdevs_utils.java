package xdevs.lib.projects.barcos;

import java.util.Iterator;
import java.util.Vector;

import com.jogamp.opengl.GL2;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;
import xdevs.core.util.Constants;
import xdevs.lib.projects.graph.Dibujable;
import xdevs.lib.projects.graph.Dibujante;
import xdevs.lib.projects.graph.ModeloNaufrago;
import xdevs.lib.projects.graph.structs.terrain.Terreno;
import xdevs.lib.projects.math.IFuncion;
import xdevs.lib.projects.math.IIntegrador;
import xdevs.lib.projects.math.RungeKutta;

public class Naufrago extends AtomicState implements IFuncion, Dibujable {

	public static final int ON	=	1;
	public static final int OFF	=	2;
	/**
	 * Constante para el puerto de entrada de las solicitudes
	 */
	public Port<Vector<Number>> inSolicitud = new Port<>("INsolicitud");
	
	/**
	 * Constante para el puerto de salida
	 */
	public Port<Vector<Number>> out = new Port<>("OUT");
	
	/** Variable que contiene el tiempo actual*/
	public static final String tactual = "tactual";
	
	/** Variable que contiene el tiempo actual*/
	public static final String n = "n";
	
	/** Variable que contiene el tiempo actual*/
	public static final String e = "e";
	
	public static final String tiempo ="tiempo";
	
	public static final String tinicial = "tinicial";
	
	public static final String Phase = "phase";
	
	
	/**
	 * Constante para la solicitud de creacion de naufrago
	 */
	public static final int Crea = 1;
	
	public static final int Encontrar = 2;
	
	private IIntegrador integrador;
	private Corrientes corriente;
	private Mar mar;
	private Terreno _terreno;
	//private GestorAplicacion gestor;
	
	private ModeloNaufrago _modeloNaufrago;
	
	
	public Naufrago(String name, int tiempoInicial, Terreno terreno){
		super(name);
		this.addInPort(inSolicitud);
		this.addOutPort(out);
		this.addState(n);
		this.addState(e);
		this.addState(tactual);
		this.addState(tiempo);
		this.addState(tinicial);
		this.addState("phase");
		this.setStateValue(tinicial, tiempoInicial);
		this.setStateValue(tiempo, 0);
		this.setStateValue("phase", Naufrago.ON);
		corriente=new Corrientes(0,1);
		mar=new Mar();
		integrador=new RungeKutta();
		this._terreno = terreno;
	}
	
	
	@Override
	public void initialize() {
		super.passivate();
	}

	@Override
	public void exit() {
	}

	@Override
	public void deltext(double eps) {
		super.resume(eps);
		if(this.getStateValue("phase").intValue()==Naufrago.ON){
		Iterator<Vector<Number>> iteradorSolicitud1 = inSolicitud.getValues().iterator();
		while (iteradorSolicitud1.hasNext()) {
			Vector<Number> solicitud = iteradorSolicitud1.next();
			if(((Integer)(solicitud.get(0)))==Naufrago.Crea){
				this.setStateValue(n, ((Double)(solicitud.get(1))));
				this.setStateValue(e, ((Double)(solicitud.get(2))));
			}
			else{
				if((Integer)(solicitud.get(0))==Naufrago.Encontrar){
					if((Integer)(solicitud.get(1))==Integer.parseInt(this.getName())){
						System.out.println("naufrago rescatado");
						this.setStateValue(e, -10000000);
						this.setStateValue(n, -10000000);
						this.setStateValue("phase",Naufrago.OFF);
						this.setStateValue(tiempo, Constants.INFINITY);
//						this.setStateValue("phase", Naufrago.OFF);

					}
				}
			}
		}
		}
			this.setSigma(this.getStateValue(tiempo).doubleValue());
		
	}

	@Override
	public void deltint() {
		if(this.getStateValue("phase").intValue()==Naufrago.ON){
		mar.avanzaTiempo();
		corriente.ponOleaje(mar.dameSalidaActual());
		
		this.avanzaTiempo();
		int tiempoIni =this.getStateValue(tinicial).intValue();
		
		if(this.getStateValue(tactual).doubleValue()<=tiempoIni){
			//this.setStateValue(e, this.getStateValue(e).doubleValue()+corriente.dameUc());
			//this.setStateValue(n, this.getStateValue(n).doubleValue()+corriente.dameVc());
			//System.out.println("n"+this.getName()+"  "+this.getStateValue(e).doubleValue()+"  "+this.getStateValue(n).doubleValue());

			//this.setStateValue(tactual, this.getStateValue(tactual).doubleValue()+1);	
		
		}

		else if(this.getStateValue(tactual).doubleValue()>tiempoIni){
			//this.setStateValue(e, this.getStateValue(e).doubleValue()+corriente.dameUc());
			//this.setStateValue(n, this.getStateValue(n).doubleValue()+corriente.dameVc());
			//gestor.a�adeNuevaPosicion(100+Integer.parseInt(this.getName()), this.getStateValue(e).doubleValue(),
			//		this.getStateValue(n).doubleValue(), 0.0);					
				//	System.out.println("tiempo "+this.getStateValue(tactual).doubleValue());
			//this.setStateValue(tactual, this.getStateValue(tactual).doubleValue()+1);		
			this.setStateValue(tiempo, 30);
		}

		this.setSigma(this.getStateValue(tiempo).doubleValue());
		}
	}

	@Override
	public void lambda() {
		if(this.getStateValue("phase").intValue()==Naufrago.ON){
			Vector<Number> todo=new Vector<Number>();
			todo.add((Double)(this.getStateValue(n).doubleValue()));
			todo.add((Double)(this.getStateValue(e).doubleValue()));
			todo.add((Double)(this.getStateValue(tactual).doubleValue()));
			todo.add((Integer)(Integer.parseInt(this.getName())));
			out.addValue(todo);	
		}
	}
	//Las incidencias las va a crear el controlador
	//El control sobre la posicion del naufrago la tiene
	//por tanto el controlador
	public void actualizaEstados(double[] estadosActuales) {
		this.setStateValue(e, estadosActuales[0]);
		this.setStateValue(n, estadosActuales[1]);
		
	}
	
	public void avanzaTiempo() {
		double dtiempo=30;
		corriente.avanzaTiempo(dtiempo);
		this.actualizaEstados(integrador.integra(this, dtiempo,this.getStateValue("tactual").doubleValue()));
		//tercero actualizamos el tiempo
		this.setStateValue(tactual, this.getStateValue(tactual).doubleValue()+dtiempo);
	}
	public double[] dameControlActual() {
		return null;
	}
	public double[] dameDerivadas(double tiempo, double[] estados,
			double[] control) {
		int num_estados = 2;
		double[] derivadas= new double[num_estados];
		//primera derivada x
		derivadas[0]=corriente.dameUc();
		//segunda derivada y
		derivadas[1]=corriente.dameVc();
		//tercera derivada h
		return derivadas;
	}
	public double[] dameEstadoActual() {
		int num_estados=2;
		double[] estado= new double[num_estados];
		//primera derivada x
		estado[0]=this.getStateValue(e).doubleValue();
		//segunda derivada y
		estado[1]=this.getStateValue(n).doubleValue();
		return estado;
	}
	
	@Override
	public void dibujar3D(GL2 gl) {
		_modeloNaufrago = new ModeloNaufrago(gl,_terreno);

        
        _modeloNaufrago.getTafin().escalar(10, 10, 10, gl);
		_modeloNaufrago.posicionar((getStateValue("n")).floatValue()/10000*Dibujante.escalado,0,(getStateValue("e")).floatValue()/10000*Dibujante.escalado,gl);
		_modeloNaufrago.dibujar3D(gl);		
	}
	
	public void inicializar(GL2 gl) {
		_modeloNaufrago = new ModeloNaufrago(gl,_terreno);
	}
	@Override
	public void dibujar2D(GL2 gl) {
			_modeloNaufrago.dibujar2D((getStateValue("n")).floatValue(), (getStateValue("e")).floatValue(), gl);

	}

	
	//Necesitamos:
	//Puerto entrada, podemos indicar:
	//Cambio de posicion
	//Indicar que ha sido encontrado
	
	//Puerto de salida, podemos indicar:
	//Cambio de posicion
	//Si se ha ahogado (opcion...)
	
	//En un momento determinado, un naufrago tiene que tener,
	//velocidad, y rumbo
	
	
}
