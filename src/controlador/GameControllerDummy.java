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
        habitacion = new Habitacion("sala_pruebas", "Sala de pruebas", filas, columnas);

        log.add("Juego iniciado (modo dummy)");

        // Añadir objetos de prueba al inventario
        inventario.add(new ObjetoDummy("poc1", "Poción", "pocion"));
        inventario.add(new ObjetoDummy("esp1", "Espada", "arma"));

        inicializarMapa();
    }

    // Clase auxiliar para crear objetos concretos en el Dummy (Objeto es abstracta)
    private static class ObjetoDummy extends Objeto {
        public ObjetoDummy(String id, String nombre, String tipo) {
            super(id, nombre, tipo);
        }

        @Override
        public String getDescripcion() {
            return nombre;
        }

        @Override
        public String toString() {
            return getDescripcion();
        }
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
        // Ahora Jugador requiere velocidad (suponemos 3 para el dummy)
        return new Jugador("Héroe", 100, 10, 5, 3, new Posicion(jugadorFila, jugadorCol));
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
        int nuevaFila = jugadorFila, nuevaCol = jugadorCol;
        switch (dir) {
            case UP:    nuevaFila--; break;
            case DOWN:  nuevaFila++; break;
            case LEFT:  nuevaCol--; break;
            case RIGHT: nuevaCol++; break;
            default: return false;
        }
        if (nuevaFila >= 0 && nuevaFila < filas && nuevaCol >= 0 && nuevaCol < columnas) {
            // Simular movimiento (ignoramos enemigos para pruebas)
            mapa[jugadorFila][jugadorCol] = "·";
            mapa[nuevaFila][nuevaCol] = "J";
            jugadorFila = nuevaFila;
            jugadorCol = nuevaCol;
            return true;
        }
        return false;
    }

    @Override
    public boolean movePlayer(Posicion destino) {
        int f = destino.getFila();
        int c = destino.getColumna();
        if (f < 0 || f >= filas || c < 0 || c >= columnas) return false;
        // Permitir solo celdas adyacentes (simula rango de movimiento 1)
        if (Math.abs(f - jugadorFila) + Math.abs(c - jugadorCol) != 1) return false;
        mapa[jugadorFila][jugadorCol] = "·";
        mapa[f][c] = "J";
        jugadorFila = f;
        jugadorCol = c;
        log.add("Movido a " + destino);
        return true;
    }

    @Override
    public boolean[][] getCeldasAlcanzables() {
        boolean[][] alcanzables = new boolean[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int dist = Math.abs(i - jugadorFila) + Math.abs(j - jugadorCol);
                alcanzables[i][j] = (dist <= 1); // Solo una casilla de alcance en el dummy
            }
        }
        return alcanzables;
    }

    @Override
    public boolean attack(Direction dir) {
        int fila = jugadorFila, col = jugadorCol;
        switch (dir) {
            case UP:    fila--; break;
            case DOWN:  fila++; break;
            case LEFT:  col--; break;
            case RIGHT: col++; break;
            default: return false;
        }
        return attackAt(new Posicion(fila, col));
    }

    private boolean attackAt(Posicion pos) {
        int fila = pos.getFila();
        int col = pos.getColumna();
        if (fila < 0 || fila >= filas || col < 0 || col >= columnas) return false;
        log.add("Atacar en " + pos);
        if (mapa[fila][col].equals("E")) {
            mapa[fila][col] = "·";
            log.add("Enemigo eliminado");
            return true;
        }
        return false;
    }

    @Override
    public boolean attackNearbyEnemy() {
        if (attack(Direction.UP)) return true;
        if (attack(Direction.DOWN)) return true;
        if (attack(Direction.LEFT)) return true;
        if (attack(Direction.RIGHT)) return true;
        log.add("No hay enemigo cerca");
        return false;
    }

    @Override
    public boolean pickItem(Posicion pos) {
        log.add("Recoger objeto en " + pos);
        if (mapa[pos.getFila()][pos.getColumna()].equals("O")) {
            mapa[pos.getFila()][pos.getColumna()] = "·";
            log.add("Objeto recogido");
            inventario.add(new ObjetoDummy("recogido1", "Objeto recogido", "misc"));
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

    @Override
    public boolean esTurnoJugador() {
        return true;
    }

    @Override
    public void finalizarTurnoJugador() {
        log.add("Turno finalizado (dummy)");
    }

    @Override
    public boolean isVictoria() {
        return false;
    }
}