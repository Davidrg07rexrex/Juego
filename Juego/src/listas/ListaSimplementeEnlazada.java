package listas;

public class ListaSimplementeEnlazada<T extends Comparable<T>> implements Listas<T> {
    private ElemSE<T> primero;
    private int tamaño;

    public ListaSimplementeEnlazada(){
        this.primero=null;
        this.tamaño=0;
    }

    public void add(T dato){
        //Objetivo:Añadir un elemento al final de la lista
        ElemSE<T> nuevo=new ElemSE<>(dato);
        if(primero==null){
            primero=nuevo;
        } else {
            ElemSE<T> actual=primero;
            while(actual.siguiente!=null){
                actual=actual.siguiente;
            }
            actual.siguiente=nuevo;
        }
        tamaño++;
    }

    public T get(T dato){
        //Objetivo:Buscar y devolver un elemento por su dato
        ElemSE<T> actual=primero;
        while(actual!=null){
            if(actual.dato.compareTo(dato)==0){
                return actual.dato;
            }
            actual=actual.siguiente;
        }
        return null;
    }

    public T del(T dato){
        //Objetivo:Eliminar un elemento de la lista
        if(primero==null) return null;
        if(primero.dato.compareTo(dato)==0){
            T eliminado=primero.dato;
            primero=primero.siguiente;
            tamaño--;
            return eliminado;
        }
        ElemSE<T> actual=primero;
        while(actual.siguiente!=null){
            if(actual.siguiente.dato.compareTo(dato)==0){
                T eliminado=actual.siguiente.dato;
                actual.siguiente=actual.siguiente.siguiente;
                tamaño--;
                return eliminado;
            }
            actual=actual.siguiente;
        }
        return null;
    }

    public boolean estaVacia(){
        //Objetivo:Comprobar si la lista está vacía
        return tamaño==0;
    }

    public int getTamaño(){
        //Objetivo:Devolver el número de elementos
        return tamaño;
    }

    public void addFirst(T dato){
        //Objetivo:Añadir un elemento al inicio de la lista
        ElemSE<T> nuevo=new ElemSE<>(dato);
        nuevo.siguiente=primero;
        primero=nuevo;
        tamaño++;
    }

    public int numApariciones(T dato){
        //Objetivo:Contar cuántas veces aparece un dato
        int contador=0;
        ElemSE<T> actual=primero;
        while(actual!=null){
            if(actual.dato.compareTo(dato)==0){
                contador++;
            }
            actual=actual.siguiente;
        }
        return contador;
    }

    public boolean contains(T dato){
        //Objetivo:Comprobar si la lista contiene un dato
        return get(dato)!=null;
    }

    public T getDatoEn(int pos){
        //Objetivo:Obtener el dato en una posición específica
        if(pos<0 || pos>=tamaño) return null;
        ElemSE<T> actual=primero;
        for(int i=0; i<pos; i++){
            actual=actual.siguiente;
        }
        return actual.dato;
    }

    public void clear(){
        //Objetivo:Vaciar la lista
        primero=null;
        tamaño=0;
    }

    public void imprimir(){
        //Objetivo:Mostrar todos los elementos de la lista
        ElemSE<T> actual=primero;
        while(actual!=null){
            System.out.print(actual.dato + " ");
            actual=actual.siguiente;
        }
        System.out.println();
    }
}
