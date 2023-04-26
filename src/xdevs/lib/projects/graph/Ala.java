package xdevs.lib.projects.graph;

import xdevs.lib.projects.graph.structs.Cara;
import xdevs.lib.projects.graph.structs.Malla;
import xdevs.lib.projects.graph.structs.PV3D;
import xdevs.lib.projects.graph.structs.VerticeNormal;

public class Ala extends Malla {
	
	public Ala(float a1, float a2, float l, float ancho) {
	    int nVert=8;
	    int nNorm=6;
	    PV3D[] vert=new PV3D[nVert];
	    PV3D[] norm=new PV3D[nNorm];
	    Cara[] car=new Cara[nNorm];
		vert[0] = new PV3D(0,0,0);
		vert[1] = new PV3D(0,ancho,0);
		vert[2] = new PV3D(0,0,a1);
		vert[3] = new PV3D(0,ancho,a1);
		vert[4] = new PV3D(l,0,a2);
		vert[5] = new PV3D(l,ancho,a2);
		vert[6] = new PV3D(l,0,0);
		vert[7] = new PV3D(l,ancho,0);
		car[0] = new Cara(4,new VerticeNormal[4]);
		car[0].getVerticeNormal()[0] = new VerticeNormal(7,0);
		car[0].getVerticeNormal()[1] = new VerticeNormal(5,0);
		car[0].getVerticeNormal()[2] = new VerticeNormal(3,0);
		car[0].getVerticeNormal()[3] = new VerticeNormal(1,0);
		car[1] = new Cara(4,new VerticeNormal[4]);
		car[1].getVerticeNormal()[0] = new VerticeNormal(5,0);
		car[1].getVerticeNormal()[1] = new VerticeNormal(4,0);
		car[1].getVerticeNormal()[2] = new VerticeNormal(2,0);
		car[1].getVerticeNormal()[3] = new VerticeNormal(3,0);
		car[2] = new Cara(4,new VerticeNormal[4]);
		car[2].getVerticeNormal()[0] = new VerticeNormal(0,0);
		car[2].getVerticeNormal()[1] = new VerticeNormal(2,0);
		car[2].getVerticeNormal()[2] = new VerticeNormal(4,0);
		car[2].getVerticeNormal()[3] = new VerticeNormal(6,0);
		car[3] = new Cara(4,new VerticeNormal[4]);
		car[3].getVerticeNormal()[0] = new VerticeNormal(6,0);
		car[3].getVerticeNormal()[1] = new VerticeNormal(5,0);
		car[3].getVerticeNormal()[2] = new VerticeNormal(1,0);
		car[3].getVerticeNormal()[3] = new VerticeNormal(0,0);
		car[4] = new Cara(4,new VerticeNormal[4]);
		car[4].getVerticeNormal()[0] = new VerticeNormal(5,0);
		car[4].getVerticeNormal()[1] = new VerticeNormal(4,0);
		car[4].getVerticeNormal()[2] = new VerticeNormal(7,0);
		car[4].getVerticeNormal()[3] = new VerticeNormal(6,0);
		car[5] = new Cara(4,new VerticeNormal[4]);
		car[5].getVerticeNormal()[0] = new VerticeNormal(1,0);
		car[5].getVerticeNormal()[1] = new VerticeNormal(3,0);
		car[5].getVerticeNormal()[2] = new VerticeNormal(2,0);
		car[5].getVerticeNormal()[3] = new VerticeNormal(0,0);
	    setCaras(car);
	    setNCaras(nNorm);
	    setNNormales(nNorm);
	    setNormales(norm);
	    setNVertices(nVert);
	    setVertices(vert);
	    calcularNormalesNewell();    	
	}
}
