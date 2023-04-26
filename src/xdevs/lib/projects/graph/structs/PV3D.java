package xdevs.lib.projects.graph.structs;

public class PV3D {
	private float _x;
	private float _y;
	private float _z;
	private int _pv;
	
	public PV3D () {
	    this._x=0;
	    this._y=0;
	    this._z=0;
	    this._pv=1;
	}

	public PV3D (float _x, float _y, float _z) {
	    this._x=_x;
	    this._y=_y;
	    this._z=_z;
	    this._pv=1;
	}

	public PV3D(float _x, float _y, float _z, int _pv) {
	    this._x=_x;
	    this._y=_y;
	    this._z=_z;
	    this._pv=_pv;
	}

	public float getX() {
	    return (_x);
	}

	public void setX(float _x) {
	    this._x=_x;
	}

	public float getY() {
	    return (_y);
	}

	public void setY(float _y) {
	    this._y=_y;
	}

	public float getZ() {
	    return (_z);
	}

	public void setZ(float _z) {
	    this._z=_z;
	}

	public int getPV() {
	    return (_pv);
	}

	public void setPV(int _pv) {
	    this._pv=_pv;
	}

	public void normalizar() {
	  float modulo=this.modulo();
	  if (modulo!=0) {
	    this._x/=modulo;
	    this._y/=modulo;
	    this._z/=modulo;
	    }
	}

	public PV3D clonar(){
	    return (new PV3D(this._x,this._y,this._z,this._pv));
	}

	public float modulo(){
	    return ((Double)Math.sqrt((_x*_x)+(_y*_y)+(_z*_z))).floatValue();
	}

	public float prodEscalar(PV3D vector) {
	    return (_x*vector.getX()+_y*vector.getY()+_z*vector.getZ());
	}

	public PV3D prodVectorial(PV3D vector) {
	    float x=this._y*vector.getZ()-this._z*vector.getY();
	    float y=-(this._x*vector.getZ()-this._z*vector.getX());
	    float z=this._x*vector.getY()-this._y*vector.getX();
	    return (new PV3D(x,y,z,0));
	}
}
