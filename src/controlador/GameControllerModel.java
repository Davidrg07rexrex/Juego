package controlador;

import modelo.Direction;
import modelo.Jugador;
import modelo.HabitacionModelo;
import modelo.Posicion;
import mundo.Objeto;
import listas.ListaSimplementeEnlazada;

public interface GameControllerModel {
    HabitacionModelo getCurrentRoom();
    Jugador getPlayer();
    ListaSimplementeEnlazada<Objeto> getInventory();
    ListaSimplementeEnlazada<String> getEventLog();
    boolean isGameOver();

    boolean movePlayer(Direction dir);
    boolean attack(Direction dir);          // Ataque en una dirección
    boolean attackNearbyEnemy();            // Ataque al primer enemigo adyacente
    boolean pickItem(Posicion pos);
    boolean useItem(Objeto item);

    void saveGame(String file);
    void loadGame(String file);

    int getCurrentRoomRows();
    int getCurrentRoomCols();
    String getCellSymbol(int row, int col);
}