/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.command;

import Model.*;
import controller.MainPanelController;
import javafx.scene.effect.Light.Point;
import javafx.scene.layout.Pane;

/**
 *
 * @author 正文
 */
public class StateDraggedCommand extends Command {
    private Point redopoint=new Point();
    private Point undopoint=new Point();
    public StateDraggedCommand(DiagramElement e){
        super(e);
        undopoint.setX(Receiver.draw().getChildren().get(0).getLayoutX());
        undopoint.setY(Receiver.draw().getChildren().get(0).getLayoutY());
    }

    public void Execute(MainPanelController controller) {
        redopoint.setX(Receiver.draw().getChildren().get(0).getLayoutX());
        redopoint.setY(Receiver.draw().getChildren().get(0).getLayoutY());
    }

    @Override
    public void undo(MainPanelController controller) {
        
        Pane state_pane=(Pane)Receiver.draw().getChildren().get(0);
        state_pane.setLayoutX(undopoint.getX());
        state_pane.setLayoutY(undopoint.getY());
        ((State)Receiver).getlinktrans().forEach(t->{
            if (("start").equals(t.getType())){
                t.getTrans().setstartpoint(t.getX()+state_pane.getLayoutX(), t.getY()+state_pane.getLayoutY());
            }else if (("end").equals(t.getType())){
                t.getTrans().setendpoint(t.getX()+state_pane.getLayoutX(), t.getY()+state_pane.getLayoutY());
            }
            if (!t.getTrans().isturnsetted()){
                t.getTrans().resetturnpoint();
            }
            t.getTrans().moveText();
        });
    }

    @Override
    public void redo(MainPanelController controller) {
        Pane state_pane=(Pane)Receiver.draw().getChildren().get(0);
        state_pane.setLayoutX(redopoint.getX());
        state_pane.setLayoutY(redopoint.getY());
        ((State)Receiver).getlinktrans().forEach(t->{
            if (("start").equals(t.getType())){
                t.getTrans().setstartpoint(t.getX()+state_pane.getLayoutX(), t.getY()+state_pane.getLayoutY());
            }else if (("end").equals(t.getType())){
                t.getTrans().setendpoint(t.getX()+state_pane.getLayoutX(), t.getY()+state_pane.getLayoutY());
            }
            if (!t.getTrans().isturnsetted()){
                t.getTrans().resetturnpoint();
            }
            t.getTrans().moveText();
            
        });
    }
}
