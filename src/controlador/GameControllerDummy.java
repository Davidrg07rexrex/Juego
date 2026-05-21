package controlador;

import modelo.Direction;
import modelo.Jugador;
import mundo.Habitacion;
import mundo.Objeto;
import mundo.Posicion;
import java.util.ArrayList;
import java.util.List;

public class GameControllerDummy implements GameControllerModel {
    private List<String> log;
    private int filas = 5;
    private int columnas = 5;
    private String[][] mapa;

    public GameControllerDummy() {
        log = new ArrayList<>();
        log.add("Juego iniciado (modo dummy)");
        inicializarMapa();
    }

    private void inicializarMapa() {
        mapa = new String[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                mapa[i][j] = "·";   // vacío
            }
        }
        // Colocar jugador (J), enemigo (E), objeto (O), puerta (P)
        mapa[2][2] = "J";
        mapa[2][3] = "E";
        mapa[1][1] = "O";
        mapa[3][3] = "P";
    }

    @Override
    public Habitacion getCurrentRoom() {
        return new Habitacion("Sala de pruebas");
    }

    @Override
    public Jugador getPlayer() {
        return new Jugador("Héroe", 100, 10, 5, 3);
    }

    @Override
    public List<Objeto> getInventory() {
        List<Objeto> inv = new ArrayList<>();
        inv.add(new Objeto("Poción"));
        inv.add(new Objeto("Espada"));
        return inv;
    }

    @Override
    public List<String> getEventLog() {
        return log;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public boolean movePlayer(Direction dir) {
        log.add("Mover hacia " + dir);
        // Aquí dummy: simplemente actualiza la matriz para que J se mueva un paso (opcional)
        // Para simular, movemos la J en la matriz (búsqueda simple)
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (mapa[i][j].equals("J")) {
                    mapa[i][j] = "·";  // borrar posición actual
                    switch (dir) {
                        case UP:    if (i-1 >= 0) mapa[i-1][j] = "J"; break;
                        case DOWN:  if (i+1 < filas) mapa[i+1][j] = "J"; break;
                        case LEFT:  if (j-1 >= 0) mapa[i][j-1] = "J"; break;
                        case RIGHT: if (j+1 < columnas) mapa[i][j+1] = "J"; break;
                    }
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public boolean attack(Posicion pos) {
        log.add("Atacar en " + pos);
        // dummy: si en esa posición hay 'E', lo elimina
        if (mapa[pos.getFila()][pos.getColumna()].equals("E")) {
            mapa[pos.getFila()][pos.getColumna()] = "·";
            log.add("Enemigo eliminado");
        }
        return true;
    }

    @Override
    public boolean pickItem(Posicion pos) {
        log.add("Recoger objeto en " + pos);
        if (mapa[pos.getFila()][pos.getColumna()].equals("O")) {
            mapa[pos.getFila()][pos.getColumna()] = "·";
            log.add("Objeto recogido");
        }
        return true;
    }

    @Override
    public boolean useItem(Objeto item) {
        log.add("Usar objeto: " + item.getNombre());
        return true;
    }

    @Override
    public void saveGame(String file) {
        log.add("Guardar partida en " + file);
    }

    @Override
    public void loadGame(String file) {
        log.add("Cargar partida desde " + file);
    }

    @Override
    public int getCurrentRoomRows() {
        return filas;
    }

    @Override
    public int getCurrentRoomCols() {
        return columnas;
    }

    @Override
    public String getCellSymbol(int row, int col) {
        if (row >= 0 && row < filas && col >= 0 && col < columnas) {
            return mapa[row][col];
        }
        return "?";
    }
}