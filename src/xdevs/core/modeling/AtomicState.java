package xdevs.core.modeling;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public abstract class AtomicState extends Atomic {
	private Map<String, Number> states;
	
	public AtomicState(String name) {
		super(name);
		states = new HashMap<String, Number>();
	}
	
	public void addState(String name) { states.put(name, 0); }
	public Set<String> getStates() { return states.keySet(); }
	public Number getStateValue(String name) { return states.get(name); }
	public void setStateValue(String name, Number value) { states.put(name, value); }

}
