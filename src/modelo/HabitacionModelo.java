package modelo;

import mundo.Objeto;

public interface HabitacionModelo {
    int getFilas();
    int getColumnas();
    boolean esTransitable(int fila, int columna);
    String getSimbolo(int fila, int columna);
    Entidad getEnemigoEn(int fila, int columna);
    void eliminarEnemigo(int fila, int columna);
    void eliminarObjeto(int fila, int columna);   // para recoger objetos más tarde// ← nuevo
    void moverEnemigo(Entidad enemigo, int nuevaFila, int nuevaColumna);
    Objeto getObjetoEn(int fila, int columna);
}