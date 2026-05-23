package mundo;

import modelo.Entidad;
import modelo.HabitacionModelo;

public class Habitacion implements HabitacionModelo {
    private String nombre;
    private int filas;
    private int columnas;

    // Constructor con dimensiones variables
    public Habitacion(String nombre, int filas, int columnas) {
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
    }

    @Override
    public int getFilas() {
        return filas;
    }

    @Override
    public int getColumnas() {
        return columnas;
    }

    @Override
    public boolean esTransitable(int fila, int columna) {
        // Provisional: solo comprueba límites, sin obstáculos
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    @Override
    public String getSimbolo(int fila, int columna) {
        // Provisional: devuelve siempre "·" (vacío)
        return "·";
    }

    // Getter para el nombre, si lo necesitas
    public String getNombre() {
        return nombre;
    }
    @Override
    public Entidad getEnemigoEn(int fila, int columna) {
        // Provisional: no hay enemigos aún, devolvemos null
        return null;
    }
    @Override
    public void eliminarEnemigo(int fila, int columna) {
        // provisional: nada que hacer hasta que haya matriz real
    }

    @Override
    public void eliminarObjeto(int fila, int columna) {
        // provisional
    }
    @Override
    public void moverEnemigo(Entidad enemigo, int nuevaFila, int nuevaColumna) {
        // Provisional: no hay matriz real aún
    }
}