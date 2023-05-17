package xdevs.lib.projects.barcos;

import java.util.ArrayList;

public class Prueba {

	public static void main(String args[]){
		Genetic a=new Genetic(4,7);
		ArrayList<Integer> uno=new ArrayList<Integer>();
		uno.add(1);
		ArrayList<ArrayList<Integer>> conj=new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> aux= new ArrayList<Integer>();
		aux.add(1);
		aux.add(2);
		aux.add(3);
		a.randomTrayectoriesAlt(aux);
		//a.randomTrayectories(aux);
		//a.vaTray(uno, conj, 1);
		/*System.out.println(conj.size());
		for(int i=0;i<conj.get(0).size();i++){
			System.out.println("A: "+conj.get(0).get(i));
			System.out.println("B: "+conj.get(1).get(i));
			System.out.println("C: "+conj.get(2).get(i));
			System.out.println("D: "+conj.get(3).get(i));
		}
		*//*
		System.out.println(a.trayectorias.size());
		System.out.println(a.trayectorias.get(0).size());
		System.out.println(a.trayectorias.get(1).size());
		System.out.println(a.trayectorias.get(2).size());
		System.out.println(a.trayectorias.get(3).size());
		System.out.println(a.trayectorias.get(4).size());
		for(int i=0;i<a.trayectorias.get(0).size();i++){
			System.out.println("A: "+a.trayectorias.get(0).get(i));
			System.out.println("B: "+a.trayectorias.get(1).get(i));
			System.out.println("C: "+a.trayectorias.get(2).get(i));
			System.out.println("D: "+a.trayectorias.get(3).get(i));
		}
	*/
		System.out.print("hola");
		System.out.println(a.trayectorias.size());
		System.out.println(a.trayectorias.get(0).size());
		System.out.println(a.trayectorias.get(1).size());
		System.out.println(a.trayectorias.get(2).size());
		System.out.println(a.trayectorias.get(3).size());
		System.out.println(a.trayectorias.get(4).size());
		for(int i=0;i<a.trayectorias.get(0).size();i++){
			System.out.println("A: "+a.trayectorias.get(0).get(i));
			System.out.println("B: "+a.trayectorias.get(1).get(i));
			System.out.println("C: "+a.trayectorias.get(2).get(i));
			System.out.println("D: "+a.trayectorias.get(3).get(i));
		}
	}	
}
