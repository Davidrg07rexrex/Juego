package logica;

import listas.Cola;
import modelo.HabitacionModelo;
import modelo.Posicion;

public class Movimiento {
    public static boolean[][] celdasAlcanzables(HabitacionModelo hab, int filaIni, int colIni, int pasos) {
        int filas = hab.getFilas();
        int cols = hab.getColumnas();
        boolean[][] visitado = new boolean[filas][cols];
        int[][] dist = new int[filas][cols];

        Cola<Posicion> cola = new Cola<>();
        Posicion inicio = new Posicion(filaIni, colIni);
        cola.encolar(inicio);
        visitado[filaIni][colIni] = true;

        while (!cola.estaVacia()) {
            Posicion actual = cola.desencolar();
            int f = actual.getFila();
            int c = actual.getColumna();
            int d = dist[f][c];
            if (d >= pasos) continue;

            for (int df = -1; df <= 1; df++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (Math.abs(df) + Math.abs(dc) != 1) continue; // solo ortogonal
                    int nf = f + df;
                    int nc = c + dc;
                    if (nf >= 0 && nf < filas && nc >= 0 && nc < cols
                            && hab.esTransitable(nf, nc) && !visitado[nf][nc]) {
                        visitado[nf][nc] = true;
                        dist[nf][nc] = d + 1;
                        cola.encolar(new Posicion(nf, nc));
                    }
                }
            }
        }
        return visitado;
    }
}