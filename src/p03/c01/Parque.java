package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{


	static int aforoMax;
	static int aforoMin;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque() {	// TODO
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		aforoMax = 20;
		aforoMin = 0;
	}


	@Override
	public synchronized void entrarAlParque(String puerta){
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		comprobarAntesDeEntrar();		
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		// Comprobamos personas y puertos
		checkInvariante();
		
		//Notificamos al resto de los hilos nuestra entrada
		notifyAll();
	}
	
	 
	@Override
	public synchronized void salirDelParque(String puerta) {
		// Si no hay salida por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		comprobarAntesDeSalir();
		
		// Disminuimos el contador total y el individual
		contadorPersonasTotales--;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
				
		// Comprobamos personas y puertos
		checkInvariante();
				
		//Notificamos al resto de los hilos nuestra salida
		notifyAll();
				
		
	}
	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert aforoMin <= sumarContadoresPuerta() :  "INV: La suma de contadores de las puertas debe ser mayor o igual al aforo minimo del parque";
		assert sumarContadoresPuerta() <= aforoMax :  "INV: La suma de contadores de las puertas debe ser menor o igual al aforo maximo del parque";
		
		
		
	}

	protected void comprobarAntesDeEntrar(){	
		while (sumarContadoresPuerta() == aforoMax) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		
		
	}

	protected void comprobarAntesDeSalir(){		
		while (sumarContadoresPuerta() == aforoMin) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}


}
