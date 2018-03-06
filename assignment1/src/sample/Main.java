package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    // Define variables
    private TableView<Spam> TableLayout;
    private BorderPane layout;
    private TextField  FileField, HamField, SpamProbField;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Setting the title to the assignment
        primaryStage.setTitle("Assignment 1");

        BorderPane layout = new BorderPane();
        //creating the table column
        TableColumn<Spam, Float> File = new TableColumn<>("File");
        File.setPrefWidth(100);
        File.setCellValueFactory(new PropertyValueFactory<>("File"));

        TableColumn<Spam, Float> Ham = new TableColumn<>("Ham");
        Ham.setPrefWidth(100);
        Ham.setCellValueFactory(new PropertyValueFactory<>("Ham"));

        TableColumn<Spam, Float> SpamProb = new TableColumn<>("Spam Probablility");
        SpamProb.setPrefWidth(100);
        SpamProb.setCellValueFactory(new PropertyValueFactory<>("Spam Probablility"));

        this.TableLayout = new TableView<>();
        this.TableLayout.getColumns().add(File);
        this.TableLayout.getColumns().add(Ham);
        this.TableLayout.getColumns().add(SpamProb);

        // Edit box
        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(5, 5, 5, 5));
        editArea.setVgap(6);
        editArea.setHgap(6);




        //Creating the text boxes
        Label assignment = new Label("Accuracy ");
        HamField = new TextField();
        HamField.setPromptText("Accuracy");
        editArea.add(HamField, 4, 1);

        Label exam = new Label("Precision ");
        SpamProbField = new TextField();
        SpamProbField.setPromptText("Precision");
        editArea.add(SpamProbField, 4, 2);

        Button addButton = new Button("Find");
        //setting the action event handler
        addButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                String File = FileField.getText();
                String Ham = HamField.getText();
                float SpamProb= Float.parseFloat(SpamProbField.getText());

                Spam newtab = new Spam(File, Ham, SpamProb);
                TableLayout.getItems().add(newtab);
            }
        });

        layout.setCenter(TableLayout);
        layout.setBottom(editArea);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
