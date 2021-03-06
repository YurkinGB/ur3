package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        Parent root = fxmlLoader.load(getClass().getResource("/sample.fxml"));
//
//        Controller controller = fxmlLoader.getController();
//        primaryStage.setOnHidden(e -> controller.onStageClose());
//
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 600, 446));
//        primaryStage.show();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        primaryStage.setOnHidden(e -> controller.onStageClose());
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 446));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
