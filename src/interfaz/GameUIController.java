package interfaz;

import javafx.scene.control.Button;

import logica.JuegoReal;
import modelo.*;
import mundo.HabitacionMock;
import mundo.Enemigo;

public class GameUIController {

    private GameView view;

    private JuegoReal juego;

    private String modoActual = "NINGUNO";

    public GameUIController() {

        Jugador jugador = new Jugador(
                "Jugador",
                100,
                10,
                5,
                new Posicion(1,1)
        );

        HabitacionModelo habitacion = new HabitacionMock(
                "Sala inicial",
                4,
                4
        );

        juego = new JuegoReal(jugador, habitacion);

        Enemigo enemigo = new Enemigo(
                "E1",
                "Goblin",
                30,
                5,
                2,
                1
        );

        enemigo.setPosicion(new Posicion(0, 2));

        ((HabitacionMock) habitacion).colocarEnemigo(enemigo);
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

        for (int fila = 0; fila < juego.getCurrentRoomRows(); fila++) {

            for (int columna = 0; columna < juego.getCurrentRoomCols(); columna++) {

                String contenido = juego.getCellSymbol(fila, columna);

                Button celda = new Button(contenido);
                celda.setMinSize(60, 60);

                if (contenido.equals("J") || contenido.equals("P")) {
                    celda.setStyle("-fx-background-color: lightblue;");
                } else if (contenido.equals("E")) {
                    celda.setStyle("-fx-background-color: lightcoral;");
                } else if (contenido.equals("O")) {
                    celda.setStyle("-fx-background-color: khaki;");
                } else if (contenido.equals("D")) {
                    celda.setStyle("-fx-background-color: lightgreen;");
                } else if (contenido.equals("#")) {
                    celda.setStyle("-fx-background-color: gray;");
                } else {
                    celda.setStyle("-fx-background-color: white;");
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

            moverJugadorHasta(fila, columna);

            refrescarVista();

        } else if (modoActual.equals("ATACAR")) {

            boolean ok = juego.attack(new Posicion(fila, columna));

            if (ok) {
                view.escribirEvento("Ataque realizado en (" + fila + "," + columna + ")");
            } else {
                view.escribirEvento("No se puede atacar en esa posición.");
            }

            refrescarVista();

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

        int filaActual = actual.getFila();
        int columnaActual = actual.getColumna();

        boolean esAdyacente =
                (fila == filaActual - 1 && columna == columnaActual) ||
                        (fila == filaActual + 1 && columna == columnaActual) ||
                        (fila == filaActual && columna == columnaActual - 1) ||
                        (fila == filaActual && columna == columnaActual + 1);

        if (esAdyacente) {
            juego.getPlayer().setPosicion(new Posicion(fila, columna));
            view.escribirEvento("Jugador movido a (" + fila + "," + columna + ")");
            refrescarVista();
        } else {
            view.escribirEvento("Movimiento inválido: solo puedes moverte a una celda adyacente.");
        }
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
}