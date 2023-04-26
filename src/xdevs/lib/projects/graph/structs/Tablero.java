package xdevs.lib.projects.graph.structs;

public class Tablero extends Malla{
	private float _largo;
    private float _ancho;
    private float _grueso;
    private int _nlargo;
    private int _nancho;
    private int _ngrueso;
    
    public Tablero(float largo, float ancho, float grueso, int nlargo, int nancho, int ngrueso) {
    	this._largo=largo;
        this._ancho=ancho;
        this._grueso=grueso;
        this._nlargo=nlargo;
        this._nancho=nancho;
        this._ngrueso=ngrueso;
        float incLargo=largo/(float)nlargo;
        float incAncho=ancho/(float)nancho;
        float incGrueso=grueso/(float)ngrueso;
        int verX=nlargo+1;
        int verY=ngrueso+1;
        int verZ=nancho+1;
        int verYZ=verY*verZ;
        int nVert=verX*verY*verZ;
        int nNorm=nlargo*ngrueso*2+nlargo*nancho*2+ngrueso*nancho*2;
        PV3D[] vert=new PV3D[nVert];
        PV3D[] norm=new PV3D[nNorm];
        Cara[] car=new Cara[nNorm];
        for (int i=0;i<nlargo+1;i++) {  //Creación de los vértices
            for (int j=0;j<ngrueso+1;j++) {
                for (int z=0;z<nancho+1;z++) {
                    vert[i*(nancho+1)*(ngrueso+1)+j*(nancho+1)+z]=new PV3D(incLargo*i,incGrueso*j,incAncho*z,1);
                }
            }
        }
        for (int i=0;i<nlargo;i++) {    //Caras del plano XY
            for (int j=0;j<ngrueso;j++) {
                Cara c=new Cara(4,new VerticeNormal[4]);
                c.getVerticeNormal()[0]= new VerticeNormal(j*verZ+i*verYZ,i*ngrueso+j);
                c.getVerticeNormal()[1]= new VerticeNormal((j+1)*verZ+i*verYZ,i*ngrueso+j);
                c.getVerticeNormal()[2]= new VerticeNormal((j+1)*verZ+(i+1)*verYZ,i*ngrueso+j);
                c.getVerticeNormal()[3]= new VerticeNormal(j*verZ+(i+1)*verYZ,i*ngrueso+j);
                car[i*ngrueso+j]=c;
                c=new Cara(4,new VerticeNormal[4]);
                c.getVerticeNormal()[0]= new VerticeNormal(j*verZ+i*verYZ+nancho,i*ngrueso+j+ngrueso*nlargo);
                c.getVerticeNormal()[1]= new VerticeNormal(j*verZ+(i+1)*verYZ+nancho,i*ngrueso+j+ngrueso*nlargo);
                c.getVerticeNormal()[2]= new VerticeNormal((j+1)*verZ+(i+1)*verYZ+nancho,i*ngrueso+j+ngrueso*nlargo);
                c.getVerticeNormal()[3]= new VerticeNormal((j+1)*verZ+i*verYZ+nancho,i*ngrueso+j+ngrueso*nlargo);
                car[i*ngrueso+j+ngrueso*nlargo]=c;
            }
        }
        for (int j=0;j<ngrueso;j++) {   //Caras del plano YZ
            for (int z=0;z<nancho;z++) {
                Cara c=new Cara(4,new VerticeNormal[4]);
                c.getVerticeNormal()[0]= new VerticeNormal(j*verZ+z,j*nancho+z+nlargo*ngrueso*2);
                c.getVerticeNormal()[1]= new VerticeNormal(j*verZ+z+1,j*nancho+z+nlargo*ngrueso*2);
                c.getVerticeNormal()[2]= new VerticeNormal((j+1)*verZ+z+1,j*nancho+z+nlargo*ngrueso*2);
                c.getVerticeNormal()[3]= new VerticeNormal((j+1)*verZ+z,j*nancho+z+nlargo*ngrueso*2);
                car[j*nancho+z+nlargo*ngrueso*2]=c;
                c=new Cara(4,new VerticeNormal[4]);
                c.getVerticeNormal()[0]= new VerticeNormal(j*verZ+z+nlargo*verYZ,j*nancho+z+nlargo*ngrueso*2+ngrueso*nancho);
                c.getVerticeNormal()[1]= new VerticeNormal((j+1)*verZ+z+nlargo*verYZ,j*nancho+z+nlargo*ngrueso*2+ngrueso*nancho);
                c.getVerticeNormal()[2]= new VerticeNormal((j+1)*verZ+z+1+nlargo*verYZ,j*nancho+z+nlargo*ngrueso*2+ngrueso*nancho);
                c.getVerticeNormal()[3]= new VerticeNormal(j*verZ+z+1+nlargo*verYZ,j*nancho+z+nlargo*ngrueso*2+ngrueso*nancho);
                car[j*nancho+z+nlargo*ngrueso*2+ngrueso*nancho]=c;
            }
        }
        for (int i=0;i<nlargo;i++) {    //Caras del plano XZ
            for (int z=0;z<nancho;z++) {
                Cara c=new Cara(4,new VerticeNormal[4]);
                c.getVerticeNormal()[0]= new VerticeNormal(i*verYZ+z,i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2);
                c.getVerticeNormal()[1]= new VerticeNormal((i+1)*verYZ+z,i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2);
                c.getVerticeNormal()[2]= new VerticeNormal((i+1)*verYZ+z+1,i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2);
                c.getVerticeNormal()[3]= new VerticeNormal(i*verYZ+z+1,i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2);
                car[i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2]=c;
                c=new Cara(4,new VerticeNormal[4]);
                c.getVerticeNormal()[0]= new VerticeNormal(i*verYZ+z+verZ*ngrueso,i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2+nlargo*nancho);
                c.getVerticeNormal()[1]= new VerticeNormal(i*verYZ+z+1+verZ*ngrueso,i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2+nlargo*nancho);
                c.getVerticeNormal()[2]= new VerticeNormal((i+1)*verYZ+z+1+verZ*ngrueso,i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2+nlargo*nancho);
                c.getVerticeNormal()[3]= new VerticeNormal((i+1)*verYZ+z+verZ*ngrueso,i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2+nlargo*nancho);
                car[i*nancho+z+nlargo*ngrueso*2+ngrueso*nancho*2+nlargo*nancho]=c;
            }
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
