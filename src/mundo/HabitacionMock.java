package mundo;

import modelo.*;
import listas.*;

public class HabitacionMock implements HabitacionModelo {
    private String id;
    private String nombre;
    private int filas;
    private int columnas;
    private ListaSimplementeEnlazada<Enemigo> enemigos;
    private Objeto[][] matrizObjetos;
    private Puerta[][] matrizPuertas;
    private Trampa[][] matrizTrampas;

    public HabitacionMock(String id, String nombre, int filas, int columnas) {
        this.id = id;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.enemigos = new ListaSimplementeEnlazada<>();
        this.matrizObjetos = new Objeto[filas][columnas];
        this.matrizPuertas = new Puerta[filas][columnas];
        this.matrizTrampas = new Trampa[filas][columnas];
    }

    @Override
    public String getId() { return id; }

    @Override
    public int getFilas() { return filas; }

    @Override
    public int getColumnas() { return columnas; }

    @Override
    public boolean esTransitable(int fila, int columna) {
        if (!dentro(fila, columna)) return false;
        if (getEnemigoEn(fila, columna) != null) return false;
        if (matrizTrampas[fila][columna] != null) return false;
        return true;
    }

    @Override
    public String getSimbolo(int fila, int columna) {
        if (!dentro(fila, columna)) return "?";
        if (getEnemigoEn(fila, columna) != null) return "E";
        if (matrizObjetos[fila][columna] != null) return "O";
        if (matrizPuertas[fila][columna] != null) return "P";
        if (matrizTrampas[fila][columna] != null) return "T";
        return "·";
    }

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
        if (dentro(fila, columna)) matrizObjetos[fila][columna] = null;
    }

    @Override
    public void moverEnemigo(Entidad enemigo, int nuevaFila, int nuevaColumna) {
        // No es necesario para los tests
    }

    @Override
    public Objeto getObjetoEn(int fila, int columna) {
        if (!dentro(fila, columna)) return null;
        return matrizObjetos[fila][columna];
    }

    @Override
    public String getDestinoPuerta(int fila, int columna) {
        Puerta p = matrizPuertas[fila][columna];
        return (p != null) ? p.getIdDestino() : null;
    }

    @Override
    public boolean puertaNecesitaLlave(int fila, int columna) {
        Puerta p = matrizPuertas[fila][columna];
        return (p != null && p.necesitaLlave());
    }

    @Override
    public String getIdLlavePuerta(int fila, int columna) {
        Puerta p = matrizPuertas[fila][columna];
        return (p != null) ? p.getIdLlaveRequerida() : null;
    }

    @Override
    public boolean esTrampa(int fila, int columna) {
        return matrizTrampas[fila][columna] != null;
    }

    @Override
    public int getDanioTrampa(int fila, int columna) {
        Trampa t = matrizTrampas[fila][columna];
        return (t != null) ? t.getDanio() : 0;
    }

    @Override
    public int compareTo(HabitacionModelo o) {
        return this.id.compareTo(o.getId());
    }

    // Métodos propios del mock

    public void colocarEnemigo(Enemigo e) {
        enemigos.add(e);
    }

    public void colocarObjeto(int fila, int columna, Objeto obj) {
        if (dentro(fila, columna)) matrizObjetos[fila][columna] = obj;
    }

    public void colocarPuerta(int fila, int columna, Puerta puerta) {
        if (dentro(fila, columna)) matrizPuertas[fila][columna] = puerta;
    }

    public void colocarTrampa(int fila, int columna, Trampa trampa) {
        if (dentro(fila, columna)) matrizTrampas[fila][columna] = trampa;
    }

    public ListaSimplementeEnlazada<Enemigo> getEnemigos() {
        return enemigos;
    }

    // Método auxiliar privado
    private boolean dentro(int f, int c) {
        return f >= 0 && f < filas && c >= 0 && c < columnas;
    }
    public void eliminarTrampa(int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            matrizTrampas[fila][columna] = null;
        }
    }
}
