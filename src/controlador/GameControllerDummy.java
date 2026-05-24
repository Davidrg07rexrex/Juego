package controlador;

import modelo.Direction;
import modelo.HabitacionModelo;
import modelo.Jugador;
import modelo.Posicion;
import mundo.Habitacion;
import mundo.Objeto;
import listas.ListaSimplementeEnlazada;

public class GameControllerDummy implements GameControllerModel {

    private ListaSimplementeEnlazada<String> log;
    private ListaSimplementeEnlazada<Objeto> inventario;
    private int filas = 5;
    private int columnas = 5;
    private String[][] mapa;
    private int jugadorFila = 2;
    private int jugadorCol = 2;
    private HabitacionModelo habitacion;

    public GameControllerDummy() {
        log = new ListaSimplementeEnlazada<>();
        inventario = new ListaSimplementeEnlazada<>();
        habitacion = new Habitacion("Sala de pruebas",filas,columnas);

        log.add("Juego iniciado (modo dummy)");

        // Añadir objetos de prueba al inventario
        inventario.add(new Objeto("poc1", "Poción", "pocion"));
        inventario.add(new Objeto("esp1", "Espada", "arma"));

        inicializarMapa();
    }

    private void inicializarMapa() {
        mapa = new String[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                mapa[i][j] = "·";
            }
        }
        mapa[jugadorFila][jugadorCol] = "J";
        mapa[2][3] = "E";
        mapa[1][1] = "O";
        mapa[3][3] = "P";
    }

    @Override
    public HabitacionModelo getCurrentRoom() {
        return habitacion;
    }

    @Override
    public Jugador getPlayer() {
        return new Jugador("Héroe", 100, 10, 5, new Posicion(jugadorFila, jugadorCol));
    }

    @Override
    public ListaSimplementeEnlazada<Objeto> getInventory() {
        return inventario;
    }

    @Override
    public ListaSimplementeEnlazada<String> getEventLog() {
        return log;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public boolean movePlayer(Direction dir) {
        log.add("Mover hacia " + dir);
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if ("J".equals(mapa[i][j])) {
                    mapa[i][j] = "·";
                    int nuevaFila = i, nuevaCol = j;
                    switch (dir) {
                        case UP:    nuevaFila = i - 1; break;
                        case DOWN:  nuevaFila = i + 1; break;
                        case LEFT:  nuevaCol = j - 1; break;
                        case RIGHT: nuevaCol = j + 1; break;
                        default: return false;
                    }
                    if (nuevaFila >= 0 && nuevaFila < filas && nuevaCol >= 0 && nuevaCol < columnas) {
                        mapa[nuevaFila][nuevaCol] = "J";
                        jugadorFila = nuevaFila;
                        jugadorCol = nuevaCol;
                        return true;
                    } else {
                        mapa[i][j] = "J";
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean attack(Posicion pos) {
        int fila = pos.getFila();
        int col = pos.getColumna();

        if (fila < 0 || fila >= filas || col < 0 || col >= columnas) {
            return false;
        }

        log.add("Atacar en " + pos);

        if (mapa[fila][col].equals("E")) {
            mapa[fila][col] = "·";
            log.add("Enemigo eliminado");
            return true;
        }

        return false;
    }

    @Override
    public boolean pickItem(Posicion pos) {
        log.add("Recoger objeto en " + pos);
        if (mapa[pos.getFila()][pos.getColumna()].equals("O")) {
            mapa[pos.getFila()][pos.getColumna()] = "·";
            log.add("Objeto recogido");
            // Añadir a inventario (simulado)
            inventario.add(new Objeto("recogido1", "Objeto recogido", "misc"));
            return true;
        }
        return false;
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

    public boolean attackNearbyEnemy() {
        int fila = jugadorFila;
        int col = jugadorCol;

        if (attack(new Posicion(fila - 1, col))) return true; // arriba
        if (attack(new Posicion(fila + 1, col))) return true; // abajo
        if (attack(new Posicion(fila, col - 1))) return true; // izquierda
        if (attack(new Posicion(fila, col + 1))) return true; // derecha

        log.add("No hay enemigo cerca");
        return false;
    }
}