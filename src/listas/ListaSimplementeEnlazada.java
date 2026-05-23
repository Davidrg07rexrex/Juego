package listas;

public class ListaSimplementeEnlazada<T> implements Listas<T> {
    private ElemSE<T> primero;
    private int tamaño;

    public ListaSimplementeEnlazada() {
        this.primero = null;
        this.tamaño = 0;
    }

    public void add(T dato) {
        ElemSE<T> nuevo = new ElemSE<>(dato);
        if (primero == null) {
            primero = nuevo;
        } else {
            ElemSE<T> actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamaño++;
    }

    public T get(T dato) {
        ElemSE<T> actual = primero;
        while (actual != null) {
            if (actual.dato.equals(dato)) {   // ahora usa equals, más genérico
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public T del(T dato) {
        if (primero == null) return null;
        if (primero.dato.equals(dato)) {
            T eliminado = primero.dato;
            primero = primero.siguiente;
            tamaño--;
            return eliminado;
        }
        ElemSE<T> actual = primero;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(dato)) {
                T eliminado = actual.siguiente.dato;
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return eliminado;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    public int getTamaño() {
        return tamaño;
    }

    public void addFirst(T dato) {
        ElemSE<T> nuevo = new ElemSE<>(dato);
        nuevo.siguiente = primero;
        primero = nuevo;
        tamaño++;
    }

    public int numApariciones(T dato) {
        int contador = 0;
        ElemSE<T> actual = primero;
        while (actual != null) {
            if (actual.dato.equals(dato)) {
                contador++;
            }
            actual = actual.siguiente;
        }
        return contador;
    }

    public boolean contains(T dato) {
        return get(dato) != null;
    }

    public T getDatoEn(int pos) {
        if (pos < 0 || pos >= tamaño) return null;
        ElemSE<T> actual = primero;
        for (int i = 0; i < pos; i++) {
            actual = actual.siguiente;
        }
        return actual.dato;
    }

    public void clear() {
        primero = null;
        tamaño = 0;
    }

    public void imprimir() {
        ElemSE<T> actual = primero;
        while (actual != null) {
            System.out.print(actual.dato + " ");
            actual = actual.siguiente;
        }
        System.out.println();
    }

    // NUEVO MÉTODO: eliminar por igualdad (necesario para la cola)
    public boolean remove(T dato) {
        if (primero == null) return false;

        if (primero.dato.equals(dato)) {
            primero = primero.siguiente;
            tamaño--;
            return true;
        }

        ElemSE<T> actual = primero;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(dato)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
}