package ssii2007.incidencias;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;

import javax.xml.parsers.*;



import org.w3c.dom.*;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


public class ProbabilidadNaufrago implements Serializable{

	/*
	 * orden t,n,e
	 */
	private int[][][] tablero;
	private int[] numNaufragosT;
	private double _tmin;
	private double _tmax;
	private double _varT;
	private double _nmin;
	private double _nmax;
	private double _varN;
	private double _emin;
	private double _emax;
	private double _varE;
	
	public void imprimeEstado(){
		System.out.println(
				"tmin "+_tmin+"tmax "+_tmax+"varT "+_varT+"-"+
				"emin "+_emin+"emax "+_emax+"varE "+_varE+"-"+
				"nmin "+_nmin+"nmax "+_nmax+"varN "+_varN+"-");
	}

	public int[][][] dameTablero(){
		return this.tablero;
	}
	
	public static int Correcto		=	1;
	public static int Incorrecto	=	2;
	public static int Norte			=	3;
	public static int Este 			= 	4;
	public static int Tiempo		=  	5;
	
	private int numNaufragosTotales;
	
	public ProbabilidadNaufrago(double tmin,double tmax,double varT,
			double nmin,double nmax,double varN,
			double emin,double emax,double varE){
		this._tmin=tmin;
		this._tmax=tmax;
		this._varT=varT;
		int numT=((int)(((_tmax-_tmin)/_varT)+1));
		this._emin=emin;
		this._emax=emax;
		this._varE=varE;
		int numE=((int)(((_emax-_emin)/_varE)+1));
		
		this._nmin=nmin;
		this._nmax=nmax;
		this._varN=varN;
		int numN=((int)(((_nmax-_nmin)/_varN)+1));
		creaTablero(numT,numN,numE);
		numNaufragosT=new int[numT];
	}
	
	public double dameTiempoFinal(){
		return _tmax+this._varT;
	}
	public static void main(String[] args){
		ProbabilidadNaufrago prob= new ProbabilidadNaufrago(0,3600*4,3600,0,10000,1000,0,10000,1000);
		prob.escribirFichero("hola.xml");
	}
	
	public int[][][] creaTablero(int numt,int numn,int nume){
		tablero = new int[numt][numn][nume];
		for(int i=0;i<tablero.length;i++)
			for(int j=0;j<tablero[i].length;j++)
				for(int k=0;k<tablero[i][j].length;k++)
					tablero[i][j][k]=0;
		
		return tablero;
	}
	public int leerFichero(String nombreFichero){
		int devolver= ProbabilidadNaufrago.Correcto;
		Document documento=readFile(nombreFichero);
		convertirDocumento(documento);
		return devolver;
	}
	


	public int escribirFichero(String nombreFichero){
		int devolver= ProbabilidadNaufrago.Correcto;
		Document doc=dameDocumento();
		writeFile(doc,nombreFichero);
		return devolver;
	}

	private void convertirDocumento(Document documento) {
		// TODO Dado un documento, convertirlo en un probabilidadnaufrago valido
		//try{
		Element elem=(Element)documento.getDocumentElement();
		/*Node raiz=elem.getFirstChild();
		Node numNaufragosTotales= raiz.getFirstChild();
		*/
		if (elem.hasChildNodes()){
	        NodeList lista=elem.getElementsByTagName("naufragos");//cogemos los hijos a partir de la raiz
	        for(int i=0;i<lista.getLength();i++){
	           Element elemento=(Element)lista.item(i);//cogemos los hijos de cada participante
	           this.numNaufragosTotales=Integer.parseInt(elemento.getAttribute("numnaufragos"));
	        }
	        
	        lista=elem.getElementsByTagName("valores_tiempo");//cogemos los hijos a partir de la raiz
	        int numT=0,numE=0,numN=0;
	        for(int i=0;i<lista.getLength();i++){
	           Element elemento=(Element)lista.item(i);//cogemos los hijos de cada participante
	           this._tmin=Double.valueOf(elemento.getAttribute("tmin"));
	           this._tmax=Double.valueOf(elemento.getAttribute("tmax"));
	           this._varT=Double.valueOf(elemento.getAttribute("varT"));
	           numT=Integer.parseInt(elemento.getAttribute("longitud"));
	        }
	        
	        lista=elem.getElementsByTagName("valores_este");//cogemos los hijos a partir de la raiz
	        for(int i=0;i<lista.getLength();i++){
	           Element elemento=(Element)lista.item(i);//cogemos los hijos de cada participante
	           this._emin=Double.valueOf(elemento.getAttribute("emin"));
	           this._emax=Double.valueOf(elemento.getAttribute("emax"));
	           this._varE=Double.valueOf(elemento.getAttribute("varE"));
	           numE=Integer.parseInt(elemento.getAttribute("longitud"));
	        }
	        
	        lista=elem.getElementsByTagName("valores_norte");//cogemos los hijos a partir de la raiz
	        for(int i=0;i<lista.getLength();i++){
	           Element elemento=(Element)lista.item(i);//cogemos los hijos de cada participante
	           this._nmin=Double.valueOf(elemento.getAttribute("nmin"));
	           this._nmax=Double.valueOf(elemento.getAttribute("nmax"));
	           this._varN=Double.valueOf(elemento.getAttribute("varN"));
	           numN=Integer.parseInt(elemento.getAttribute("longitud"));
	        }
	        this.tablero= creaTablero(numT,numN,numE);
	        
	        for(int i=0;i<numT;i++){
	        	String nombreT = "tiempo"+ (new Integer(i)).toString();
		        lista=elem.getElementsByTagName(nombreT);//cogemos los hijos a partir de la raiz
		        for(int j=0;j<lista.getLength();j++){
		        	Element elemento=(Element)lista.item(j);
		        	
		        	
			        for(int i2=0;i2<numN;i2++){
			        	String nombreN = "norte"+ (new Integer(i2)).toString();
			        	NodeList lista2=elemento.getElementsByTagName(nombreN);//cogemos los hijos a partir de la raiz
				        for(int j2=0;j2<lista.getLength();j2++){
				        	Element elemento2=(Element)lista2.item(j2);
					        for(int i3=0;i3<numE;i3++){
					        	String nombreE = "este"+ (new Integer(i3)).toString();
					        	NodeList lista3=elemento2.getElementsByTagName(nombreE);//cogemos los hijos a partir de la raiz
						        for(int j3=0;j3<lista.getLength();j3++){
							           Element elemento3=(Element)lista3.item(j3);//cogemos los hijos de cada participante
							           //posicion es == i,i2,i3
							           this.tablero[i][i2][i3]=Integer.valueOf(elemento3.getAttribute("numNaufragos"));
						        }       
					        }				        
				        }       
			        }
		        }
	        }
	   }
	//	}
	//	catch(Exception e){
	//		System.err.println("error en la lectura del fichero");
	//		System.out.println("error "+e );
	//	}
		
		/*
		System.out.println("elem "+elem);
		System.out.println("raiz "+raiz);
		System.out.println("numNaufragosTotales "+numNaufragosTotales);
		*/
	}
	private Document dameDocumento() {
		//TODO devolver un documento valido, dado un probabilidadnaufrago
		/*Creaci�n del documento*/
		Document document=null;
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			/*Creaci�n del elemento r�iz*/
			Element root = (Element) document.createElement("xml");
			root.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
			document.appendChild(root);
	    
			Element numNaufragosTotales = (Element) document.createElement("naufragos");
			numNaufragosTotales.setAttribute("numnaufragos",(new Integer(this.numNaufragosTotales)).toString());
			root.appendChild(numNaufragosTotales);
			
			Element tiempo = (Element) document.createElement("valores_tiempo");
			tiempo.setAttribute("tmin",(new Double(this._tmin)).toString());
			tiempo.setAttribute("tmax",(new Double(this._tmax)).toString());
			tiempo.setAttribute("varT",(new Double(this._varT)).toString());
			tiempo.setAttribute("longitud",( new Integer(this.tablero.length).toString()));
			root.appendChild(tiempo);
			
			Element norte = (Element) document.createElement("valores_norte");
			norte.setAttribute("nmin",(new Double(this._nmin)).toString());
			norte.setAttribute("nmax",(new Double(this._nmax)).toString());
			norte.setAttribute("varN",(new Double(this._varN)).toString());
			norte.setAttribute("longitud",( new Integer(this.tablero[0].length).toString()));
			root.appendChild(norte);
			
			Element este = (Element) document.createElement("valores_este");
			este.setAttribute("emin",(new Double(this._emin)).toString());
			este.setAttribute("emax",(new Double(this._emax)).toString());
			este.setAttribute("varE",(new Double(this._varE)).toString());
			este.setAttribute("longitud",( new Integer(this.tablero[0][0].length).toString()));
			root.appendChild(este);
			
			for(int i=0;i<tablero.length;i++){
				Element tvar = (Element) document.createElement("tiempo"+(new Integer(i).toString()));
				tvar.setAttribute("numNaufragos", new Integer(this.numNaufragosT[i]).toString());
				for(int j=0;j<tablero[i].length;j++){
					Element nvar = (Element) document.createElement("norte"+(new Integer(j).toString()));
					for(int k=0;k<tablero[i][j].length;k++){
						Element evar = (Element) document.createElement("este"+(new Integer(k).toString()));
						evar.setAttribute("numNaufragos", (new Integer(this.tablero[i][j][k]).toString()));
						nvar.appendChild(evar);
					}
					tvar.appendChild(nvar);
				}
				root.appendChild(tvar);
			}
			
		
		
		
		}
		catch(Exception e){
			System.err.println("error en la generacion del xml de naufragos");
		}
	    
		return document;
	}
	
	
	
	

	public double dameProbabilidad(double n,double e,double t){
		double devolver=0;
		int n_norm = normalizar(ProbabilidadNaufrago.Norte,n);
		int e_norm = normalizar(ProbabilidadNaufrago.Este,e);
		int t_norm = normalizar(ProbabilidadNaufrago.Tiempo,t);
		int num_naufragos= tablero[t_norm][n_norm][e_norm]; 
		devolver = (((double)num_naufragos)/((double)this.numNaufragosT[t_norm]));		
		return devolver;
	}
	
	   //El m�todo ReadFile recibe un archivo de texto xml, y lo almacena en un Document
	public Document readFile(String filename) {
		Document doc=null;
	    try {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        doc = factory.newDocumentBuilder().parse(new File(filename));
	    } catch (java.lang.Exception e) {
	    	System.err.println("error en la lectura de fichero");
	    }
	return doc;
	}
	
	//El m�todo writeFile recibe un Document y un archivo, y almacena la
	//estructura xml en el archivo dado.
	public void writeFile(Document doc, String filename) {
	    try {
	    	OutputFormat format = new OutputFormat("xml","UTF-8",true);
			XMLSerializer serializer = new XMLSerializer(format);
			FileWriter xmlFile = new FileWriter(new File(filename));
			serializer.setOutputCharStream(xmlFile);
			serializer.serialize(doc);
			xmlFile.flush();
			xmlFile.close();
	    } catch (java.lang.Exception e) {
	    	System.err.println("error en la creacion del xml de naufragos");
	    }
	}
	
	public void llegaNaufrago(double n,double e, double t){
		
		
		int n_norm = normalizar(ProbabilidadNaufrago.Norte,n);
		int e_norm = normalizar(ProbabilidadNaufrago.Este,e);
		int t_norm = normalizar(ProbabilidadNaufrago.Tiempo,t);
		//System.out.println("n_norm"+n_norm+"e_norm"+e_norm+"t_norm"+t_norm);
		this.numNaufragosT[t_norm]=this.numNaufragosT[t_norm]+1;
		this.numNaufragosTotales++;
		tablero[t_norm][n_norm][e_norm]=tablero[t_norm][n_norm][e_norm]+1;

	}
	
	public int normalizar(int tipo,double t){
		
		int normalizado=0;
		if(tipo==Norte){
			if(t>this._nmax){
				t=this._nmax;
			}
			if(t<this._nmin){
				t=this._nmin;
			}
			normalizado=(int)((t-this._nmin)/this._varN);
		}
		else if(tipo==Este){
			if(t>this._emax){
				t=this._emax;
			}
			if(t<this._emin){
				t=this._emin;
			}
			normalizado=(int)((t-this._emin)/this._varE);
		}
		else if(tipo==Tiempo){
			if(t>this._tmax){
				t=this._tmax;
			}
			if(t<this._tmin){
				t=this._tmin;
			}
			normalizado=(int)((t-this._tmin)/this._varT);
		}
		return normalizado;
	}

	public int[][][] getTablero() {
		return tablero;
	}

	public void setTablero(int[][][] tablero) {
		this.tablero = tablero;
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

	public double get_nmin() {
		return _nmin;
	}

	public void set_nmin(double _nmin) {
		this._nmin = _nmin;
	}

	public double get_nmax() {
		return _nmax;
	}

	public void set_nmax(double _nmax) {
		this._nmax = _nmax;
	}

	public double get_varN() {
		return _varN;
	}

	public void set_varN(double _varn) {
		_varN = _varn;
	}

	public double get_emin() {
		return _emin;
	}

	public void set_emin(double _emin) {
		this._emin = _emin;
	}

	public double get_emax() {
		return _emax;
	}

	public void set_emax(double _emax) {
		this._emax = _emax;
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
