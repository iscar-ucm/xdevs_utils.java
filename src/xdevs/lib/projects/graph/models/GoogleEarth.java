package xdevs.lib.projects.graph.models;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import xdevs.lib.projects.graph.structs.ListaPosicion;
import xdevs.lib.projects.graph.structs.Punto;

public class GoogleEarth {
	private static final int BUFFER_SIZE = 36;
	public GoogleEarth() {
	}
	
	public static void crearFicheroPrincipal(int refresco, String ruta, String ficheroRefresco) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document doc;
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			Element kml = doc.createElement("kml");
			kml.setAttribute("xmlns", "http://earth.google.com/kml/2.2");
			Element document = doc.createElement("Document");
			kml.appendChild(document);
			Element nombre = doc.createElement("name");
			nombre.setTextContent("Simulación xDEVS");
			document.appendChild(nombre);
			Element descripcion = doc.createElement("description");
			descripcion.setTextContent("Visualización de la simulación DEVS en tiempo real");
			document.appendChild(descripcion);
			Element networklink = doc.createElement("NetworkLink");
			nombre = doc.createElement("name");
			nombre.setTextContent("Vínculo al fichero actualizado");
			networklink.appendChild(nombre);
			Element open = doc.createElement("open");
			open.setTextContent("1");
			networklink.appendChild(open);
			Element url = doc.createElement("Url");
			Element href = doc.createElement("href");
			href.setTextContent(ficheroRefresco);
			url.appendChild(href);
			Element refreshMode = doc.createElement("refreshMode");
			refreshMode.setTextContent("onInterval");
			url.appendChild(refreshMode);
			Element refreshInterval = doc.createElement("refreshInterval");
			refreshInterval.setTextContent(((Integer)(refresco/1000)).toString());
			url.appendChild(refreshInterval);
			networklink.appendChild(url);
			document.appendChild(networklink);
			doc.appendChild(kml);
			FileWriter xmlFile = new FileWriter(new File(ruta));
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
    		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    		DOMSource source = new DOMSource(doc);
    		StreamResult result = new StreamResult(xmlFile);
    		transformer.transform(source, result);
			xmlFile.flush();
			xmlFile.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void crearDocumentoRefresco(String ruta, ListaPosicion lista, CoupledSimulacion simulacion, double lonIzq, double latInf) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document doc;
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			Element kml = doc.createElement("kml");
			kml.setAttribute("xmlns", "http://earth.google.com/kml/2.2");
			Element document = doc.createElement("Document");
			kml.appendChild(document);
			Element nombre = doc.createElement("name");
			nombre.setTextContent("Simulaci�n xDEVS");
			document.appendChild(nombre);
			Element descripcion = doc.createElement("description");
			descripcion.setTextContent("Datos de la simulaci�n DEVS en tiempo real");
			document.appendChild(descripcion);
		
			String nombreE = null;
			String descripcionE = null;
			String icono = null;
			double[] lonlat;
			Punto punto;
			for (int i=0;i<lista.getLista().size();i++) {
				punto = lista.getLista().get(i).getPunto();
				lonlat = transformarXYLatLon(punto.getN(),punto.getE(), latInf, lonIzq);
				switch (lista.getLista().get(i).getTipoVehiculo()) {
				case DatosTipoPosicion.Avion: {
					nombreE = "Avión"; 
					descripcionE = "Avión "+lista.getLista().get(i).getNombre();
					icono = "http://maps.google.com/mapfiles/kml/shapes/airports.png";
				};break;
				case DatosTipoPosicion.Barco: {
					nombreE = "Barco";
					descripcionE = "Barco "+lista.getLista().get(i).getNombre();
					icono = "http://maps.google.com/mapfiles/kml/shapes/sailing.png";
				};break;
				case DatosTipoPosicion.Naufrago: {
					nombreE = "Naufrago";
					descripcionE = "Naufrago "+lista.getLista().get(i).getNombre();
					icono = "http://maps.google.com/mapfiles/kml/shapes/swimming.png";
				};break;
				}
				document.appendChild(crearPlacemark(doc,nombreE,descripcionE,icono,lonlat[0],lonlat[1],punto.getH(),lista.getLista().get(i).getTipoVehiculo(),lista.getLista().get(i).getNombre(),simulacion));
			}
			
			doc.appendChild(kml);
			FileWriter xmlFile = new FileWriter(new File(ruta));
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
    		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    		DOMSource source = new DOMSource(doc);
    		StreamResult result = new StreamResult(xmlFile);
    		transformer.transform(source, result);
			xmlFile.flush();
			xmlFile.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Element crearPlacemark(Document doc, String nombre, String descripcion, String icono, double latitud, double longitud, double altura, int tipo, int numeroVehiculo, CoupledSimulacion simulacion) {
		float h,r,t;
		h =0;
		r=0;
		t=0;
		float scaleX,scaleY,scaleZ;
		scaleX=10;
		scaleY=10;
		scaleZ=10;
		String modelo="";
		switch(tipo) {
		case DatosTipoPosicion.Avion: {
			h = 270-simulacion._aviones.get(numeroVehiculo).getAvion().obtenerRumbo();
			modelo = "./modelos/Plane.dae";
			Element placemark = doc.createElement("Placemark");
			Element name = doc.createElement("name");
			name.setTextContent(nombre);
			Element description = doc.createElement("description");
			description.setTextContent(descripcion);
			Element point = doc.createElement("Point");
			Element coordinates = doc.createElement("coordinates");
			coordinates.setTextContent(longitud+", "+latitud+", "+altura);
			point.appendChild(coordinates);
			Element icon = doc.createElement("Icon");
			Element href = doc.createElement("href");
			href.setTextContent(icono);
			icon.appendChild(href);
			placemark.appendChild(name);
			placemark.appendChild(description);
			placemark.appendChild(point);
			placemark.appendChild(icon);
			Element model = doc.createElement("Model");
			Element altitudeMode = doc.createElement("altitudeMode");
			altitudeMode.setTextContent("clampToGround");
			model.appendChild(altitudeMode);
			Element location = doc.createElement("Location");
			Element longitude = doc.createElement("longitude");
			longitude.setTextContent(((Double)longitud).toString());
			location.appendChild(longitude);
			Element latitude = doc.createElement("latitude");
			latitude.setTextContent(((Double)latitud).toString());
			location.appendChild(latitude);
			Element altitude = doc.createElement("altitude");
			location.appendChild(altitude);
			model.appendChild(location);
			Element orientation = doc.createElement("Orientation");
			Element heading = doc.createElement("heading");
			heading.setTextContent(((Float)h).toString());
			orientation.appendChild(heading);
			Element tilt = doc.createElement("tilt");
			tilt.setTextContent(((Float)t).toString());
			orientation.appendChild(tilt);
			Element roll = doc.createElement("roll");
			roll.setTextContent(((Float)r).toString());
			orientation.appendChild(roll);
			model.appendChild(orientation);
			Element scale = doc.createElement("Scale");
			Element x = doc.createElement("x");
			x.setTextContent(((Float)scaleX).toString());
			scale.appendChild(x);
			Element y = doc.createElement("y");
			y.setTextContent(((Float)scaleY).toString());
			scale.appendChild(y);
			Element z = doc.createElement("z");
			z.setTextContent(((Float)scaleZ).toString());
			scale.appendChild(z);
			model.appendChild(scale);
			Element link = doc.createElement("Link");
			href = doc.createElement("href");
			href.setTextContent(modelo);
			link.appendChild(href);
			model.appendChild(link); 
			placemark.appendChild(model);
			return placemark;
		}
		case DatosTipoPosicion.Barco: {
			numeroVehiculo = numeroVehiculo-simulacion._aviones.size();
			h = 270-simulacion._barcos.get(numeroVehiculo).getBarco().obtenerRumbo();
			modelo = "./modelos/barco.dae";
			Element placemark = doc.createElement("Placemark");
			Element name = doc.createElement("name");
			name.setTextContent(nombre);
			Element description = doc.createElement("description");
			description.setTextContent(descripcion);
			Element point = doc.createElement("Point");
			Element coordinates = doc.createElement("coordinates");
			coordinates.setTextContent(longitud+", "+latitud+", "+altura);
			point.appendChild(coordinates);
			Element icon = doc.createElement("Icon");
			Element href = doc.createElement("href");
			href.setTextContent(icono);
			icon.appendChild(href);
			placemark.appendChild(name);
			placemark.appendChild(description);
			placemark.appendChild(point);
			placemark.appendChild(icon);
			Element model = doc.createElement("Model");
			Element altitudeMode = doc.createElement("altitudeMode");
			altitudeMode.setTextContent("clampToGround");
			model.appendChild(altitudeMode);
			Element location = doc.createElement("Location");
			Element longitude = doc.createElement("longitude");
			longitude.setTextContent(((Double)longitud).toString());
			location.appendChild(longitude);
			Element latitude = doc.createElement("latitude");
			latitude.setTextContent(((Double)latitud).toString());
			location.appendChild(latitude);
			Element altitude = doc.createElement("altitude");
			location.appendChild(altitude);
			model.appendChild(location);
			Element orientation = doc.createElement("Orientation");
			Element heading = doc.createElement("heading");
			heading.setTextContent(((Float)h).toString());
			orientation.appendChild(heading);
			Element tilt = doc.createElement("tilt");
			tilt.setTextContent(((Float)t).toString());
			orientation.appendChild(tilt);
			Element roll = doc.createElement("roll");
			roll.setTextContent(((Float)r).toString());
			orientation.appendChild(roll);
			model.appendChild(orientation);
			Element scale = doc.createElement("Scale");
			Element x = doc.createElement("x");
			x.setTextContent(((Float)scaleX).toString());
			scale.appendChild(x);
			Element y = doc.createElement("y");
			y.setTextContent(((Float)scaleY).toString());
			scale.appendChild(y);
			Element z = doc.createElement("z");
			z.setTextContent(((Float)scaleZ).toString());
			scale.appendChild(z);
			model.appendChild(scale);
			Element link = doc.createElement("Link");
			href = doc.createElement("href");
			href.setTextContent(modelo);
			link.appendChild(href);
			model.appendChild(link); 
			placemark.appendChild(model);
			return placemark;
		}
		case DatosTipoPosicion.Naufrago: {
			modelo = "";
			Element placemark = doc.createElement("Placemark");
			Element name = doc.createElement("name");
			name.setTextContent(nombre);
			Element description = doc.createElement("description");
			description.setTextContent(descripcion);
			Element point = doc.createElement("Point");
			Element coordinates = doc.createElement("coordinates");
			coordinates.setTextContent(longitud+", "+latitud+", "+altura);
			point.appendChild(coordinates);
			Element icon = doc.createElement("Icon");
			Element href = doc.createElement("href");
			href.setTextContent(icono);
			icon.appendChild(href);
			placemark.appendChild(name);
			placemark.appendChild(description);
			placemark.appendChild(point);
			placemark.appendChild(icon);
			return placemark;
		}
		}
		return null;
	}
	
	public static double[] transformarXYLatLon(double x, double y, double latInf, double lonIzq) {
		double lat_orig = latInf;
		double lon_orig = lonIzq;
		lat_orig=lat_orig*Math.PI/180.0;
		x=x/1000.0;
	//	System.out.println("X: "+x);
		y=y/1000.0;
	//	System.out.println("Y: "+y);
	//	System.out.println();
	//	System.out.println("Lat origen: "+lat_orig);
		double R=6378;  //(en km)  
		double ro=R*(1.0/Math.tan(lat_orig));
	//	System.out.println("Ro: "+ro);
		double theta=Math.atan(x/(ro-y));
	//	System.out.println("Theta: "+theta);
		double lambda = theta/Math.sin(lat_orig);
	//	System.out.println("Lambda: "+lambda);
		double longitud=lambda*180/Math.PI;
	//	System.out.println("Longitud: "+longitud);
		longitud=longitud+lon_orig;
	//	System.out.println("Longitud: "+longitud);
		double rp=(ro-y)/Math.cos(theta);
	//	System.out.println("Rp: "+rp);
		double latitud=Math.asin((1/Math.sin(lat_orig))-(rp*Math.cos(lat_orig)/R))*180/Math.PI;
		double[] resultado = new double[2];
		resultado[1] = latitud;
		resultado[0] = longitud;
		return resultado;
	}
	
	public static void crearFicheroKMZ(String ruta, ListaPosicion lista, CoupledSimulacion simulacion, double latInf, double lonIzq) {
		try {
			crearDocumentoRefresco("./resources/fichero.kml", lista, simulacion, latInf, lonIzq);
			//Creaci�n del fichero kmz
			// Reference to the file we will be adding to the zipfile
			BufferedInputStream origin = null;
			// Reference to our zip file
			FileOutputStream dest = new FileOutputStream(ruta);
			// Wrap our destination zipfile with a ZipOutputStream
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
			// Create a byte[] buffer that we will read data 
	
			// from the source
			// files into and then transfer it to the zip file
			byte[] data = new byte[BUFFER_SIZE];
	
			FileInputStream fi;
			ZipEntry entry;
			int count;
				
			// source file
			fi = new FileInputStream("./resources/fichero.kml");
			origin = new BufferedInputStream(fi, BUFFER_SIZE);
			// Setup the entry in the zip file
			entry = new ZipEntry("fichero.kml");
			out.putNextEntry(entry);
			// Read data from the source file and write it out to the zip file
			while((count = origin.read(data,0,BUFFER_SIZE)) != -1)
			{
				out.write(data, 0, count);
			}
			// Close the source file
			origin.close();
			
			// source file
			fi = new FileInputStream("./resources/Plane.dae");
			origin = new BufferedInputStream(fi, BUFFER_SIZE);
			// Setup the entry in the zip file
			entry = new ZipEntry("/modelos/plane.dae");
			out.putNextEntry(entry);
			// Read data from the source file and write it out to the zip file
			while((count = origin.read(data,0,BUFFER_SIZE)) != -1)
			{
				out.write(data, 0, count);
			}
			// Close the source file
			origin.close();
			
			// source file
			fi = new FileInputStream("./Imagenes/texture0.jpg");
			origin = new BufferedInputStream(fi, BUFFER_SIZE);
			// Setup the entry in the zip file
			entry = new ZipEntry("/imagenes/texture0.jpg");
			out.putNextEntry(entry);
			// Read data from the source file and write it out to the zip file
			while((count = origin.read(data,0,BUFFER_SIZE)) != -1)
			{
				out.write(data, 0, count);
			}
			// Close the source file
			origin.close();
			
			// source file
			fi = new FileInputStream("./Imagenes/texture1.jpg");
			origin = new BufferedInputStream(fi, BUFFER_SIZE);
			// Setup the entry in the zip file
			entry = new ZipEntry("/imagenes/texture1.jpg");
			out.putNextEntry(entry);
			// Read data from the source file and write it out to the zip file
			while((count = origin.read(data,0,BUFFER_SIZE)) != -1)
			{
				out.write(data, 0, count);
			}
			// Close the source file
			origin.close();
			
			// source file
			fi = new FileInputStream("./Imagenes/texture2.jpg");
			origin = new BufferedInputStream(fi, BUFFER_SIZE);
			// Setup the entry in the zip file
			entry = new ZipEntry("/imagenes/texture2.jpg");
			out.putNextEntry(entry);
			// Read data from the source file and write it out to the zip file
			while((count = origin.read(data,0,BUFFER_SIZE)) != -1)
			{
				out.write(data, 0, count);
			}
			// Close the source file
			origin.close();
	
			// source file
			fi = new FileInputStream("./Imagenes/texture3.jpg");
			origin = new BufferedInputStream(fi, BUFFER_SIZE);
			// Setup the entry in the zip file
			entry = new ZipEntry("/imagenes/texture3.jpg");
			out.putNextEntry(entry);
			// Read data from the source file and write it out to the zip file
			while((count = origin.read(data,0,BUFFER_SIZE)) != -1)
			{
				out.write(data, 0, count);
			}
			// Close the source file
			origin.close();
			
			// source file
			fi = new FileInputStream("./resources/barco.dae");
			origin = new BufferedInputStream(fi, BUFFER_SIZE);
			// Setup the entry in the zip file
			entry = new ZipEntry("/modelos/barco.dae");
			out.putNextEntry(entry);
			// Read data from the source file and write it out to the zip file
			while((count = origin.read(data,0,BUFFER_SIZE)) != -1)
			{
				out.write(data, 0, count);
			}
			// Close the source file
			origin.close();
			
			// source file
			fi = new FileInputStream("./resources/textures.txt");
			origin = new BufferedInputStream(fi, BUFFER_SIZE);
			// Setup the entry in the zip file
			entry = new ZipEntry("textures.txt");
			out.putNextEntry(entry);
			// Read data from the source file and write it out to the zip file
			while((count = origin.read(data,0,BUFFER_SIZE)) != -1)
			{
				out.write(data, 0, count);
			}
			// Close the source file
			origin.close();			
			
			// Close the zip file
			out.close();
			
			//Borro el fichero kml que ya est� metido en el kmz
			File residuo = new File("./resources/fichero.kml");
			residuo.delete(); 
		}
		catch(Exception e) {
			
		}
	}
	
	public static void main (String[] args) {
		GoogleEarth.crearFicheroPrincipal(1, "./Principal.kml", "./refresco.kml");
	}
}
