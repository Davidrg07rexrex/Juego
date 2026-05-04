package listas;

public class ElemDE<T> {
    T dato;
    ElemDE<T> siguiente;
    ElemDE<T> anterior;

    public ElemDE(T dato){
        this.dato=dato;
    }
}
