package output;
import com.fasterxml.jackson.databind.JsonNode;
import excel.WriteExcel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import server.ApiCalls;

import javax.swing.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Interface extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException{

        StackPane root = new StackPane();

        Scene introScene = new Scene(root, 450, 350);
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

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                GridPane grid = new GridPane();
                Scene paramScene = new Scene(grid,450, 350);
                stage.setScene(paramScene);

                grid.setPadding(new Insets(30,100,30,100));
                grid.setVgap(20);
                grid.setHgap(20);
                paramScene.setRoot(grid);

                //Date From
                TextField dateFrom = new TextField();
                dateFrom.setPromptText("Date From (dd/mm/yyyy)");
                dateFrom.setPrefColumnCount(20);
                GridPane.setConstraints(dateFrom, 0, 0);
                grid.getChildren().add(dateFrom);
                dateFrom.setPrefHeight(20);

                //Date To
                TextField dateTo = new TextField();
                dateTo.setPromptText("Date To (dd/mm/yyyy)");
                dateTo.setPrefColumnCount(20);
                GridPane.setConstraints(dateTo, 0, 1);
                grid.getChildren().add(dateTo);
                dateTo.setPrefHeight(20);

                //Direct Flights
                TextField directFlights = new TextField();
                directFlights.setPromptText("Direct Flights (0 or 1)");
                directFlights.setPrefColumnCount(20);
                GridPane.setConstraints(directFlights, 0, 2);
                grid.getChildren().add(directFlights);
                directFlights.setPrefHeight(20);

                //Results limit
                TextField limit = new TextField();
                limit.setPromptText("Max results");
                limit.setPrefColumnCount(20);
                GridPane.setConstraints(limit, 0, 3);
                grid.getChildren().add(limit);
                limit.setPrefHeight(20);

                //Button Search
                Button searchBtn = new Button("Search");
                grid.getChildren().add(searchBtn);
                GridPane.setConstraints(searchBtn, 0, 5);
                GridPane.setHalignment(searchBtn, HPos.RIGHT);

                searchBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //String dateFromValue = dateFrom.getText();
                        //String dateToValue = dateTo.getText();

                        //root Pane
                        StackPane rootPane = new StackPane();
                        rootPane.setPadding(new Insets(10));

                        //new Scene
                        Scene searchScene = new Scene(rootPane, 650, 350);
                        stage.setScene(searchScene);
                        stage.centerOnScreen();

                        //BorderPane to be able to set a Label centered at the top of the window
                        BorderPane bPane = new BorderPane();

                        GridPane searchGrid = new GridPane();
                        searchGrid.setPadding(new Insets(10));
                        searchGrid.setVgap(20);
                        searchGrid.setHgap(20);

                        //Label with destination
                        Label destination = new Label("Madeira -> Europe");
                        destination.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
                        bPane.setTop(destination);
                        BorderPane.setAlignment(destination, Pos.CENTER);

                        int max = Integer.parseInt(limit.getText());

                        ApiCalls api = new ApiCalls();
                        String url = api.createSearchUrl(dateFrom.getText(), dateTo.getText(), directFlights.getText(), max);

                        try {
                            JsonNode rootNode = api.rootNodeTree(url);

                            //Column labels with destination, cost, duration, date
                            for (int i=0; i<4; i++)
                            {
                                // Destino | Custo | Duração | Data
                                Label column = new Label();

                                switch(i){
                                    case 0: column.setText("Destino"); break;
                                    case 1: column.setText("Custo"); break;
                                    case 2: column.setText("Duração"); break;
                                    case 3: column.setText("Data"); break;
                                    default: break;
                                }

                                column.setFont(Font.font("Verdana", FontWeight.BLACK, 14));
                                GridPane.setConstraints(column, i, 3);
                                GridPane.setHalignment(column, HPos.CENTER);
                                searchGrid.getChildren().add(column);
                            }



                            //Rows with each destination
                            for(int i=0; i<max; i++)
                            {
                                JsonNode tempNode = rootNode.get("data").get(i);

                                //For each field in one row
                                for(int j=0; j<4; j++)
                                {
                                    Label text = new Label();
                                    String str = "";

                                    switch(j){
                                        case(0): str = tempNode.get("cityTo").toString() + ", " + tempNode.get("countryTo").get("name").toString();
                                                 break;
                                        case(1): str = tempNode.get("conversion").get("EUR").toString() + "€";
                                                 break;
                                        case(2): str = tempNode.get("fly_duration").toString();
                                                 break;
                                        case(3): long dateUnix = Long.parseLong(tempNode.get("dTimeUTC").toString());
                                                 Date date = new Date (dateUnix * 1000);
                                                 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                 str = sdf.format(date);
                                                 break;
                                        default: break;
                                    }

                                    str = str.replaceAll("\"", "");
                                    text.setText(str);

                                    //text.setFont(Font.font("Verdana", FontWeight.BLACK, 14));
                                    GridPane.setConstraints(text, j, i + 4);
                                    GridPane.setHalignment(text, HPos.CENTER);
                                    searchGrid.getChildren().add(text);
                                }

                                String str = tempNode.get("cityTo").toString();
                                str += ", " + tempNode.get("countryTo").get("name").toString();
                                str = str.replaceAll("\"", "");
                            }
                        } catch (IOException e) { e.printStackTrace(); return; }

                        //Button 'back'
                        Button backBtn = new Button("Back");
                        searchGrid.getChildren().add(backBtn);
                        GridPane.setConstraints(backBtn, 0, max+5);
                        GridPane.setHalignment(backBtn, HPos.LEFT);

                        //Button 'Print year'
                        Button printBtn = new Button("Print year");
                        searchGrid.getChildren().add(printBtn);
                        GridPane.setConstraints(printBtn, 4, max+5);
                        GridPane.setHalignment(printBtn, HPos.RIGHT);

                        printBtn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                WriteExcel excel = new WriteExcel();
                                excel.printYear(dateFrom.getText());

                                JOptionPane.showMessageDialog(null, "Excel file created successfully.");
                            }
                        });

                        backBtn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                stage.setScene(paramScene);
                            }
                        });







                        rootPane.getChildren().addAll(bPane, searchGrid);
                    }
                });
            }
        });

    }

}
