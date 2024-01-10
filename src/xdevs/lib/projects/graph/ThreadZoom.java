package xdevs.lib.projects.graph;

import java.util.Iterator;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;

public class ThreadZoom extends AtomicState {
	public Port<Vector<Number>> InZoom = new Port<>("INZoom");
	public Port<Vector<Number>> OutZoom = new Port<>("OUTZoom");
	
	private final String factor = "Factor";
	private final String xRight = "XRight";
	private final String xLeft = "XLeft";
	private final String yTop = "YTop";
	private final String yBot = "YBot";
	private final String pasos = "Pasos";
	private final String pasoActual = "PasoActual";
	private final String espera = "Espera";
	
	public ThreadZoom() {
		super("ThreadZoom");
		addInPort(InZoom);
		addOutPort(OutZoom);
		addState(factor);
		addState(xRight);
		addState(xLeft);
		addState(yTop);
		addState(yBot);
		addState(pasos);
		addState(pasoActual);
		addState(espera);
	}

	@Override
	public void deltext(double e) {
		super.resume(e);
		Iterator<Vector<Number>> iteradorIn = InZoom.getValues().iterator();
		Vector<Number> datos;
		if (iteradorIn.hasNext()) {
			datos = iteradorIn.next();
			while (iteradorIn.hasNext()) {
				datos = iteradorIn.next();
			}
			setStateValue(xLeft,(Float)datos.get(0));
			setStateValue(xRight,(Float)datos.get(1));
			setStateValue(yBot,(Float)datos.get(2));
			setStateValue(yTop,(Float)datos.get(3));
			setStateValue(factor,(Float)datos.get(4));
			setStateValue(pasos,(Integer)datos.get(5));
			setStateValue(pasoActual, Integer.valueOf(1));
			setStateValue(espera,(Integer)datos.get(6));
			setSigma(getStateValue(espera).doubleValue());
		}
	}

	@Override
	public void deltint() {
		if (getStateValue(pasoActual).intValue()<=getStateValue(pasos).intValue()) {
			setStateValue(pasoActual,new Integer(getStateValue(pasoActual).intValue()+1));
			setSigma(getStateValue(espera).doubleValue());
		}
		else {
			super.passivate();
		}
	}

	@Override
	public void lambda() {
		float xRight=getStateValue(this.xRight).floatValue();
		float xLeft=getStateValue(this.xLeft).floatValue();
		float yTop=getStateValue(this.yTop).floatValue();
		float yBot=getStateValue(this.yBot).floatValue();
		int pasos=getStateValue(this.pasos).intValue();
		float factor=getStateValue(this.factor).floatValue();
		float ixRight=((xRight*factor-xRight)/(float)pasos);
		float ixLeft=((xLeft*factor-xLeft)/(float)pasos);
		float iyTop=((yTop*factor-yTop)/(float)pasos);
		float iyBot=((yBot*factor-yBot)/(float)pasos);
		int pasoActual=getStateValue(this.pasoActual).intValue();
		Vector<Number> vector= new Vector<>();
		vector.add(Float.valueOf(xLeft+ixLeft*pasoActual));
		vector.add(Float.valueOf(xRight+ixRight*pasoActual));
		vector.add(Float.valueOf(yBot+iyBot*pasoActual));
		vector.add(Float.valueOf(yTop+iyTop*pasoActual));
		OutZoom.addValue(vector);			
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}
}
