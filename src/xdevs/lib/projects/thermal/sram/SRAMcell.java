package xdevs.lib.projects.thermal.sram;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
* This class represents the atomic model for a cell of SRAM memory
*
* @author J. Manuel Colmenar
*/
public class SRAMcell extends Atomic{

	/** Default room temperature: 318º K = 45º C **/
    public static final Double DefaultRoomTemp = 318.15;
    
    /** Default room capacitance **/
    public static final Double DefaultRoomCapacitance = 0.0;
    
    /** Default room resistance **/
    public static final Double DefaultRoomResistance= 0.0;
    
    /** Default cell temperature: 358º K = 85º C **/
    public static final Double DefaultCellTemp = 358.15;
    
    /** Initial temperature **/
    protected Double initialTemp;
       
	/** Temperature threshold that a temperature change must exceed in
	 *  order to spread the temperature from this cell to the neighbors. **/
	// private static final double ThermalThreshold = 0.1;
	
	/** Power dissipation of the cell. **/
	private double power;
	
	/** Thermal capacitance of the cell. **/
	private double capacitance;
	
	/** Thermal capacitance of the neighbors **/
	private double neighborCapacitance[];
	
	/** Thermal resistance of the cell. Represents the conductivity. **/
	private double resistance;

	/** Thermal resistance of the neighbors **/
	private double neighborResistance[];
	   
    /** Current temperature: this value is increased when the cell is accessed and when neighbors
     * spread temperature, but after changing the state (lambda) **/
    protected double temperature;
    
    /** Auxiliar value representing the new temperature after the access or the neighbors. It is
     * modified by external stimuli (deltext) and stored to temperature (in deltin) after changing
     * state (after lambda) **/
    protected double newTemp;
    
    /** Temperature of the neighbors **/
	private double neighborTemperature[];
	
	/* Thermal properties for silicon (from Stan et al. "HotSpot: a Dynamic Compact Thermal
     *  Model at the Processor-Architecture Level") */
	/** Default resistance:
	 *  R = t / (k·A)
	 *  t is the thickness, A is the cross-sectional area across which the heat is being transferred
	 *  and k is the thermal conductivity, which is 100 W/m·K for silicon at 85º C. 
	 *  We assume t = A = 1 */
    private static final Double DefaultResistance = 0.001;
    /** Default capacitance:
     *  C = c·t·A
     *  t is the thickness, A is the cross-sectional area across which the heat is being transferred
     *  and c is the thermal capacitance per unit volume, 1.75x10^6 J/m^3 for silicon.
     *  We assume t = A = 1 */
    private static final Double DefaultCapacitance = 1750000.0;
    /** Default power: 8.44 W for a D-cache on gcc
     *  0.35815 is the power to be stable at 85ºC */
    private static final Double DefaultPower = 0.35815; 
	
    /** Power due to a read access */
    private static final Double PowerDueToRead = 5.0;
    private static final Double PowerDueToWrite = 10.0;
    
    /** Account for the number of accesses to the cell **/
    protected int accesses;

    /** Last operation of the cell */
	private int lastAccess;
    
    /* Constant values representing accesses */
    public static final int NONE = 0;
    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int NEIGHBOR = 3;
    
    /** Number of neighbors **/
    private static final int NEIGHBORS = 4;
    
    /* Neighbor identifiers, which are array indexes */
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    public static final int EAST = 3;
    
    /* Bit masks to define border cells */
    public static final byte BORDERMASK[] = {8,4,2,1};
    
	// DEVS stuff --------------------------------------------------------------------------

    /** Default delay value for temperature changes **/
    public static final Double DefaultDelay = 1.0;
    
    /** Response delay of the cell */
    protected Double delay;
    
	/** Access port: this port will be stimulated any time an operation was 
	 * performed over this cell. Assumed to be binary valued (1,0). **/
	protected Port<Integer> input;
	
	/** Connection ports: these ports connect the cell with the neighbors and
	 * spread the temperature (output ports) or receive temperature from 
	 * neighbors (input ports). **/
    protected Port<Double> Nin;		// North	
    protected Port<Double> Sin;		// South
    protected Port<Double> Win;		// West
    protected Port<Double> Ein;		// East

    protected Port<Double> Nout;		// North	
    protected Port<Double> Sout;		// South
    protected Port<Double> Wout;		// West
    protected Port<Double> Eout;		// East
    
    /* Port names */
    public static final String InputPortName = "Input";
    
    public static final String NinPortName = "NorthIn";
    public static final String SinPortName = "SouthIn";
    public static final String WinPortName = "WestIn";
    public static final String EinPortName = "EastIn";
    
    public static final String NoutPortName = "NorthOut";
    public static final String SoutPortName = "SouthOut";
    public static final String WoutPortName = "WestOut";
    public static final String EoutPortName = "EastOut";

    /* Constants for cell phases */
    // TODO: cooling means the cell is passive. This string should be independent from the state of passive
	public static final String COOLING = "passive";
	public static final String HEATINGBYACCESS = "Heating by Access";
	public static final String HEATINGBYNEIGHBOR = "Heating by Neighbor";
    
	
    public SRAMcell(String name, Double delay, Double initTemperature, Byte borderMask){

        super(name);
        
        /* Initialize thermal info */
        this.resistance = DefaultResistance;
        this.capacitance = DefaultCapacitance;
        this.power = DefaultPower;

        this.delay = delay;
        this.temperature = initTemperature;
        this.initialTemp = initTemperature;
        
        this.lastAccess = NONE;
        
        /* Neighbor data */
        this.neighborResistance = new double[NEIGHBORS];
        this.neighborCapacitance  = new double[NEIGHBORS];
        this.neighborTemperature  = new double[NEIGHBORS];
        
        // Border cells have different R, C and T
        for (int i=0; i<NEIGHBORS; i++) {
        	if ((borderMask & BORDERMASK[i])!=0) {
        		this.neighborResistance[i] = DefaultRoomResistance;
        		this.neighborCapacitance[i] = DefaultRoomCapacitance;
        		this.neighborTemperature[i] = DefaultRoomTemp;       	
        	} else {
        		this.neighborResistance[i] = DefaultResistance;
        		this.neighborCapacitance[i] = DefaultCapacitance;
        		this.neighborTemperature[i] = initTemperature;
        	}
        }
        
        input = new Port<Integer>(SRAMcell.InputPortName);
        super.addInPort(input);
        
        Nin = new Port<Double>(SRAMcell.NinPortName);
        Sin = new Port<Double>(SRAMcell.SinPortName);
        Win = new Port<Double>(SRAMcell.WinPortName);
        Ein = new Port<Double>(SRAMcell.EinPortName);
        super.addInPort(Nin);
        super.addInPort(Sin);
        super.addInPort(Win);
        super.addInPort(Ein);
        
        Nout = new Port<Double>(SRAMcell.NoutPortName);
        Sout = new Port<Double>(SRAMcell.SoutPortName);
        Wout = new Port<Double>(SRAMcell.WoutPortName);
        Eout = new Port<Double>(SRAMcell.EoutPortName);
        super.addOutPort(Nout);
        super.addOutPort(Sout);
        super.addOutPort(Wout);
        super.addOutPort(Eout);
                      
    }
    
    
	public SRAMcell(String name, Byte borderMask) {
		this(name,SRAMcell.DefaultDelay,SRAMcell.DefaultCellTemp,borderMask);
	}

	
	@Override
	public void initialize() {
        super.phaseIs(COOLING);
        super.passivate();
	}

	@Override
	public void exit() { }


	@Override
	public void deltext(double e) {
		/* State the current time: if resume here, e means the delay since the last event, either
		 * lambda either the last resume on deltext */
		super.resume(e);
		
		// First we need to obtain the current temperature
		double currTemp = obtainCurrentTemperature(e);
		double tempAux = this.initialTemp;
		
		// Process input:
		Integer powerValueAtInput = input.getSingleValue();
        if(powerValueAtInput!=null) {
        	// There was a direct access to the cell
        	tempAux = increaseTemperatureByAccess(currTemp,powerValueAtInput,e);
        }
		
        // Process thermal stimuli from neighbors:
        Double tempValueAtNin = Nin.getSingleValue();
        if(tempValueAtNin!=null) {
        	tempAux = modifyTemperatureDueToNeighbor(currTemp, tempValueAtNin,NORTH,e);
        }

        Double tempValueAtSin = Sin.getSingleValue();
        if(tempValueAtSin!=null) {
        	tempAux = modifyTemperatureDueToNeighbor(currTemp, tempValueAtSin,SOUTH,e);
        }
        
        Double tempValueAtWin = Win.getSingleValue();
        if(tempValueAtWin!=null) {
        	tempAux = modifyTemperatureDueToNeighbor(currTemp, tempValueAtWin,WEST,e);
        }
        
        Double tempValueAtEin = Ein.getSingleValue();
        if(tempValueAtEin!=null) {
        	tempAux = modifyTemperatureDueToNeighbor(currTemp, tempValueAtEin,EAST,e);
        }
        
        
        /* The state must change always */
       	super.holdIn("active",delay);
        
        // The new temperature is stored for being transmitted after the delay
        this.newTemp = tempAux;
        
	}

	
	@Override
	public void deltint() {
		/* Update temperature to new value here because several external
		 * stimuli overlapped may happen before a lambda execution. */
		this.temperature = this.newTemp;

		// Reduce power
		if (this.lastAccess == READ) this.power -= PowerDueToRead;
		else if (this.lastAccess == WRITE) this.power -= PowerDueToWrite;
		
		// Starts cooling phase
		this.lastAccess = NONE;

		super.passivate();
	}

	
	@Override
	public void lambda() {			
		/* All state changes mean a temperature change. Therefore, the new temperature
		 * has to be spread to neighbors. */
		Nout.addValue(this.newTemp);
        Sout.addValue(this.newTemp);
        Wout.addValue(this.newTemp);
        Eout.addValue(this.newTemp);
	}

	
	/** Returns the temperature. This method is not part of the simulation. Therefore
	 * it just returns the value out of any modification. */
	public Double getTemperature() {
		return this.temperature;
	}
	
	
	/** Obtains the current temperature depending on the current phase.
	 * @param time is the gap since the last event occurred.  */
	private double obtainCurrentTemperature(double timeGap) {
		
		/* Current temperature to be obtained */
		Double temp;
		if (super.getPhase() == COOLING) {
			// The last event was a lambda execution. This means the current temperature rules
			temp = solveDifEquation(this.temperature, timeGap);
		} else {
			/* Assume to be heating. The last event was a deltext. Then, newTemp rules because
			temperature was not updated */
			temp = solveDifEquation(this.newTemp, timeGap);
		}
				
		// Cannot be cooler than initial temp (assumed to be the room temperature)
		if (temp < this.initialTemp) temp = this.initialTemp;
		
		System.out.println(name+" - curr temp is "+String.valueOf(temp));
		
		return temp;
	}
	
	
	/** Method that solves the finite-difference equation that represents the differential equation
	 *  of the temperature. Considering current power, capacitance and resistance of the cell and
	 *  neighbors, it returns the temperature after the given time gap assuming no accesses. */
	private Double solveDifEquation(Double currentTemp, Double timeGap) {
		
		/* TODO: recode !!!  It needs to take into account neighbor temperatures, capacitances
		and resistances */
		
		Double deltaTemp;
		
		/* Aux vars for integer time units */
		int initTime, endTime;
		initTime = 0;
		endTime = (int) Math.round(timeGap);		
		
		Double accu = 0.0;
		for (int i=initTime; i<=endTime; i++) {
			accu+= (this.power - this.resistance*currentTemp);
		}
		
		deltaTemp = accu/this.capacitance;
		
		return (currentTemp + deltaTemp);
	}

	
	/** Increases the given temperature due to a direct access to the cell. The
	 *  kind of access determines the increment on the power, which influences 
	 *  the temperature. */
	private Double increaseTemperatureByAccess(Double temp, Integer kindOfAccess, Double timeGap) {

		// First: increase power
		if (kindOfAccess == READ) {
			// Read
			this.power += PowerDueToRead;
			this.lastAccess = READ;
		} else {
			if (kindOfAccess == WRITE) {
				// Write
				this.power += PowerDueToWrite;
				this.lastAccess = WRITE;
			}
		}
		
		// Second: solve equation
		Double newTemp = solveDifEquation(temp,timeGap);	
	
        System.out.println(name+" - new access; temp is "+String.valueOf(newTemp));
		
        // Here starts the heating phase:
		super.phaseIs(HEATINGBYACCESS);
        
        return newTemp;
	}
	

    /** Modifies the given temperature due to the influence of the temperature of
	 * a neighbor. **/
	private Double modifyTemperatureDueToNeighbor(Double temp, Double neighborTemp, int neighborId, Double timeGap) {
		
		// First: update neighbor temperature
		this.neighborTemperature[neighborId] = neighborTemp;
		
		// Second: solve equation
		Double newTemp = solveDifEquation(temp,timeGap);	
		
		// Here starts the heating phase:
		super.phaseIs(HEATINGBYNEIGHBOR);
		
		return newTemp;
	}	
}
