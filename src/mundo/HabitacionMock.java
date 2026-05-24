package mundo;

import modelo.Entidad;
import modelo.HabitacionModelo;
import modelo.Posicion;
import listas.ListaSimplementeEnlazada;

public class HabitacionMock implements HabitacionModelo {
    private String nombre;
    private int filas;
    private int columnas;
    private ListaSimplementeEnlazada<Enemigo> enemigos;

    public HabitacionMock(String nombre, int filas, int columnas) {
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.enemigos = new ListaSimplementeEnlazada<>();
    }

    public void colocarEnemigo(Enemigo e) {
        enemigos.add(e);
    }

    public ListaSimplementeEnlazada<Enemigo> getEnemigos() {
        return enemigos;
    }

    @Override
    public int getFilas() { return filas; }

    @Override
    public int getColumnas() { return columnas; }

    @Override
    public boolean esTransitable(int fila, int columna) {
        // Simplificación: permitir todo excepto fuera de límites
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    @Override
    public String getSimbolo(int fila, int columna) {

        for (int i = 0; i < enemigos.getTamaño(); i++) {
            Enemigo e = enemigos.getDatoEn(i);
            Posicion p = e.getPosicion();

            if (p.getFila() == fila && p.getColumna() == columna && e.estaVivo()) {
                return "E";
            }
        }

        return "·";
    }

    @Override
    public Entidad getEnemigoEn(int fila, int columna) {
        // Buscar enemigo en esa posición exacta
        for (int i = 0; i < enemigos.getTamaño(); i++) {
            Enemigo e = enemigos.getDatoEn(i);
            Posicion p = e.getPosicion();
            if (p.getFila() == fila && p.getColumna() == columna && e.estaVivo()) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void eliminarEnemigo(int fila, int columna) {
        // Quitar enemigo muerto de la lista
        for (int i = 0; i < enemigos.getTamaño(); i++) {
            Enemigo e = enemigos.getDatoEn(i);
            Posicion p = e.getPosicion();
            if (p.getFila() == fila && p.getColumna() == columna) {
                enemigos.remove(e);
                return;
            }
        }
    }

    @Override
    public void eliminarObjeto(int fila, int columna) {
        // sin implementar en mock
    }

    @Override
    public void moverEnemigo(Entidad enemigo, int nuevaFila, int nuevaColumna) {
        // Actualizar la posición del enemigo (ya se hace en IAEnemigo, pero aquí reflejamos en la matriz interna si la hubiera)
        // En este mock no hay matriz, solo actualizamos la posición mediante setPosicion ya hecho en IAEnemigo.
    }
}