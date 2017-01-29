/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import Model.Button.*;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.canvas.*;
import javafx.scene.input.MouseEvent;
import Model.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.CubicCurve;

public class ToolButtonController  {
    private MainPanelController mainController;
    private Pane Tool;
    private Statebtn statebtn;
    private Transbtn transitionbtn;
    
    public ToolButtonController(MainPanelController c,Pane v){
        mainController =c;
        Tool=v;
        statebtn=new Statebtn((Button)Tool.lookup("#statebtn"),statebtn_default.getInstance());
        transitionbtn=new Transbtn((Button)Tool.lookup("#transitionbtn"),transbtn_default.getInstance());
        
        iniEvent();
        
        mainController.setTool(this);
    }
    public void DrawState(double X,double Y){
        //State state=new State();
        if (bufferstate!=null){
            bufferstate.setX(X);
            bufferstate.setY(Y);
            
            mainController.setState(bufferstate);
        }
    }
    
    public Transition DrawTransition(){
        Transition trans=new Transition();
        //mainController.setTransition(trans );
        return trans;
    }
    private State bufferstate=null;
    private void iniEvent(){
        Button btn=statebtn.getButton();
 
        btn.setOnMouseDragged(e->{
            
            if(e.getButton()==e.getButton().PRIMARY){ 
                if (bufferstate==null){
                    bufferstate=new State();
                    ((Group)mainController.getSelectdPane().getChildren().get(0)).getChildren().add(bufferstate.draw());
                }
                
                bufferstate.setX(e.getSceneX());
                bufferstate.setY(e.getSceneY());
            }
        });
        btn.setOnMouseReleased((MouseEvent e) -> {
            if(e.getButton()==e.getButton().PRIMARY){
                if (bufferstate!=null){
                    ((Group)mainController.getSelectdPane().getChildren().get(0)).getChildren().remove(bufferstate.draw());
                    DrawState(e.getSceneX(),e.getSceneY());
                    bufferstate=null;
                }
            }
        });
        btn=transitionbtn.getButton();
        btn.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e) -> {
            if(e.getButton()==e.getButton().PRIMARY){
                
                transitionbtn.change();
                TabPane tp=mainController.getTabPane();
                tp.getTabs().forEach(t-> {
                    transitionbtn.changecursor((Pane)t.getContent());
                });
                
                //DrawTransition();
            }
        });

    }
    public Transbtn getTransitionbtn(){
        return transitionbtn;
    }
    public Statebtn getStatebtn(){
        return statebtn;
    }
}
