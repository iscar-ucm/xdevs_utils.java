package xdevs.lib.projects.graph;

import com.jogamp.opengl.GL2;

import xdevs.lib.projects.graph.structs.Cilindro;
import xdevs.lib.projects.graph.structs.Color;
import xdevs.lib.projects.graph.structs.Objeto3D;
import xdevs.lib.projects.graph.structs.ObjetoCompuesto3D;

public class Arbol1 extends ObjetoCompuesto3D {
	public Arbol1 (float alturaTronco, float alturaCopa, GL2 gl) {
		Objeto3D[] objetos = new Objeto3D[2];
		Objeto3D tronco = new Cilindro(6, 2, alturaTronco/7, alturaTronco/7-alturaTronco/20);
		tronco.getTafin().escalar(1, 1, alturaTronco, gl);
		tronco.setColor(new Color(210f/256f,105f/256f,30f/256f));
		tronco.getTafin().rotar(90, 1, 0, 0, gl);
		Objeto3D copa = new Cilindro(10, 2, 4*alturaTronco/5,0);
		copa.setColor(new Color(0,0.39f,0));
		copa.getTafin().rotar(90, 1, 0, 0, gl);
		copa.getTafin().transladar(0, 0, -alturaTronco*2, gl);
		copa.getTafin().escalar(1, 1, alturaCopa, gl);
		objetos[0] = tronco;
		objetos[1] = copa;
		setNObjetos(2);
		setObjetos(objetos);
	}
}
