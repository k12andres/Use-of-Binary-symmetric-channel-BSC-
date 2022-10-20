//import java.math.*;
import java.util.*;

//https://drive.google.com/file/d/1Q9FwRhjPCIvPnprFFxjxvn1Hs072QS1G/view 
//minuto 38 para hacer el a)
public class BSC{
	private static ArrayList<Integer> ENTRADA = new ArrayList<Integer>();
	
	private final double PROBABILIDAD0 = 0.33;
	private final double PROBABILIDAD1 = 0.66;
	
	private final int TAMANIOTRANSMISION = 3;
	private final int CANTREPETICIONES = 8;
	
	private final double EPSILON = 0.0001;

	public BSC(){}

	private void generarTransmision(double p0, double p1, double beta){
        int valor;
		for (int i=0; i<TAMANIOTRANSMISION; i++){
			double probError = (double) Math.random();
			double prob = (double) Math.random();
        	if (prob <= p0){
				valor = 0;
			} else 
        		valor = 1;
			if (probError < beta){
				if (valor == 1){
					valor = 0;
				} else {
					valor = 1;
				}
			}
			ENTRADA.add(valor);
		}
    }

	private int verificarSimboloCoincidente(){
		for (int cursor = 0; cursor<ENTRADA.size(); cursor++){
			if (ENTRADA.get(cursor)==1){
				return 1;
			} 
		}
		return 0;
	}

	private double tomarSimboloCoincidente(double beta){
		int repeticiones = 0;
		int cantidadDeCeros = 0;
		int cantidadDeUnos = 0;
		int valor;
		double probabilidadDeError = 0;

		while  (repeticiones<CANTREPETICIONES){
			generarTransmision(PROBABILIDAD0, PROBABILIDAD1, beta);
			valor = verificarSimboloCoincidente();
			//System.out.println(valor);
			
			if (valor != 0){
				for (int cursor = 0; cursor<ENTRADA.size(); cursor++){
					if (ENTRADA.get(cursor)==0){
						cantidadDeCeros++;
					} else {
						cantidadDeUnos++;
					}
				}
				probabilidadDeError = probabilidadDeError + Math.pow((1-beta), cantidadDeUnos) * Math.pow(beta, cantidadDeCeros);
			}
			
			repeticiones++;
			ENTRADA.clear();
			cantidadDeCeros = 0;
			cantidadDeUnos = 0;
		}
		return probabilidadDeError;
	}
	
	private int masRepetido(){
		int numRepetido = 0;
		for (int cursor = 0; cursor<ENTRADA.size(); cursor++){
			if (ENTRADA.get(cursor)==0){
				numRepetido++;
			}
		}
		if (numRepetido <= (ENTRADA.size()/2)){
			return 0;
		} else return 1;
	}

	private double tomarMasRepetidos(double beta){
		int repeticiones = 0;
		int cantidadDeCeros = 0;
		int cantidadDeUnos = 0;
		int valor;
		double probabilidadDeError = 0;

		while  (repeticiones<CANTREPETICIONES){
			generarTransmision(PROBABILIDAD0, PROBABILIDAD1, beta);
			valor = masRepetido();
			//System.out.println(valor);
			
			if (valor != 0){
				for (int cursor = 0; cursor<ENTRADA.size(); cursor++){
					if (ENTRADA.get(cursor)==0){
						cantidadDeCeros++;
					} else {
						cantidadDeUnos++;
					}
				}
				probabilidadDeError = probabilidadDeError + Math.pow((1-beta), cantidadDeCeros) * Math.pow(beta, cantidadDeUnos);
			}
			
			repeticiones++;
			ENTRADA.clear();
			cantidadDeCeros = 0;
			cantidadDeUnos = 0;
		}
		return probabilidadDeError;
	}
	
	public double convergenciaTomarMasRepetidos(double beta){
		double valorActual = 0;
		double valorAnterior = -1;//Para que no converja de entrada
		int repeticiones = 0;
		valorActual = tomarMasRepetidos(beta);
		double valorAcumulado = 0;
		double valor = 0;
		
		while (!converge (valorActual, valorAnterior) || (repeticiones<10000)){
			valor = tomarMasRepetidos(beta);
			valorAcumulado = valorAcumulado + valor;
			valorAnterior = valorActual;
			valorActual = valorAcumulado / repeticiones;
			repeticiones++;
		}
		return valorActual;
	}


	public double convergenciaTomarSimboloCoincidente(double beta){
		double valorActual = 0;
		double valorAnterior = -1;//Para que no converja de entrada
		int repeticiones = 0;
		valorActual = tomarSimboloCoincidente(beta);
		double valorAcumulado = 0;
		double valor = 0;
		
		while (!converge (valorActual, valorAnterior) || (repeticiones<10000)){
			valor = tomarSimboloCoincidente(beta);
			valorAcumulado = valorAcumulado + valor;
			valorAnterior = valorActual;
			valorActual = valorAcumulado / repeticiones;
			repeticiones++;
		}
		return valorActual;
	}
	
	private boolean converge(double probactual, double probanterior){
		if (Math.abs(probactual-probanterior)>this.EPSILON){
			return false;
		}
		else return true;
	}
	
	/*
	Inciso a realizado computacionalmente a modo practica.

	private final int REPETICIONES = 1000;
	public double infoMutua(int p){
		double infoMutuaAct = 0;
		double infoMutuaAnt = -1;//Para que no converja de entrada
		int[][] sumaXY = new int[FILA][COLUMNA];
		int sumaX[] = new int[FILA];
		int sumaY[] = new int[FILA];
		int repeticiones=0;

		while (!converge (infoMutuaAct, infoMutuaAnt) || (repeticiones<REPETICIONES)){
			int x = generarEntrada(PROBABILIDAD0, PROBABILIDAD1);
			int y = pasarPorCanal(x);
			//System.out.println(y);
			sumaXY[x][y]++;//sumo la conjunta
			sumaX[x]++;
			sumaY[y]++;
			repeticiones++;
			infoMutuaAnt = infoMutuaAct;
			infoMutuaAct = calcularMatrizInfoMutua(sumaXY, sumaX, sumaY, repeticiones, x);
		}
		return infoMutuaAct;
	}
	
	private double calcularMatrizInfoMutua(int sumaXY[][], int sumaX[],int sumaY[], int repeticiones, int columna){
		double entropiaY = 0.0;
		double entropiaXY = 0.0;
		
		for (int recorredor = 0; recorredor<sumaY.length; recorredor++){
			if (sumaY[recorredor] != 0){ //Para evitar hacer log sobre 0.
				entropiaY = entropiaY + (sumaY[recorredor] * (Math.log(sumaY[recorredor]) / Math.log(2)));
			}
		}

		for (int recorredor=0; recorredor<COLUMNA; recorredor++){
			if (sumaXY[recorredor][columna] != 0){
				double valor = (double) sumaXY[recorredor][columna] / sumaX[recorredor];
				entropiaXY = entropiaXY + (valor * (Math.log(valor) / Math.log(2)));
			}
		}
		
		//System.out.println(entropiaY);
		return entropiaY - entropiaXY;
	}
	
	
	
	private int pasarPorCanal(int entrada){// genero una salida a partir del x generado y de un random.
		double prob = (double) Math.random();
		if (entrada == 0){
			if (prob <= 1-Beta1)
				return 0;
			else return 1;
		} else {
			if (prob <= Beta2)
				return 0;
			else return 1;
		}
	}*/
	
}