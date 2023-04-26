package xdevs.lib.projects.graph.structs.terrain;

import ssii2007.grafico.estructura.Cara;
import ssii2007.grafico.estructura.Color;
import ssii2007.grafico.estructura.PV3D;
import ssii2007.grafico.estructura.VerticeNormal;

public class TerrenoCosta extends Terreno {

	public TerrenoCosta (int lon, int anc, int numIteraciones, int alturaMax, int profundidadMax, float filter, float pCosta) {
		int iHeight, iDeep;
		int iRandX1, iRandZ1;
		int iRandX2, iRandZ2;
		int iDirX1, iDirZ1;
		int iDirX2, iDirZ2;

		int _longitud = lon+1;
		int _anchura = anc+1;
		setLongitud(_longitud);
		setAnchura(_anchura);
		float[][] _alturas = new float [_longitud][_anchura];
		
		for (int i=0;i<_longitud;i++) {
			for (int j=0;j<_anchura;j++) {
				_alturas[i][j] = 0;
			}
		}
		

		for(int iteracion=0; iteracion<numIteraciones; iteracion++ ) {
			//calculate the height range (linear interpolation from iMaxDelta to
			//iMinDelta) for this fault-pass
			iHeight= alturaMax - (alturaMax*iteracion)/numIteraciones;
			iDeep = profundidadMax - (profundidadMax*iteracion)/numIteraciones;
			
			//pick two points at random from the entire height map
			iRandX1= ((Double)(Math.random()*_longitud)).intValue();
			iRandZ1= ((Double)(Math.random()*_anchura)).intValue();
			
			//check to make sure that the points are not the same
			do
			{
				iRandX2= ((Double)(Math.random()*_longitud)).intValue();
				iRandZ2= ((Double)(Math.random()*_anchura)).intValue();
			} while ( iRandX2==iRandX1 && iRandZ2==iRandZ1 );

			
			//iDirX1, iDirZ1 is a vector going the same direction as the line
			iDirX1= iRandX2-iRandX1;
			iDirZ1= iRandZ2-iRandZ1;
			
			int costa;
			for(int x=0; x<_longitud; x++ )
			{
				costa = (int)(_anchura*pCosta+Math.random()*(_anchura*(pCosta+0.1)-_anchura*pCosta));
				for(int z=0; z<costa; z++ )
				{
					//iDirX2, iDirZ2 is a vector from iRandX1, iRandZ1 to the current point (in the loop)
					iDirX2= x-iRandX1;
					iDirZ2= z-iRandZ1;
					
					//if the result of ( iDirX2*iDirZ1 - iDirX1*iDirZ2 ) is "up" (above 0), 
					//then raise this point by iHeight
					if ((iDirX2*iDirZ1 - iDirX1*iDirZ2) > 0)
						_alturas[x][z]+=iHeight;
				}
				for(int z=costa;z<_anchura; z++) {
					//iDirX2, iDirZ2 is a vector from iRandX1, iRandZ1 to the current point (in the loop)
					_alturas[x][z]-=profundidadMax;
				}
			}
	        setAlturas(_alturas);
			filtrar(filter);
		}

		//normalize the terrain for our purposes
		normalizar(alturaMax,profundidadMax);
	}
}
