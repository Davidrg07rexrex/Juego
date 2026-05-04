package listas;

public class NodoArbol<T extends Comparable<T>> implements Comparable<NodoArbol<T>>{
    T dato; //El dato que almacena el nodo
    ListaSimplementeEnlazada<NodoArbol<T>> hijos; //Lista de hijos

    public NodoArbol(T dato){
        this.dato=dato;
        this.hijos=new ListaSimplementeEnlazada<>();
    }

    @Override
    public int compareTo(NodoArbol<T> otro){
        //Objetivo:Comparar dos nodos por su dato
        return this.dato.compareTo(otro.dato);
    }
}
