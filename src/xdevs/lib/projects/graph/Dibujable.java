package xdevs.lib.projects.graph;

import com.jogamp.opengl.GL2;

public interface Dibujable {
	public void inicializar(GL2 gl);
	public void dibujar3D(GL2 gl);
	public void dibujar2D(GL2 gl);
}
