package xdevs.lib.projects.graph.structs;

public class Cara {
    private int _numVertices;
    private VerticeNormal[] _indices;
    
    public Cara (int nVertices, VerticeNormal[] indicesV) {
        this._numVertices=nVertices;
        this._indices=indicesV;
    }

    public int getNumVertices() {
        return (_numVertices);
    }

    public VerticeNormal[] getVerticeNormal() {
        return (_indices);
    }
}
