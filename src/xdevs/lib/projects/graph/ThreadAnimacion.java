package xdevs.lib.projects.graph;

import java.util.Iterator;
import java.util.Vector;

import ssii2007.aviones.AvionState;
import testing.kernel.modeling.AtomicState;
import testing.kernel.modeling.DevsDessMessage;


public class ThreadAnimacion extends AtomicState {
	public static final String InFPS = "INFPS";
	public static final String InEspera = "INEspera";
	
	public static final String OutSeñalAnimacion = "OUTSeñalAnimacion";
	
	private final String FPS = "FPS";
	private final String espera = "Espera";
	
	public ThreadAnimacion() {
		super("ThreadAnimacion");
		addInport(InFPS);
		addOutport(OutSeñalAnimacion);
		addState(this.FPS);
		setStateValue(this.FPS,0);
		addState(this.espera);
		double espera=INFINITY;
		setStateValue(this.espera,espera);
		setSigma(espera);		
	}
	
	public ThreadAnimacion(int FPS) {
		super("ThreadAnimaci�n");
		addInport(InFPS);
		addOutport(OutSeñalAnimacion);
		addState(this.FPS);
		setStateValue(this.FPS,FPS);
		addState(this.espera);
		double espera=((double)1000)/((double)FPS);
		setStateValue(this.espera,espera);
		setSigma(espera);
	}
	
	public void setEspera(double espera) {
		setStateValue(this.espera,espera);
		setStateValue(this.FPS,((Double)(1000*espera)).intValue());
	}
	
	public void setFPS(int FPS) {
		setStateValue(this.FPS,FPS);
		setStateValue(this.espera,((double)1000)/((double)FPS));
	}
	
	public int getFPS() {
		return getStateValue(FPS).intValue();
	}

	@Override
	public void deltext(double e, DevsDessMessage x) {
		Iterator iteradorFPS = x.getValuesOnPort(ThreadAnimacion.InFPS).iterator();
		Iterator iteradorEspera = x.getValuesOnPort(ThreadAnimacion.InEspera).iterator();
		int FPS=0;
		if (iteradorFPS.hasNext()) {
			while (iteradorFPS.hasNext()) {
				FPS = ((Integer)iteradorFPS.next());
			}
			double espera=((double)1000)/((double)FPS);
			setStateValue(this.FPS,FPS);
			setStateValue(this.espera,espera);
			setSigma(espera);
		}	
		double espera=INFINITY;
		if (iteradorEspera.hasNext()) {
			while (iteradorEspera.hasNext()) {
				espera = ((Integer)iteradorEspera.next());
			}
			setStateValue(this.espera,espera);
			setStateValue(this.FPS,((Double)(1000*espera)).intValue());
			setSigma(espera);
		}
	}

	@Override
	public void deltint() {
		try {
			Thread.sleep(getStateValue(espera).longValue());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setSigma(getStateValue(espera).doubleValue());
	}

	@Override
	public DevsDessMessage lambda() {
		DevsDessMessage msg = new DevsDessMessage();
		msg.add(OutSeñalAnimacion,getStateValue(FPS));			
		return msg;
	}
}
