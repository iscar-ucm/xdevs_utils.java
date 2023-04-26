package xdevs.lib.projects.math;

public class Funcion {
	//MIRAR LOS NOMBRES...
	double[][] a;
	double[][] x;
	
	String nombre;
	
	public Funcion(String nombre,double[][] a){
		this.nombre=nombre;
		this.a=a;
	}
	public double[][] dameDerivadas(){
		double[][] derivadas = multiplicaMatrices(a,x);
		return derivadas;
	} 
	public static double[][] sumaMatrices(double[][] matriz1,double[][] matriz2){
		double[][] resultado=null;
		if(((matriz1.length)!=(matriz2.length))||((matriz1[0].length)!=(matriz2[0].length))){
			System.out.println("ERROR EN LA SUMA DE MATRICES, MATRICES INCOMPATIBLES");
		}
		else{
			resultado=inicializar(matriz1.length,matriz1[0].length,0);
			for(int cont=0;cont<matriz1.length;cont++){
				for(int cont2=0;cont2<matriz1[0].length;cont2++){
					resultado[cont][cont2]=matriz1[cont][cont2]+matriz2[cont][cont2];
				}
			}
		}
		return resultado;
	}
	
	public static double[][] inicializar(int numFilas,int numColumnas,double valor){
		double [][] resultado = new double[numFilas][];
		for(int cont=0;cont<numFilas;cont++){
			resultado[cont]= new double[numColumnas];
		}
		for(int cont=0;cont<numFilas;cont++){
			for(int cont2=0;cont2<numColumnas;cont2++){
				resultado[cont][cont2]=valor;
			}
		}
		return resultado;
	}
	
	public static double[][] multiplicaMatrices(double[][] matriz1,double[][] matriz2){
		double[][] resultado=null;
		if((matriz1[0].length)!=matriz2.length){
			System.out.println("ERROR EN LA MULTIPLICACION DE MATRICES, MATRICES INCOMPATIBLES");
		}
		else{
			resultado=inicializar(matriz1.length,matriz2[0].length,0);
			for(int cont=0;cont<(resultado[0].length*resultado.length);cont++){
				int fila = cont / resultado[0].length;
				int columna = cont - fila * resultado[0].length;
				for(int cont2=0;cont2<matriz1[0].length;cont2++){
					resultado[fila][columna]+=((
							matriz1[fila][cont2])*(matriz2[cont2][columna])
							);
				}
			}
		}
		return resultado;
	}
}
