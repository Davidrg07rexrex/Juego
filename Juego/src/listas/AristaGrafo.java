package listas;

public class AristaGrafo<T extends Comparable<T>> implements Comparable<AristaGrafo<T>>{
    NodoGrafo<T> destino; //Nodo al que apunta esta arista
    double peso; //Peso de la arista (opcional, para grafos ponderados)

    public AristaGrafo(NodoGrafo<T> destino){
        this.destino=destino;
        this.peso=1.0; //Por defecto peso 1
    }

    public AristaGrafo(NodoGrafo<T> destino, double peso){
        this.destino=destino;
        this.peso=peso;
    }

    @Override
    public int compareTo(AristaGrafo<T> otra){
        //Objetivo:Comparar dos aristas por su peso
        return Double.compare(this.peso, otra.peso);
    }
}
