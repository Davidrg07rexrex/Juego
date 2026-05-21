package listas;

public class Pila<T extends Comparable<T>> {
    private ListaSimplementeEnlazada<T> lista;

    public Pila(){
        this.lista=new ListaSimplementeEnlazada<>();
    }

    public void apilar(T dato){
        //Objetivo:Añadir un elemento a la cima de la pila
        lista.addFirst(dato);
    }

    public T desapilar(){
        //Objetivo:Eliminar y devolver el elemento de la cima
        if(lista.estaVacia()) return null;
        return lista.del(lista.getDatoEn(0));
    }

    public T cima(){
        //Objetivo:Ver el elemento de la cima sin eliminarlo
        if(lista.estaVacia()) return null;
        return lista.getDatoEn(0);
    }

    public boolean estaVacia(){
        //Objetivo:Comprobar si la pila está vacía
        return lista.estaVacia();
    }

    public int getTamaño(){
        //Objetivo:Devolver el número de elementos
        return lista.getTamaño();
    }

    public void clear(){
        //Objetivo:Vaciar la pila
        lista.clear();
    }

    public void imprimir(){
        //Objetivo:Mostrar todos los elementos de la pila
        lista.imprimir();
    }
}
