/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import controller.*;
public class OOAD_TeamWork extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainpage=FXMLLoader.load(getClass().getResource("/view/MainPanel.fxml"));
        Pane toolbtn=FXMLLoader.load(getClass().getResource("/view/ToolButton.fxml"));
        
        /*Pane CanvasLayer =new Pane();
        CanvasLayer.setStyle("-fx-background-color: #E5E5E5");
        mainpage.setRight(CanvasLayer);*/
        mainpage.setLeft(toolbtn);
        Parent root = mainpage;
        Scene scene = new Scene(root);
        MainPanelController maincontrol=new MainPanelController(scene,mainpage);
        ToolButtonController toolcontrol=new ToolButtonController(maincontrol,toolbtn);
        
        stage.setScene(scene);
        
        stage.setTitle("State Diagram Editor");
        
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
