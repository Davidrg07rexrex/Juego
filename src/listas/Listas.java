package listas;

public interface Listas<T> {
    void add(T dato);
    T get(T dato);
    T del(T dato);
    boolean estaVacia();
    int getTamaño();
    void addFirst(T dato);
    int numApariciones(T dato);
    boolean contains(T dato);
    T getDatoEn(int pos);
    void clear();
    void imprimir();
}
