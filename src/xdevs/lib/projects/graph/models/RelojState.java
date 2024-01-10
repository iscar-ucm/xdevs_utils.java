package xdevs.lib.projects.graph.models;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;

public class RelojState extends AtomicState{
	
	public Port<Integer> in = new Port<>("IN");
	public Port<Integer> out = new Port<>("OUT");

	private int _tiempo_segundo=1000;
	
	public RelojState (String name,int _tiempo_segundo) {
		super(name);
		addInPort(in);
		addOutPort(out);
		this.setSigma(1);
		this._tiempo_segundo=_tiempo_segundo;
	}

	@Override
	public void deltext(double e) {
		super.resume(e);
	}

	@Override
	public void deltint() {
	//	System.out.print("Ciclos a simular: ");
	/*	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String opcionS = in.readLine();
			ciclos = Integer.parseInt(opcionS);
		}
		catch (Exception e) {
		} */
		//int tiempo=(int)1000/_num_aviones;
		int tiempo=(this._tiempo_segundo/10);
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.holdIn("active", 0.1);
	}
	
	public void ponVelocidad(double numVecesMasRapido){
		this._tiempo_segundo=(int)(1000/numVecesMasRapido);
	}

	@Override
	public void lambda() {
		out.addValue(1);
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.holdIn("active", 1.0);
	}
}
