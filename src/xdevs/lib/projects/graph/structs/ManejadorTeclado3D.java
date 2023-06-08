package xdevs.lib.projects.graph.structs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import xdevs.lib.projects.graph.ManagerGL3D;

public class ManejadorTeclado3D extends KeyAdapter{
	private ManagerGL3D _managerGL;
	
	public ManejadorTeclado3D(ManagerGL3D managerGL) {
		this._managerGL = managerGL;		
	}
	
	public void keyPressed(KeyEvent e) {
		float x,y,z;
		x = 0;
		y = 0;
		z = 0;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP: {
			z = -100;
		}; break;
		case KeyEvent.VK_DOWN: {
			z = 100;
		}; break;
		case KeyEvent.VK_LEFT: {
			x = -100;
		}; break;
		case KeyEvent.VK_RIGHT: {
			x = 100;
		}; break;
		}
		_managerGL.desplazar(x, y, z);
	}
}
