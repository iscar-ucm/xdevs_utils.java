package xdevs.lib.projects.graph;

import com.jogamp.opengl.GL2;

import xdevs.lib.projects.graph.structs.Cilindro;
import xdevs.lib.projects.graph.structs.Color;
import xdevs.lib.projects.graph.structs.Objeto3D;
import xdevs.lib.projects.graph.structs.ObjetoCompuesto3D;
import xdevs.lib.projects.graph.structs.PV3D;
import xdevs.lib.projects.graph.structs.terrain.Terreno;

public class ModeloNaufrago extends ObjetoCompuesto3D {
	private Terreno _terreno;
	private PV3D _pAnterior;
	
	public ModeloNaufrago(GL2 gl, Terreno terreno) {
		int nObjetos = 3;
		int nLados = 8;
		Objeto3D[] objetos = new Objeto3D[nObjetos];
		objetos[0] = new Cilindro(nLados,2,0,2);
		objetos[0].getTafin().rotar(270, 1, 0, 0, gl);
		objetos[0].setColor(new Color(0,0,0));
		objetos[0].getTafin().escalar(1, 1, 1, gl);

		objetos[1] = new Cilindro(nLados,2,3,0);
		objetos[1].setColor(new Color(1,1,0));
		objetos[1].getTafin().rotar(270, 1, 0, 0, gl);
		objetos[1].getTafin().transladar(0, 0, 1, gl);
		objetos[1].getTafin().escalar(1, 1, 3, gl);

		objetos[2] = new Cilindro(nLados,2,0,3);
		objetos[2].setColor(new Color(1,1,0));
		objetos[2].getTafin().rotar(270, 1, 0, 0, gl);
		objetos[2].getTafin().transladar(0, 0, 4, gl);
		objetos[2].getTafin().escalar(1, 1, 3, gl);

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
			gl.glColor3f(1,1,0);
			gl.glVertex2f(x/1000.0f,y/1000.0f);
		gl.glEnd();
		_pAnterior = new PV3D(x,y,0);
	}
}
