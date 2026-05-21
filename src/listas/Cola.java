package listas;

public class Cola<T extends Comparable<T>> {
    private ListaSimplementeEnlazada<T> lista;

    public Cola(){
        this.lista=new ListaSimplementeEnlazada<>();
    }

    public void encolar(T dato){
        //Objetivo:Añadir un elemento al final de la cola
        lista.add(dato);
    }

    public T desencolar(){
        //Objetivo:Eliminar y devolver el elemento del frente de la cola
        if(lista.estaVacia()) return null;
        return lista.del(lista.getDatoEn(0));
    }

    public T frente(){
        //Objetivo:Ver el elemento del frente sin eliminarlo
        if(lista.estaVacia()) return null;
        return lista.getDatoEn(0);
    }

    public boolean estaVacia(){
        //Objetivo:Comprobar si la cola está vacía
        return lista.estaVacia();
    }

    public int getTamaño(){
        //Objetivo:Devolver el número de elementos
        return lista.getTamaño();
    }

    public void clear(){
        //Objetivo:Vaciar la cola
        lista.clear();
    }

    public void imprimir(){
        //Objetivo:Mostrar todos los elementos de la cola
        lista.imprimir();
    }
}
