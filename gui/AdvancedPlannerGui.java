package gui;

import compile.Compiler;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.AdvancedPlanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.Optional;

/**
 * Created by John Elizarraras on 3/15/2016.
 */
public class AdvancedPlannerGui extends Application{
    private Stage mainStage;

    public static void main(String[] args){
        launch(args);
    }

    public void start(){
        main(new String[0]);
    }

    public void stage(){
        main(new String[0]);
    }

    public void start(Stage primStage){
        this.mainStage = primStage;
        primStage.setOnCloseRequest(confirmCloseEventHandler);
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20,0,20,20));
        primStage.setTitle("Advanced Planner");
        TextField textField = new TextField("Put File Name Here");
        Button button = new Button("Run");
        button.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                String[] file = {textField.getText()};
                AdvancedPlanner.main(file);
            }
        });
        button.setVisible(false);
        Text notFile = new Text("This is not a file!");
        notFile.setVisible(false);
        Text compileError = new Text("Compile Error!");
        compileError.setVisible(false);
        Button compile = new Button("Compile");
        compile.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                final int e = errors(textField.getText());
                if(isFile(textField.getText()) && e < 1){
                    button.setVisible(true);
                    notFile.setVisible(false);
                    compileError.setVisible(false);
                }
                else if(!isFile(textField.getText())){
                    notFile.setVisible(true);
                    button.setVisible(false);
                }
                else if(e > 0){
                    compileError.setVisible(true);
                    button.setVisible(false);
                }
            }
        });
        Button exit = new Button("Exit");
        exit.setVisible(true);
        exit.setOnAction(event -> primStage.fireEvent(new WindowEvent(primStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
//        StackPane root = new StackPane();
//        root.getChildren().add(textField);
//        root.getChildren().add(compile);
//        root.getChildren().add(button);
//        root.getChildren().add(exit);
//        root.getChildren().add(notFile);
//        root.getChildren().add(compileError);
//        primStage.setScene(new Scene(root, 300, 250));
//        primStage.setFullScreen(true);
//        primStage.alwaysOnTopProperty();
//        primStage.show();
        HBox vbButtons = new HBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(0,20,10,20));
        vbButtons.getChildren().addAll(textField, compile, button, exit, notFile, compileError);
        border.setCenter(vbButtons);
        Scene scene = new Scene(border,800,600);
        primStage.setScene(scene);
        primStage.show();
    }

    private boolean isFile(String name){
        boolean res = true;
        try {
            FileReader f = new FileReader(name);
        }
        catch(Exception e){
            res = false;
        }
        return res;
    }

    /**
     * Credit goes to jewelsea from stackoverflow(http://stackoverflow.com/users/1155209/jewelsea)
     */
    private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?"
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Confirm Exit");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(mainStage);
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get())) {
            event.consume();
        }
    };

    private int errors(String file){
        int ef = 0;
        if(isFile(file)){
            Compiler comp = new Compiler(file);
            ef = comp.errors();
        }
        return ef;
    }
}
