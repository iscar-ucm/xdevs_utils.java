package xdevs.lib.projects.thermal.sram;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.profile.CoordinatorProfile;

/**
* This class represents the coupled model for a SRAM memory.
* The matrix representing the memory may be configured (rows x columns)
*
* @author J. Manuel Colmenar
*/
public class SRAMmemory extends Coupled {

	/** Default rows and cols values **/
	public static int DefaultRows = 2;
	public static int DefaultCols = 2;
	
	/** Rows number **/
	protected int numRows;

	/** Columns number **/
	protected int numCols;
	
	
	public SRAMmemory(String name, String profileName, int rows, int cols) {
		super(name);
		this.numRows = rows;
		this.numCols = cols;
		
		// Create profile accessor.
		SRAMaccessor accessor = new SRAMaccessor("Accessor",rows,cols,profileName);
		super.addComponent(accessor);
		
		// Create cell matrix. Add cells from west to east, north to south:
		SRAMcell cell = null;
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				
				// Create cell
				byte borderMask = 0;
				if (i==0) borderMask = SRAMcell.BORDERMASK[SRAMcell.NORTH];
				if (i==(rows-1)) borderMask = (byte) (borderMask | SRAMcell.BORDERMASK[SRAMcell.SOUTH]);
				if (j==0) borderMask = (byte) (borderMask | SRAMcell.BORDERMASK[SRAMcell.WEST]);
				if (j==(cols-1)) borderMask = (byte) (borderMask | SRAMcell.BORDERMASK[SRAMcell.EAST]);
				cell = new SRAMcell(String.valueOf(getCellId(i,j)),borderMask);
				super.addComponent(cell);
				// Connection to accessor
				super.addCoupling(accessor.getOutPort("out"+String.valueOf(getCellId(i,j))), cell.getInPort(SRAMcell.InputPortName));
				
				// Connections to neighbors as a matrix:
				if (i>0) {
					// First row cells have no north neighbor
					SRAMcell north = (SRAMcell) super.getComponentByName(String.valueOf(getCellId(i-1,j)));
					super.addCoupling(cell.getOutPort(SRAMcell.NoutPortName), north.getInPort(SRAMcell.SinPortName));
					super.addCoupling(north.getOutPort(SRAMcell.SoutPortName), cell.getInPort(SRAMcell.NinPortName));
				}
				if (j>0) {
					// First column cells have no west neighbor
					SRAMcell west = (SRAMcell) super.getComponentByName(String.valueOf(getCellId(i,j-1)));
					super.addCoupling(cell.getOutPort(SRAMcell.WoutPortName), west.getInPort(SRAMcell.EinPortName));
					super.addCoupling(west.getOutPort(SRAMcell.EoutPortName), cell.getInPort(SRAMcell.WinPortName));
				}
				
			}
		}
		
	}
	
	
	public SRAMmemory(String name, String profile) {
		this(name,profile,SRAMmemory.DefaultRows, SRAMmemory.DefaultCols);
	}
	
	
	/** Generates cell id String considering a matrix where the cells are numbered
	 *  consecutively starting from 0 for the (0,0) cell 1 to (0,1) cell ... and 
	 *  numCols*i+j to (i,j). **/
	public final int getCellId(int row, int col) {
		return numCols*row + col;
	}
		

	/** Prints out the stats of the memory **/
	private void printStats() {
		System.out.println("\nStats\n=====");
		for (int i=0; i<numRows; i++) {
			for (int j=0; j<numCols; j++) {
				SRAMcell cell = (SRAMcell) super.getComponentByName(String.valueOf(getCellId(i,j)));
				System.out.println("Cell "+cell.getName()+": "+String.valueOf(cell.getTemperature())+" degrees");
			}
		}
	}
	
	
	public static void main(String[] args) {
		SRAMmemory mem = new SRAMmemory("Example","profiles/profile.mem");
		
		CoordinatorProfile coordinator = new CoordinatorProfile(mem);
		coordinator.simulate(30);
		
		mem.printStats();
	}


	
}
