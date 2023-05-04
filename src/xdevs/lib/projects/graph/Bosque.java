package xdevs.lib.projects.graph;

import com.jogamp.opengl.GL2;

import xdevs.lib.projects.graph.structs.Objeto3D;
import xdevs.lib.projects.graph.structs.ObjetoCompuesto3D;

public class Bosque extends ObjetoCompuesto3D {
	public Bosque(float ii, float id, float si, float sd, int arboles, GL2 gl) {
		System.out.println("ii: "+ii);
		System.out.println("id: "+ii);
		System.out.println("si: "+ii);
		System.out.println("sd: "+ii);
		double x = Math.random();
		double y = Math.random();
		double p00 = (1.0d-x)*(1.0d-y)*ii;
		double p10 = (x)*(1.0d-y)*si;
		double p01 = (1.0d-x)*(y)*id;
		double p11 = x*y*sd;
		double altura = p00+p10+p01+p11;
		System.out.println("x: "+x);
		System.out.println("y: "+y);
		System.out.println("p00: "+p00);
		System.out.println("p01: "+p01);
		System.out.println("p10: "+p10);
		System.out.println("p11: "+p11);
		System.out.println("Calculado: "+altura);
		Objeto3D[] objetos = new Objeto3D[arboles];
		for (int i=0; i<arboles; i++) {
		//	tronco = (float)(2+Math.random()*5);
		//	copa = 2*tronco;
			//objetos[i] = new Arbol1(tronco, copa, gl);
			objetos[i] = new Arbol1(2, 4, gl);
			objetos[i].getTafin().transladar((float)(ii+(id-ii)*x), (float)altura, (float)(ii+(si-ii)*y), gl);
		}
		setNObjetos(arboles);
		setObjetos(objetos);
	}
}
