package controlador;

import mundo.Jugador;
import mundo.Habitacion;
import mundo.Objeto;
import mundo.Posicion;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

public class GameControllerDummy implements GameControllerModel {
    private List<String> log = new ArrayList<>();

    public GameControllerDummy() {
        log.add("Juego iniciado (modo dummy)");
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
    public boolean movePlayer(String direccion) {
        log.add("Se mueve hacia " + direccion);
        return true;
    }

    @Override
    public boolean attack(Posicion pos) {
        log.add("Ataca a enemigo en " + pos);
        return true;
    }

    @Override
    public boolean pickItem(Posicion pos) {
        return false;
    }


    public boolean pickItem(Position pos) {
        log.add("Recoge objeto en " + pos);
        return true;
    }

    @Override
    public boolean useItem(Objeto item) {
        log.add("Usa objeto: " + item.getNombre());
        return true;
    }

    @Override
    public void saveGame(String file) {
        log.add("Guardando partida en " + file);
    }

    @Override
    public void loadGame(String file) {
        log.add("Cargando partida desde " + file);
    }

    @Override
    public List<String> getEventLog() {
        return log;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }
}