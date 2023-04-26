package xdevs.lib.projects.graph;

import ssii2007.grafico.estructura.PV3D;

public interface Seguible {
	public PV3D obtenerPosicion();
	public float obtenerRumbo();
	public int devolverTipoSeguible();
}
