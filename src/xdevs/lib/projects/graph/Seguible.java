package xdevs.lib.projects.graph;

import xdevs.lib.projects.graph.structs.PV3D;

public interface Seguible {
	public PV3D obtenerPosicion();
	public float obtenerRumbo();
	public int devolverTipoSeguible();
}
