package xdevs.lib.projects.graph.structs;

public class Cilindro extends Malla {
    private int _nLados;
    private int _nCapas;
    private float _radioSup;
    private float _radioInf;
    
    public Cilindro () {
        _nLados=0;
        _nCapas=0;
        _radioSup=1;
        _radioInf=1;
    }

    public Cilindro (int nLados, int nCapas, float radioSup, float radioInf) {
        float altura=1;
        this._nLados=nLados;
        this._nCapas=nCapas;
        this._radioSup=radioSup;
        this._radioInf=radioInf;
        int verticeActualCapa=0;
        int nVert=2*nLados*nCapas*2;
        int nNorm=2*nLados*(nCapas-1);
        int inN=nLados*(nCapas-1);
        int inV=nLados*nCapas;
        PV3D[] vert = new PV3D[nVert];
        PV3D[] norm = new PV3D[nNorm];
        Cara[] car = new Cara[nNorm];
        float rad=radioInf;
        float incrZ=altura/(nCapas-1);
        float incrRadio=(radioSup-radioInf)/(nCapas-1);
        for(int i=0; i<nCapas; i++){
            for (int j=0;j<nLados;j++){
                double angulo= 2*Math.PI/nLados*j;
                vert[verticeActualCapa+j]=new PV3D((float)(rad*Math.cos(angulo)),(float)(rad*Math.sin(angulo)),incrZ*i);
                vert[verticeActualCapa+j+inV]=new PV3D((float)(rad*Math.cos(angulo)),(float)(rad*Math.sin(angulo)),incrZ*i);
                vert[verticeActualCapa+j+inV*2]=new PV3D((float)(rad*Math.cos(angulo)-0.001*Math.cos(angulo)),(float)(rad*Math.sin(angulo)-0.001*Math.sin(angulo)),incrZ*i);
                vert[verticeActualCapa+j+inV*3]=new PV3D((float)(rad*Math.cos(angulo)-0.001*Math.cos(angulo)),(float)(rad*Math.sin(angulo)-0.001*Math.sin(angulo)),incrZ*i);
                if (i<nCapas-1) {
                    Cara c= new Cara(4,new VerticeNormal[4]);
                    c.getVerticeNormal()[0]= new VerticeNormal(verticeActualCapa+j,verticeActualCapa+j);
                    c.getVerticeNormal()[1]= new VerticeNormal(verticeActualCapa+((j+1)%nLados),verticeActualCapa+j);
                    c.getVerticeNormal()[2]= new VerticeNormal(verticeActualCapa+((j+1)%nLados)+nLados,verticeActualCapa+j);
                    c.getVerticeNormal()[3]= new VerticeNormal(verticeActualCapa+j+nLados,verticeActualCapa+j);
                    car[verticeActualCapa+j]=c;
                    Cara c2= new Cara(4,new VerticeNormal[4]);
                    c2.getVerticeNormal()[0]= new VerticeNormal(verticeActualCapa+j+inV*3,verticeActualCapa+j+inN);
                    c2.getVerticeNormal()[3]= new VerticeNormal(verticeActualCapa+((j+1)%nLados)+inV*3,verticeActualCapa+j+inN);
                    c2.getVerticeNormal()[2]= new VerticeNormal(verticeActualCapa+((j+1)%nLados)+nLados+inV*3,verticeActualCapa+j+inN);
                    c2.getVerticeNormal()[1]= new VerticeNormal(verticeActualCapa+j+nLados+inV*3,verticeActualCapa+j+inN);
                    car[verticeActualCapa+j+inN]=c2;
                }
            }
            verticeActualCapa+=nLados;
            rad+=incrRadio;
        }
        setCaras(car);
        setNCaras(nNorm);
        setNNormales(nNorm);
        setNormales(norm);
        setNVertices(nVert);
        setVertices(vert);
        calcularNormalesNewell();
    }
}
