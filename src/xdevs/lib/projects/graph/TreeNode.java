package xdevs.lib.projects.graph;

import javax.swing.ImageIcon;

public class TreeNode {
	public static final int AVION = 0;
	public static final int AVIONES = 1;
	public static final int BARCO = 2;
	public static final int BARCOS = 3;
	public static final int CAMARA_CENITAL_AVION = 4;
	public static final int CAMARA_CENITAL_BARCO = 5;
	public static final int CAMARA_FRONTAL_AVION = 6;
	public static final int CAMARA_FRONTAL_BARCO = 7;
	public static final int CAMARA_LATERAL_AVION = 8;
	public static final int CAMARA_LATERAL_BARCO = 9;
	public static final int CAMARA_POSTERIOR_AVION = 10;
	public static final int CAMARA_POSTERIOR_BARCO = 11;
	public static final int OPERACIONES_AVION = 12;
	public static final int OPERACIONES_BARCO = 13;
	public static final int ELEMENTOS = 14;
	
	private int _tipo;
	private ImageIcon _icono;
	private String _texto;
	private int _numero;
	
	public TreeNode(int tipo, int numero) {
		this._tipo=tipo;
		this._numero=numero;
		switch (tipo) {
		case 0: {
			try {
				_texto = "Avion "+numero;
				_icono = new ImageIcon("./Imagenes/Avion.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear el icono 0");
			}
		}; break;
		case 1: {
			try {
				_texto = "Aviones";
				_icono = new ImageIcon("./Imagenes/Aviones.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear el icono 1");
			}
		}; break;
		case 2: {
			try {
				_texto = "Barco "+numero;
				_icono = new ImageIcon("./Imagenes/Barco.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear el icono 2");
			}
		}; break;
		case 3: {
			try {
				_texto = "Barcos";
				_icono = new ImageIcon("./Imagenes/Barcos.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 4: {
			try {
				_texto = "Cámara Cenital";
				_icono = new ImageIcon("./Imagenes/CamaraCenital.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 5: {
			try {
				_texto = "Cámara Cenital";
				_icono = new ImageIcon("./Imagenes/CamaraCenital.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 6: {
			try {
				_texto = "Cámara Frontal";
				_icono = new ImageIcon("./Imagenes/CamaraFrontal.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 7: {
			try {
				_texto = "Cámara Frontal";
				_icono = new ImageIcon("./Imagenes/CamaraFrontal.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 8: {
			try {
				_texto = "C�mara Lateral";
				_icono = new ImageIcon("./Imagenes/CamaraLateral.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 9: {
			try {
				_texto = "C�mara Lateral";
				_icono = new ImageIcon("./Imagenes/CamaraLateral.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 10: {
			try {
				_texto = "C�mara Posterior";
				_icono = new ImageIcon("./Imagenes/CamaraPosterior.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 11: {
			try {
				_texto = "C�mara Posterior";
				_icono = new ImageIcon("./Imagenes/CamaraPosterior.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 12: {
			try {
				_texto = "Operaciones";
				_icono = new ImageIcon("./Imagenes/Operaciones.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 13: {
			try {
				_texto = "Operaciones";
				_icono = new ImageIcon("./Imagenes/Operaciones.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		case 14: {
			try {
				_texto = "Elementos";
				_icono = new ImageIcon("./Imagenes/Elementos.gif");
			}
			catch(Exception e) {
				System.out.println("Error al crear un icono");
			}
		}; break;
		}
	}
	
	public int getTipo() {
		return _tipo;
	}
	
	public ImageIcon getIcono() {
		return _icono;
	}
	
	public String getTexto() {
		return _texto;
	}
	
	public int getNumero() {
		return _numero;
	}
}
