package xdevs.lib.projects.graph.structs.terrain;

import com.jogamp.opengl.GL2;

import xdevs.lib.projects.graph.structs.Color;
import xdevs.lib.projects.graph.structs.Objeto3D;
import xdevs.lib.projects.graph.structs.PV3D;

public abstract class Terreno extends Objeto3D {
	
	protected boolean m_bTextureMapping;
	
	private int _longitud;
	
	private int _anchura;
	
	private float[][] _alturas;
	
	public Terreno() {
	}
	
	public void normalizar(float alturaMax, float alturaMin) {
		float min,max;

		min=_alturas[0][0];
		max=_alturas[0][0];

		for (int i=0; i<_longitud; i++) {
			for (int j=0; j<_anchura; j++) {
				if (_alturas[i][j]>max) max=_alturas[i][j];
				else if(_alturas[i][j]<min) min=_alturas[i][j];
			}
		}

		for (int i=0; i<_longitud; i++) {
			for (int j=0; j<_anchura; j++) {
				_alturas[i][j]=((_alturas[i][j]-min)/(max-min))*(alturaMax-alturaMin)+alturaMin;
			}
		}
	}
	
	private void FilterHeightBand(int indiceX, int indiceZ, int iStride, int iCount, float fFilter ) {
		float v= _alturas[indiceX][indiceZ];
		int j = indiceZ+indiceX*_anchura;
		int x,z;
		//go through the height band and apply the erosion filter
		for(int i=0; i<iCount-1; i++ )
		{
			z = (j)%_anchura;
			x = (int)(j/_anchura);
			_alturas[x][z] = fFilter*v + (1-fFilter)*_alturas[x][z];
			
			v = _alturas[x][z];
			j+= iStride;
		}
	}
	
	public void filtrar(float fFilter) {

		//erode left to right
		for (int i=0; i<_longitud; i++) {
			FilterHeightBand(i, 0, 1, _anchura, fFilter);
		}
			
		//erode right to left
		for (int i=0; i<_longitud; i++) {
				FilterHeightBand(i, _anchura-1, -1, _anchura, fFilter);
		}

		//erode top to bottom
		for (int j=0; j<_anchura; j++) {
				FilterHeightBand(0, j, _anchura, _longitud, fFilter);
		}

		//erode from bottom to top
		for (int j=0; j<_anchura; j++) {
				FilterHeightBand(_longitud-1, j, -_anchura, _longitud, fFilter);
		}
			
	}
	
	public float getAltura(int x, int z) {
		return _alturas[x][z];
	}
	
	public void setLongitud(int longitud) {
		this._longitud = longitud;
	}
	
	public int getLongitud() {
		return _longitud;
	}
	
	public void setAnchura(int anchura) {
		this._anchura = anchura;
	}
	
	public int getAnchura() {
		return _anchura;
	}
	
	public void setAlturas(float[][] alturas) {
		this._alturas = alturas;
	}
	
	public float[][] getAlturas() {
		return _alturas;
	}
	
	public static Color colorDeMapa(float altura) {
		if (altura>3500) {
			return new Color(1,1,1);
		}
		else if (altura>3400) {
			return new Color((252f/256f),(252f/256f),(252f/256f));
		}
		else if (altura>3300) {
			return new Color((248f/256f),(248f/256f),(248f/256f));
		}
		else if (altura>3200) {
			return new Color((244f/256f),(244f/256f),(244f/256f));
		}
		else if (altura>3100) {
			return new Color((240f/256f),(240f/256f),(240f/256f));
		}
		else if (altura>3000) {
			return new Color((236f/256f),(236f/256f),(236f/256f));
		}
		else if (altura>2900) {
			return new Color((222.6f/256f),(222.6f/256f),(222.6f/256f));
		}
		else if (altura>2800) {
			return new Color((209.2f/256f),(209.2f/256f),(209.2f/256f));
		}
		else if (altura>2700) {
			return new Color((195.8f/256f),(195.8f/256f),(195.8f/256f));
		}
		else if (altura>2600) {
			return new Color((182.4f/256f),(182.4f/256f),(182.4f/256f));
		}
		else if (altura>2500) {
			return new Color((169f/256f),(169f/256f),(169f/256f));
		}
		else if (altura>2400) {
			return new Color((163f/256f),(149f/256f),(134f/256f));
		}
		else if (altura>2300) {
			return new Color((157f/256f),(129f/256f),(104f/256f));
		}
		else if (altura>2200) {
			return new Color((151f/256f),(109f/256f),(74f/256f));
		}
		else if (altura>2100) {
			return new Color((145f/256f),(89f/256f),(44f/256f));
		}
		else if (altura>2000) {
			return new Color((139f/256f),(69f/256f),(19f/256f));
		}
		else if (altura>1900) {
			return new Color((148f/256f),(82f/256f),(17.4f/256f));
		}
		else if (altura>1800) {
			return new Color((157f/256f),(95f/256f),(15.8f/256f));
		}
		else if (altura>1700) {
			return new Color((166f/256f),(108f/256f),(14.2f/256f));
		}
		else if (altura>1600) {
			return new Color((175f/256f),(121f/256f),(12.6f/256f));
		}
		else if (altura>1500) {
			return new Color((184f/256f),(134f/256f),(11f/256f));
		}
		else if (altura>1400) {
			return new Color((172.8f/256f),(132.8f/256f),(11f/256f));
		}
		else if (altura>1300) {
			return new Color((161.6f/256f),(131.6f/256f),(11f/256f));
		}
		else if (altura>1200) {
			return new Color((150.4f/256f),(130.4f/256f),(11f/256f));
		}
		else if (altura>1100) {
			return new Color((139.2f/256f),(129.2f/256f),(11f/256f));
		}
		else if (altura>1000) {
			return new Color((128f/256f),(128f/256f),(11f/256f));
		}		
		else if (altura>900) {
			return new Color((128f/256f),(122.4f/256f),(8.8f/256f));
		}
		else if (altura>800) {
			return new Color((128f/256f),(116.8f/256f),(6.6f/256f));
		}
		else if (altura>700) {
			return new Color((128f/256f),(111.2f/256f),(4.4f/256f));
		}
		else if (altura>600) {
			return new Color((128f/256f),(105.6f/256f),(2.2f/256f));
		}
		else if (altura>500) {
			return new Color((128f/256f),(100f/256f),0);
		}
		else if (altura>400) {
			return new Color((102.4f/256f),(105.6f/256f),0);
		}
		else if (altura>300) {
			return new Color((76.8f/256f),(111.2f/256f),0);
		}
		else if (altura>200) {
			return new Color((51.2f/256f),(116.8f/256f),0);
		}
		else if (altura>100) {
			return new Color((25.6f/256f),(122.4f/256f),0);
		}
		else if (altura>0) {
			return new Color(0,(128f/256f),0);		
		}
		else if (altura>-100) {
			return new Color((14.4f/256f),(152.4f/256f),(30.8f/256f));		
		}
		else if (altura>-200) {
			return new Color((28.8f/256f),(176.8f/256f),(61.6f/256f));		
		}
		else if (altura>-300) {
			return new Color((43.2f/256f),(201.2f/256f),(92.4f/256f));		
		}
		else if (altura>-400) {
			return new Color((57.6f/256f),(225.6f/256f),(123.2f/256f));		
		}
		else if (altura>-500) {
			return new Color((72f/256f),(250f/256f),(154f/256f));
		}
		else if (altura>-600) {
			return new Color((72f/256f),(241.8f/256f),(204f/256f));
		}
		else if (altura>-700) {
			return new Color((72f/256f),(233.6f/256f),(204f/256f));
		}
		else if (altura>-800) {
			return new Color((72f/256f),(225.4f/256f),(204f/256f));
		}
		else if (altura>-900) {
			return new Color((72f/256f),(217.2f/256f),(204f/256f));
		}
		else if (altura>-1000) {
			return new Color((72f/256f),(209f/256f),(204f/256f));
		}
		else if (altura>-1100) {
			return new Color((72f/256f),(209f/256f),(191f/256f));
		}
		else if (altura>-1200) {
			return new Color((72f/256f),(209f/256f),(178f/256f));
		}
		else if (altura>-1300) {
			return new Color((72f/256f),(209f/256f),(165f/256f));
		}
		else if (altura>-1400) {
			return new Color((72f/256f),(209f/256f),(152f/256f));
		}
		else if (altura>-1500) {
			return new Color((72f/256f),(209f/256f),(139f/256f));
		}
		else if (altura>-1600) {
			return new Color((57.6f/256f),(167.2f/256f),(139f/256f));
		}
		else if (altura>-1700) {
			return new Color((43.2f/256f),(125.4f/256f),(139f/256f));
		}
		else if (altura>-1800) {
			return new Color((28.8f/256f),(83.6f/256f),(139f/256f));
		}
		else if (altura>-1900) {
			return new Color((14.4f/256f),(41.8f/256f),(139f/256f));
		}
		else if (altura>-2000) {
			return new Color(0,0,(139f/256f));
		}
		else if (altura>-2100) {
			return new Color(0,0,(127.2f/256f));
		}
		else if (altura>-2200) {
			return new Color(0,0,(115.4f/256f));
		}
		else if (altura>-2300) {
			return new Color(0,0,(103.6f/256f));
		}
		else if (altura>-2400) {
			return new Color(0,0,(91.8f/256f));
		}
		else return new Color(0,0,(80f/256f));
	}
	
	public static Color colorDeAltura(float altura) {
		if (altura>3500) {
			return new Color(1,1,1);
		}
		else if (altura>3000) {
			return new Color(((236f+(20f*(altura-3000))/500f)/256f),((236f+(20f*(altura-3000))/500f)/256f),((236f+(20f*(altura-3000))/500f)/256f));
		}
		else if (altura>2500) {
			return new Color(((169f+(67f*(altura-2500))/500f)/256f),((169f+(67f*(altura-2500))/500f)/256f),((169f+(67f*(altura-2500))/500f)/256f));
		}
		else if (altura>2000) {
			return new Color(((139f+(30f*(altura-2000))/500f)/256f),((69f+(100f*(altura-2000))/500f)/256f),((19f+(156f*(altura-2000))/500f)/256f));
		}
		else if (altura>1500) {
			return new Color(((184f-(45f*(altura-1500))/500f)/256f),((134f-(65f*(altura-1500))/500f)/256f),((11f+(8f*(altura-1500))/500f)/256f));
		}
		else if (altura>1000) {
			return new Color(((128f+(56f*(altura-1000))/500f)/256f),((128f+(6f*(altura-1000))/500f)/256f),(((11f*(altura-1000))/500f)/256f));
		}
		else if (altura>500) {
			return new Color((((128f*(altura-500))/500f)/256f),((100f+(28f*(altura-500))/500f)/256f),0);
		}
		else if (altura>30) {
			return new Color(0,((128f-(28f*(altura-30))/470f)/256f),0);		
		}
		else if (altura>20) {
			return new Color(((256f-(256*(altura-20)/10f))/256f),((231f-(103f*(altura-20))/10f)/256f),0);
		}
		else if (altura>0) {
			return new Color(1,231f/256f,0);
		}
		else if (altura>-500) {
			return new Color(((72*(-altura)/500f)/256f),((250f-(41f*(-altura))/500f)/256f),((154f+(50f*(-altura))/500f)/256f));
		}
		else if (altura>-1000) {
			return new Color(((72f-(72f*(-(altura+500)))/500f)/256f),((209f-(70f*(-(altura+500)))/500f)/256f),((204f-(65f*(-(altura+500)))/500f)/256f));
		}
		else if (altura>-1500) {
			return new Color(0,((139f-(139f*(-(altura+1000)))/500f)/256f),(139f/256f));
		}
		else if (altura>-2000) {
			return new Color(0,0,((139f-(59f*(-(altura+1500)))/500f)/256f));
		}
		else return new Color(0,0,(80f/256f));
	}
	
	public void dibujar3D(GL2 gl) {
		boolean sumergido;
		Color color;
        gl.glPushMatrix();
            gl.glMultMatrixf(getTafin().getMatriz(),0);
            for (int fila=0;fila<_longitud-1; fila++) {
            	for (int columna=0;columna<_anchura-1; columna++) {
                	PV3D[] normales = calcularNormal(fila,columna);
                	
                	//Primera cara
                    sumergido = true;
                    gl.glBegin(GL2.GL_POLYGON);
	                	//Vertice x,z
	                	color = colorDeAltura(_alturas[fila][columna]);
	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
	                    gl.glNormal3f(normales[0].getX(),normales[0].getY(),normales[0].getZ());
	                    gl.glVertex3f(fila,_alturas[fila][columna],columna);
	                    if (_alturas[fila][columna]>0) sumergido = false;
	                    
	                    //Vertice x,z+1
	                	color = colorDeAltura(_alturas[fila][columna+1]);
	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
	                    gl.glNormal3f(normales[0].getX(),normales[0].getY(),normales[0].getZ());
	                    gl.glVertex3f(fila,_alturas[fila][columna+1],columna+1);
	                    if (_alturas[fila][columna+1]>0) sumergido = false;
	                    
	                    //Vertyice x+1,z
	                	color = colorDeAltura(_alturas[fila+1][columna]);
	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
	                    gl.glNormal3f(normales[0].getX(),normales[0].getY(),normales[0].getZ());
	                    gl.glVertex3f(fila+1,_alturas[fila+1][columna],columna);
	                    if (_alturas[fila+1][columna]>0) sumergido = false;  
                    gl.glEnd();
                    
                    if (sumergido) {
                        gl.glBegin(GL2.GL_POLYGON);
                        	//Vertice x,z
                        	color = colorDeAltura(_alturas[fila][columna]);
	                    	gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
                            gl.glNormal3f(0,1,0);
                            gl.glVertex3f(fila,0,columna);
                            
                        	//Vertice x,z+1
                            color = colorDeAltura(_alturas[fila][columna+1]);
    	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
                            gl.glNormal3f(0,1,0);
                            gl.glVertex3f(fila,0,columna+1);
                            
                        	//Vertice x+1,z
                            color = colorDeAltura(_alturas[fila+1][columna]);
    	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
                            gl.glNormal3f(0,1,0);
                            gl.glVertex3f(fila+1,0,columna);
                        gl.glEnd();
                    } 
                    
                  //Segunda cara
                    sumergido = true;
                    gl.glBegin(GL2.GL_POLYGON);
	                	//Vertice x,z+1
	                	color = colorDeAltura(_alturas[fila][columna+1]);
	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
	                    gl.glNormal3f(normales[1].getX(),normales[1].getY(),normales[1].getZ());
	                    gl.glVertex3f(fila,_alturas[fila][columna+1],columna+1);
	                    if (_alturas[fila][columna]>0) sumergido = false;
	                    
	                    //Vertice x+1,z+1
	                	color = colorDeAltura(_alturas[fila+1][columna+1]);
	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
	                    gl.glNormal3f(normales[1].getX(),normales[1].getY(),normales[1].getZ());
	                    gl.glVertex3f(fila+1,_alturas[fila+1][columna+1],columna+1);
	                    if (_alturas[fila][columna+1]>0) sumergido = false;
	                    
	                    //Vertyice x+1,z
	                	color = colorDeAltura(_alturas[fila+1][columna]);
	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
	                    gl.glNormal3f(normales[1].getX(),normales[1].getY(),normales[1].getZ());
	                    gl.glVertex3f(fila+1,_alturas[fila+1][columna],columna);
	                    if (_alturas[fila+1][columna]>0) sumergido = false;  
                    gl.glEnd();
                    
                    if (sumergido) {
                        gl.glBegin(GL2.GL_POLYGON);
                        	//Vertice x,z+1
                        	color = colorDeAltura(_alturas[fila][columna+1]);
	                    	gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
                            gl.glNormal3f(0,1,0);
                            gl.glVertex3f(fila,0,columna+1);
                            
                        	//Vertice x+1,z+1
                            color = colorDeAltura(_alturas[fila+1][columna+1]);
    	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
                            gl.glNormal3f(0,1,0);
                            gl.glVertex3f(fila+1,0,columna+1);
                            
                        	//Vertice x+1,z
    	                	color = colorDeAltura(_alturas[fila+1][columna]);
    	                    gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
                            gl.glNormal3f(0,1,0);
                            gl.glVertex3f(fila+1,0,columna);
                        gl.glEnd();
                    }
            	}
            }
        gl.glPopMatrix();
	}
	
	private PV3D[] calcularNormal(int fila, int columna) {
        float nx,ny,nz;
        PV3D[] normales = new PV3D[2];
        
        //Primera normal 
        nx=0;
        ny=0;
        nz=0;
        //Punto x,z -> x,z+1 
        nx+=(_alturas[fila][columna]-_alturas[fila][columna+1])*(columna+(columna+1));
        ny+=(columna-(columna+1))*(fila+fila);
        nz+=(fila-fila)*(_alturas[fila][columna]+_alturas[fila][columna+1]);
        //Punto x,z+1 -> x+1,z
        nx+=(_alturas[fila][columna+1]-_alturas[fila+1][columna])*((columna+1)+columna);
        ny+=((columna+1)-columna)*(fila+(fila+1));
        nz+=(fila-(fila+1))*(_alturas[fila][columna+1]+_alturas[fila+1][columna]);
        //Punto x+1,z -> x,z
        nx+=(_alturas[fila+1][columna]-_alturas[fila][columna])*(columna+(columna));
        ny+=(columna-(columna))*((fila+1)+fila);
        nz+=((fila+1)-fila)*(_alturas[fila+1][columna]+_alturas[fila][columna]);
        normales[0] = new PV3D(nx,ny,nz);
        
        //Segunda normal 
        nx=0;
        ny=0;
        nz=0;
        //Punto x,z+1 -> x+1,z+1 
        nx+=(_alturas[fila][columna+1]-_alturas[fila+1][columna+1])*((columna+1)+(columna+1));
        ny+=((columna+1)-(columna+1))*(fila+(fila+1));
        nz+=(fila-(fila+1))*(_alturas[fila][columna+1]+_alturas[fila+1][columna+1]);
        //Punto x+1,z+1 -> x+1,z
        nx+=(_alturas[fila+1][columna+1]-_alturas[fila+1][columna])*((columna+1)+columna);
        ny+=((columna+1)-columna)*((fila+1)+(fila+1));
        nz+=((fila+1)-(fila+1))*(_alturas[fila+1][columna+1]+_alturas[fila+1][columna]);
        //Punto x+1,z -> x,z+1
        nx+=(_alturas[fila+1][columna]-_alturas[fila][columna+1])*(columna+(columna+1));
        ny+=(columna-(columna+1))*((fila+1)+fila);
        nz+=((fila+1)-fila)*(_alturas[fila+1][columna]+_alturas[fila][columna+1]);
        normales[1] = new PV3D(nx,ny,nz);
        
        return normales;
	}
	
	public void dibujar2D(GL2 gl, float ancho, float alto, float xLeft, float xRight, float yBot, float yTop) {	
		Color color;
		float margenError=0.05f;
		float incremento = (xRight-xLeft)/ancho;
		incremento=incremento-incremento*margenError;
		gl.glBegin(GL2.GL_POINTS);
		float x = xLeft;
		float y = yBot;
		float altura;
		float p00, p01, p10, p11;
		while (x<xRight) {
			y = yBot;
			while (y<yTop) {
				if ((x>=0)&&(x<_longitud)&&(y>=0)&&(y<_anchura)) {
					if ((x<_longitud-1)&&(y<_anchura-1)){
						p00 = (1-(x-((int)x)))*(1-(y-((int)y)))*_alturas[(int)x][(int)y];
						p10 = (x-((int)x))*(1-(y-((int)y)))*_alturas[(int)(x+1)][(int)y];
						p01 = (1-(x-((int)x)))*(y-((int)y))*_alturas[(int)x][(int)(y+1)];
						p11 = (x-((int)x))*(y-((int)y))*_alturas[(int)(x+1)][(int)(y+1)];
						altura = p00+p10+p01+p11;
					}
					else {
						altura = _alturas[(int)x][(int)y];
					}
					color = colorDeMapa(altura);
					gl.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
					gl.glVertex2f(x,y);
				}
				y+=incremento;
			}
			x+=incremento;
		}
		gl.glEnd(); 
	}
}