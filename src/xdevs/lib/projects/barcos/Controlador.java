package xdevs.lib.projects.barcos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.lib.projects.graph.models.DatosTipoPosicion;
import xdevs.lib.projects.graph.structs.ListaPosicion;
import xdevs.lib.projects.graph.structs.Punto;
import xdevs.lib.projects.graph.structs.Ruta;
import xdevs.lib.projects.math.FuncionProbabilidadAbstracta;
import xdevs.lib.projects.math.MaximaVerosimilitud;

public class Controlador extends AtomicState {
	
	private boolean rescatanAviones;
	
	
	FuncionProbabilidadAbstracta probabilidadAngulo;
	
	FuncionProbabilidadAbstracta probabilidadVelocidad;
	
	FuncionProbabilidadAbstracta probabilidadAnguloReal;
	
	FuncionProbabilidadAbstracta probabilidadVelocidadReal;
	
	ListaEnviar listaEnviar;
	
	public static int num_encontrados=0;
	
	
	/** Constante para la solicitud de reset */
	public static final int CreaNaufragos = 1;
	
	/** Constante para la solicitud de reset */
	public static final int CreaAviones = 2;
	
	/** Constante para la solicitud de reset */
	public static final int CreaBarcos = 3;
	
	/** Constante para la solicitud de reset */
	public static final int ActualizaPosicion = 4;
	
	public static final String tiempo = "tiempo";
	public static final String estado = "estado";
	public static final String numAviones = "numAviones";
	public static final String numBarcos = "numBarcos";
	
	//acciones que va realizando
	public static int crea=1;
	public static int creaAviones=2;
	public static int creaBarcos=3;
	public static int inicia=4;
	public static int daRumbo=5;
	public static int espera=6;
	
	private int avion_creado=0;
	private int barco_creado=0;
	private ArrayList<Rescate> rescates;
	private ArrayList<Aprendizaje> evolucion;
	private int num_naufragos=0;
	private boolean escribir_historial=false;
	
	/** Constante para el puerto de entrada de las solicitudes de los aviones */
	public static final String InAviones = "INA";
	
	/** Constante para el puerto de entrada de las solicitudes del reloj */
	public static final String InReloj = "INR";
	
	
	/** Constante para el puerto de entrada de las solicitudes de los aviones */
	public static final String InControladorAviones = "INCA";
	
	
	/** Constante para el puerto de entrada de las solicitudes de los controladores
	 * de los barcos */
	public static final String InControladorBarcos = "INCB";
	//tareas, llega movimiento de avion:
	//detecta:
	//choque -> se�al destruccion avion
	//encuentra naufrago -> envia se�al a barco
	
	/** Constante para el puerto de entrada de las solicitudes de los barcos */
	public static final String InBarcos = "INB";
	
	/** Constante para el puerto de entrada de las solicitudes del terreno */
	public static final String InTerreno = "INT";
	
	/** Constante para el puerto de entrada de las solicitudes de los naufragos */
	public static final String InNaufragos = "INN";
	
	/** Constante para el puerto de salida de las solicitudes a los aviones */
	public static final String OutAviones = "OUTA";
	
	/** Constante para el puerto de salida para la visualizacion en google earth */
	public static final String salidaGE = "salidaGoogle";
	
	/** Constante para el puerto de salida de las solicitudes a los aviones */
	public static final String OutControladorAviones = "OUTCA";
	
	/** Constante para el puerto de salida de las solicitudes a los aviones */
	public static final String OutControladorBarcos = "OUTCB";
	
	/** Constante para el puerto de salida de las solicitudes a los barcos */
	public static final String OutBarcos = "OUTB";
	
	/** Constante para el puerto de salida de las solicitudes a los naufragos */
	public static final String OutNaufragos = "OUTN";
	
	ListaPosicion posiciones;
	public Algoritmo algoritmo;
	
	private ProbabilidadNaufrago _probabilidad_naufrago;
	private ProbabilidadNaufragoCasillas _probabilidad_naufrago_casillas;
	private double tiempoSimulacion;
	private boolean ficheroCerrado=false;
	private boolean guardarFichero;
	private double velocidad_avion;
	double[][] terreno;
	int tamaño_matriz_terreno;

	
	public static final String posn_ini = "posn_ini";
	
	public static final String pose_ini = "pose_ini";
	
	public boolean _xmlOSer =false;
	
	Punto pinicial=new Punto(0,0,0);
	private String nombreEjecucion;
	double posicion_n_avion;
	double posicion_e_avion;
	double distanciaAvion;
	double posicion_n_barco;
	double posicion_e_barco;
	double distanciaBarco;
	
	public double tiempo_ini=0;
	/** Constructor 
	 * @param tama�o */
	public Controlador(String name,double tiempoIni, int numaviones,int numbarcos,int numnaufragos,
			boolean guardarXML,String nombreEjecucion,boolean XmlOSer,Double velocidadAvion,
			Double posicionNAvion, Double posicionEAvion, Double distanciaCuadradoAvion,
			Double posicionNBarco, Double posicionEBarco, Double distanciaCuadradoBarco
			,boolean rescatanaviones) {
		super(name);
		this.rescatanAviones=rescatanaviones;
		velocidad_avion=velocidadAvion;
		posicion_n_avion=posicionNAvion;
		posicion_e_avion=posicionEAvion;
		distanciaAvion=distanciaCuadradoAvion;
		posicion_n_barco=posicionNBarco;
		posicion_e_barco=posicionEBarco;
		distanciaBarco=distanciaCuadradoBarco;
		this._xmlOSer=XmlOSer;
		this.guardarFichero=guardarXML;
		this.rescates=new ArrayList<Rescate>();
		this.evolucion= new ArrayList<Aprendizaje>();
		this.tiempo_ini=tiempoIni;
		this.num_naufragos=numnaufragos;
		probabilidadAngulo=new MaximaVerosimilitud(1,1,0);
		probabilidadVelocidad=new MaximaVerosimilitud(3,3,0);
		probabilidadAnguloReal=new MaximaVerosimilitud(1,1,0);
		probabilidadVelocidadReal=new MaximaVerosimilitud(3,3,0);
		algoritmo = new Genetic(0,10);
		this.nombreEjecucion=nombreEjecucion;
		try{
			
			/*
			 * Ahora mismo:
			 * 	-Si guardamos, leemos y al final guardamos
			 * 	-Si no guardamos, leemos simplemente
			 */
		//	if(!guardarFichero){
			if(_xmlOSer){
			this._probabilidad_naufrago.leerFichero(this.nombreEjecucion);
			}
			else{
	        FileInputStream fis = null;
	        ObjectInputStream in = null;
	        try {
	            fis = new FileInputStream("probabilidad2.nau");
	            in = new ObjectInputStream(fis);
	            _probabilidad_naufrago_casillas = (ProbabilidadNaufragoCasillas)in.readObject();
	        	tiempoSimulacion = _probabilidad_naufrago.dameTiempoFinal();
	            in.close();
	            System.out.println("fichero leido");
	        } catch(IOException ex) {            
	        	_probabilidad_naufrago_casillas = new ProbabilidadNaufragoCasillas(0,3600*10,60,1000,1000);
	        	tiempoSimulacion = _probabilidad_naufrago_casillas.dameTiempoFinal();
	        	System.out.println("fichero creado");
	        } catch(ClassNotFoundException ex) {
	            ex.printStackTrace();
	            _probabilidad_naufrago_casillas = new ProbabilidadNaufragoCasillas(0,3600*10,60,1000,1000);
	        	tiempoSimulacion = _probabilidad_naufrago_casillas.dameTiempoFinal();
	        	System.out.println("fichero creado");
	        }
			}
		//	}
		//	else{
		//		_probabilidad_naufrago = new ProbabilidadNaufrago(0,3600*10,360,50000,200000,4000,50000,200000,4000);
	     //   	tiempoSimulacion = _probabilidad_naufrago.dameTiempoFinal();
		//	}

		}
		catch(Exception e){
			System.err.println("fichero"+nombreEjecucion+" no creado");
            _probabilidad_naufrago = new ProbabilidadNaufrago(0,3600*10,360,50000,200000,4000,50000,200000,4000);
        	tiempoSimulacion = _probabilidad_naufrago.dameTiempoFinal();
		}
		this.addInPort(Controlador.InReloj);
		this.addInPort(InAviones);
		this.addInPort(InControladorAviones);
		this.addInPort(InControladorBarcos);
		this.addInPort(InBarcos);
		this.addInPort(InTerreno);
		this.addInPort(InNaufragos);
		this.addOutPort(OutAviones);
		this.addOutPort(OutControladorAviones);
		this.addOutPort(OutControladorBarcos);
		this.addOutPort(Controlador.salidaGE);
		this.addOutPort(OutBarcos);
		this.addOutPort(OutNaufragos);
		this.addState(tiempo);
		this.addState(numAviones);
		this.addState(numBarcos);
		this.setStateValue(numAviones, numaviones);
		this.setStateValue(numBarcos, numbarcos);
		this.setStateValue(tiempo, tiempoIni);
		this.addState(estado);
		this.addState(posn_ini);
		this.addState(pose_ini);
		listaEnviar=new ListaEnviar();
		this.setStateValue(estado, Controlador.crea);
		posiciones=new ListaPosicion();
		//System.out.println("constructor ayd");
		this.setSigma(0.1);
		
		//Genetico
	//	this._genetico=new Genetic(0, 15);
	//	_genetico.contruccionEspacio(_probabilidad_naufrago_casillas.dameTablero().get(0), new Punto(0,0,0));
		
	}

	
	public int barcoMasCercano(Punto punto){
		ArrayList<DatosTipoPosicion> posiciones_barcos= posiciones.filtraTipo(DatosTipoPosicion.Barco);
		int i =-1;
		double dist_min=Double.MAX_VALUE;
		double distancia=0;
		for(int cont=0;cont<posiciones_barcos.size();cont++){
			distancia=distancia(posiciones_barcos.get(cont).getPunto(), punto);
			if(distancia<dist_min) {
				dist_min=distancia;
				i=cont;
			}
		}
		return i;
	}
	

	
	public double distancia(Punto p1,Punto p2){
		double de=Math.pow(p1.getE()-p2.getE(),2);
		double dn=Math.pow(p1.getN()-p2.getN(),2);
		return Math.sqrt(dn+de);
	}

	@Override
	public void deltext(double e) {
		super.resume(e);
		if(((this.getStateValue(tiempo).intValue()%600)<100)&&(this.escribir_historial)){
			//apuntar datos y decir que no se apunten mas
			
			Aprendizaje actual =new Aprendizaje(this.getStateValue(tiempo).intValue(),this.probabilidadVelocidad.dameMedia(),this.probabilidadAngulo.dameMedia(),
					this.probabilidadVelocidad.dameVarianza(),this.probabilidadAngulo.dameVarianza());
			actual.ponDatosReales(this.probabilidadVelocidadReal.dameMedia(), this.probabilidadAnguloReal.dameMedia(),
					this.probabilidadVelocidadReal.dameVarianza(),this.probabilidadAnguloReal.dameVarianza());
		
			this.evolucion.add(
					actual
					);
			
			escribir_historial=false;
		}
		if((this.getStateValue(tiempo).intValue()%600)>100){
			//volver a apuntar datos de aprendizaje
			escribir_historial=true;
		}
		//PIDE PUNTO REFERENCIA
		//BUSCAR PUNTO REFERENCIA, ENVIAR EL TIEMPO PASADO
		//puede llegar desde
		// avion
		//*******************************************************************************
		//*******************************************AVION ******************************
		//*******************************************************************************
		Iterator iteradorAvion = arg1.getValuesOnPort(Controlador.InAviones).iterator();
		//Vector solicitud;

		Vector entrada=null;
		Vector lista=null;
		while(iteradorAvion.hasNext()){
		entrada = (Vector) iteradorAvion.next();
		lista = (Vector) entrada.get(0);
		for(int cont=0;cont<lista.size();cont++){
			DatoMux dato = (DatoMux)lista.get(cont);
			Vector solicitud = (Vector)dato.dameDato();
			Vector posicion = (Vector)solicitud.get(0);
			double e= (Double)posicion.get(0);
			double n= (Double)posicion.get(1);
			double h= (Double)posicion.get(2);
			Punto punto= new Punto(n,e,h);
			DatosTipoPosicion datosp=new DatosTipoPosicion((Integer)solicitud.get(6),DatosTipoPosicion.Avion,punto);
			
			
			
			this.posiciones.añadePosicion(datosp);
			int pos=this.posiciones.buscaPosicion(datosp);
			if(this.getStateValue(tiempo).intValue()>this.tiempo_ini){
			if((posiciones.dameLista().get(pos).ruta==null)){
				probabilidadAngulo.NuevoValor(true, this.probabilidadAnguloReal.dameMedia());
				probabilidadVelocidad.NuevoValor(true, this.probabilidadVelocidadReal.dameMedia());
			}
			//System.out.println("media angulo real"+this.probabilidadAnguloReal.dameMedia());
			//System.out.println("media velocidad real"+this.probabilidadVelocidadReal.dameMedia());
			if((posiciones.dameLista().get(pos).geRuta()==null)||(posiciones.dameLista().get(pos).geRuta().dameRestantes()<1)){
				//System.out.println("pide ruta");
				Ruta rutaActual = algoritmo.dameRuta(1,punto,this.pinicial ,
				this.getStateValue("tiempo").intValue(), probabilidadAnguloReal, 
				probabilidadVelocidadReal, this._probabilidad_naufrago_casillas);
				posiciones.dameLista().get(pos).ponRuta(rutaActual,true);
			}
			}
			//si pasa cerca del algun naufrago, poner que lo encuentra y cambiar probabilidad.
			ArrayList<DatosTipoPosicion> listaNaufragos = this.posiciones.filtraTipo(DatosTipoPosicion.Naufrago);
			double velocidad=0;
			double distancia=0;
			double angulo=0;
		
			int num_encontrados_ant=num_encontrados;
			
			for(int cont2=0;cont2<listaNaufragos.size();cont2++){
				if(
						(Math.abs(punto.getE()-listaNaufragos.get(cont2).getPunto().getE())<1000)&&
						(Math.abs(punto.getN()-listaNaufragos.get(cont2).getPunto().getN())<1000)
						){
				//if(distancia(punto,listaNaufragos.get(cont2).punto)<1000){
					num_encontrados++;
					
					angulo = Math.atan2(-this.pinicial.getN()+punto.getN(), -this.pinicial.getE()+punto.getE());
					
					
					distancia = distancia(this.pinicial,punto);
					velocidad=distancia/this.getStateValue(tiempo).doubleValue();
					this.probabilidadAngulo.NuevoValor(true, angulo);
					this.probabilidadVelocidad.NuevoValor(true, velocidad);
					
					///////////
					rescates.add(new Rescate((Integer)solicitud.get(6),this.getStateValue("tiempo").intValue()));
					//enviar algo al barco encontrado
					int nombreNaufrago=(listaNaufragos.get(cont2).getNombre());
					Vector encontrado = new Vector();
					encontrado.add(Naufrago.Encontrar);
					encontrado.add((Integer)nombreNaufrago);
					this.posiciones.eliminaPosicion(nombreNaufrago,DatosTipoPosicion.Naufrago);
					if(rescatanAviones){
					listaEnviar.añadePar(new ParEnviar(Controlador.OutNaufragos,encontrado));
					}
					
					
					
				};
			}

			
			if(((this.getStateValue(tiempo).doubleValue()%20)==0)&&((Integer)solicitud.get(6)==0)){
			//	System.out.println("tiempo busqueda "+(this.getStateValue(tiempo).doubleValue()-tiempo_ini)+"encontrados"+num_encontrados);
			}
		}
		}
		
		
		
		//*******************************************************************************
		//*******************************************BARCOS******************************
		//*******************************************************************************
		
		//BUSCAR PUNTO REFERENCIA, ENVIAR EL TIEMPO PASADO
		//puede llegar desde
		// avion
		Iterator iteradorBarco = arg1.getValuesOnPort(Controlador.InBarcos).iterator();
		//Vector solicitud;

		while(iteradorBarco.hasNext()){
		entrada = (Vector) iteradorBarco.next();
		lista = (Vector) entrada.get(0);
		for(int cont=0;cont<lista.size();cont++){
			DatoMux dato = (DatoMux)lista.get(cont);
			Vector solicitud = (Vector)dato.dameDato();
			Vector posicion = (Vector)solicitud.get(0);
			double e= (Double)posicion.get(1);
			double n= (Double)posicion.get(0);
			
			Punto punto= new Punto(n,e,0);
			DatosTipoPosicion datosp=new DatosTipoPosicion((Integer)posicion.get(9),DatosTipoPosicion.Barco,punto);
			this.posiciones.añadePosicion(datosp);
			int pos=this.posiciones.buscaPosicion(datosp);
			if(this.getStateValue(tiempo).intValue()>this.tiempo_ini){
				if((posiciones.dameLista().get(pos).ruta==null)){
					probabilidadAngulo.NuevoValor(true, this.probabilidadAnguloReal.dameMedia());
					probabilidadVelocidad.NuevoValor(true, this.probabilidadVelocidadReal.dameMedia());
				}
				if((posiciones.dameLista().get(pos).ruta==null)||(posiciones.dameLista().get(pos).ruta.dameRestantes()<1)){
					//System.out.println("nueva ruta barco");
					//System.out.println("punto inicial e"+this.pinicial.getE()+"n"+pinicial.getN());
					Ruta rutaActual = algoritmo.dameRuta(2,punto,this.pinicial ,
					this.getStateValue("tiempo").intValue(), probabilidadAngulo, probabilidadVelocidad, this._probabilidad_naufrago_casillas);
					posiciones.dameLista().get(pos).ponRuta(rutaActual,true);
				}
			}
			
			//si pasa cerca del algun naufrago, poner que lo encuentra y cambiar probabilidad.
			ArrayList<DatosTipoPosicion> listaNaufragos = this.posiciones.filtraTipo(DatosTipoPosicion.Naufrago);
			double velocidad=0;
			double distancia=0;
			double angulo=0;
		
			int num_encontrados_ant=num_encontrados;
			
			for(int cont2=0;cont2<listaNaufragos.size();cont2++){
				if(
						(Math.abs(punto.getE()-listaNaufragos.get(cont2).punto.getE())<1000)&&
						(Math.abs(punto.getN()-listaNaufragos.get(cont2).punto.getN())<1000)
						){
				//if(distancia(punto,listaNaufragos.get(cont2).punto)<1000){ //vista circular
				//	System.out.println("naufrago"+listaNaufragos.get(cont2).nombre+
				//			"barco"+(Integer)posicion.get(9)+"tiempo"+this.getStateValue("tiempo").doubleValue());
					rescates.add(new Rescate((Integer)posicion.get(9),this.getStateValue("tiempo").intValue()));
					//enviar algo al barco encontrado
					int nombreNaufrago=(listaNaufragos.get(cont2).nombre);
					Vector encontrado = new Vector();
					encontrado.add(Naufrago.Encontrar);
					encontrado.add((Integer)nombreNaufrago);
					this.posiciones.eliminaPosicion(nombreNaufrago,DatosTipoPosicion.Naufrago);
					
					listaEnviar.añadePar(new ParEnviar(Controlador.OutNaufragos,encontrado));
					num_encontrados++;
					angulo = Math.atan2(this.pinicial.getN()-punto.getN(), this.pinicial.getE()-punto.getE());
					distancia = distancia(this.pinicial,punto);
					velocidad=distancia/this.getStateValue(tiempo).doubleValue();
					this.probabilidadAngulo.NuevoValor(true, angulo);
					this.probabilidadVelocidad.NuevoValor(true, velocidad);
					
				};
			}
		}
		}
		
		//------------------CONTROLADOR AVIONES
		Iterator iteradorControladorAvion = arg1.getValuesOnPort(Controlador.InControladorAviones).iterator();
		while(iteradorControladorAvion.hasNext()){
			entrada = (Vector) iteradorControladorAvion.next();
			lista = (Vector) entrada.get(0);
			DatoMux dato =null;
			for(int cont=0;cont<lista.size();cont++){

				dato = (DatoMux)lista.get(cont);
				//System.out.println("llega el avion "+((Vector)dato.dameDato()).get(1));
				
				listaEnviar.añadePar(
						enviaPunto(
								(Integer)((Vector)dato.dameDato()).get(1),
								DatosTipoPosicion.Avion, 
								Controlador.OutControladorAviones)
						);
				//System.out.println("llega a algoritmo de controlador" + " "+ ((Vector)dato.dameDato()).get(1));
			}

			
		}
		
		//-----------------CONTROLADOR BARCO
		Iterator iteradorControladorBarco = arg1.getValuesOnPort(Controlador.InControladorBarcos).iterator();
		while(iteradorControladorBarco.hasNext()){
			entrada = (Vector) iteradorControladorBarco.next();
			lista = (Vector) entrada.get(0);

			for(int cont=0;cont<lista.size();cont++){
				DatoMux dato = (DatoMux)lista.get(cont);
				ParEnviar enviar =enviaPunto((Integer)((Vector)dato.dameDato()).get(1),
						DatosTipoPosicion.Barco,
						Controlador.OutControladorBarcos);
				listaEnviar.añadePar(
						enviar
				);

			}
			
		}
		
			
		//-----------------NAUFRAGOS
		Iterator iteradorNaufragos = arg1.getValuesOnPort(Controlador.InNaufragos).iterator();
		while(iteradorNaufragos.hasNext()){
			entrada = (Vector) iteradorNaufragos.next();
			lista= (Vector) entrada.get(0);
			
			for(int cont=0;cont<lista.size();cont++){
				DatoMux dato = (DatoMux)lista.get(cont);
				Vector solicitud = (Vector)dato.dameDato();
				
				double e= (Double)solicitud.get(1);
				double n= (Double)solicitud.get(0);
				double h= 0.0;
				Punto punto= new Punto(n,e,h);
				
				this.setStateValue(tiempo, (Double)solicitud.get(2));	
				//apuntar probabilidad real
				double angulo = Math.atan2(this.pinicial.getN()-punto.getN(), this.pinicial.getE()-punto.getE());
				double distancia = distancia(this.pinicial,punto);
			
				double velocidad=distancia/this.getStateValue(tiempo).doubleValue();
				if((this.getStateValue(tiempo).intValue()>100)&&(velocidad!=Double.NaN)&&(velocidad!=Double.POSITIVE_INFINITY)&&(velocidad!=Double.NEGATIVE_INFINITY)){
				this.probabilidadAnguloReal.NuevoValor(true, angulo);
				this.probabilidadVelocidadReal.NuevoValor(true, velocidad);
				}

				
				DatosTipoPosicion datosp=new DatosTipoPosicion((Integer)solicitud.get(3),DatosTipoPosicion.Naufrago,punto);
				if(this.guardarFichero==true){
					
				if(((Double)solicitud.get(2)>this.tiempoSimulacion)&&(this.ficheroCerrado==false)){
					if(this._xmlOSer){
						System.out.println("fichero xml cerrado");
						this._probabilidad_naufrago.escribirFichero(this.nombreEjecucion);
						ficheroCerrado=true;
					}
					else{
						  FileOutputStream fos = null;
			               ObjectOutputStream out = null;
			                try {
			                    fos = new FileOutputStream("probabilidad2.nau");
			                    out = new ObjectOutputStream(fos);
			                    out.writeObject(_probabilidad_naufrago_casillas);
			                    out.close();
			                    ficheroCerrado=true;
			                    System.out.println("fichero nau cerrado");
			                } catch(IOException ex) {
			                  //  ex.printStackTrace();
			                    System.err.println("fichero no cerrado");
			                }
			                

					}

				}
				if(!ficheroCerrado){
					if(_xmlOSer){
					this._probabilidad_naufrago.llegaNaufrago(n, e, (Double)solicitud.get(2));
					}
					else{
						this._probabilidad_naufrago_casillas.llegaNaufrago(n, e, (Double)solicitud.get(2));
					}
				}
				}

				this.posiciones.añadePosicion(datosp);
			}
				
		}
		listaEnviar.añadePar(new ParEnviar(Controlador.salidaGE,this.posiciones));
		
		setSigma(0);
	}

	private ParEnviar enviaPunto(Integer numero,int tipoVehiculo,String puerto) {
		DatosTipoPosicion posicionBuscar = new DatosTipoPosicion(numero,tipoVehiculo,null);
		int posicion=posiciones.buscaPosicion(posicionBuscar);
		posicionBuscar = this.posiciones.lista.get(posicion);
		Punto siguiente=null;
		
		//reducir
		double n=Double.MAX_VALUE,e=Double.MAX_VALUE,h=100;
		if(posicionBuscar!=null){
			e=posicionBuscar.getPunto().getE();
			n=posicionBuscar.getPunto().getN();
			if(posicionBuscar.ruta!=null){
			Punto actual= posicionBuscar.punto;
			Punto punto=posicionBuscar.ruta.damePuntoActual();
			posicionBuscar.ruta.avanzaPunto();
			//System.out.println("numero de puntos que qdan en ruta"+posicionBuscar.ruta.dameRestantes());
			e=punto.getE();
			n=punto.getN();
			}

		}
		

		h=posicionBuscar.punto.getH();

		siguiente = new Punto(n,e,h);
		//fin reducir
		 
		
		Vector solicitud= new Vector();
		if(posicionBuscar.tipoVehiculo==DatosTipoPosicion.Avion){
			solicitud.add(new Integer (ControladorRumboState.CamPosRef));
			//System.out.println("objetivo avion "+"e"+siguiente.getE()+"n"+siguiente.getN());
			solicitud.add(new Double(siguiente.getE()));
			solicitud.add(new Double(siguiente.getN()));
			solicitud.add(new Double(siguiente.getH()));	
			solicitud.add(numero);
		}
		if(posicionBuscar.tipoVehiculo==DatosTipoPosicion.Barco){
			solicitud.add(numero);
			//System.out.println("objetivo barco "+"e"+siguiente.getE()+"n"+siguiente.getN());
			solicitud.add(new Integer (ControladorBarco.cambioRumbo));
			solicitud.add(new Double(siguiente.getN()));
			solicitud.add(new Double(siguiente.getE()));
			solicitud.add(new Double(siguiente.getH()));
		}
		ParEnviar enviar= new ParEnviar(puerto, solicitud);
		return enviar;
	}

	

	
	@Override
	public void deltint() {

			setSigma(INFINITY);
	}

	@Override
	public DevsDessMessage lambda() {
		//enviar mensaje de creacion de naufragos
		DevsDessMessage msg=new DevsDessMessage();
		if(this.getStateValue(estado).doubleValue()==crea){
			Vector<Number> todo=new Vector<Number>();	
			todo.add(0,((Integer)(Naufrago.Crea)));
			//mas adelante hacer aleatorio.
			double norte =125000;
			double este =150000;
			todo.add(1,(Double)(norte));//norte
			this.setStateValue(posn_ini, norte);
			todo.add(2,(Double)(este));//este
			this.setStateValue(pose_ini, este);
			todo.add(3,(Double)(0.0));//este
			this.pinicial=new Punto(norte,este,0);
			msg.add(Controlador.OutNaufragos,todo);
			this.setStateValue(estado, Controlador.creaAviones);
		}
		else{
			if (this.getStateValue(estado).doubleValue()==Controlador.creaAviones){
				Vector<Number>todo = new Vector<Number>();
				todo.add(0,((Integer)(AvionState.Reset)));
				Random randomizador= new Random();
				todo.add(1,new Double((randomizador.nextDouble())*this.distanciaAvion)+this.posicion_e_avion);
				todo.add(2,new Double((randomizador.nextDouble())*this.distanciaAvion)+this.posicion_n_avion);
				todo.add(3,new Double(1000));
				todo.add(4,new Double(velocidad_avion));
				todo.add(5,new Double(70));
				todo.add(6,new Double(0));
				todo.add(7,new Integer(this.avion_creado));
				avion_creado++;
				msg.add(Controlador.OutAviones,todo);
				if(avion_creado==this.getStateValue(numAviones).doubleValue()){
					this.setStateValue(estado, CreaBarcos);
				}
			}
			else if (this.getStateValue(estado).doubleValue()==Controlador.CreaBarcos){
				
			//	System.out.println("num barcos"+this.getStateValue(numBarcos).intValue());
				Vector<Number>todo = new Vector<Number>();
				todo.add(0,((Integer)(BarcoState.Reset)));
				Random randomizador= new Random();
				todo.add(1,new Double((randomizador.nextDouble())*this.distanciaBarco)+this.posicion_e_barco);
				todo.add(2,new Double((randomizador.nextDouble())*this.distanciaBarco)+this.posicion_n_barco);
				todo.add(3,new Double(100));
				todo.add(4,new Double(100));
				todo.add(5,new Integer(this.getStateValue(numAviones).intValue()+this.barco_creado));
				//do.add(5,new Double(70));
				//do.add(6,new Double(0));
				//System.out.println("barco creado"+barco_creado+"posicion "+ todo.get(1)+"-"+todo.get(2));
				barco_creado++;
				
				msg.add(Controlador.OutBarcos,todo);
				if(barco_creado==this.getStateValue(numBarcos).intValue()){
					this.setStateValue(estado, inicia);
				}
			}
			else{
			if (this.getStateValue(estado).doubleValue()==Controlador.inicia){
				Vector<Number> todo = new Vector<Number>();
				todo.add(0,((Integer)(AvionState.IniciarSimulacion)));
				todo.add(1,new Double(0.1));
				msg.add(Controlador.OutAviones,todo);
				this.setStateValue(estado, Controlador.daRumbo);
			}
			}
			
		}
		
		/*
		 * Envia todos los mensajes acumulados en la lista
		 */
		for(int cont=0;cont<this.listaEnviar.dameLista().size();cont++){
			msg.add(listaEnviar.dameLista().get(cont).damePuerto(), listaEnviar.dameLista().get(cont).dameMensaje());
		}
		
		/*
		 * Se crea una nueva lista de mensajes
		 */
		listaEnviar=new ListaEnviar();
		return msg;
	}
	
	public int NumNaufragosRescatados(){
		return this.rescates.size();
	}
	
	public int NumNaufragosSinRescatar(){
		return (this.num_naufragos-this.rescates.size());
	}
	
	
	//TODO
	public void dEvolucionRescates(){
		
	}
	public void dEvolucionMediaVarianzaV(){
		
	}
	public void dEvolucionMediaVarianzaA(){
		
	}

}
