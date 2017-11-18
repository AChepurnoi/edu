package lab;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Created by Sasha on 3/5/17.
 */

public class Lab1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lab 1");
        final Group root = new Group();
        final Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.ORANGE);

        Pram pram = new Pram(275,175);
        root.getChildren().add(pram.ellips().get(0));

        root.getChildren().addAll(pram.drawableParts());
        root.getChildren().add(pram.text());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}