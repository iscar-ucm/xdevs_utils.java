package xdevs.lib.projects.barcos;

import xdevs.lib.projects.graph.structs.Punto;
import xdevs.lib.projects.graph.structs.Ruta;
import xdevs.lib.projects.math.FuncionProbabilidadAbstracta;

public interface Algoritmo {
	
	public Ruta dameRuta(int tipoVehiculo,Punto posicion_actual,Punto punto_inicial,int tiempo,	FuncionProbabilidadAbstracta probabilidadAngulo,
			FuncionProbabilidadAbstracta probabilidadVelocidad,ProbabilidadNaufragoCasillas probabilidad);
}
