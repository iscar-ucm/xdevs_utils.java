package ssii2007.incidencias;

import java.io.Serializable;






import java.util.ArrayList;

public class ProbabilidadNaufragoCasillas implements Serializable{
	private double _tmin;
	private double _tmax;
	private double _varT;
	private double _varE;
	private double _varN;
	/*
	 * orden t,n,e
	 */
	private ArrayList<ArrayList<CasillaNaufrago>> tablero;
	private int[] numNaufragosT;
	

	public ArrayList<ArrayList<CasillaNaufrago>> dameTablero(){
		return this.tablero;
	}
	
	public static int Correcto		=	1;
	public static int Incorrecto	=	2;
	public static int Norte			=	3;
	public static int Este 			= 	4;
	public static int Tiempo		=  	5;
	
	private int numNaufragosTotales;
	
	public ProbabilidadNaufragoCasillas(double tmin,double tmax,double varT,double varN,
double varE){
		this._tmin=tmin;
		this._tmax=tmax;
		this._varT=varT;
		int numT=((int)(((_tmax-_tmin)/_varT)+1));
		this._varE=varE;
		this._varN=varN;
		this.tablero= new ArrayList<ArrayList<CasillaNaufrago>>();
		numNaufragosT=new int[numT];
	}
	
	public double dameTiempoFinal(){
		return _tmax+this._varT;
	}


	public double dameProbabilidad(double n,double e,double t){
		double devolver=0;

		int t_norm = normalizar(t);
		int num_naufragos = -1;
		int cont=0;
		if(tablero.size()<=t_norm){
			tablero.add(new ArrayList<CasillaNaufrago>());
		}
		while((num_naufragos==-1)&&(cont<tablero.get(t_norm).size())){
			num_naufragos = tablero.get(t_norm).get(cont).pertenece(n,e);
			if(num_naufragos==-1)	cont++;
		}
		if(num_naufragos<0){num_naufragos=0;}
		devolver = (((double)num_naufragos)/((double)this.numNaufragosT[t_norm]));		
		return devolver;
	}
	
	
	
	public void llegaNaufrago(double n,double e, double t){
		int t_norm = normalizar(t);
		int num_naufragos = -1;
		int cont=0;
		if(tablero.size()<=t_norm){
			tablero.add(new ArrayList<CasillaNaufrago>());
		}
		while((num_naufragos==-1)&&(cont<(tablero.get(t_norm).size()-1))){
			num_naufragos = tablero.get(t_norm).get(cont).pertenece(n,e);
			if(num_naufragos==-1)	cont++;
		}
		if(num_naufragos<0){
			//tenemos que crear una casilla... que...
			//n sea multiplo de varN
			//e sea multiplo de varE
			int kn = (int)(n/this._varN);
			int ke = (int)(e/this._varE);
			tablero.get(t_norm).add(new CasillaNaufrago(kn*_varN,_varN,ke*_varE,_varE));
			
		}
		else{
			tablero.get(t_norm).get(cont).llegaNaufrago();
		}
		//System.out.println("n_norm"+n_norm+"e_norm"+e_norm+"t_norm"+t_norm);
		this.numNaufragosT[t_norm]=this.numNaufragosT[t_norm]+1;
		this.numNaufragosTotales++;

	}
	
	public int normalizar(double t){
		
		int normalizado=0;
		if(t>this._tmax){
			t=this._tmax;
		}
		if(t<this._tmin){
			t=this._tmin;
		}
		normalizado=(int)((t-this._tmin)/this._varT);
		return normalizado;
	}



	public int[] getNumNaufragosT() {
		return numNaufragosT;
	}

	public void setNumNaufragosT(int[] numNaufragosT) {
		this.numNaufragosT = numNaufragosT;
	}

	public double get_tmin() {
		return _tmin;
	}

	public void set_tmin(double _tmin) {
		this._tmin = _tmin;
	}

	public double get_tmax() {
		return _tmax;
	}

	public void set_tmax(double _tmax) {
		this._tmax = _tmax;
	}

	public double get_varT() {
		return _varT;
	}

	public void set_varT(double _vart) {
		_varT = _vart;
	}



	public double get_varN() {
		return _varN;
	}

	public void set_varN(double _varn) {
		_varN = _varn;
	}

	

	public double get_varE() {
		return _varE;
	}

	public void set_varE(double _vare) {
		_varE = _vare;
	}

	public int getNumNaufragosTotales() {
		return numNaufragosTotales;
	}

	public void setNumNaufragosTotales(int numNaufragosTotales) {
		this.numNaufragosTotales = numNaufragosTotales;
	}
}
