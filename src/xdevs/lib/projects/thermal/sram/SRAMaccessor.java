package xdevs.lib.projects.thermal.sram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
* This class represents the atomic model for an element that
* processes a profile and access to a destination cell of SRAM memory
*
* @author J. Manuel Colmenar
*/
public class SRAMaccessor extends Atomic {

	/** Output ports that will be connected to SRAM cells */
	protected List<Port<Integer>> outPorts;
	
	/** Time to wait for the memory operation */
	protected Double currentOperDelay;
	
	/** Address of memory operation to be scheduled **/
	protected String currentOperAddr;
	
	/** Memory operation to be scheduled **/
	protected String currentOper;

	/** Size of memory operation to be scheduled **/
	protected Integer currentOperSize;
	
	/** Delay since the last operation occurred **/
	protected Double delaySinceLastOperation;
	
	/** Default rows and columns values **/
	public static int DefaultRows = 2;
	public static int DefaultCols = 2;
	
	/** Buffer that reads profile **/
	protected String profileName;
	protected BufferedReader profileReader;
	
	
	public SRAMaccessor(String name, int rows, int cols, String profileName) {
		super(name);
		
		// Create output ports:
		outPorts = new ArrayList<Port<Integer>>();
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				int label = i*cols + j;
				Port<Integer> port = new Port<>("out"+String.valueOf(label));
				outPorts.add(port);
				super.addOutPort(port);
			}
		}	
		this.profileName = profileName;	
	}


	public SRAMaccessor(String name) {
		this(name, SRAMaccessor.DefaultRows, SRAMaccessor.DefaultCols, null);
	}
	
	
	@Override
	public void initialize() {
		// No previous operations were performed.
		this.delaySinceLastOperation = 0.0;
		
		// Open profile:
		try {
			this.profileReader = new BufferedReader(new FileReader(profileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		// Decode first operation and schedule first event guided by that operation
		processNextProfileLine();	
		super.holdIn("active", this.currentOperDelay);
	}

	@Override
	public void exit() {
		try {
			this.profileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/** Reads next profile line, decodes it and stores the fields on the
	 *  corresponding class attributes: time, operation, address and size */
	private final void processNextProfileLine() {
		
		// Read line
		String currentProfileLine = null;
		try {
			currentProfileLine = this.profileReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}		

		if (currentProfileLine !=null) {
		
			/* Decode profile line: <time> <operation> <address> <size>
			 * Changes on the format should change this method */		
			String[] parts = currentProfileLine.split(" ");

			if (parts.length == 4) {
				this.currentOperDelay = Double.valueOf(parts[0]);
				this.currentOper = parts[1];
				this.currentOperAddr = parts[2];
				this.currentOperSize = Integer.valueOf(parts[3]);
				
				return;
			}
			
		}
		
		/* If the execution reaches this point, we have the end of file 
		   or a blank line or wrong format. Then, close the profile. */  
		try {
			this.profileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.currentOperDelay = 0.0;
		
	}

	
	@Override
	public void deltext(double e) {
		// No inputs, therefore, this method will never be invoked.
	}

	
	@Override
	public void deltint() {
		
		// Read and decode next operation and schedule event
		processNextProfileLine();
		if (this.currentOperDelay == 0.0) {
			// End of simulation
			super.passivate();
		} else {
			super.holdIn("active", this.currentOperDelay);
		}
		
	}

	
	@Override
	public void lambda() {
		/* In order to deal with the profile, where the <time> value represents the delay
		   since the last operation occurred, each lambda execution will read an
		   operation but the access will be scheduled to e + <time> */
		
		// Destination cell:
		// TODO: decode from memory address hexadecimal to cell memories
		Integer destCell = 0;
		
		if (this.currentOper.equals("W")) {
			outPorts.get(destCell).addValue(SRAMcell.WRITE);
		} else {
			if (this.currentOper.equals("R")) {
				outPorts.get(destCell).addValue(SRAMcell.READ);
			} else {
				System.out.println("SRAMaccessor: Invalid operation -" + this.currentOper +"-");
				return;
			}
		}
		
	}
}
