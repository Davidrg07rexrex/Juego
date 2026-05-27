package interfaz;

import javafx.scene.control.Button;
import logica.JuegoReal;
import logica.TurnoManager;
import modelo.*;
import mundo.HabitacionMock;
import mundo.Enemigo;
import listas.*;
import logica.InicializadorJuego;
import modelo.Posicion;

public class GameUIController {

    private GameView view;
    private JuegoReal juego;
    private String modoActual = "NINGUNO";

    public GameUIController() {
        juego = InicializadorJuego.cargarDesdeJSON("src/partida1.json");

        if (juego == null) {
            throw new RuntimeException("No se pudo cargar la partida desde JSON.");
        }
    }

    public void setView(GameView view) {
        this.view = view;
        inicializarVista();
    }

    private void inicializarVista() {
        pintarHabitacionDePrueba();
        view.setVida("100");
        view.setAtaque("10");
        view.setDefensa("5");
        view.setTurnos("20");
        view.escribirEvento("Partida iniciada.");
    }

    private void pintarHabitacionDePrueba() {
        view.getGridHabitacion().getChildren().clear();

        boolean[][] alcanzables = juego.getCeldasAlcanzables();

        for (int fila = 0; fila < juego.getCurrentRoomRows(); fila++) {
            for (int columna = 0; columna < juego.getCurrentRoomCols(); columna++) {
                String contenido = juego.getCellSymbol(fila, columna);
                Button celda = new Button(contenido);
                celda.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                celda.setMinSize(60, 60);

                // Estilos según contenido
                switch (contenido) {
                    case "J":
                        celda.setText("J");
                        celda.setStyle("-fx-background-color: lightblue; -fx-font-size: 18px; -fx-font-weight: bold;");
                        break;

                    case "P":
                        celda.setText("P");
                        celda.setStyle("-fx-background-color: lightblue; -fx-font-size: 18px; -fx-font-weight: bold;");
                        break;

                    case "E":
                        celda.setText("E");
                        celda.setStyle("-fx-background-color: lightcoral; -fx-font-size: 18px; -fx-font-weight: bold;");
                        break;

                    case "O":
                        celda.setText("O");
                        celda.setStyle("-fx-background-color: khaki; -fx-font-size: 18px; -fx-font-weight: bold;");
                        break;

                    case "D":
                        celda.setStyle("-fx-background-color: lightgreen;");
                        break;
                    case "#":
                        celda.setStyle("-fx-background-color: gray;");
                        break;
                    default:
                        celda.setText("");
                        celda.setStyle("-fx-background-color: white;");
                }

                if (alcanzables != null && alcanzables[fila][columna]) {
                    celda.setStyle("-fx-background-color: lightgreen; -fx-border-color: black;");
                } else {
                    celda.setStyle(celda.getStyle() + "-fx-border-color: black;");
                }

                final int f = fila;
                final int c = columna;
                celda.setOnAction(e -> clickEnCelda(f, c));

                view.getGridHabitacion().add(celda, columna, fila);
            }
        }
    }

    private void clickEnCelda(int fila, int columna) {

        if (modoActual.equals("MOVER")) {

            boolean ok = juego.movePlayer(new Posicion(fila, columna));

            if (ok) {
                view.escribirEvento("Jugador movido a (" + fila + "," + columna + ")");
            } else {
                view.escribirEvento("Movimiento inválido.");
            }

            refrescarVista();

        } else if (modoActual.equals("ATACAR")) {

            atacarHacia(fila, columna);

        } else if (modoActual.equals("RECOGER")) {

            boolean ok = juego.pickItem(new Posicion(fila, columna));

            if (ok) {
                view.escribirEvento("Objeto recogido en (" + fila + "," + columna + ")");
            } else {
                view.escribirEvento("No hay objeto para recoger ahí.");
            }

            refrescarVista();

        } else {

            view.escribirEvento("Click en (" + fila + "," + columna + ")");
        }

        modoActual = "NINGUNO";
    }

    private void moverJugadorHasta(int fila, int columna) {
        Posicion actual = juego.getPlayer().getPosicion();
        int fa = actual.getFila();
        int ca = actual.getColumna();

        Direction dir = null;
        if (fila == fa - 1 && columna == ca) dir = Direction.UP;
        else if (fila == fa + 1 && columna == ca) dir = Direction.DOWN;
        else if (fila == fa && columna == ca - 1) dir = Direction.LEFT;
        else if (fila == fa && columna == ca + 1) dir = Direction.RIGHT;

        if (dir != null) {
            boolean ok = juego.movePlayer(dir);
            if (ok) {
                view.escribirEvento("Jugador movido hacia " + dir);
            } else {
                view.escribirEvento("Movimiento bloqueado.");
            }
            refrescarVista();
        } else {
            view.escribirEvento("Solo puedes moverte a una celda adyacente.");
        }
    }

    private void atacarHacia(int fila, int columna) {
        Posicion actual = juego.getPlayer().getPosicion();
        int fa = actual.getFila();
        int ca = actual.getColumna();

        Direction dir = null;
        if (fila == fa - 1 && columna == ca) dir = Direction.UP;
        else if (fila == fa + 1 && columna == ca) dir = Direction.DOWN;
        else if (fila == fa && columna == ca - 1) dir = Direction.LEFT;
        else if (fila == fa && columna == ca + 1) dir = Direction.RIGHT;

        if (dir != null) {
            boolean ok = juego.attack(dir);

            view.escribirEvento(ok
                    ? "Ataque realizado hacia " + dir
                    : "No hay enemigo en esa dirección.");
        } else {
            view.escribirEvento("Solo puedes atacar a una celda adyacente.");
        }
        refrescarVista();
    }

    private void refrescarVista() {
        pintarHabitacionDePrueba();
        view.setVida(String.valueOf(juego.getPlayer().getVida()));
        view.setAtaque(String.valueOf(juego.getPlayer().getAtaque()));
        view.setDefensa(String.valueOf(juego.getPlayer().getDefensa()));

        // Refrescar inventario
        view.getInventarioList().getItems().clear();
        for (int i = 0; i < juego.getInventory().getTamaño(); i++) {
            view.getInventarioList().getItems().add(
                    juego.getInventory().getDatoEn(i).getNombre()
            );
        }
    }

    public void activarModoMover() {
        modoActual = "MOVER";
        view.escribirEvento("Modo mover activado.");
    }

    public void activarModoAtacar() {
        modoActual = "ATACAR";
        view.escribirEvento("Modo atacar activado.");
    }

    public void activarModoRecoger() {
        modoActual = "RECOGER";
        view.escribirEvento("Modo recoger activado.");
    }

    public void guardarPartida() {
        juego.saveGame("partida.json");
        view.escribirEvento("Partida guardada en partida.json");
        refrescarVista();
    }

    public void usarObjetoSeleccionado() {
        view.escribirEvento("Selecciona un objeto del inventario para usarlo.");
    }

    public void cargarPartida() {
        juego.loadGame("partida.json");
        view.escribirEvento("Partida cargada desde partida.json");
        refrescarVista();
    }

    public void finalizarTurno() {
        juego.finalizarTurnoJugador();
        view.escribirEvento("Turno finalizado.");
        refrescarVista();
    }
}