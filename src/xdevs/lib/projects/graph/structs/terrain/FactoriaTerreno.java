package xdevs.lib.projects.graph.structs.terrain;

public class FactoriaTerreno {
	public static Terreno crearTerrenoMar(int lon, int anc, int numIteraciones, int profundidadMaxima, int profundidadMinima, float filtro) {
		return new TerrenoMar(lon,anc,numIteraciones,profundidadMaxima,profundidadMinima,filtro);
	}
	
	public static Terreno crearTerrenoMontaña(int lon, int anc, int numIteraciones, int altMax, int altMin, float filtro) {
		return new TerrenoMontaña(lon,anc,numIteraciones,altMax,altMin,filtro);
	}
	
	public static Terreno crearTerrenoCosta(int lon, int anc, int numIteraciones, int alturaMax, int profundidadMax, float filtro, float pCosta) {
		return new TerrenoCosta(lon,anc,numIteraciones,alturaMax,profundidadMax,filtro,pCosta);
	}
}
