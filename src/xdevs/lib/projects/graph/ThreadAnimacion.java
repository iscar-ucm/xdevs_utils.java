package xdevs.lib.projects.graph;

import java.util.Iterator;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;
import xdevs.core.util.Constants;

public class ThreadAnimacion extends AtomicState {
	public Port<Number> inFPS = new Port<>("INFPS");
	public Port<Number> inEspera = new Port<>("INEspera");
	public Port<Number> outSeñalAnimacion = new Port<>("OUTSeñalAnimacion");
	
	public ThreadAnimacion() {
		super("ThreadAnimacion");
		addInPort(inFPS);
		addOutPort(outSeñalAnimacion);
		addState("FPS");
		setStateValue("FPS", 0);
		addState("ESPERA");
		double espera = Constants.INFINITY;
		setStateValue("ESPERA", espera);
		setSigma(espera);		
	}
	
	public ThreadAnimacion(int FPS) {
		super("ThreadAnimaci�n");
		addInPort(inFPS);
		addOutPort(outSeñalAnimacion);
		addState("FPS");
		setStateValue("FPS", FPS);
		addState("ESPERA");
		double espera=((double)1000)/((double)FPS);
		setStateValue("ESPERA", espera);
		setSigma(espera);
	}
	
	public void setEspera(double espera) {
		setStateValue("ESPERA",espera);
		setStateValue("FPS", ((Double)(1000*espera)).intValue());
	}
	
	public void setFPS(int FPS) {
		setStateValue("FPS", FPS);
		setStateValue("ESPERA", ((double)1000)/((double)FPS));
	}
	
	public int getFPS() {
		return getStateValue("FPS").intValue();
	}

	@Override
	public void deltext(double e) {
		Iterator<Number> iteradorFPS = this.inFPS.getValues().iterator();
		Iterator<Number> iteradorEspera = this.inEspera.getValues().iterator();
		int FPS=0;
		if (iteradorFPS.hasNext()) {
			while (iteradorFPS.hasNext()) {
				FPS = ((Integer)iteradorFPS.next());
			}
			double espera=((double)1000)/((double)FPS);
			setStateValue("FPS",FPS);
			setStateValue("ESPERA", espera);
			setSigma(espera);
		}	
		double espera=Constants.INFINITY;
		if (iteradorEspera.hasNext()) {
			while (iteradorEspera.hasNext()) {
				espera = ((Integer)iteradorEspera.next());
			}
			setStateValue("ESPERA", espera);
			setStateValue("FPS",((Double)(1000*espera)).intValue());
			setSigma(espera);
		}
	}

	@Override
	public void deltint() {
		try {
			Thread.sleep(getStateValue("ESPERA").longValue());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setSigma(getStateValue("ESPERA").doubleValue());
	}

	@Override
	public void lambda() {
		outSeñalAnimacion.addValue(getStateValue("FPS"));			
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}
}
