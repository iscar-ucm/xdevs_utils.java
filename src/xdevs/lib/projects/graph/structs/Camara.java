package xdevs.lib.projects.graph.structs;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;

import xdevs.lib.projects.graph.Seguible;

public class Camara {
	public static final int CENITAL = 0;
	public static final int FRONTAL = 1;
	public static final int LATERAL = 2;
	public static final int POSTERIOR = 3;
	
	private float _desX;
	private float _desY;
	private float _desZ;
	private boolean _anclada;
	private int _tipoCamara;
	private Seguible _seguible;
	float _pitch;
	float _yaw;
    PV3D _eye;
    PV3D _look;
    PV3D _up;
    PV3D _u;
    PV3D _v;
    PV3D _n;
    
	public Camara () {
	    _eye=new PV3D();
	    _look=new PV3D();
	    _up=new PV3D();
	    _u=new PV3D();
	    _v=new PV3D();
	    _n=new PV3D();
	    _pitch=0;
	    _yaw=0;
	    _anclada = false;
	    _seguible = null;
	    _tipoCamara = -1;
	    _desX = 0;
	    _desY = 0;
	    _desZ = 0;
	}

	public Camara (PV3D eye, PV3D look, PV3D up) {
	    _eye=null;
	    _look=null;
	    _up=null;
	    _u=null;
	    _v=null;
	    _n=null;
	    _pitch=0;
	    _yaw=0;
	    _anclada = false;
	    _seguible = null;
	    _tipoCamara = -1;
	    _desX = 0;
	    _desY = 0;
	    _desZ = 0;
	    setView(eye,look,up);
	}

	public void set_eye (PV3D _eye) {
	    this._eye=_eye;
	}

	public PV3D get_eye () {
	    return (_eye);
	}

	public void set_look (PV3D _look) {
	    this._look=_look;
	}

	public PV3D get_look () {
	    return (_look);
	}

	public void set_up (PV3D _up) {
	    this._up=_up;
	}

	public PV3D get_up () {
	    return (_up);
	}

	public void set_u (PV3D _u) {
	    this._u=_u;
	}

	public PV3D get_u () {
	    return (_u);
	}

	public void set_v (PV3D _v) {
	    this._v=_v;
	}

	public PV3D get_v () {
	    return (_v);
	}

	public void set_n (PV3D _n) {
	    this._n=_n;
	}

	public PV3D get_n () {
	    return (_n);
	}

	public void roll (float ang) {
	    //El ángulo pasado como parámetro lo suponemos en grados
	    float cs = ((Double)Math.cos((Math.PI*ang)/((float)180))).floatValue();
	    float sn = ((Double)Math.sin((Math.PI*ang)/((float)180))).floatValue();
	    PV3D viejoU = _u.clonar();
	    PV3D viejoV = _v.clonar();
	    _u = new PV3D(cs*viejoU.getX()+sn*viejoV.getX(),cs*viejoU.getY()+sn*viejoV.getY(),cs*viejoU.getZ()+sn*viejoV.getZ(),0);
	    _v = new PV3D(-sn*viejoU.getX()+cs*viejoV.getX(),-sn*viejoU.getY()+cs*viejoV.getY(),-sn*viejoU.getZ()+cs*viejoV.getZ(),0);
	}

	public void yaw (float ang) {
	    //El ángulo pasado como parámetro lo suponemos en grados
	    float cs = ((Double)Math.cos((Math.PI*ang)/((float)180))).floatValue();
	    float sn = ((Double)Math.sin((Math.PI*ang)/((float)180))).floatValue();
	    PV3D viejoN= _n.clonar();
	    PV3D viejoU= _u.clonar();
	    _u = new PV3D(cs*viejoU.getX()+sn*viejoN.getX(),cs*viejoU.getY()+sn*viejoN.getY(),cs*viejoU.getZ()+sn*viejoN.getZ(),0);
	    _n = new PV3D(-sn*viejoU.getX()+cs*viejoN.getX(),-sn*viejoU.getY()+cs*viejoN.getY(),-sn*viejoU.getZ()+cs*viejoN.getZ(),0);
	}

	public void pitch (float ang) {
	    //El ángulo pasado como parámetro lo suponemos en grados
	    float cs = ((Double)Math.cos((Math.PI*ang)/((float)180))).floatValue();
	    float sn = ((Double)Math.sin((Math.PI*ang)/((float)180))).floatValue();
	    PV3D viejoN= _n.clonar();
	    PV3D viejoV= _v.clonar();
	    _v = new PV3D(cs*viejoV.getX()+sn*viejoN.getX(),cs*viejoV.getY()+sn*viejoN.getY(),cs*viejoV.getZ()+sn*viejoN.getZ(),0);
	    _n = new PV3D(-sn*viejoV.getX()+cs*viejoN.getX(),-sn*viejoV.getY()+cs*viejoN.getY(),-sn*viejoV.getZ()+cs*viejoN.getZ(),0);
	    _pitch+=ang;
	}
	
	public void yawTo (float ang) {
		yaw(ang-_yaw);
	}
	
	public void pitchTo (float ang) {
		pitch(ang-_pitch);
	}

	public void desplazar (float desU, float desV, float desN) {
		System.out.println(">>> "+desV);
	    _eye.setX(_eye.getX()+desU*_u.getX()+desV*_v.getX()+desN*_n.getX());
	    _eye.setY(_eye.getY()+desU*_u.getY()+desV*_v.getY()+desN*_n.getY());
	    _eye.setZ(_eye.getZ()+desU*_u.getZ()+desV*_v.getZ()+desN*_n.getZ());
	}
	
	public void goTo (float x, float y, float z) {
		System.out.println(">>> "+y);
		_eye.setX(x);
		_eye.setY(y);
		_eye.setZ(z);
	}

	public void setModelViewMatrix(GL2 gl) {
	    float[] m = new float[16];
	    m[0] = _u.getX();
	    m[1] = _v.getX();
	    m[2] = _n.getX();
	    m[3] = 0;
	    m[4] = _u.getY();
	    m[5] = _v.getY();
	    m[6] = _n.getY();
	    m[7] = 0;
	    m[8] = _u.getZ();
	    m[9] = _v.getZ();
	    m[10]= _n.getZ();
	    m[11]= 0;
	    m[12]= -_eye.prodEscalar(_u);
	    m[13]= -_eye.prodEscalar(_v);
	    m[14]= -_eye.prodEscalar(_n);
	    m[15]= 1;
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadMatrixf(m,0);
	}

	public void setView(PV3D eye, PV3D look, PV3D up) {
	    _eye=eye;
	    _look=look;
	    _up=up;
	    _n = new PV3D(_eye.getX()-_look.getX(),_eye.getY()-_look.getY(),_eye.getZ()-_look.getZ(),0);
	    _n.normalizar();
	    _u = _up.prodVectorial(_n);
	    _u.normalizar();
	    _v = _n.prodVectorial(_u);
	    _v.normalizar();
	}

	public void fijarVistaOrtogonal(float xLeft, float xRight, float yBot, float yTop, float N, float F, GL2 gl) {
	    gl.glMatrixMode (GL2.GL_PROJECTION);
	    gl.glLoadIdentity ();
	    gl.glOrtho(xLeft,xRight, yBot,yTop, N,F);
	    setModelViewMatrix(gl);
	}

	public void fijarVistaPerspectiva(float angulo, float proporcion, float N, float F, GL2 gl) {
		GLUgl2 glu = new GLUgl2();
	    gl.glMatrixMode (GL2.GL_PROJECTION);
	    gl.glLoadIdentity ();
	    glu.gluPerspective(angulo,proporcion,N,F);
	    setModelViewMatrix(gl);
	}

	public void fijarVistaOblicua(float xLeft, float xRight, float yBot, float yTop, float N, float F, PV3D d, GL2 gl) {
	    gl.glMatrixMode (GL2.GL_PROJECTION);
	    gl.glLoadIdentity ();
	    gl.glOrtho(xLeft,xRight, yBot,yTop, N,F);
	    if(d.getZ()==0.0) return;
	    float m[] = new float[16];
	    for (int i=0;i<16;i++) m[i]=(i%5==0)? 1.0f:0.0f;
	    m[8]=-d.getX()/d.getZ();
	    m[9]=-d.getY()/d.getZ();
	    gl.glMultMatrixf(m,0);
	    setModelViewMatrix(gl);
	}
	
	public void anclar(Seguible seguible, int tipoCamara) {
		_tipoCamara = tipoCamara;
		switch (tipoCamara) {
		case CENITAL: {
			if (seguible.devolverTipoSeguible() == 1) {
				_desY = 5000;
				_desX = 10;
				_desZ = 25;
				setView(_eye,new PV3D(0,-1,0),new PV3D(0,0,1));
			}
			else {
				_desY = 50;
				_desX = 0;
				_desZ = 50;
				setView(_eye,new PV3D(0,-1,0),new PV3D(0,0,1));
			}
		}; break;
		case FRONTAL: {
			if (seguible.devolverTipoSeguible() == 1) {
				_desY = 0;
				_desX = 0;
				_desZ = 30000;
				setView(_eye,new PV3D(0,-1,0),new PV3D(0,1,0));
			}
			else {
				_desY = 10;
				_desX = 0;
				_desZ = 700;				
				setView(_eye,new PV3D(0,-1,0),new PV3D(0,1,0));
			}
		}; break;
		case POSTERIOR: {
			if (seguible.devolverTipoSeguible() == 1) {
				_desY = 0;
				_desX = 0;
				_desZ = -30000;
				setView(_eye,new PV3D(0,1,0),new PV3D(0,1,0));
			}
			else {
				_desY = 10;
				_desX = 0;
				_desZ = -1000;				
				setView(_eye,new PV3D(0,1,0),new PV3D(0,1,0));
			}
		}; break;
		case LATERAL: {
			if (seguible.devolverTipoSeguible() == 1) {
				_desY = 0;
				_desX = 10000;
				_desZ = 0;
				setView(_eye,new PV3D(-1,0,0),new PV3D(0,1,0));
			}
			else {
				_desY = 10;
				_desX = 1000;
				_desZ = 0;				
				setView(_eye,new PV3D(-1,0,0),new PV3D(0,1,0));
			}
		}; break;
		}
		_seguible = seguible;
		_anclada = true;
	}
	
	public void desanclar() {
		_anclada = false;
	}
	
	public void actualizar(GL2 gl) {
		if (_anclada) {
			System.out.println("desX "+_desX);
			System.out.println("desY "+_desY);
			System.out.println("desz "+_desZ);
			PV3D posicion = _seguible.obtenerPosicion();
			_eye.setX((posicion.getX()+_desX)/10);
			_eye.setY(posicion.getY()+_desY);
			_eye.setZ((posicion.getZ()+_desZ)/10);
		}
		setModelViewMatrix(gl);
	}
	
	public void dibujar2d(GL2 gl) {
		gl.glPointSize(4);
		gl.glColor3f(1,0,0);
		gl.glBegin(GL2.GL_POINTS);
			gl.glVertex2f(_eye.getX()/100,_eye.getZ()/100);
		gl.glEnd();
	}
}