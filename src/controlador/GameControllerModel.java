package controlador;

import modelo.Direction;
import modelo.Jugador;
import modelo.Posicion;                       // ← desde modelo
import mundo.Habitacion;
import mundo.Objeto;
import listas.ListaSimplementeEnlazada;      // tu estructura propia

public interface GameControllerModel {
    Habitacion getCurrentRoom();
    Jugador getPlayer();
    ListaSimplementeEnlazada<Objeto> getInventory();
    ListaSimplementeEnlazada<String> getEventLog();
    boolean isGameOver();

    boolean movePlayer(Direction dir);
    boolean attack(Posicion pos);
    boolean pickItem(Posicion pos);
    boolean useItem(Objeto item);

    void saveGame(String file);
    void loadGame(String file);

    int getCurrentRoomRows();
    int getCurrentRoomCols();
    String getCellSymbol(int row, int col);
}