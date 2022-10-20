class Main {	
	private static final double Beta1 = 0.05;
	private static final double Beta2 = 0.5;
	
  	public static void main(String[] args) {
		BSC binario = new BSC();
		
		//Estrategia 1
		double resultadob1 = binario.convergenciaTomarMasRepetidos(Beta1);
		double resultadob2 = binario.convergenciaTomarMasRepetidos(Beta2);
		System.out.println("El resultado para beta 1 tomando el simbolo mas repetido = " + resultadob1);
		System.out.println("Y para beta 2 es = " + resultadob2);

		//Estrategia 2
		resultadob1 = binario.convergenciaTomarSimboloCoincidente(Beta1);
		resultadob2 = binario.convergenciaTomarSimboloCoincidente(Beta2);
		
		System.out.println("\nEl resultado para beta 1 tomando el simbolo inicial igual al ultimo = " + resultadob1);
		System.out.println("Y para beta 2 es = " + resultadob2);
  	}
}