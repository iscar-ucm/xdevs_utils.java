package xdevs.lib.projects.graph.structs;

import com.jogamp.opengl.GL2;

public abstract class ObjetoDegradado extends Objeto3D {
    private int _nVertices;
    private int _nNormales;
    private int _nCaras;
    private Cara[] _caras;
    private PV3D[] _normales;
    private PV3D[] _vertices;
    private Color[] _colores;
    
    public ObjetoDegradado() {
        this._nVertices=0;
        this._nNormales=0;
        this._nCaras=0;
        this._caras=null;
        this._normales=null;
        this._vertices=null;
        this._colores=null;
    }

    public ObjetoDegradado (int nVertices, int nNormales, int nCaras, Cara[] caras, PV3D[] normales, PV3D[] vertices, Color[] colores) {
        this._nVertices=nVertices;
        this._nNormales=nNormales;
        this._nCaras=nCaras;
        this._caras=caras;
        this._normales=normales;
        this._vertices=vertices;
    }

    public void setNVertices(int nVertices) {
        this._nVertices=nVertices;
    }
    
    public int getNVertices() {
    	return _nVertices;
    }

    public void setNNormales(int nNormales) {
        this._nNormales=nNormales;
    }
    
    public int getNNormales() {
    	return _nNormales;
    }

    public void setNCaras(int nCaras) {
        this._nCaras=nCaras;
    }
    
    public int getNCaras() {
    	return _nCaras;
    }

    public void setCaras(Cara[] caras) {
        this._caras=caras;
    }
    
    public Cara[] getCaras() {
    	return _caras;
    }

    public void setNormales(PV3D[] normales) {
        this._normales=normales;
    }
    
    public PV3D[] getNormales() {
    	return _normales;
    }

    public void setVertices(PV3D[] vertices) {
        this._vertices=vertices;
    }
    
    public PV3D[] getVertices() {
    	return _vertices;
    }
    
    public void setColores(Color[] colores) {
    	this._colores=colores;
    }
    
    public Color[] getColores() {
    	return _colores;
    }
 

    public abstract void dibujar3D(GL2 gl); 

    public void calcularNormalesNewell() {
        float nx,ny,nz;
        for (int i=0;i<_nNormales;i++) {
            nx=0;
            ny=0;
            nz=0;
            for (int j=0;j<_caras[i].getNumVertices();j++) {
                PV3D actual=_vertices[_caras[i].getVerticeNormal()[j].getVertice()];
                int suc;
                if (j==(_caras[i].getNumVertices()-1)) suc=0;
                else suc=j+1;
                PV3D sucesor=_vertices[_caras[i].getVerticeNormal()[suc].getVertice()];
                nx+=(actual.getY()-sucesor.getY())*(actual.getZ()+sucesor.getZ());
                ny+=(actual.getZ()-sucesor.getZ())*(actual.getX()+sucesor.getX());
                nz+=(actual.getX()-sucesor.getX())*(actual.getY()+sucesor.getY());
            }
            _normales[i]=new PV3D(nx,ny,nz);
        }
    }

    public void invertirNormales() {
        for (int i=0;i<_nNormales;i++) {
            _normales[i].setX(-_normales[i].getX());
            _normales[i].setY(-_normales[i].getY());
            _normales[i].setZ(-_normales[i].getZ());
        }
    }

}
