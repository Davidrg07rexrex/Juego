package logica;

import listas.Cola;
import modelo.Posicion;
import modelo.Direction;
import modelo.HabitacionMovimiento;

public class Movimiento {

    /**
     * Devuelve una matriz booleana con las celdas alcanzables desde (filaIni, colIni)
     * con un máximo de 'pasos' movimientos.
     */
    public static boolean[][] celdasAlcanzables(HabitacionMovimiento hab, int filaIni, int colIni, int pasos) {
        int filas = hab.getFilas();
        int cols = hab.getColumnas();
        boolean[][] visitado = new boolean[filas][cols];
        int[][] distancia = new int[filas][cols];

        Cola<Posicion> cola = new Cola<>();
        Posicion inicio = new Posicion(filaIni, colIni);
        cola.encolar(inicio);
        visitado[filaIni][colIni] = true;
        distancia[filaIni][colIni] = 0;

        while (!cola.estaVacia()) {
            Posicion actual = cola.desencolar();
            int fila = actual.getFila();
            int col = actual.getColumna();
            int dist = distancia[fila][col];

            if (dist >= pasos) continue;

            for (Direction dir : Direction.values()) {
                Posicion vecina = actual.mover(dir);
                int fv = vecina.getFila();
                int cv = vecina.getColumna();
                if (fv >= 0 && fv < filas && cv >= 0 && cv < cols
                        && hab.esTransitable(fv, cv) && !visitado[fv][cv]) {
                    visitado[fv][cv] = true;
                    distancia[fv][cv] = dist + 1;
                    cola.encolar(vecina);
                }
            }
        }
        return visitado;
    }

    /**
     * Comprueba si una posición concreta está en la matriz de alcanzables.
     */
    public static boolean esAlcanzable(boolean[][] alcanzables, int fila, int col) {
        return fila >= 0 && fila < alcanzables.length
                && col >= 0 && col < alcanzables[0].length
                && alcanzables[fila][col];
    }
}