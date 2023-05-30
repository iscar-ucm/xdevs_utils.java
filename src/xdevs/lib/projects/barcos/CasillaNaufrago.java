package xdevs.lib.projects.barcos;

import java.io.Serializable;

public class CasillaNaufrago implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -591178642030423221L;
	private double _nmin;
	private double _varN;
	private double _emin;
	private double _varE;
	private int num_naufragos;
	
	public CasillaNaufrago(double _nmin,double _varN,double _emin,double _varE){
		this._emin=_emin;
		this._nmin=_nmin;
		this._varE=_varE;
		this._varN=_varN;
		this.num_naufragos=1;
		
	}
	public int pertenece(double n, double e) {
		int devolver=-1;
		if(((n<(_nmin+_varN))&&(e<(_emin+_varE))) && (n>=_nmin) && (e>=_emin) ){devolver=num_naufragos;}
		return devolver;
	}

	public void llegaNaufrago() {
		this.num_naufragos++;
	}
	
	public int dameNumNaufragos(){
		return num_naufragos;
	}
	
	public String toString(){
		return "Nmin:"+_nmin+" VarN:"+_varN+" Emin:"+_emin+" VarE:"+_varE+" numNaufragos:"+num_naufragos;	
	}
	
	public double get_nmin() {
		return _nmin;
	}
	public double get_varN() {
		return _varN;
	}
	public double get_emin() {
		return _emin;
	}
	public double get_varE() {
		return _varE;
	}
}
