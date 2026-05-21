package controlador;

import modelo.Direction;
import modelo.Jugador;
import mundo.Habitacion;
import mundo.Objeto;
import mundo.Posicion;
import java.util.List;

public interface GameControllerModel {

    // Información general
    Habitacion getCurrentRoom();
    Jugador getPlayer();
    List<Objeto> getInventory();          // o MyList si prefieres
    List<String> getEventLog();
    boolean isGameOver();

    // Acciones del jugador
    boolean movePlayer(Direction dir);     // ahora usa enum
    boolean attack(Posicion pos);
    boolean pickItem(Posicion pos);
    boolean useItem(Objeto item);

    // Persistencia
    void saveGame(String file);
    void loadGame(String file);

    // Métodos específicos para la vista (matriz de la habitación)
    int getCurrentRoomRows();
    int getCurrentRoomCols();
    String getCellSymbol(int row, int col);  // devuelve "J", "E", "O", "P", "·", etc.
}