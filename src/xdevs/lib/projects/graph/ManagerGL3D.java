package xdevs.lib.projects.graph;


import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import xdevs.lib.projects.graph.structs.Camara;
import xdevs.lib.projects.graph.structs.ManejadorRaton3D;
import xdevs.lib.projects.graph.structs.ManejadorTeclado3D;

public class ManagerGL3D implements GLEventListener {
	private Camara _camara;
	private GLCanvas _lienzo;
	private Dibujante _dibujante;
	private ManagerVista _manager;
	private boolean _cambio;
	private float _xLeft;
	private float _xRight;
	private float _yBot;
	private float _yTop;
	private float _N;
	private float _F;
	
	public ManagerGL3D (ManagerVista manager, float xLeft, float xRight, float yBot, float yTop, float N, float F, JFrame contenedor, Dibujante dibujante, Camara camara) {
		this._xLeft = xLeft;
		this._xRight = xRight;
		this._yBot = yBot;
		this._yTop = yTop;
		this._N = N;
		this._F = F;
		this._camara = camara;
	    _lienzo = new GLCanvas();
		_lienzo.addGLEventListener(this);
		ManejadorRaton3D manejadorRaton = new ManejadorRaton3D(this);
		_lienzo.addMouseListener(manejadorRaton);
		_lienzo.addMouseMotionListener(manejadorRaton);
		_lienzo.addKeyListener(new ManejadorTeclado3D(this));
		contenedor.getContentPane().add(_lienzo,BorderLayout.CENTER);
		_cambio = false;
		_dibujante = dibujante;
		_manager = manager;
	}
	
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		_dibujante.inicializar(gl);
		GLU glu = new GLU();
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    glu.gluLookAt(1000,1000,1000,0.0f,0.0f,0.0f,0,1,0); 
		gl.glClearColor(0.6f,0.7f,0.8f,1.0f); 
	//	_camara.fijarVistaOrtogonal(_xLeft,_xRight,_yBot,_yTop,_N,_F,gl);
		_camara.fijarVistaPerspectiva(90, 1, _N, _F, gl);
		gl.glDisable(GL2.GL_TEXTURE_2D );								//disable two dimensional texture mapping
		gl.glDisable(GL2.GL_LIGHTING );								//disable lighting
		gl.glDisable(GL2.GL_BLEND );									//disable blending
		gl.glEnable(GL2.GL_DEPTH_TEST );								//Enable depth testing
		gl.glShadeModel(GL2.GL_SMOOTH );								//enable smooth shading
		gl.glClearDepth( 1.0 );									//depth buffer setup
		gl.glDepthFunc(GL2.GL_LEQUAL );								//set the type of depth test
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );	//the nicest perspective look 
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int ancho, int alto) {
		//se actualiza puerto de vista y el radio
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		float ratioViewPort=(float)ancho/(float)alto;
		gl.glViewport(0,0,ancho,alto);

		// se actualiza el volumen de vista
		// para que su radio coincida con RatioViewPort
		float ratioVolVista=_xRight/_yTop;

		if (ratioVolVista>=ratioViewPort) {
			//Aumentamos yTop-yBot
			_yTop= _xRight/ratioViewPort;
			_yBot=-_yTop;
		}
		else {
			//Aumentamos xRight-xLeft
			_xRight=ratioViewPort*_yTop;
			_xLeft=-_xRight;
		}
	//	_camara.fijarVistaOrtogonal(_xLeft,_xRight,_yBot,_yTop,_N,_F,gl);
		_camara.fijarVistaPerspectiva(90, 1, _N, _F, gl);
	}
	
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		if (_cambio) {
		//	_camara.fijarVistaOrtogonal(_xLeft, _xRight, _yBot, _yTop, _N, _F, gl);
			_camara.fijarVistaPerspectiva(90, 1, _N, _F, gl);
			_cambio = false;
		}
	    _camara.setModelViewMatrix(gl);
	    gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		//gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,PosicionLuz0);
		
		_dibujante.dibujar3D(drawable);

	}
	
	public void displayChanged(GLAutoDrawable drawable, boolean modeChage, boolean displayChanged) {
		System.out.println("DisplayChanged");
	}
	
	public void roll (float angulo) {
		_camara.roll(angulo);
	}
	
	public void yaw (float angulo) {
		_camara.yaw(angulo);
	}
	
	public void pitch (float angulo) {
		_camara.pitch(angulo);
	}
	
	public void rollTo (float angulo) {
	}
	
	public void yawTo (float angulo) {
		_camara.yawTo(angulo);
	}
	
	public void pitchTo (float angulo) {
		_camara.pitchTo(angulo);
	}
	
	public void desplazar (float x, float y, float z) {
		_camara.desplazar(x*1, y*1, z*1);
		_manager.movimientoCamara(_camara.get_eye());
	}
	
	public void goTo (float x, float y, float z) {
		_camara.goTo(x, y, z);
		_manager.movimientoCamara(_camara.get_eye());
	}
	
	public void actualizar() {
		_lienzo.repaint();
	}
	
	public void actualizarZoom(float xRight, float xLeft, float yTop, float yBot) {
		_xRight=xRight;
		_xLeft=xLeft;
		_yTop=yTop;
		_yBot=yBot;
		_cambio=true;
	}
	
	public float getXRight() {
		return _xRight;
	}
	
	public float getXLeft() {
		return _xLeft;
	}
	
	public float getYTop() {
		return _yTop;
	}
	
	public float getYBot() {
		return _yBot;
	}
	
	public void movimientoRaton(float x, float y) {
		int alto = _lienzo.getSize().height;
		int ancho = _lienzo.getSize().width;
		if (x!=0) {
			float pAncho = x/ancho;
			_camara.yaw(pAncho*360);
		}
		if (y!=0) {
			float pAlto = y/alto;
			_camara.pitch(pAlto*360);
		}
	}
	
	public void desplazamientoRaton(float x, float y) {
		int alto = _lienzo.getSize().height;
		int ancho = _lienzo.getSize().width;
		float desX = (_xRight-_xLeft)*Dibujante.escalado/(ancho/x);
		float desY = (_yTop-_yBot)*Dibujante.escalado/(alto/y);
		_camara.desplazar(desX, desY, 0);
		_manager.movimientoCamara(_camara.get_eye());
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'dispose'");
	}

}
