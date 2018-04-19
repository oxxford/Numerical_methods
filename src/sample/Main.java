package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Standard method to run JavaFX app
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Differential equations: assignment");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        Controller controller = loader.getController();
        controller.setScene(scene); // or what you want to do
    }

    public static void main(String[] args) {
        launch(args);
    }
}
