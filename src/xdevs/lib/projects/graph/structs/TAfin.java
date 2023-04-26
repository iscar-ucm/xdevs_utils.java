package xdevs.lib.projects.graph.structs;

import com.jogamp.opengl.GL2;

public class TAfin {
	private float[] _matriz;
	private float _angX;
	private float _angY;
	private float _angZ;
	private float _posX;
	private float _posY;
	private float _posZ;
	
	public TAfin() {
		_matriz = new float[16];
		for (int i=0;i<16;i++) {
			_matriz[i]=0;
		}
		_matriz[0]=1;
		_matriz[5]=1;
		_matriz[10]=1;
		_matriz[15]=1;
		_angX = 0;
		_angY = 0;
		_angZ = 0;
		_posX = 0;
		_posY = 0;
		_posZ = 0;
	}
	
	public TAfin (float[] matriz) {
		this._matriz=matriz;
	}
	
	public float[] getMatriz() {
		return _matriz;
	}
	
	public void transladar (float x, float y, float z, GL2 gl) {
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glPushMatrix();
	        gl.glLoadMatrixf(_matriz,0);
	        gl.glTranslatef(x,y,z);
	        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX,_matriz,0);
	    gl.glPopMatrix();
	}
	
	public void rotar (float ang, float x, float y, float z, GL2 gl) {
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glPushMatrix();
	        gl.glLoadMatrixf(_matriz,0);
	        gl.glRotatef(ang,x,y,z);
	        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX,_matriz,0);
	    gl.glPopMatrix();
	}
	
	public void escalar (float x, float y, float z, GL2 gl) {
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glPushMatrix();
	        gl.glLoadMatrixf(_matriz,0);
	        gl.glScalef(x,y,z);
	        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX,_matriz,0);
	    gl.glPopMatrix();
	}
	
	public void giroAbsolutoX(float ang, GL2 gl) {
		float dif = ang-_angX;
		_angX = ang;
//		System.out.println(_angX);
	//	System.out.println(dif);
		rotar(dif,1,0,0,gl);
	}
	
	public void giroAbsolutoY(float ang, GL2 gl) {
		float dif = ang-_angY;
		_angY = ang;
	//	System.out.println(_angY);
	//	System.out.println(dif);
		rotar(dif,0,1,0,gl);
	}
	
	public void giroAbsolutoZ(float ang, GL2 gl) {
		float dif = ang-_angZ;
		_angZ = ang;
	//	System.out.println(_angZ);
	//	System.out.println(dif);
		rotar(dif,0,0,1,gl);
	}
	
	public void translacionAbsolutaX(float x, GL2 gl) {
		float dif = x-_posX;
		_posX = x;
		transladar(dif,0,0,gl);
	}
	
	public void translacionAbsolutaY(float y, GL2 gl) {
		float dif = y-_posY;
		_posY = y;
		transladar(0,dif,0,gl);
	}
	
	public void translacionAbsolutaZ(float z, GL2 gl) {
		float dif = z-_posZ;
		_posZ = z;
		transladar(0,0,dif,gl);
	}
	
	public void translacionAbsolutaXYZ(float x, float y, float z, GL2 gl) {
		translacionAbsolutaX(x,gl);
		translacionAbsolutaY(y,gl);
		translacionAbsolutaZ(z,gl);
	}
}
