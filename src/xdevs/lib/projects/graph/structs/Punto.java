package xdevs.lib.projects.graph.structs;

public class Punto {
	

	private double n;
	private double e;
	private double h;

	
	public Punto(double n,double e,double h){
		this.n=n;
		this.e=e;
		this.h=h;
	}


	public double getN() {
		return n;
	}


	public void setN(double n) {
		this.n = n;
	}


	public double getE() {
		return e;
	}


	public void setE(double e) {
		this.e = e;
	}
	
	public String toString(){
		return ("norte:"+n+"este:"+e+"altura:"+h);
	}


	public double getH() {
		return h;
	}


	public void setH(double h) {
		this.h = h;
	}
	
	
	
	
}
