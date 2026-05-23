package listas;

public class Cola<T> {
    private ListaSimplementeEnlazada<T> lista;

    public Cola() {
        this.lista = new ListaSimplementeEnlazada<>();
    }

    public void encolar(T dato) {
        lista.add(dato);
    }

    public T desencolar() {
        if (lista.estaVacia()) return null;
        T dato = lista.getDatoEn(0);
        lista.remove(dato);   // usa remove con equals
        return dato;
    }

    public T frente() {
        if (lista.estaVacia()) return null;
        return lista.getDatoEn(0);
    }

    public boolean estaVacia() {
        return lista.estaVacia();
    }

    public int getTamaño() {
        return lista.getTamaño();
    }

    public void clear() {
        lista.clear();
    }

    public void imprimir() {
        lista.imprimir();
    }

    // NUEVO: eliminar un elemento concreto
    public void eliminar(T dato) {
        lista.remove(dato);
    }
}
