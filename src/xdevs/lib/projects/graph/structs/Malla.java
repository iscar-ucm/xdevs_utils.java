package xdevs.lib.projects.graph.structs;

import com.jogamp.opengl.GL2;

public class Malla extends Objeto3D {
    private int _nNormales;
    private int _nCaras;
    private Cara[] _caras;
    private PV3D[] _normales;
    private PV3D[] _vertices;
    
    public Malla() {
        this._nNormales=0;
        this._nCaras=0;
        this._caras=null;
        this._normales=null;
        this._vertices=null;
    }

    public Malla (int nVertices, int nNormales, int nCaras, Cara[] caras, PV3D[] normales, PV3D[] vertices) {
        this._nNormales=nNormales;
        this._nCaras=nCaras;
        this._caras=caras;
        this._normales=normales;
        this._vertices=vertices;
    }

    public void setNVertices(int nVertices) {
    }

    public void setNNormales(int nNormales) {
        this._nNormales=nNormales;
    }

    public void setNCaras(int nCaras) {
        this._nCaras=nCaras;
    }

    public void setCaras(Cara[] caras) {
        this._caras=caras;
    }

    public void setNormales(PV3D[] normales) {
        this._normales=normales;
    }

    public void setVertices(PV3D[] vertices) {
        this._vertices=vertices;
    }

    public void dibujar3D (GL2 gl) {
        gl.glPushMatrix();
            gl.glMultMatrixf(getTafin().getMatriz(),0);
            gl.glColor3f(getColor().getRed(),getColor().getGreen(),getColor().getBlue());
            for (int i=0;i<_nCaras; i++) {
                gl.glBegin(GL2.GL_POLYGON);
                for (int j=0; j<_caras[i].getNumVertices(); j++){
                    int iV = _caras[i].getVerticeNormal()[j].getVertice();
                    int iN = _caras[i].getVerticeNormal()[j].getNormal();
                    gl.glNormal3f(_normales[iN].getX(),_normales[iN].getY(),_normales[iN].getZ());
                    gl.glVertex3f(_vertices[iV].getX(),_vertices[iV].getY(),_vertices[iV].getZ());
                }
                gl.glEnd();
            }
        gl.glPopMatrix();
    }

    public static Malla construirPared(PV3D ii,PV3D id, PV3D si, PV3D sd) {
        PV3D[] vertices = new PV3D[4];
        PV3D[] normales = new PV3D[1];
        Cara[] caras = new Cara[1];
        vertices[0]=id;
        vertices[1]=sd;
        vertices[2]=si;
        vertices[3]=ii;
        Cara c= new Cara(4,new VerticeNormal[4]);
        c.getVerticeNormal()[0]=new VerticeNormal(0,0);
        c.getVerticeNormal()[1]=new VerticeNormal(1,0);
        c.getVerticeNormal()[2]=new VerticeNormal(2,0);
        c.getVerticeNormal()[3]=new VerticeNormal(3,0);
        caras[0]=c;
        Malla pared=new Malla(4,1,1,caras,normales,vertices);
        pared.calcularNormalesNewell();
        return (pared);
    }

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
