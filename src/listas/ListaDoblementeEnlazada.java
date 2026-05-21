package listas;

public class ListaDoblementeEnlazada<T extends Comparable<T>> implements Listas<T> {
    private ElemDE<T> primero;
    private ElemDE<T> ultimo;
    private int tamaño;

    public ListaDoblementeEnlazada(){
        this.primero=null;
        this.ultimo=null;
        this.tamaño=0;
    }

    public void add(T dato){
        //Objetivo:Añadir un elemento al final de la lista
        ElemDE<T> nuevo=new ElemDE<>(dato);
        if(primero==null){
            primero=nuevo;
            ultimo=nuevo;
        } else {
            nuevo.anterior=ultimo;
            ultimo.siguiente=nuevo;
            ultimo=nuevo;
        }
        tamaño++;
    }

    public T get(T dato){
        //Objetivo:Buscar y devolver un elemento por su dato
        ElemDE<T> actual=primero;
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
        ElemDE<T> actual=primero;
        while(actual!=null){
            if(actual.dato.compareTo(dato)==0){
                if(actual==primero){
                    primero=primero.siguiente;
                    if(primero!=null) primero.anterior=null;
                } else if(actual==ultimo){
                    ultimo=ultimo.anterior;
                    ultimo.siguiente=null;
                } else {
                    actual.anterior.siguiente=actual.siguiente;
                    actual.siguiente.anterior=actual.anterior;
                }
                tamaño--;
                return actual.dato;
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
        ElemDE<T> nuevo=new ElemDE<>(dato);
        if(primero==null){
            primero=nuevo;
            ultimo=nuevo;
        } else {
            nuevo.siguiente=primero;
            primero.anterior=nuevo;
            primero=nuevo;
        }
        tamaño++;
    }

    public int numApariciones(T dato){
        //Objetivo:Contar cuántas veces aparece un dato
        int contador=0;
        ElemDE<T> actual=primero;
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
        ElemDE<T> actual=primero;
        for(int i=0; i<pos; i++){
            actual=actual.siguiente;
        }
        return actual.dato;
    }

    public void clear(){
        //Objetivo:Vaciar la lista
        primero=null;
        ultimo=null;
        tamaño=0;
    }

    public void imprimir(){
        //Objetivo:Mostrar todos los elementos de la lista
        ElemDE<T> actual=primero;
        while(actual!=null){
            System.out.print(actual.dato + " ");
            actual=actual.siguiente;
        }
        System.out.println();
    }
}
