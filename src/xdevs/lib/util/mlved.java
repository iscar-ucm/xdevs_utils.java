package xdevs.lib.util;

public class mlved {
	
	private double[][] A;
	private double[][] B;
	private double[][] C;
	private double[][] D;
	
	private double[][] y;
	private double[][] x;
	
	int cont=0;
	
	public mlved(double[][] A,double[][] B,double[][] C,double[][]D){
		this.A=A;this.B=B;this.C=C;this.D=D;
		x=mlved.inicializar(A[0].length, 1, 0);
		
	}
	
	public double[][] update(double u){
		double[][] matrizU= mlved.inicializar(1, 1, u);
	//	cont++;
	//	System.out.println("cont"+cont);
		y=mlved.sumaMatrices(mlved.multiplicaMatrices(C, x), mlved.multiplicaMatrices(D, matrizU));
		x=mlved.sumaMatrices(mlved.multiplicaMatrices(A, x), mlved.multiplicaMatrices(B, matrizU));
		//System.out.println("prueba x");
		//mlved.imprimeMatriz(x);
		//System.out.println("fin prueba x");
		return y;
	}
	
	public void setInitial(double[][] nuevox){
		x=nuevox;
	}
	
	public double[][] getState(){
		return x;
	}
	

	
	public static void imprimeMatriz(double[][] matriz){
		for(int cont=0;cont<matriz.length;cont++){
			for(int cont2=0;cont2<matriz[0].length;cont2++){
				System.out.print(matriz[cont][cont2]+"  ");
			}
			System.out.println();
		}
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
	
	public static void main(String args[]){
		double[][] A={{-1.5,-0.7},{1,0}};
		double[][] B={{1},{0}};
		double[][] C={{0.28,0.26}};
		double[][] D={{0}};
		
		mlved prueba= new mlved(A,B,C,D);
		
		for(int cont=0;cont<100;cont++){

			imprimeMatriz(prueba.update(1));
		}
		
	}
}
