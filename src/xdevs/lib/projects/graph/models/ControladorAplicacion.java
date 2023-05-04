package xdevs.lib.projects.graph.models;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import xdevs.core.simulation.Coordinator;
import xdevs.lib.projects.graph.VentanaInicial;
import xdevs.lib.projects.graph.VistaDEVS;
import xdevs.lib.projects.graph.structs.terrain.FactoriaTerreno;
import xdevs.lib.projects.graph.structs.terrain.Terreno;

public class ControladorAplicacion {
	private VistaDEVS _vistaDEVS;
	private CoupledSimulacion _simulacion;
	private Terreno _terreno;
	
	private boolean _simulacionConstruida;
	
	public ControladorAplicacion() {
		VentanaInicial inicial = new VentanaInicial(this);
		inicial.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		inicial.inicializar();
		inicial.setVisible(true);
		_simulacionConstruida=false;
	}

	public void inicializar(int numeroAviones, int numeroBarcos,int numeroNaufragos,
			int tiempo,boolean escribirFichero,boolean XmlOSerializable,long tiempo_ge,double velocidad_avion,
			double posicionnavion,double posicioneavion,double distanciaavion,double posicionnbarco,double  posicionebarco,double distanciabarco,
			boolean rescatanaviones) {
	    _terreno = FactoriaTerreno.crearTerrenoMar(250,250,32,-5000,-200,0.2f);
	    
	    
		_simulacion = new CoupledSimulacion("simulacion",numeroAviones,numeroBarcos,numeroNaufragos,
				tiempo,_terreno,escribirFichero,"NuevaSimulacion2.xml",XmlOSerializable,tiempo_ge,velocidad_avion
				,posicionnavion,posicioneavion,distanciaavion,posicionnbarco,posicionebarco,distanciabarco,rescatanaviones);
		_vistaDEVS = new VistaDEVS(this,_terreno,_simulacion,numeroAviones,numeroBarcos);
		_simulacionConstruida=true;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ControladorAplicacion controlador = new ControladorAplicacion();
		while (!controlador.getSimulacionConstruida());
		controlador.iniciarSimulacion(Long.MAX_VALUE);		
		
	}
	
	public void iniciarSimulacion (long tiempo) {
		
		new Thread(_simulacion).start();
		
	/*	new Thread(_vistaDEVS).start();
		System.out.println("aquiiqiqiqi<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"); */
		Coordinator coordinator = new Coordinator(_vistaDEVS);
		coordinator.simulate(Integer.MAX_VALUE);
	}
	
	public boolean getSimulacionConstruida() {
		return (_simulacionConstruida);
	}
	
	public float[][] getTerreno() {
		return null;
	}

}
