package xdevs.lib.projects.graph.structs;

public class VerticeNormal {
    private int _vertice;
    private int _normal;
    
    public VerticeNormal (int vertice, int normal) {
        this._vertice=vertice;
        this._normal=normal;
    }
    
    public int getVertice() {
        return (_vertice);
    }

    public int getNormal() {
        return (_normal);
    }

    public void setVertice (int vertice) {
        this._vertice=vertice;
    }

    public void setNormal (int normal) {
        this._normal=normal;
    }
}
