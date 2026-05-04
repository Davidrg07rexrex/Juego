package listas;

public class NodoGrafo<T extends Comparable<T>> implements Comparable<NodoGrafo<T>>{
    T dato; //El dato que almacena el nodo
    ListaSimplementeEnlazada<AristaGrafo<T>> aristas; //Lista de aristas que salen de este nodo

    public NodoGrafo(T dato){
        this.dato=dato;
        this.aristas=new ListaSimplementeEnlazada<>();
    }

    @Override
    public int compareTo(NodoGrafo<T> otro){
        //Objetivo:Comparar dos nodos por su dato
        return this.dato.compareTo(otro.dato);
    }
}
