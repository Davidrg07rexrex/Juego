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
    private Objeto[][] matrizObjetos;

    public HabitacionMock(String nombre, int filas, int columnas) {
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.enemigos = new ListaSimplementeEnlazada<>();
        this.matrizObjetos = new Objeto[filas][columnas];
    }

    @Override
    public String getId() { return nombre; }

    @Override
    public int compareTo(HabitacionModelo o) {
        return this.nombre.compareTo(o.getId());
    }

    public void colocarEnemigo(Enemigo e) { enemigos.add(e); }
    public void colocarObjeto(Objeto obj, int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            matrizObjetos[fila][columna] = obj;
        }
    }

    public ListaSimplementeEnlazada<Enemigo> getEnemigos() { return enemigos; }

    @Override
    public int getFilas() { return filas; }
    @Override
    public int getColumnas() { return columnas; }

    @Override
    public boolean esTransitable(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    @Override
    public String getSimbolo(int fila, int columna) { return "·"; }

    @Override
    public Entidad getEnemigoEn(int fila, int columna) {
        for (int i = 0; i < enemigos.getTamaño(); i++) {
            Enemigo e = enemigos.getDatoEn(i);
            Posicion p = e.getPosicion();
            if (p.getFila() == fila && p.getColumna() == columna && e.estaVivo()) return e;
        }
        return null;
    }

    @Override
    public void eliminarEnemigo(int fila, int columna) {
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
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            matrizObjetos[fila][columna] = null;
        }
    }

    @Override
    public void moverEnemigo(Entidad enemigo, int nuevaFila, int nuevaColumna) {
        // ya se actualiza en IAEnemigo, no es necesario modificar matriz
    }

    @Override
    public Objeto getObjetoEn(int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            return matrizObjetos[fila][columna];
        }
        return null;
    }

    @Override
    public String getDestinoPuerta(int fila, int columna) { return null; }

    @Override
    public boolean puertaNecesitaLlave(int fila, int columna) { return false; }

    @Override
    public String getIdLlavePuerta(int fila, int columna) { return null; }

    @Override
    public boolean esTrampa(int fila, int columna) { return false; }

    @Override
    public int getDanioTrampa(int fila, int columna) { return 0; }
}