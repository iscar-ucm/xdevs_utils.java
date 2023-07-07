package xdevs.lib.projects.graph.structs;

import com.jogamp.opengl.GL2;

public class Satelite extends ObjetoCompuesto3D{
    
    public Satelite(float longAspas, float ancAspas, float grAspas, float longCuerpo, 
    		 	    float longDisco, float longCono, float radMayor, float radMenor, GL2 gl) {
        int nPartes=5;
        Objeto3D[] objetos=new Objeto3D[nPartes];
        objetos[0]=new Tablero(longAspas,ancAspas,grAspas,1,1,1);
        objetos[1]=new Tablero(longAspas,ancAspas,grAspas,1,1,1);
        objetos[2]=new Cilindro(40,2,radMayor,radMayor);
        objetos[3]=new Cilindro(40,2,radMenor,radMayor);
        objetos[4]=new Cilindro(40,2,radMayor,radMenor);
        objetos[0].getTafin().transladar(-longAspas/2,0,-ancAspas/2,gl);
        objetos[0].setColor(new Color(1,0,0));
        objetos[1].getTafin().rotar(90,0,1,0,gl);
        objetos[1].getTafin().transladar(-longAspas/2,0,-ancAspas/2,gl);
        objetos[1].setColor(new Color(1,0,0));
        objetos[2].getTafin().rotar(-90,1,0,0,gl);
        objetos[2].getTafin().transladar(0,0,grAspas,gl);
        objetos[2].getTafin().escalar(1,1,longCuerpo,gl);
        objetos[2].setColor(new Color(0,1,0));
        objetos[3].getTafin().rotar(-90,1,0,0,gl);
        objetos[3].getTafin().transladar(0,0,grAspas+longCuerpo,gl);
        objetos[3].getTafin().escalar(1,1,longDisco,gl);
        objetos[3].setColor(new Color(0,1,0));
        objetos[4].getTafin().rotar(-90,1,0,0,gl);
        objetos[4].getTafin().transladar(0,0,grAspas+longCuerpo+longDisco,gl);
        objetos[4].getTafin().escalar(1,1,longCono,gl);
        objetos[4].setColor(new Color(0,0,1));
        setNObjetos(nPartes);
        setObjetos(objetos);
    } 
}
