package xdevs.lib.projects.graph;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import ssii2007.grafico.estructura.Camara;
import ssii2007.grafico.estructura.PV3D;
import ssii2007.grafico.estructura.terreno.Terreno;
import ssii2007.modelo.CoupledSimulacion;

public class Dibujante {
	private Terreno _terreno;
	private boolean _inicializado;
	private Camara _camara;
	public static final float escalado = 100;
	private CoupledSimulacion _simulacion;

	public Dibujante(Terreno terreno, CoupledSimulacion simulacion, Camara camara) {
	    _terreno = terreno;
	    _simulacion = simulacion;
	    _inicializado = false;
	    this._camara = camara;
	}
	
	public void dibujar3D(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL.GL_FILL);
		_camara.actualizar(gl);
		_terreno.dibujar3D(gl);
		_simulacion.dibujar3D(gl);
	}
	
	public void inicializar(GL gl) {
		if (!_inicializado) {
			_terreno.getTafin().escalar(escalado, 1, escalado, gl);
			_inicializado = true;
			_simulacion.inicializar(gl);
		}
	}
	
	public void dibujar2D(GLAutoDrawable drawable, float ancho, float alto, float xLeft, float xRight, float yBot, float yTop) {
		GL gl = drawable.getGL();
		gl.glPointSize(1);
		_simulacion.dibujar2D(gl);
		_camara.dibujar2d(gl);
	}
	
	public void repintarTerreno(GLAutoDrawable drawable, float ancho, float alto, float xLeft, float xRight, float yBot, float yTop) {
		GL gl = drawable.getGL();
		gl.glPointSize(1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		_terreno.dibujar2D(gl, ancho, alto, xLeft, xRight, yBot, yTop);
	}
}
