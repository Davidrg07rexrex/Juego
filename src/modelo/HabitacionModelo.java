package modelo;

import mundo.Objeto;

public interface HabitacionModelo extends Comparable<HabitacionModelo> {
    String getId();
    int getFilas();
    int getColumnas();
    boolean esTransitable(int fila, int columna);
    String getSimbolo(int fila, int columna);
    Entidad getEnemigoEn(int fila, int columna);
    void eliminarEnemigo(int fila, int columna);
    void eliminarObjeto(int fila, int columna);
    void moverEnemigo(Entidad enemigo, int nuevaFila, int nuevaColumna);
    Objeto getObjetoEn(int fila, int columna);
    String getDestinoPuerta(int fila, int columna);
    boolean puertaNecesitaLlave(int fila, int columna);
    String getIdLlavePuerta(int fila, int columna);
    boolean esTrampa(int fila, int columna);
    int getDanioTrampa(int fila, int columna);
}