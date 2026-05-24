package interfaz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        GameUIController controller = new GameUIController();

        GameView view = new GameView(controller);

        controller.setView(view);

        Scene scene = new Scene(view.getRoot(), 1000, 700);

        stage.setTitle("Juego");

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}