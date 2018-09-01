package output;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;

public class Interface extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException{

        StackPane root = new StackPane();

        Scene introScene = new Scene(root, 310, 350);
        introScene.setRoot(root);
        stage.setScene(introScene);
        stage.setTitle("KIWI APPLICATION TO SAVE MY LIFE");

        //VBox para ter o(s) botao(oes) centrado(s)
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        root.getChildren().add(vbox);

        Button btn = new Button("LETS GO!!!");
        btn.setFont(Font.font(Font.getDefault().getName(), FontWeight.BLACK, 17));
        vbox.getChildren().add(btn);

        stage.centerOnScreen();
        stage.show();


    }

}
