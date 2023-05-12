package xdevs.lib.projects.graph;

import com.jogamp.opengl.GL2;

import xdevs.lib.projects.graph.structs.Cilindro;
import xdevs.lib.projects.graph.structs.Color;
import xdevs.lib.projects.graph.structs.Objeto3D;
import xdevs.lib.projects.graph.structs.ObjetoCompuesto3D;
import xdevs.lib.projects.graph.structs.PV3D;
import xdevs.lib.projects.graph.structs.terrain.Terreno;

public class ModeloAvion extends ObjetoCompuesto3D {
	private PV3D _pAnterior;
	private Terreno _terreno;
	
	public ModeloAvion(GL2 gl, Terreno terreno) {
		int nObjetos = 16;
		int nLados = 8;
		float l1,l2,l3,l4,l5;
		Objeto3D[] objetos = new Objeto3D[nObjetos];
		objetos[0] = new Cilindro(nLados,2,1,1);
		objetos[0].setColor(new Color(1,1,1));
		l1 = 11;
		objetos[0].getTafin().escalar(1, 1, l1, gl);
		objetos[1] = new Cilindro(nLados,2,0.3f,1);
		objetos[1].setColor(new Color(1,1,1));
		objetos[1].getTafin().transladar(0, 0, l1, gl);
		l2 = 1.75f;
		objetos[1].getTafin().escalar(1,1,l2,gl);
		objetos[2] = new Cilindro(nLados,2,0,0.3f);
		objetos[2].setColor(new Color(1,1,1));
		objetos[2].getTafin().transladar(0f, 0f, l1+l2, gl);
		l3 = 0.2f;
		objetos[2].getTafin().escalar(1, 1, l3, gl);
		objetos[3] = new Cilindro(nLados,2,1,0.3f);
		objetos[3].setColor(new Color(0,0,0));
		l4 = 1.5f;
		objetos[3].getTafin().transladar(0, 0, -l4, gl);
		objetos[3].getTafin().escalar(1, 1, l4, gl);
		objetos[4] = new Cilindro(nLados,2,0.3f,0);
		objetos[4].setColor(new Color(0.75f,0.75f,0.75f));
		l5 = 7;
		objetos[4].getTafin().transladar(0, 0, -l4-l5, gl);
		objetos[4].getTafin().escalar(1, 1, l5, gl);
		objetos[5] = new Ala(1f,6f,4,0.1f);
		objetos[5].setColor(new Color(0.9f,0.9f,0.97f));
		objetos[5].getTafin().transladar(-5f, 0, 0, gl);
		objetos[6] = new Ala(6f,1f,4f,0.1f);
		objetos[6].setColor(new Color(0.9f,0.9f,0.97f));
		objetos[6].getTafin().transladar(1f, 0, 0, gl);
		objetos[7] = new Ala(2f,0.4f,2f,0.1f);
		objetos[7].getTafin().transladar(0,1f,0,gl);
		objetos[7].getTafin().rotar(90, 0, 0, 1, gl);
		objetos[7].setColor(new Color(0.75f,0.75f,0.75f));
		objetos[8] = new Cilindro(nLados,2,0.5f,0.5f);
		objetos[8].setColor(new Color(0,0,0));
		objetos[8].getTafin().escalar(1, 1, 1.5f, gl);
		objetos[8].getTafin().transladar(-3f, -0.5f, 0.5f, gl);
		objetos[9] = new Cilindro(nLados,2,0,0.5f);
		objetos[9].getTafin().transladar(-3f, -0.5f, 2f, gl);
		objetos[9].setColor(new Color(1,0,0));
		objetos[10] = new Cilindro(nLados,2,0.5f,0.3f);
		objetos[10].setColor(new Color(0,0,0));
		objetos[10].getTafin().escalar(1, 1, 0.3f, gl);
		objetos[10].getTafin().transladar(-3f, -0.5f, 1.5f, gl);
		objetos[11] = new Cilindro(nLados,2,0.3f,0f);
		objetos[11].setColor(new Color(1,0.26f,0));
		objetos[11].getTafin().transladar(-3f,-0.5f,-6.5f,gl);
		objetos[11].getTafin().escalar(1, 1, 7, gl);
		objetos[12] = new Cilindro(nLados,2,0.5f,0.5f);
		objetos[12].setColor(new Color(0,0,0));
		objetos[12].getTafin().escalar(1, 1, 1.5f, gl);
		objetos[12].getTafin().transladar(3f, -0.5f, 0.5f, gl);
		objetos[13] = new Cilindro(nLados,2,0,0.5f);
		objetos[13].getTafin().transladar(3f, -0.5f, 2f, gl);
		objetos[13].setColor(new Color(1,0,0));
		objetos[14] = new Cilindro(nLados,2,0.5f,0.3f);
		objetos[14].setColor(new Color(0,0,0));
		objetos[14].getTafin().escalar(1, 1, 0.3f, gl);
		objetos[14].getTafin().transladar(3f, -0.5f, 1.5f, gl);
		objetos[15] = new Cilindro(nLados,2,0.3f,0f);
		objetos[15].setColor(new Color(1,0.26f,0));
		objetos[15].getTafin().transladar(3f,-0.5f,-6.5f,gl);
		objetos[15].getTafin().escalar(1, 1, 7, gl);
		setNObjetos(nObjetos);
		setObjetos(objetos);
		this._terreno = terreno;
	}
	
	public void roll(float ang, GL2 gl) {
		this.getTafin().giroAbsolutoZ(ang, gl);
	}
	
	public void yaw(float ang, GL2 gl) {
		this.getTafin().giroAbsolutoY(ang, gl);
	}
	
	public void pitch(float ang, GL2 gl) {
		this.getTafin().giroAbsolutoX(ang, gl);
	}
	
	public void posicionar(float x, float y, float z, GL2 gl) {
		this.getTafin().translacionAbsolutaXYZ(x, y, z, gl);
	}
	
	public void dibujar2D(float x, float y, GL2 gl) {
		float p00,p10,p01,p11,altura;
		gl.glPointSize(4);
		gl.glBegin(GL2.GL_POINTS);
			if (_pAnterior!=null) {
				if ((x>=0)&&((x/1000)<_terreno.getLongitud())&&((y/1000)<_terreno.getAnchura())&&(y>=0)) {
					int auxX = (int)x/1000;
					int auxY = (int)y/1000;
					p00 = (1-(auxX-((int)auxX)))*(1-(auxY-((int)auxY)))*_terreno.getAltura((int)auxX,(int)auxY);
					p10 = (auxX-((int)auxX))*(1-(auxY-((int)auxY)))*_terreno.getAltura((int)(auxX+1),(int)auxY);
					p01 = (1-(auxX-((int)auxX)))*(auxY-((int)auxY))*_terreno.getAltura((int)auxX,(int)(auxY+1));
					p11 = (auxX-((int)auxX))*(auxY-((int)auxY))*_terreno.getAltura((int)(auxX+1),(int)(auxY+1));
					altura = p00+p10+p01+p11;
					Color c = Terreno.colorDeMapa(altura);
					gl.glColor3f(c.getRed(),c.getGreen(),c.getBlue());
					gl.glVertex2f(_pAnterior.getX()/1000.0f,_pAnterior.getY()/1000.0f);
				}
				else {
					gl.glColor3f(0,0,0);
					gl.glVertex2f(_pAnterior.getX()/1000.0f,_pAnterior.getY()/1000.0f);
				}
			}
			gl.glColor3f(1,1,1);
			gl.glVertex2f(x/1000.0f,y/1000.0f);
		gl.glEnd();
		_pAnterior = new PV3D(x,y,0);
	}
}
