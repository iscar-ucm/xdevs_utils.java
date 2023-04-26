package xdevs.lib.projects.graph.structs;

import javax.media.opengl.GL;

public class TrianguloTerreno extends ObjetoDegradado {
	private PV3D _p1;
	private PV3D _p2;
	private PV3D _p3;
//	private Texture _textura;
	
	public TrianguloTerreno() {
		_p1 = null;
		_p2 = null;
		_p3 = null;
	}
	
	public TrianguloTerreno(PV3D p1, PV3D p2, PV3D p3) {
		_p1 = p1;
		_p2 = p2;
		_p3 = p3;
	    int nVert=3;
	    int nNorm=1;
	    PV3D[] vert = new PV3D[nVert];
	    PV3D[] norm = new PV3D[nNorm];
	    Cara[] car = new Cara[nNorm];
	    vert[0] = p1;
	    vert[1] = p2;
	    vert[2] = p3;
	    Cara c = new Cara(3, new VerticeNormal[3]);
	    c.getVerticeNormal()[0] = new VerticeNormal(0,0);
	    c.getVerticeNormal()[1] = new VerticeNormal(1,0);
	    c.getVerticeNormal()[2] = new VerticeNormal(2,0);
	    car[0] = c;
	    setCaras(car);
	    setNCaras(nNorm);
	    setNNormales(nNorm);
	    setNormales(norm);
	    setNVertices(nVert);
	    setVertices(vert);
	    calcularNormalesNewell();
	}
	
	public void setP1(PV3D p1) {
		_p1 = p1;
	}
	
	public void setP2(PV3D p2) {
		_p2 = p2;
	}
	
	public void setP3(PV3D p3) {
		_p3 = p3;
	}
	
	public PV3D getP1() {
		return _p1;
	}
	
	public PV3D getP2() {
		return _p2;
	}
	
	public PV3D getP3() {
		return _p3;
	}
	
	public void inicializar() {
	    int nVert=3;
	    int nNorm=2;
	    PV3D[] vert = new PV3D[nVert];
	    PV3D[] norm = new PV3D[nNorm];
	    Cara[] car = new Cara[nNorm];
	    vert[0] = _p1;
	    vert[1] = _p2;
	    vert[2] = _p3;
	    Cara c = new Cara(3, new VerticeNormal[3]);
	    c.getVerticeNormal()[0] = new VerticeNormal(0,0);
	    c.getVerticeNormal()[1] = new VerticeNormal(1,0);
	    c.getVerticeNormal()[2] = new VerticeNormal(2,0);
	    car[0] = c;
	    c = new Cara(3, new VerticeNormal[3]);
	    c.getVerticeNormal()[2] = new VerticeNormal(0,1);
	    c.getVerticeNormal()[1] = new VerticeNormal(1,1);
	    c.getVerticeNormal()[0] = new VerticeNormal(2,1);
	    car[1] = c;
	    setCaras(car);
	    setNCaras(nNorm);
	    setNNormales(nNorm);
	    setNormales(norm);
	    setNVertices(nVert);
	    setVertices(vert);
	    calcularNormalesNewell();
	}
	/*
	public void a�adirTextura(File file) {
		try {
			_textura = TextureIO.newTexture(file, false);
		} catch (GLException e) {
			System.out.println("Error GL al leer la textura");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error al leer la textura");
			e.printStackTrace();
		}
	} */
	
	public void dibujar3D(GL gl) {
		int nCaras = getNCaras();
		Cara[] caras = getCaras();
		PV3D[] vertices = getVertices();
		PV3D[] normales = getNormales();
		Color[] colores = getColores();
        gl.glPushMatrix();
            gl.glMultMatrixf(getTafin().getMatriz(),0);
            for (int i=0;i<nCaras; i++) {
                gl.glBegin(GL.GL_POLYGON);
                for (int j=0; j<caras[i].getNumVertices(); j++){
                    int iV = caras[i].getVerticeNormal()[j].getVertice();
                    int iN = caras[i].getVerticeNormal()[j].getNormal();
                    gl.glColor3f(colores[iV].getRed(),colores[iV].getGreen(),colores[iV].getBlue());
                    gl.glNormal3f(normales[iN].getX(),normales[iN].getY(),normales[iN].getZ());
                    gl.glVertex3f(vertices[iV].getX(),vertices[iV].getY(),vertices[iV].getZ());
                }
                gl.glEnd();
            }
        gl.glPopMatrix();
    }
}
