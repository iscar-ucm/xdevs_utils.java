package ssii2007.simulacion;

public class Aprendizaje {
	private int tiempo;
	private double media_v;
	private double media_a;
	private double desviacion_v;
	private double desviacion_a;
	private double media_vr;
	private double media_ar;
	private double desviacion_vr;
	private double desviacion_ar;
	
	public Aprendizaje(int tiempo,double media_v,double media_a,double desviacion_v,double desviacion_a){
		this.tiempo=tiempo;
		this.media_a=media_a;
		this.media_v=media_v;
		this.desviacion_a=desviacion_a;
		this.desviacion_v=desviacion_v;
	}
	
	public void ponDatosReales(double media_vr,double media_ar,double desviacion_vr,double desviacion_ar){
		this.media_vr=media_vr;
		this.media_ar=media_ar;
		this.desviacion_vr=desviacion_vr;
		this.desviacion_ar=desviacion_ar;
	}
	
	public int dameTiempo(){return tiempo;}
	public double dameMedia_V(){return media_v;}
	public double dameMedia_A(){return media_a;}
	public double dameDesv_V(){return desviacion_v;}
	public double dameDesv_A(){return desviacion_a;}

	public double dameMedia_VR(){return media_vr;}
	public double dameMedia_AR(){return media_ar;}
	public double dameDesv_VR(){return desviacion_vr;}
	public double dameDesv_AR(){return desviacion_ar;}
	
	public void imprimeEstado(){
		System.out.println("REAL: "+"MEDIA_V"+media_vr+"MEDIA_A"+media_ar);
		System.out.println("ESTIMADA: "+"MEDIA_V"+media_v+"MEDIA_A"+media_a);
	}

}
