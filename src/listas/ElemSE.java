package listas;

public class ElemSE<T> {
    T dato;
    ElemSE<T> siguiente;

    public ElemSE(T dato){
        this.dato=dato;
        this.siguiente=null;
    }
}
