package xdevs.lib.general.sources;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 * <P>Modelo atómico que representa una señal sinusoidal.</P>
 * <P><B>Puertos de entrada:</B> No posee puertos de entrada.</P> 
 * <P><B>Puertos de salida:</B> Un puerto de salida que arroja un valor de tipo
 * Double. y=A*sin(w*t+p).</P>
 * @author José Luis Risco Martín.
 *
 */
public class SineWave extends Atomic {
	public Port<Double> out = new Port<>("out");
	protected double time = 0.0;
	/** Amplitud. */
	protected Double amplitude; // Amplitud
	/** Frecuencia. */
	protected Double frecuency; // Frecuency
	/** Fase. */
	protected Double phase; // Phase
	/** Periodo de muestreo. */
	protected Double sampleTime; // Sample time of simulation
	/** Constructor. */
	public SineWave(String name, double amplitude, double frecuency, double phase, double sampleTime) {
		super(name);
		this.amplitude = amplitude;
		this.frecuency = frecuency;
		this.phase = phase;
		this.sampleTime = sampleTime;
	}
	
	@Override
	public void initialize() {
		this.time = 0.0;
		super.activate();
	}
	
	@Override
	public void exit() { }

	@Override
	public void deltint() {
		time += super.getSigma();
		super.setSigma(sampleTime);
	}
	
	@Override
	public void lambda() {
		Double output = amplitude*Math.sin(frecuency*time+phase);
		out.addValue(output);
	}

	@Override
	public void deltext(double e) {
		super.resume(e);
		time += e;
	}

}
