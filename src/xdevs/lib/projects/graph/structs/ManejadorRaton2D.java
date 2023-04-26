package xdevs.lib.projects.graph.structs;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import ssii2007.grafico.ManagerGL2D;

public class ManejadorRaton2D extends MouseInputAdapter {
	private ManagerGL2D _managerGL;
	private Point _punto;
	
	public ManejadorRaton2D(ManagerGL2D managerGL) {
		this._managerGL = managerGL;
		_punto = null;
	}
	
	public void mousePressed(MouseEvent e) {
		_punto = e.getPoint();
	}
	
	public void mouseDragged(MouseEvent e) {
		Point puntoAnterior = _punto;
		_punto = e.getPoint();
		if (e.getModifiers()==MouseEvent.BUTTON1_MASK) {
		    _managerGL.movimientoRaton((float)(-_punto.getX()+puntoAnterior.getX()), (float)(_punto.getY()-puntoAnterior.getY()));
		}
		if (e.getModifiers()==MouseEvent.BUTTON2_MASK) {
			_managerGL.movimientoRaton((float)(-_punto.getX()+puntoAnterior.getX()),0);
		}
		if (e.getModifiers()==MouseEvent.BUTTON3_MASK) {
			_managerGL.movimientoRaton(0,(float)(_punto.getY()-puntoAnterior.getY()));
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		_punto = null;
	}
}
