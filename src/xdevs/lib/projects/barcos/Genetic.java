package xdevs.lib.projects.barcos;
/* Resumen direcciones
 * 1 es arriba
 * 2 es arriba derecha
 * 3 es  dcha
 * 4 es dcha abajo
 * 5 es abajo
 * 6 el izq abajo
 * 7 es izq
 * 8 es izq arriba
 * Siempre considerando casillas adyacentes
 * 
 */
import java.util.ArrayList;
import java.util.Random;

import xdevs.lib.projects.graph.structs.Punto;
import xdevs.lib.projects.graph.structs.Ruta;
import xdevs.lib.projects.math.FuncionProbabilidadAbstracta;


/**
 * Clase que gestiona el algoritmo genetico de busqueda de naufragos.
 * Da una ruta como solucion de busqueda.
 * @author Carlos Sanchez
 *
 */
public class Genetic implements Algoritmo {
	private int dimension; //numero de casillas de un lado
	protected ArrayList<ArrayList<Integer>> trayectorias;
	private int numpoints;
	private double distancia;
	private Punto punto1;
	
	
	
	public Genetic(int dimension,int numpoints) {
		super();
		this.dimension = dimension;
		this.trayectorias = new ArrayList<ArrayList<Integer>>();
		this.numpoints=numpoints;
		distancia=0;
		punto1 = new Punto(0, 0, 0);
	}
	/**
	 * Funci�n que genera las trayectorias aleatoriamente
	 * @param puntosiniciales Todos los puntos a partir de los caules queremos que se generen trayectorias
	 */
	public void randomTrayectories(ArrayList<Integer> puntosinciales){
		//Aqui vamos a generar unas cuantas trayectorias por cada casilla del cuadrado, pero
		//considerando solo las casillas de los bordes de una mitad, la mas cercana 
		//a la posicion inicial del avion
		Random a= new Random();
		for(int i=0; i<puntosinciales.size();i++){
			ArrayList<Integer> sol= new ArrayList<Integer>();
			ArrayList<ArrayList<Integer>> conj= new ArrayList<ArrayList<Integer>>();
			sol.add(puntosinciales.get(i));
			vaTray(sol, conj, 1);
			
			int aux=a.nextInt(conj.size());
			trayectorias.add(conj.get(aux));
			int aux1=a.nextInt(conj.size());
			while(aux==aux1)
				aux1=a.nextInt(conj.size());
			trayectorias.add(conj.get(aux1));
			
		}
		
		
	}
	
	/**
	 * Genera trayectorias random
	 * @param puntosinciales
	 */
	public void randomTrayectoriesAlt(ArrayList<Integer> puntosinciales){
		//Aqui vamos a generar unas cuantas trayectorias por cada casilla del cuadrado, pero
		//considerando solo las casillas de los bordes de una mitad, la mas cercana 
		//a la posicion inicial del avion
		
		for(int i=0; i<puntosinciales.size();i++){
			Random a= new Random();
			ArrayList<Integer> sol= new ArrayList<Integer>();
			ArrayList<Integer> sol1= new ArrayList<Integer>();
			sol.add(puntosinciales.get(i));
			sol1.add(puntosinciales.get(i));
			for (int j = 1; j < numpoints; j++) {
				ArrayList<Integer> sigvalido=sigvalido(sol);
				if(sigvalido.size()>=1)
				sol.add(sigvalido.get(a.nextInt(sigvalido.size())));
				ArrayList<Integer> sigvalido1=sigvalido(sol1);
				if(sigvalido1.size()>=1)
				sol1.add(sigvalido1.get(a.nextInt(sigvalido1.size())));
			}
			trayectorias.add(sol);
			trayectorias.add(sol1);
		}
		
		
	}
	
	private ArrayList<Integer> sigvalido(ArrayList<Integer> sol) {
		//primero obtenemos la casilla actual sabiendo q la primera posicion
		//del ArrayList nos da la casilla primera
		ArrayList<Integer> lista=new ArrayList<Integer>();
		int casillaact=sol.get(0);
		lista.add(casillaact);
		for(int i=1;i<sol.size();i++){
			switch (sol.get(i)) {
			case 1:
				casillaact-=dimension;
				break;
			case 2:
				casillaact=casillaact-dimension+1;
				break;
			case 3:
				casillaact+=1;
				break;
			case 4:
				casillaact=casillaact+dimension+1;
				break;
			case 5:
				casillaact+=dimension;
				break;
			case 6:
				casillaact=casillaact+dimension-1;
				break;
			case 7:
				casillaact-=1;
				break;
			case 8:
				casillaact=casillaact-dimension-1;
				break;

			default:
				break;
			}
			lista.add(casillaact);
		}
		//ya tenemos el valor de la casilla actual sin hacer el ultimo movimiento
		//comprobamos el ultimo momiento a ver si es factible
		ArrayList<Integer> posibles=new ArrayList<Integer>();
		for (int i = 1; i < 9; i++) {


			boolean aux=true;
			int dir=i;
			//las 4 esquinas
			if(casillaact==1 &&(dir!=3)&&(dir!=4)&&(dir!=5))
				aux=false;
			if(casillaact==dimension &&(dir!=7)&&(dir!=6)&&(dir!=5))
				aux=false;
			if(casillaact==(dimension*dimension)-dimension+1 &&(dir!=1)&&(dir!=2)&&(dir!=3))
				aux=false;
			if(casillaact==dimension*dimension &&(dir!=1)&&(dir!=8)&&(dir!=7))
				aux=false;
			//Ahora comprobamos los bordes
			//borde superior
			if(casillaact>1 && casillaact<dimension &&(dir!=6)&&(dir!=5)&&(dir!=4)&&(dir!=3)&&(dir!=7))
				aux=false;
			//borde inferior
			if(casillaact>(dimension*dimension)-dimension+1 && casillaact<dimension*dimension &&(dir!=8)&&(dir!=1)&&(dir!=2)&&(dir!=7)&&(dir!=3))
				aux=false;
			//borde izq
			if(casillaact % dimension==1 &&casillaact!=1 && casillaact!=(dimension*dimension)-dimension+1 &&(dir!=1)&&(dir!=2)&&(dir!=3)&&(dir!=4)&&(dir!=5))
				aux=false;
			//borde dcho
			if(casillaact % dimension==0 &&casillaact!=dimension && casillaact!=(dimension*dimension) &&(dir!=5)&&(dir!=6)&&(dir!=7)&&(dir!=8)&&(dir!=1))
				aux=false;

			if(aux==true) posibles.add(dir);
		}
		ArrayList<Integer> mejores=new ArrayList<Integer>();
		for (int i = 0; i < posibles.size(); i++) {
			int casillaaux=casillaact;
			switch (posibles.get(i)) {
			case 1:
				casillaaux-=dimension;
				break;
			case 2:
				casillaaux=casillaaux-dimension+1;
				break;
			case 3:
				casillaaux+=1;
				break;
			case 4:
				casillaaux=casillaaux+dimension+1;
				break;
			case 5:
				casillaaux+=dimension;
				break;
			case 6:
				casillaaux=casillaaux+dimension-1;
				break;
			case 7:
				casillaaux-=1;
				break;
			case 8:
				casillaaux=casillaaux-dimension-1;
				break;

			default:
				break;
			}
			if(!lista.contains(casillaaux)) mejores.add(posibles.get(i));
		}
		if(mejores.size()>0)
			return mejores;
		else
			return posibles;
	}
	/**
	 * Funci�n que realiza el cruzamiento
	 * @param arrayList2 Es uno de los padres
	 * @param arrayList  El otro padre
	 * @param infoOrden 
	 */
	public void crossover(ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2, ArrayList<Integer> infoOrden){
		if(arrayList.size()>1){
			boolean valido=false;
			int i=0;
			ArrayList<Integer> hijo1=new ArrayList<Integer>(arrayList.size());
			ArrayList<Integer> hijo2=new ArrayList<Integer>(arrayList.size());
			while(!valido && i<1000){
				hijo1.clear();
				hijo2.clear();
				int aux=new Random().nextInt(arrayList.size()-1);
				aux++;
				for(int j=0;j<arrayList.size();j++){
					if(j<aux){
						hijo1.add(arrayList.get(j));
						hijo2.add(arrayList2.get(j));
					}else{
						hijo1.add(arrayList2.get(j));
						hijo2.add(arrayList.get(j));
					}
				}
				valido=validarRuta(hijo1) && validarRuta(hijo2);
				i++;
			}
			// si se han generado correctamente las metemos en trayectorias
			if(i<1000){
				ArrayList<Double> valores=new ArrayList<Double>();
				double min1=Integer.MAX_VALUE;
				double min2=Integer.MAX_VALUE;
				int pos1=0,pos2=0;
				for(int d=0;d<trayectorias.size();d++){
					valores.add(valor(trayectorias.get(d),infoOrden));
					if(valor(trayectorias.get(d),infoOrden)<=min1){
						min2=min1;
						pos2=pos1;
						min1=valor(trayectorias.get(d),infoOrden);
						pos1=d;
					}else{
						if(valor(trayectorias.get(d),infoOrden)<min2){
							min2=valor(trayectorias.get(d),infoOrden);
							pos2=d;
						}
					}
				}
				//una vez tenemos los dos valores minimos y sus posiciones vemos si se pueden meter
				double valor1=valor(hijo1,infoOrden);
				double valor2=valor(hijo2,infoOrden);
				if(valor1>min1){
					trayectorias.set(pos1, hijo1);
					if(valor2>min2){
						trayectorias.set(pos2, hijo2);
					}else{
						if(valor2>valor1){
							trayectorias.set(pos1, hijo2);
						}
					}
				}else{
					if(valor2>min1){
						trayectorias.set(pos1, hijo2);
					}
				}
			}
		}
		
	}
	/**
	 * Funci�n que me dice si una ruta es valida
	 * @param hijo1 ruta a validar
	 * @return true si es valida false si no lo es
	 */
	private boolean validarRuta(ArrayList<Integer> ruta) {
		int casillaact=ruta.get(0);
		boolean valido=true;
		
		int dir=ruta.get(1);
		//las 4 esquinas
		if(casillaact==1 &&(dir!=3)&&(dir!=4)&&(dir!=5))
			valido=false;
		if(casillaact==dimension &&(dir!=7)&&(dir!=6)&&(dir!=5))
			valido=false;
		if(casillaact==(dimension*dimension)-dimension+1 &&(dir!=1)&&(dir!=2)&&(dir!=3))
			valido=false;
		if(casillaact==dimension*dimension &&(dir!=1)&&(dir!=8)&&(dir!=7))
			valido=false;
		//Ahora comprobamos los bordes
		//borde superior
		if(casillaact>1 && casillaact<dimension &&(dir!=6)&&(dir!=5)&&(dir!=4)&&(dir!=3)&&(dir!=7))
			valido=false;
		//borde inferior
		if(casillaact>(dimension*dimension)-dimension+1 && casillaact<dimension*dimension &&(dir!=8)&&(dir!=1)&&(dir!=2)&&(dir!=7)&&(dir!=3))
			valido=false;
		//borde izq
		if(casillaact<(dimension*dimension)-dimension+1 && casillaact>1 && borde(casillaact,1,(dimension*dimension)-dimension+1) &&(dir!=1)&&(dir!=2)&&(dir!=3)&&(dir!=4)&&(dir!=5))
			valido=false;
		//borde dcho
		if(casillaact<dimension*dimension && casillaact>1 && borde(casillaact,4,dimension*dimension) &&(dir!=5)&&(dir!=6)&&(dir!=7)&&(dir!=8)&&(dir!=1))
			valido=false;
		
		
		for(int i=1;i<ruta.size()-1 && valido ;i++){
			switch (ruta.get(i)) {
			case 1:
				casillaact-=dimension;
				break;
			case 2:
				casillaact=casillaact-dimension+1;
				break;
			case 3:
				casillaact+=1;
				break;
			case 4:
				casillaact=casillaact+dimension+1;
				break;
			case 5:
				casillaact+=dimension;
				break;
			case 6:
				casillaact=casillaact+dimension-1;
				break;
			case 7:
				casillaact-=1;
				break;
			case 8:
				casillaact=casillaact-dimension-1;
				break;

			default:
				break;
			}
			
			//ya tenemos el valor de la casilla actual sin hacer el ultimo movimiento
			//comprobamos el ultimo momiento a ver si es factible
			dir=ruta.get(i+1);
			//las 4 esquinas
			if(casillaact==1 &&(dir!=3)&&(dir!=4)&&(dir!=5))
				valido=false;
			if(casillaact==dimension &&(dir!=7)&&(dir!=6)&&(dir!=5))
				valido=false;
			if(casillaact==(dimension*dimension)-dimension+1 &&(dir!=1)&&(dir!=2)&&(dir!=3))
				valido=false;
			if(casillaact==dimension*dimension &&(dir!=1)&&(dir!=8)&&(dir!=7))
				valido=false;
			//Ahora comprobamos los bordes
			//borde superior
			if(casillaact>1 && casillaact<dimension &&(dir!=6)&&(dir!=5)&&(dir!=4)&&(dir!=3)&&(dir!=7))
				valido=false;
			//borde inferior
			if(casillaact>(dimension*dimension)-dimension+1 && casillaact<dimension*dimension &&(dir!=8)&&(dir!=1)&&(dir!=2)&&(dir!=7)&&(dir!=3))
				valido=false;
			//borde izq
			if(casillaact<(dimension*dimension)-dimension+1 && casillaact>1 && borde(casillaact,1,(dimension*dimension)-dimension+1) &&(dir!=1)&&(dir!=2)&&(dir!=3)&&(dir!=4)&&(dir!=5))
				valido=false;
			//borde dcho
			if(casillaact<dimension*dimension && casillaact>1 && borde(casillaact,4,dimension*dimension) &&(dir!=5)&&(dir!=6)&&(dir!=7)&&(dir!=8)&&(dir!=1))
				valido=false;
			
		}
		
		
		return valido;
	}
	/**
	 * Funci�n que hace mutaciones
	 */
	public void mutation(){
		
	}
	
	
	public void selection(){
		
	}
	
	/**
	 * Genera por vuelta atras todas las trayectorias posibles desde ese punto sin repetir casilla
	 * @param sol vector solucion actual
	 * @param conj vector conjunto de soluciones
	 * @param k parametro recursivo
	 */
	public void vaTray(ArrayList<Integer> sol,ArrayList<ArrayList<Integer>> conj, int k){
		for (int i = 1; i <= 8; i++) {
			sol.add(i);
			if(valido(sol) && norepite(sol)){
				if(k==numpoints-1){
					ArrayList<Integer> aux=new ArrayList<Integer>();
					//Bucle de copia
					for (int j = 0; j < sol.size(); j++) {
						aux.add(sol.get(j));
					}
					conj.add(aux);
				}else{
					vaTray(sol,conj,k+1);
				}
			}
			sol.remove(sol.size()-1);
		}
	}
	private boolean norepite(ArrayList<Integer> sol) {
		boolean aux=true;
		ArrayList<Integer> lista=new ArrayList<Integer>();
		int casillaact=sol.get(0);
		lista.add(casillaact);
		for(int i=1;i<sol.size()-1 && aux;i++){
			switch (sol.get(i)) {
			case 1:
				casillaact-=dimension;
				break;
			case 2:
				casillaact=casillaact-dimension+1;
				break;
			case 3:
				casillaact+=1;
				break;
			case 4:
				casillaact=casillaact+dimension+1;
				break;
			case 5:
				casillaact+=dimension;
				break;
			case 6:
				casillaact=casillaact+dimension-1;
				break;
			case 7:
				casillaact-=1;
				break;
			case 8:
				casillaact=casillaact-dimension-1;
				break;

			default:
				break;
			}
			if(lista.contains(casillaact)){
				aux=false;
			}else{
				lista.add(casillaact);
			}
		}
		
		return aux;
	}
	/**
	 * Funcion que nos dice si es valida la direccion metida
	 * @param sol vector solucion
	 * @return true or false segun es valido o no respectivamente
	 */
	private boolean valido(ArrayList<Integer> sol) {
		//primero obtenemos la casilla actual sabiendo q la primera posicion
		//del ArrayList nos da la casilla primera
		int casillaact=sol.get(0);
		for(int i=1;i<=sol.size()-2;i++){
			switch (sol.get(i)) {
			case 1:
				casillaact-=dimension;
				break;
			case 2:
				casillaact=casillaact-dimension+1;
				break;
			case 3:
				casillaact+=1;
				break;
			case 4:
				casillaact=casillaact+dimension+1;
				break;
			case 5:
				casillaact+=dimension;
				break;
			case 6:
				casillaact=casillaact+dimension-1;
				break;
			case 7:
				casillaact-=1;
				break;
			case 8:
				casillaact=casillaact-dimension-1;
				break;

			default:
				break;
			}
		}
		//ya tenemos el valor de la casilla actual sin hacer el ultimo movimiento
		//comprobamos el ultimo momiento a ver si es factible
		boolean aux=true;
		int dir=sol.get(sol.size()-1);
		//las 4 esquinas
		if(casillaact==1 &&(dir!=3)&&(dir!=4)&&(dir!=5))
			aux=false;
		if(casillaact==dimension &&(dir!=7)&&(dir!=6)&&(dir!=5))
			aux=false;
		if(casillaact==(dimension*dimension)-dimension+1 &&(dir!=1)&&(dir!=2)&&(dir!=3))
			aux=false;
		if(casillaact==dimension*dimension &&(dir!=1)&&(dir!=8)&&(dir!=7))
			aux=false;
		//Ahora comprobamos los bordes
		//borde superior
		if(casillaact>1 && casillaact<dimension &&(dir!=6)&&(dir!=5)&&(dir!=4)&&(dir!=3)&&(dir!=7))
			aux=false;
		//borde inferior
		if(casillaact>(dimension*dimension)-dimension+1 && casillaact<dimension*dimension &&(dir!=8)&&(dir!=1)&&(dir!=2)&&(dir!=7)&&(dir!=3))
			aux=false;
		//borde izq
		if(casillaact<(dimension*dimension)-dimension+1 && casillaact>1 && borde(casillaact,1,(dimension*dimension)-dimension+1) &&(dir!=1)&&(dir!=2)&&(dir!=3)&&(dir!=4)&&(dir!=5))
			aux=false;
		//borde dcho
		if(casillaact<dimension*dimension && casillaact>1 && borde(casillaact,4,dimension*dimension) &&(dir!=5)&&(dir!=6)&&(dir!=7)&&(dir!=8)&&(dir!=1))
			aux=false;
		
		return aux;
	}
	private boolean borde(int casillaact, int i,int lim) {
		boolean aux=false;
		int auxint=i;
		while(auxint<lim){
			auxint+=dimension;
			if(casillaact==auxint) aux=true;
		}
		return aux;
	}
	/**
	 * Funcion que contruye el espacio por el que se va a mover el avion
	 * @param nauf COnjunto de casillas con naufragos
	 * @param avion coordenadas del avion
	 * @param probabilidadVelocidad 
	 * @param probabilidadAngulo 
	 * @param punto_inicial 
	 * @param tiempo 
	 */
	public ArrayList<Integer> contruccionEspacio(ArrayList<CasillaNaufrago> nauf,Punto avion, Punto punto_inicial, FuncionProbabilidadAbstracta probabilidadAngulo, FuncionProbabilidadAbstracta probabilidadVelocidad, int tiempo){
		double norte,sur,este,oeste;
		norte=masNorte(nauf);
		sur=masSur(nauf);
		este=masEste(nauf);
		oeste=masOeste(nauf);
		distancia=nauf.get(0).get_varE();
		punto1.setE(oeste); //creo q es oeste!!!! estaba este
		punto1.setN(norte-distancia);
		
		//el punto donde empezamos a contruir sera el norte,oeste
		int lon=0;
		double auxN=norte;
		while (auxN>sur) {
			auxN-=distancia;
			lon++;	
		}
		int lon1=0;
		double auxO=oeste;
		while (auxO<este) {
			auxO+=distancia;
			lon1++;	
		}
		int lonfin;
		if (lon>lon1)
			lonfin=lon;
		else
			lonfin=lon1;
		
		dimension=lonfin;
		//Aqui ya tenemos la dimension del espacio, ahora hay q crear el cuadrado con informaci�n
		int [] [] info=new int [dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				boolean encontrado=false;
				for (int j2 = 0; j2 < nauf.size() && !encontrado; j2++) {
					if(nauf.get(j2).pertenece(norte-(i*distancia)-distancia,oeste+j*distancia)!=-1){
						info[i][j]=nauf.get(j2).pertenece(norte-(i*distancia)-distancia,oeste+j*distancia);
						encontrado=true;
					}
				}
			}
		}
		//Por comodidad de acceso vamos a pasar la informacion a un arrayList 
		//recordar q empieza en 0 y no 1 como esta la matriz imaginaria
		ArrayList<Integer> infoOrden = new ArrayList<Integer>();
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				//System.out.println("nauf i:"+i+" j: "+j+" "+infoOrden.get(0));
				int naufragos=info[i][j];
				infoOrden.add(naufragos);
			}
		}
		//Aprendizaje
		
		for (int i = 0; i < infoOrden.size(); i++) {
			Punto aux=new Punto(punto1.getN()+distancia/2-(i/dimension)*distancia,punto1.getE()+distancia/2+distancia*(i % dimension),0);
			infoOrden.set(i,(int) (infoOrden.get(i)+infoOrden.get(i)*valoracion(aux, punto_inicial, probabilidadAngulo, probabilidadVelocidad, tiempo)));
		}
		//Fin aprendizaje
		//Ahora hay q elegir los puntos inciales del avi�n, los de las trayectorias q se generan primero
		//Si esta dentro del cuadrado y si esta fuera
		ArrayList<Integer> puntosinciales=new ArrayList<Integer>();
		if(avion.getE()<=auxO && avion.getE()>=oeste && avion.getN()<= norte && avion.getN() >=auxN){//si esta dentro
			//miramos en que punto esta y cogemos todos los adyacentes
			//para ver su casilla primero miramos
			int i=0,j=0;
			double	calcN=norte-nauf.get(0).get_varN();
			while(avion.getN()<calcN){
				i++;
				calcN-=nauf.get(0).get_varN();
			}
			double calcE=oeste;
			while(avion.getE()>calcE){
				j++;
				calcE+=nauf.get(0).get_varE();
			}
			//estara en la posicion i,j
			int casilla=1;
			casilla=casilla+i*dimension+j;
			//Ahora hay q sacar todos los adyacentes a esta casilla
			puntosinciales=adyacentes(casilla);
			
		}else{//si ela vi�n esta fuera
			//pillamos los mas cercanos primero todos con sus distancias y seleccionamos
			ArrayList<Tupla> tupla= new ArrayList<Tupla>();
			for(int i=1;i<=dimension;i++){
				//borde superior
				tupla.add(new Tupla(distancia(avion.getE(),avion.getN(),norte-nauf.get(0).get_varN(),oeste+(i-1)*nauf.get(0).get_varE()),i));
				//borde inferior
				tupla.add(new Tupla(distancia(avion.getE(),avion.getN(),norte-dimension*nauf.get(0).get_varN(),oeste+(i-1)*nauf.get(0).get_varE()),(dimension*dimension)-dimension+1));
				//borde izq
				tupla.add(new Tupla(distancia(avion.getE(),avion.getN(),norte-i*nauf.get(0).get_varN(),oeste),dimension*(i-1)+1));
				//borde dcho
				tupla.add(new Tupla(distancia(avion.getE(),avion.getN(),norte-i*nauf.get(0).get_varN(),oeste+(dimension-1)*nauf.get(0).get_varE()),i*dimension));
			}
			//Aqui ya tenemos un arraylist con todos los valores de distancia y casillas solo habria que coger los adecuados
			while(tupla.size()>dimension*2){
				int pos=0;
				double valor=tupla.get(0).getDistancia();
				for(int i=1;i<tupla.size();i++){
					if(tupla.get(i).getDistancia()>valor){
						valor=tupla.get(i).getDistancia();
						pos=i;
					}
				}
				tupla.remove(pos);
			}
			//Ahora lo pasamos a puntos inciales
			for(int i=0;i<tupla.size();i++){
				puntosinciales.add(tupla.get(i).getCasilla());
			}
			
			
		}
		
		//System.out.println("tam puntos ini:"+puntosinciales.size());
		//Aqui llamamos a la que genera las trayectorias con los puntos inciales
		randomTrayectoriesAlt(puntosinciales);
		//System.out.println("fin tray aleatorias");
		for(int j=0;j<100;j++){
			//System.out.println("CrossOver N�: "+j);
			//Aqui selecionamos dos como padres
			ArrayList<Double> valores=new ArrayList<Double>();
			double sumvalores=0;
			for(int i=0;i<trayectorias.size();i++){
				valores.add(valor(trayectorias.get(i),infoOrden));
				sumvalores+=valores.get(i);
			}
			//System.out.println("El valor de todos: "+sumvalores);
			//Aqui meteremos el rango de valores 0-360 de cada trayectoria para la eleccion por ruleta
			ArrayList<Double> rango=new ArrayList<Double>();
			double cuenta=0;
			for(int i=0;i<valores.size();i++){
				rango.add(cuenta+(valores.get(i)*360)/sumvalores);
				cuenta+=(valores.get(i)*360)/sumvalores;
			}
			
			//Aqui tenemos que elegir dos numeros aleatorios que nos daran los padres
			int num1=new Random().nextInt(360);
			int num2=new Random().nextInt(360);
			int aux1=-1;
			int aux2=-1;
			//System.out.println(num1+ " "+num2);
			for (int i = 0; i < rango.size() ; i++) {
				if(num1<rango.get(i) && aux1==-1)
					aux1=i;
				if(num2<rango.get(i) && aux2==-1)
					aux2=i;
			}
			//System.out.println(aux1+ " "+aux2);
			int abc=0;
			while(aux1==aux2 &&abc<1000){
				num2=new Random().nextInt(360);
				aux2=-1;
				for (int i = 0; i < rango.size() ; i++) {
					if(num2<rango.get(i) && aux2==-1)
						aux2=i;
				}
				abc++;
			}
			
			//aux1 y aux2 me dan lso indices del arrylist trayectorias que coinciden con los padres
			//System.out.println("padres elegidos");
			//Aqui los cruzamos
			if(aux1!=-1 && aux2!=-1)
			crossover(trayectorias.get(aux1),trayectorias.get(aux2),infoOrden);

			//Aqui los mutamos

			//repetir proceso varias veces

		}
		//System.out.println("Crossover terminado");
		//Aqui ya podemos devolver el mejor
		ArrayList<Double> valores=new ArrayList<Double>();
		double max=0;
		int posmax=0;
		for(int i=0;i<trayectorias.size();i++){
			valores.add(valor(trayectorias.get(i),infoOrden));
			if(valor(trayectorias.get(i),infoOrden)>max){
				max=valor(trayectorias.get(i),infoOrden);
				posmax=i;
			}
		}
		// La ruta elegida es el de trayectorias[posmax]
		return trayectorias.get(posmax);
		
	}
	private double distancia(double este1, double norte, double norte2, double este2) {
		return Math.sqrt( ((este1-este2) * (este1-este2)) + ((norte-norte2) * (norte-norte2)) );
	}
	/**
	 * Me devuelve todas las casilla adyacentes a la proporcionada
	 * @param casilla casilla de la que se quieren obtener los adyacentes
	 * @return ArrayList con las casillas
	 */
	private ArrayList<Integer> adyacentes(int casilla) {
		ArrayList<Integer> aux= new ArrayList<Integer>();
		if(casilla-dimension>0)
			aux.add(casilla-dimension);
		if(casilla % dimension!=0 && casilla-dimension+1>0)
			aux.add(casilla-dimension+1);
		if(casilla % dimension!=0)
			aux.add(casilla+1);
		if(casilla % dimension!=0 && casilla+dimension+1<=dimension*dimension)
			aux.add(casilla+dimension+1);
		if(casilla+dimension<=dimension*dimension)
			aux.add(casilla+dimension);
		if(casilla % dimension !=1 && casilla+dimension-1<=dimension*dimension)
			aux.add(casilla+dimension-1);
		if(casilla % dimension !=1)
			aux.add(casilla-1);
		if(casilla % dimension !=1 && casilla-dimension-1>0)
			aux.add(casilla-dimension-1);
			
		return aux;
	}
	private double masNorte(ArrayList<CasillaNaufrago> nauf){
		double aux=0.0;
		for (int i = 0; i < nauf.size(); i++) {
			if(nauf.get(i).get_nmin()+nauf.get(i).get_varN()>aux)
				aux=nauf.get(i).get_nmin()+nauf.get(i).get_varN();
		}
		return aux;
	}
	
	private double masSur(ArrayList<CasillaNaufrago> nauf){
		double aux=Double.MAX_VALUE;
		for (int i = 0; i < nauf.size(); i++) {
			if(nauf.get(i).get_nmin()<aux)
				aux=nauf.get(i).get_nmin();
		}
		return aux;
	}
	
	private double masEste(ArrayList<CasillaNaufrago> nauf){
		double aux=0.0;
		for (int i = 0; i < nauf.size(); i++) {
			if(nauf.get(i).get_emin()+nauf.get(i).get_varE()>aux)
				aux=nauf.get(i).get_emin()+nauf.get(i).get_varE();
		}
		return aux;
	}
	
	private double masOeste(ArrayList<CasillaNaufrago> nauf){
		double aux=Double.MAX_VALUE;
		for (int i = 0; i < nauf.size(); i++) {
			if(nauf.get(i).get_emin()<aux)
				aux=nauf.get(i).get_emin();
		}
		return aux;
	}
	/**
	 * Funcion que elige los padres
	 * @return Devuelve los padres que van a ser cruzados
	 */
	private ArrayList<ArrayList<Integer>> padres(ArrayList<Integer> infoOrden){
		ArrayList<ArrayList<Integer>> padres=new ArrayList<ArrayList<Integer>>(2);
		ArrayList<Double> valores= new ArrayList<Double>();
		for (int i = 0; i < valores.size(); i++) {
			//Lista con todos los valores
			valores.add(valor(trayectorias.get(i),infoOrden));
		}
		//Aqui hay que ahcer el metodo de la ruleta
		return padres;
	}
	
	private double valor(ArrayList<Integer> tray,ArrayList<Integer> infoOrden){
		ArrayList<Integer> visitados=new ArrayList<Integer>();
		double valor=0.0;
		int casillaact=tray.get(0);
		valor+=infoOrden.get(casillaact-1);
		visitados.add(tray.get(0));
		for(int i=1;i<=tray.size()-2;i++){
			switch (tray.get(i)) {
			case 1:
				casillaact-=dimension;
				break;
			case 2:
				casillaact=casillaact-dimension+1;
				break;
			case 3:
				casillaact+=1;
				break;
			case 4:
				casillaact=casillaact+dimension+1;
				break;
			case 5:
				casillaact+=dimension;
				break;
			case 6:
				casillaact=casillaact+dimension-1;
				break;
			case 7:
				casillaact-=1;
				break;
			case 8:
				casillaact=casillaact-dimension-1;
				break;

			default:
				break;
			}
			if(!visitados.contains(casillaact)){
				valor+=infoOrden.get(casillaact-1);
				visitados.add(casillaact);
			}
		}
		return valor;
	}
	
	public Ruta dameRuta(ArrayList<CasillaNaufrago> nauf,Punto avion){
		Ruta route=new Ruta();
		Punto aux;
		trayectorias.clear();
		ArrayList<Integer> ruta=contruccionEspacio(nauf, avion,null,null,null,0);
		aux=new Punto(punto1.getN()+distancia/2-(((ruta.get(0)-1)/dimension)*distancia),punto1.getE()+((ruta.get(0)-1)% dimension)*distancia+distancia/2,0);
		route.añadePunto(aux);
		Punto aux1=new Punto(punto1.getN()+distancia/2-(((ruta.get(0)-1)/dimension)*distancia),punto1.getE()+((ruta.get(0)-1)% dimension)*distancia+distancia/2,0);
		for (int i = 1; i < ruta.size(); i++) {
			Punto a= new Punto(0,0,0);
			switch (ruta.get(i)) {
			case 1:
				a.setE(aux1.getE());
				a.setN(aux1.getN()+distancia);
				aux1.setN(aux1.getN()+distancia);
				break;
			case 2:
				a.setE(aux1.getE()+distancia);
				a.setN(aux1.getN()+distancia);
				aux1.setE(aux1.getE()+distancia);
				aux1.setN(aux1.getN()+distancia);
				break;
			case 3:
				a.setE(aux1.getE()+distancia);
				a.setN(aux1.getN());
				aux1.setE(aux1.getE()+distancia);
				aux1.setN(aux1.getN());
				break;
			case 4:
				a.setE(aux1.getE()+distancia);
				a.setN(aux1.getN()-distancia);
				aux1.setE(aux1.getE()+distancia);
				aux1.setN(aux1.getN()-distancia);
				break;
			case 5:
				a.setE(aux1.getE());
				a.setN(aux1.getN()-distancia);
				aux1.setE(aux1.getE());
				aux1.setN(aux1.getN()-distancia);
				break;
			case 6:
				a.setE(aux1.getE()-distancia);
				a.setN(aux1.getN()-distancia);
				aux1.setE(aux1.getE()-distancia);
				aux1.setN(aux1.getN()-distancia);
				break;
			case 7:
				a.setE(aux1.getE()-distancia);
				a.setN(aux1.getN());
				aux1.setE(aux1.getE()-distancia);
				aux1.setN(aux1.getN());
				break;
			case 8:
				a.setE(aux1.getE()-distancia);
				a.setN(aux1.getN()+distancia);
				aux1.setE(aux1.getE()-distancia);
				aux1.setN(aux1.getN()+distancia);
				break;

			default:
				break;
			}
			route.añadePunto(a);
		}
		return route;
	}
	

	public Ruta dameRuta(int tipoVehiculo, Punto posicion_actual,Punto punto_inicial, int tiempo,
			FuncionProbabilidadAbstracta probabilidadAngulo,FuncionProbabilidadAbstracta probabilidadVelocidad,
			ProbabilidadNaufragoCasillas probabilidad) {
		Ruta route=new Ruta();
		Punto aux;
		trayectorias.clear();
		//System.out.println("tama�o "+ probabilidad.dameTablero().get(probabilidad.normalizar(tiempo)).size());
		//System.out.println(posicion_actual.getN()+" "+posicion_actual.getE());
		
		//estimamos timpo de llegada del avion
		//double dist=distancia(posicion_actual.getE(), posicion_actual.getN(), punto_inicial.getN(), punto_inicial.getE());
		//double time=(dist/100)*3600;
		//fin estimacion
		ArrayList<Integer> ruta=contruccionEspacio(probabilidad.dameTablero().get(probabilidad.normalizar(tiempo)), posicion_actual,punto_inicial,probabilidadAngulo,probabilidadVelocidad,tiempo);

		aux=new Punto(punto1.getN()+distancia/2-(((ruta.get(0)-1)/dimension)*distancia),punto1.getE()+((ruta.get(0)-1)% dimension)*distancia+distancia/2,0);
		route.añadePunto(aux);
		Punto aux1=new Punto(punto1.getN()+distancia/2-(((ruta.get(0)-1)/dimension)*distancia),punto1.getE()+((ruta.get(0)-1)% dimension)*distancia+distancia/2,0);
		for (int i = 1; i < ruta.size(); i++) {
			Punto a= new Punto(0,0,0);
			switch (ruta.get(i)) {
			case 1:
				a.setE(aux1.getE());
				a.setN(aux1.getN()+distancia);
				aux1.setN(aux1.getN()+distancia);
				break;
			case 2:
				a.setE(aux1.getE()+distancia);
				a.setN(aux1.getN()+distancia);
				aux1.setE(aux1.getE()+distancia);
				aux1.setN(aux1.getN()+distancia);
				break;
			case 3:
				a.setE(aux1.getE()+distancia);
				a.setN(aux1.getN());
				aux1.setE(aux1.getE()+distancia);
				aux1.setN(aux1.getN());
				break;
			case 4:
				a.setE(aux1.getE()+distancia);
				a.setN(aux1.getN()-distancia);
				aux1.setE(aux1.getE()+distancia);
				aux1.setN(aux1.getN()-distancia);
				break;
			case 5:
				a.setE(aux1.getE());
				a.setN(aux1.getN()-distancia);
				aux1.setE(aux1.getE());
				aux1.setN(aux1.getN()-distancia);
				break;
			case 6:
				a.setE(aux1.getE()-distancia);
				a.setN(aux1.getN()-distancia);
				aux1.setE(aux1.getE()-distancia);
				aux1.setN(aux1.getN()-distancia);
				break;
			case 7:
				a.setE(aux1.getE()-distancia);
				a.setN(aux1.getN());
				aux1.setE(aux1.getE()-distancia);
				aux1.setN(aux1.getN());
				break;
			case 8:
				a.setE(aux1.getE()-distancia);
				a.setN(aux1.getN()+distancia);
				aux1.setE(aux1.getE()-distancia);
				aux1.setN(aux1.getN()+distancia);
				break;

			default:
				break;
			}
			route.añadePunto(a);
		}
		route.setBucle(false);
		return route;
	}
	
	public static double valoracion(Punto punto,Punto puntoInicial,FuncionProbabilidadAbstracta probabilidadAngulo,
			FuncionProbabilidadAbstracta probabilidadVelocidad,int tiempo){
		double angulo = Math.atan2(puntoInicial.getN()-punto.getN(),puntoInicial.getE()-punto.getE());
		double distancia = distancia(puntoInicial,punto);
		double probabilidadA=probabilidadAngulo.evalua(angulo);
		double probabilidadV=probabilidadVelocidad.evalua(distancia/tiempo);
		return probabilidadA*probabilidadV;
	}
	
	public static double distancia(Punto p1,Punto p2){
		double de=Math.pow(p1.getE()-p2.getE(),2);
		double dn=Math.pow(p1.getN()-p2.getN(),2);
		return Math.sqrt(dn+de);
	}
}
