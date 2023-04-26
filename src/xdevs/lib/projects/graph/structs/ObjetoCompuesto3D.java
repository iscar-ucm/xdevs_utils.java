package xdevs.lib.projects.graph.structs;

import com.jogamp.opengl.GL2;

public class ObjetoCompuesto3D extends Objeto3D {
    private Objeto3D[] _objetos;
    private int _nobjetos;
    
    public ObjetoCompuesto3D () {
        _nobjetos=0;
        _objetos=null;
    }

    public void setNObjetos (int nobjetos) {
        this._nobjetos=nobjetos;
    }

    public void setObjetos (Objeto3D[] objetos) {
        this._objetos=objetos;
    }

    public int getNObjetos () {
        return (_nobjetos);
    }

    public Objeto3D[] getObjetos () {
        return (_objetos);
    }

    public void dibujar3D(GL2 gl) {
        gl.glPushMatrix();
            gl.glMultMatrixf(getTafin().getMatriz(),0);
            for (int i=0;i<_nobjetos;i++) {
            	gl.glColor3f(_objetos[i].getColor().getRed()/256f, _objetos[i].getColor().getGreen()/256f, _objetos[i].getColor().getBlue()/256f);
                _objetos[i].dibujar3D(gl);
            }
        gl.glPopMatrix();
    }

    public void establecerColor(Color color) {
        setColor(color);
        for (int i=0;i<_nobjetos;i++) _objetos[i].setColor(new Color(color.getRed(),color.getGreen(),color.getBlue()));
    }
}
