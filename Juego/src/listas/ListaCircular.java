package listas;

public class ListaCircular<T extends Comparable<T>> implements Listas<T> {
    private ElemSE<T> ultimo;
    private int tamaño;

    public ListaCircular(){
        this.ultimo=null;
        this.tamaño=0;
    }

    public void add(T dato){
        //Objetivo:Añadir un elemento al final de la lista circular
        ElemSE<T> nuevo=new ElemSE<>(dato);
        if(ultimo==null){
            ultimo=nuevo;
            nuevo.siguiente=nuevo;
        } else {
            nuevo.siguiente=ultimo.siguiente;
            ultimo.siguiente=nuevo;
            ultimo=nuevo;
        }
        tamaño++;
    }

    public T get(T dato){
        //Objetivo:Buscar y devolver un elemento por su dato
        if(ultimo==null) return null;
        ElemSE<T> actual=ultimo.siguiente;
        do {
            if(actual.dato.compareTo(dato)==0){
                return actual.dato;
            }
            actual=actual.siguiente;
        } while(actual!=ultimo.siguiente);
        return null;
    }

    public T del(T dato){
        //Objetivo:Eliminar un elemento de la lista circular
        if(ultimo==null) return null;
        ElemSE<T> actual=ultimo.siguiente;
        ElemSE<T> anterior=ultimo;
        do {
            if(actual.dato.compareTo(dato)==0){
                if(actual==ultimo && tamaño==1){
                    ultimo=null;
                } else if(actual==ultimo){
                    anterior.siguiente=actual.siguiente;
                    ultimo=anterior;
                } else {
                    anterior.siguiente=actual.siguiente;
                }
                tamaño--;
                return actual.dato;
            }
            anterior=actual;
            actual=actual.siguiente;
        } while(actual!=ultimo.siguiente);
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
        //Objetivo:Añadir un elemento al inicio de la lista circular
        ElemSE<T> nuevo=new ElemSE<>(dato);
        if(ultimo==null){
            ultimo=nuevo;
            nuevo.siguiente=nuevo;
        } else {
            nuevo.siguiente=ultimo.siguiente;
            ultimo.siguiente=nuevo;
        }
        tamaño++;
    }

    public int numApariciones(T dato){
        //Objetivo:Contar cuántas veces aparece un dato
        if(ultimo==null) return 0;
        int contador=0;
        ElemSE<T> actual=ultimo.siguiente;
        do {
            if(actual.dato.compareTo(dato)==0){
                contador++;
            }
            actual=actual.siguiente;
        } while(actual!=ultimo.siguiente);
        return contador;
    }

    public boolean contains(T dato){
        //Objetivo:Comprobar si la lista contiene un dato
        return get(dato)!=null;
    }

    public T getDatoEn(int pos){
        //Objetivo:Obtener el dato en una posición específica
        if(pos<0 || pos>=tamaño) return null;
        ElemSE<T> actual=ultimo.siguiente;
        for(int i=0; i<pos; i++){
            actual=actual.siguiente;
        }
        return actual.dato;
    }

    public void clear(){
        //Objetivo:Vaciar la lista circular
        ultimo=null;
        tamaño=0;
    }

    public void imprimir(){
        //Objetivo:Mostrar todos los elementos de la lista circular
        if(ultimo==null) return;
        ElemSE<T> actual=ultimo.siguiente;
        do {
            System.out.print(actual.dato + " ");
            actual=actual.siguiente;
        } while(actual!=ultimo.siguiente);
        System.out.println();
    }
}
