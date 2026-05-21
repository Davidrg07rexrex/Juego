package controlador;

import mundo.Jugador;
import mundo.Habitacion;
import mundo.Objeto;
import mundo.Posicion;
import java.util.List;

public interface GameControllerModel {
    Habitacion getCurrentRoom();
    Jugador getPlayer();
    List<Objeto> getInventory();   // o usa tu MyList si prefieres
    boolean movePlayer(String direccion); // "ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA"
    boolean attack(Posicion pos);
    boolean pickItem(Posicion pos);
    boolean useItem(Objeto item);
    void saveGame(String file);
    void loadGame(String file);
    List<String> getEventLog();
    boolean isGameOver();
    int getCurrentRoomRows();
    int getCurrentRoomCols();
    String getCellType(int row, int col);   // devuelve "J", "E", "O", "P", "V" (vacío), etc.
}