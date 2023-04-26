package xdevs.lib.projects.graph;

import javax.media.opengl.GL;

import ssii2007.grafico.estructura.PV3D;

public interface Dibujable {
	public void inicializar(GL gl);
	public void dibujar3D(GL gl);
	public void dibujar2D(GL gl);
}
