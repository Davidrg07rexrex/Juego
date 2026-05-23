package modelo;

public class Posicion {
    private final int fila;
    private final int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    /**
     * Devuelve una nueva posición movida en la dirección indicada.
     */
    public Posicion mover(Direction dir) {
        switch (dir) {
            case UP:    return new Posicion(fila - 1, columna);
            case DOWN:     return new Posicion(fila + 1, columna);
            case LEFT: return new Posicion(fila, columna - 1);
            case RIGHT:   return new Posicion(fila, columna + 1);
            default: throw new IllegalArgumentException("Dirección desconocida: " + dir);
        }
    }

    // equals y hashCode (sin usar java.util.Objects)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicion pos = (Posicion) o;
        return fila == pos.fila && columna == pos.columna;
    }

    @Override
    public int hashCode() {
        return 31 * fila + columna;
    }

    @Override
    public String toString() {
        return "(" + fila + ", " + columna + ")";
    }
}