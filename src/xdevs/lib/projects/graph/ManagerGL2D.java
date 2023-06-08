package xdevs.lib.projects.graph;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import xdevs.lib.projects.graph.structs.ManejadorRaton2D;

public class ManagerGL2D implements GLEventListener {
	private float _xLeft;
	private float _xRight;
	private float _yBot;
	private float _yTop;
	private GLCanvas _lienzo;
	private Dibujante _dibujante;
	private boolean _actualizar;
	
	public ManagerGL2D (JFrame contenedor, int xLeft, int xRight, int yBot, int yTop, Dibujante dibujante) {
		this._xLeft = xLeft;
		this._xRight = xRight;
		this._yBot = yBot;
		this._yTop = yTop;
	    _lienzo = new GLCanvas();
		_lienzo.addGLEventListener(this);
		ManejadorRaton2D manejadorRaton = new ManejadorRaton2D(this);
		_lienzo.addMouseListener(manejadorRaton);
		_lienzo.addMouseMotionListener(manejadorRaton);
		contenedor.getContentPane().add(_lienzo,BorderLayout.CENTER);
		_dibujante = dibujante;
		_actualizar = false;
	}
	
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		drawable.swapBuffers();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        glu.gluOrtho2D(_xLeft,_xRight,_yBot,_yTop);
    //    if (_actualizar) {
        	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        	_dibujante.repintarTerreno(drawable,_lienzo.getWidth(),_lienzo.getHeight(),_xLeft,_xRight,_yBot,_yTop);
        	_actualizar = false;
   //     }
	    _dibujante.dibujar2D(drawable,_lienzo.getWidth(),_lienzo.getHeight(),_xLeft,_xRight,_yBot,_yTop);
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		_dibujante.inicializar(gl);
		gl.glClearColor(0,0,0,0.0f);
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
		GLU glu = new GLU(); 
	    glu.gluOrtho2D(_xLeft,_xRight,_yBot,_yTop);
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
	    _actualizar = true;
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int ancho, int alto) {
		GL2 gl = drawable.getGL().getGL2();
	    float radioVolVista=(_xRight-_xLeft)/(_yTop-_yBot);
	    float radioViewPort=(float)ancho/(float)alto;
	    gl.glViewport(0,0,ancho,alto);
	    if (radioVolVista>=radioViewPort){
	        //Aumentamos yTop-yBot
	        float altura_anterior = _yTop-_yBot;
	        _yTop-= (altura_anterior-((_xRight-_xLeft)/radioViewPort))/2.0f;
	        _yBot+=(altura_anterior-((_xRight-_xLeft)/radioViewPort))/2.0f;
	    }
	    else {
	        //Aumentamos xRight-xLeft
	        float anchura_anterior = _xRight-_xLeft;
	        _xRight-=(anchura_anterior-((_yTop-_yBot)*radioViewPort))/2.0f;
	        _xLeft+=(anchura_anterior-((_yTop-_yBot)*radioViewPort))/2.0f;
	    }
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
		GLU glu = new GLU(); 
	    glu.gluOrtho2D(_xLeft,_xRight,_yBot,_yTop);
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity(); 
	   // _actualizar = true;
	    actualizar();
	}

	public void actualizar() {
		_lienzo.repaint();
	}
	
	public void zoom(float factor) {
	    float xRightAnt = _xRight;
	    float xLeftAnt = _xLeft;
	    float yTopAnt = _yTop;
	    float yBotAnt = _yBot;
	    float ancho = (xRightAnt-xLeftAnt)*factor;
	    float alto = (yTopAnt-yBotAnt)*factor;
	    _xRight = (xRightAnt+xLeftAnt)/2+ancho/2;
	    _xLeft = (xRightAnt+xLeftAnt)/2-ancho/2;
	    _yTop = (yTopAnt+yBotAnt)/2+alto/2;
	    _yBot = (yTopAnt+yBotAnt)/2-alto/2;
	    _actualizar = true;
        actualizar();
	}
	
	public void desplazar(float x, float y) {
		_xRight+=x;
		_xLeft+=x;
		_yTop+=y;
		_yBot+=y;
		_actualizar = true;
		actualizar();
	}
	
	public void desplazarProporcionadoDerecha() {
		float desplazamiento = (_xRight-_xLeft)/10;
		_xRight += desplazamiento;
		_xLeft += desplazamiento;
		_actualizar = true;
		actualizar();
	}
	
	public void desplazarProporcionadoIzquierda() {
		float desplazamiento = (_xRight-_xLeft)/10;
		_xRight -= desplazamiento;
		_xLeft -= desplazamiento;
		_actualizar = true;
		actualizar();
	}
	
	public void desplazarProporcionadoArriba() {
		float desplazamiento = (_yTop-_yBot)/10;
		_yTop += desplazamiento;
		_yBot += desplazamiento;
		_actualizar = true;
		actualizar();
	}
	
	public void desplazarProporcionadoAbajo() {
		float desplazamiento = (_yTop-_yBot)/10;
		_yTop -= desplazamiento;
		_yBot -= desplazamiento;
		_actualizar = true;
		actualizar();
	}
	
	public void setXLeft(float xLeft) {
		this._xLeft = xLeft;
	}
	
	public float getXLeft() {
		return (_xLeft);
	}
	
	public void setXRight(float xRight) {
		this._xRight = xRight;
	}
	
	public float getXRight() {
		return (_xRight);
	}
	
	public void setYBot(float yBot) {
		this._yBot = yBot;
	}
	
	public float getYBot() {
		return (_yBot);
	}
	
	public void setYTop(float yTop) {
		this._yTop = yTop;
	}
	
	public float getYTop() {
		return _yTop;
	}
	
	public void movimientoRaton(float x, float y) {
		int alto = _lienzo.getSize().height;
		int ancho = _lienzo.getSize().width;
		float desX = (_xRight-_xLeft)/(ancho/x);
		float desY = (_yTop-_yBot)/(alto/y);
		desplazar(desX,desY);
	}
	
	public void dibujarTerreno() {
		_actualizar = true;
		_lienzo.repaint();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'dispose'");
	}
}
