package xdevs.lib.projects.graph.structs;

import com.jogamp.opengl.GL2;

public abstract class Objeto3D {
    private Color _color;
    private TAfin _tafin;
    
    public Objeto3D () {
        _color=new Color(0,0,0);
        _tafin=new TAfin();
    }

    public Objeto3D (Color color, TAfin tafin) {
        this._color=color;
        this._tafin=tafin;
    }


    public void setTafin (TAfin tafin) {
        this._tafin=tafin;
    }

    public void setColor (Color color) {
        this._color=color;
    }

    public TAfin getTafin () {
        return (_tafin);
    } 

    public Color getColor () {
        return (_color);
    }
    
    public abstract void dibujar3D(GL2 gl);
}
