package xdevs.lib.projects.graph.structs;

import java.util.ArrayList;
import java.util.Random;

public class GeneradorTerreno {
	private int _tipoTerreno;
	private int _tipoActual;
	private int _longitudTablero;
	private float _numTurnosCambio;
	private float _alturaMaxima;
	private float _alturaMinima;
	private static final int TERRENOMONTAÑOSO=4;
	private static final int TERRENOINTERMEDIO1 =3;
	private static final int TERRENOINTERMEDIO2 =2;
	private static final int TERRENOMAR=1;
	
	private ArrayList<ArrayList<Float>> terreno;
	
	public GeneradorTerreno(int tipo,int longitud,float altM,float altm ){
		
		this._tipoActual=tipo;
		this._tipoTerreno=tipo;
		this._longitudTablero=longitud;
		this._numTurnosCambio=0;
		this._alturaMaxima=altM;
		this._alturaMinima=altm;

		terreno=new ArrayList<ArrayList<Float>>();
		
		for (int i=0;i<this._longitudTablero;i++){
			ArrayList<Float> actual=new ArrayList<Float>();
			terreno.add(i, actual);
			for(int j=0;j<this._longitudTablero;j++){
				terreno.get(i).add(j,-1.0f);
			}
		}
	}
	
	public void AlerarCasillasAdyacentes(int x,int y){
		//PRIMERO RECIBO INFO
		this._numTurnosCambio--;
		//System.out.println(this._numTurnosCambio);
		if(_numTurnosCambio<=0){
			calculaTipo();
			_numTurnosCambio=100;
		}
		ArrayList<Float> cercania = new ArrayList<Float>();
		if(x>0){
			cercania.add (terreno.get(x-1).get(y));	
		}
		if((x+1)<terreno.size()){
			cercania.add (terreno.get(x+1).get(y));	
		}
		if(y>0){
			cercania.add (terreno.get(x).get(y-1));	
		}
		if(x>0&&y>0){
			cercania.add (terreno.get(x-1).get(y-1));
		}
		if((y+1)<terreno.get(terreno.size()-1).size()){
			cercania.add (terreno.get(x).get(y+1));	
		}
		if(((x+1)<terreno.size())&&((y+1)<terreno.get(terreno.size()-1).size())){
			cercania.add (terreno.get(x+1).get(y+1));	
		}
		
		if(x>0&&(y>0)){
			cercania.add (terreno.get(x-1).get(y-1));
		}
		
		if(x>0&&((y+1)<terreno.get(terreno.size()-1).size())){
			cercania.add (terreno.get(x-1).get(y+1));
		}
		
		if(((x+1)<terreno.size())&&(y>0)){
			cercania.add (terreno.get(x+1).get(y-1));	
		}
		
		float total=0;
		int num_validos=0;
		
		Random randomizador = new Random();
		boolean asciende = (randomizador.nextFloat()>convierteProb(this._tipoActual));
		for(int i=0;i<cercania.size();i++){
			if(cercania.get(i)!=-1){
				total+=cercania.get(i);
				num_validos++;
			}
		}

		float valor_media = (Float)(total/num_validos);
		//System.out.println("valormedio"+valor_media);
		float valor_teorico = variacion(valor_media,asciende);
		//System.out.println("valorteorico"+valor_teorico);
		float valor_real = restriccion(valor_teorico);

		//System.out.println("valorreal"+valor_real);
		this.terreno.get(x).set(y, valor_real);
		//SEGUNDO, ENVIO INFO
	}

	private void calculaTipo() {
		// TODO Auto-generated method stub
		Random randomizador=new Random();
		float num = randomizador.nextFloat();
		if(num<0.5){
			//System.out.println("cambio1");
			this._tipoActual=this._tipoTerreno;}
		else{
			//System.out.println("cambio2");
			if(num<(0.5+(1.5*(1.0-0.5)/4.0))){
				//System.out.println("terreno mar");
				this._tipoActual=GeneradorTerreno.TERRENOMAR;
				}
			else if(num<(0.5+(2*(1.0-0.5)/4.0))){
				//System.out.println("terreno intermedio1");
				this._tipoActual=GeneradorTerreno.TERRENOINTERMEDIO1;
				}
			else if(num<(0.5+(3*(1.0-0.5)/4.0))){
				//System.out.println("terreno intermedio2");
				this._tipoActual=GeneradorTerreno.TERRENOINTERMEDIO2;
				}
			else if(num<(0.5+(4*(1.0-0.5)/4.0))){
				//System.out.println("terreno montanhoso");
				this._tipoActual=GeneradorTerreno.TERRENOMONTAÑOSO;
				}
		}
	}

	private float restriccion(float valor_teorico) {
		// TODO Auto-generated method stub
		float valor=valor_teorico;
		if(valor_teorico>this._alturaMaxima){valor=this._alturaMaxima;}
		if(valor_teorico<this._alturaMinima){valor=this._alturaMinima;}
		if(valor_teorico<10){valor=0;}
		return valor;
	}

	private float variacion(float valor_media,boolean asciende) {
		// TODO Auto-generated method stub
		Random randomizador=new Random();
		float porcv = (Float)(randomizador.nextFloat());
		if(!asciende) {	porcv = - porcv;	}
		porcv= (float)(valor_media + porcv*350);
		//System.out.println("vm"+valor_media+"calcular porcv"+porcv);
		return (porcv);
	}

	private void modificacionInicial(int x,int y){
		//Random randomizador= new Random();
		//Float altura =randomizador.nextFloat()*this._alturaMaxima;
		Float altura= new Float(0.1f);
		this.terreno.get(x).set(y, altura);
	}
	
	private float convierteProb(int terreno2) {
		// TODO Auto-generated method stub
		return ((float)(1.0f/(float)terreno2));
	}
	
	public void imprimeMatriz(){
		for (int i=0;i<this._longitudTablero;i++){
			//System.out.println();
			for(int j=0;j<this._longitudTablero;j++){
			//	System.out.print(this.terreno.get(i).get(j)+"  ");
			}
		}
	}
	public void creaTablero(){
		this.modificacionInicial(1, 1);
		for(int cont=1;cont<30;cont++){
			this.añadeMar(1);	
		}
		this.añadeMar(300);
		this.añadeMar(300);		
		for(int i=0;i<this._longitudTablero;i++){
			for(int j=0;j<this._longitudTablero;j++){
				this.AlerarCasillasAdyacentes(i, j);
			}
		}
		//this.a�adeMar(1000);

		this.convierteMatriz(this._alturaMaxima,1);
	//	this.imprimeMatriz();

	}

	private void añadeMar(int numIteraciones) {
		// TODO Auto-generated method stub
		int numRestantes=numIteraciones;
		Random randomizador=new Random();
		int px = randomizador.nextInt(this._longitudTablero);
		int py = randomizador.nextInt(this._longitudTablero);
		float grado_admitir=0.001f;
		int num_iteraciones=0;
		while (this.terreno.get(px).get(py)>grado_admitir){
			px = randomizador.nextInt(this._longitudTablero);
			py = randomizador.nextInt(this._longitudTablero);
			num_iteraciones++;
			if(num_iteraciones>100){
				grado_admitir=grado_admitir*2;
				num_iteraciones=0;
			}
		}
		grado_admitir=0.01f;
		num_iteraciones=0;
		while(numRestantes>0){
			num_iteraciones++;
			if(num_iteraciones>8){
				
				grado_admitir=grado_admitir*2;
				num_iteraciones=0;
			}
			//System.out.println("posicion"+px+py+" "+numRestantes);
			if(this.terreno.get(px).get(py)!=0.0f){
				this.terreno.get(px).set(py, 0.0f);
				numRestantes--;
				grado_admitir=0.01f;
			}
			

			
			int direccion=randomizador.nextInt(4);
			//arriba
			if(direccion==0){
				
				if(py<(this._longitudTablero-1)){
					if(terreno.get(px).get(py+1)<grado_admitir){
					py++;}
				
				}
			}
			//abajo
			if(direccion==1){

				if(py>0){
					if(terreno.get(px).get(py-1)<grado_admitir){
						
					py--;}
				}
			}
			//izq
			if(direccion==2){
				if(px>0){
					if(terreno.get(px-1).get(py)<grado_admitir){
						
					px--;}
				}
			}
			//der
			if(direccion==3){
				if(px<(this._longitudTablero-1)){
					if(terreno.get(px+1).get(py)<grado_admitir){
						
					px++;}
				}
			}
		}
	}
	
	private void convierteMatriz(float MaximoAnterior,float nuevoMaximo){
		for(int i=0;i<this.terreno.size();i++){
			for(int j=0;j<this.terreno.size();j++){
				float ant=this.terreno.get(i).get(j);
				ant = ant/MaximoAnterior*nuevoMaximo;
				this.terreno.get(i).set(j,ant);
			}
		}
	}

	public ArrayList<ArrayList<Float>> dameTerreno() {
		// TODO Auto-generated method stub
		return this.terreno;
	}
}
