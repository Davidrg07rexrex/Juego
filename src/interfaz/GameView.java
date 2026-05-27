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

    private Button pasarTurnoButton;

    private GameUIController controller;

    public GameView(GameUIController controller) {

        this.controller = controller;

        crearInterfaz();
    }

    private void crearInterfaz() {

        root = new BorderPane();
        root.setPrefSize(1200, 800);

        Label titulo = new Label("Juego de Mazmorras - MVC");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        root.setTop(titulo);

        gridHabitacion = new GridPane();

        gridHabitacion.setHgap(5);

        gridHabitacion.setVgap(5);

        panelDerecho = new VBox(8);
        panelDerecho.setPrefWidth(250);

        vidaLabel = new Label("Vida: ");

        ataqueLabel = new Label("Ataque: ");

        defensaLabel = new Label("Defensa: ");

        turnosLabel = new Label("Turnos: ");

        inventarioList = new ListView<>();

        inventarioList.setPrefHeight(160);
        inventarioList.setMinHeight(160);
        inventarioList.setMaxHeight(160);

        moverButton = new Button("Mover");

        atacarButton = new Button("Atacar");

        recogerButton = new Button("Recoger");

        usarButton = new Button("Usar");

        guardarButton = new Button("Guardar");

        cargarButton = new Button("Cargar");

        pasarTurnoButton = new Button("Pasar turno");

        moverButton.setOnAction(e -> controller.activarModoMover());

        atacarButton.setOnAction(e -> controller.activarModoAtacar());

        recogerButton.setOnAction(e -> controller.activarModoRecoger());

        usarButton.setOnAction(e -> controller.usarObjetoSeleccionado());

        guardarButton.setOnAction(e -> controller.guardarPartida());

        cargarButton.setOnAction(e -> controller.cargarPartida());

        pasarTurnoButton.setOnAction(e -> controller.finalizarTurno());

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

                cargarButton,

                pasarTurnoButton
        );

        registroEventos = new TextArea();

        registroEventos.setStyle("-fx-font-size: 16px;");

        registroEventos.setEditable(false);

        registroEventos.setPrefHeight(180);
        registroEventos.setMinHeight(180);
        registroEventos.setMaxHeight(180);
        registroEventos.setScrollTop(Double.MAX_VALUE);

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
        registroEventos.setScrollTop(Double.MAX_VALUE);
    }

    public ListView<String> getInventarioList() {
        return inventarioList;
    }

    public TextArea getRegistroEventos() {
        return registroEventos;
    }
}
