package xdevs.lib.projects.graph;

import java.util.Iterator;
import java.util.Vector;

import testing.kernel.modeling.AtomicState;
import testing.kernel.modeling.DevsDessMessage;


public class ThreadZoom extends AtomicState {
	public static final String InZoom = "INZoom";
	public static final String OutZoom = "OUTZoom";
	
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
		addInport(InZoom);
		addOutport(OutZoom);
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
	public void deltext(double e, DevsDessMessage x) {
		Iterator iteradorIn = x.getValuesOnPort(ThreadZoom.InZoom).iterator();
		Vector datos;
		if (iteradorIn.hasNext()) {
			datos=((Vector)iteradorIn.next());
			while (iteradorIn.hasNext()) {
				datos = ((Vector)iteradorIn.next());
			}
			setStateValue(xLeft,(Float)datos.get(0));
			setStateValue(xRight,(Float)datos.get(1));
			setStateValue(yBot,(Float)datos.get(2));
			setStateValue(yTop,(Float)datos.get(3));
			setStateValue(factor,(Float)datos.get(4));
			setStateValue(pasos,(Integer)datos.get(5));
			setStateValue(pasoActual,new Integer(1));
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
			setSigma(INFINITY);
		}
	}

	@Override
	public DevsDessMessage lambda() {
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
		Vector<Float> vector= new Vector<Float>();
		vector.add(new Float(xLeft+ixLeft*pasoActual));
		vector.add(new Float(xRight+ixRight*pasoActual));
		vector.add(new Float(yBot+iyBot*pasoActual));
		vector.add(new Float(yTop+iyTop*pasoActual));
		DevsDessMessage msg = new DevsDessMessage();
		msg.add(OutZoom,vector);			
		return msg;
	}
}
