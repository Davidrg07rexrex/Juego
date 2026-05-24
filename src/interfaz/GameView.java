package interfaz;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class GameView {

    private BorderPane root;

    private GridPane gridHabitacion;

    private VBox panelDerecho;

    private TextArea registroEventos;

    private Label vidaLabel;

    private Label ataqueLabel;

    private Label defensaLabel;

    private Label turnosLabel;

    private ListView<String> inventarioList;

    private Button moverButton;

    private Button atacarButton;

    private Button recogerButton;

    private Button usarButton;

    private Button guardarButton;

    private Button cargarButton;

    private GameUIController controller;

    public GameView(GameUIController controller) {

        this.controller = controller;

        crearInterfaz();
    }

    private void crearInterfaz() {

        root = new BorderPane();

        gridHabitacion = new GridPane();

        gridHabitacion.setHgap(5);

        gridHabitacion.setVgap(5);

        panelDerecho = new VBox(10);

        vidaLabel = new Label("Vida: ");

        ataqueLabel = new Label("Ataque: ");

        defensaLabel = new Label("Defensa: ");

        turnosLabel = new Label("Turnos: ");

        inventarioList = new ListView<>();

        inventarioList.setPrefHeight(120);

        inventarioList.getItems().addAll(
                "Poción",
                "Llave",
                "Espada"
        );

        moverButton = new Button("Mover");

        atacarButton = new Button("Atacar");

        recogerButton = new Button("Recoger");

        usarButton = new Button("Usar");

        guardarButton = new Button("Guardar");

        cargarButton = new Button("Cargar");

        moverButton.setOnAction(e -> controller.activarModoMover());

        atacarButton.setOnAction(e -> controller.activarModoAtacar());

        recogerButton.setOnAction(e -> controller.activarModoRecoger());

        usarButton.setOnAction(e -> controller.usarObjetoSeleccionado());

        guardarButton.setOnAction(e -> controller.guardarPartida());

        cargarButton.setOnAction(e -> controller.cargarPartida());

        panelDerecho.getChildren().addAll(

                new Label("Estado del jugador"),

                vidaLabel,

                ataqueLabel,

                defensaLabel,

                turnosLabel,

                new Separator(),

                new Label("Inventario"),

                inventarioList,

                new Separator(),

                new Label("Acciones"),

                moverButton,

                atacarButton,

                recogerButton,

                usarButton,

                guardarButton,

                cargarButton
        );

        registroEventos = new TextArea();

        registroEventos.setEditable(false);

        registroEventos.setPrefHeight(150);

        root.setCenter(gridHabitacion);

        root.setRight(panelDerecho);

        root.setBottom(registroEventos);
    }

    public Parent getRoot() {
        return root;
    }

    public GridPane getGridHabitacion() {
        return gridHabitacion;
    }

    public void setVida(String vida) {
        vidaLabel.setText("Vida: " + vida);
    }

    public void setAtaque(String ataque) {
        ataqueLabel.setText("Ataque: " + ataque);
    }

    public void setDefensa(String defensa) {
        defensaLabel.setText("Defensa: " + defensa);
    }

    public void setTurnos(String turnos) {
        turnosLabel.setText("Turnos: " + turnos);
    }

    public void escribirEvento(String texto) {
        registroEventos.appendText(texto + "\n");
    }
}
