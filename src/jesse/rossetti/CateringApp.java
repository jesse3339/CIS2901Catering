package jesse.rossetti;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jesse.rossetti.SQL.util.GUIUtil;

public class CateringApp extends Application {

    public static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CIS2901.fxml"));
        Parent root = loader.load();

        controller = loader.getController();

        GUIUtil.formatTextBoxes();


        primaryStage.setTitle("Catering App");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
