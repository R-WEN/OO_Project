/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.command;

import Model.*;
import controller.MainPanelController;
import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

/**
 *
 * @author 正文
 */
public class NewTransCommand extends Command {
    private LinkTrans oldstartlinktrans;
    private LinkTrans oldendlinktrans;
    private Pane deletedpane;
    public NewTransCommand(DiagramElement e){
        super(e);
    }
    @Override
    public void Execute(MainPanelController controller) {

        
        controller.getstateDiagram().remove(Receiver);
        controller.getstateDiagram().add(Receiver);
        deletedpane=controller.getSelectdPane();
        Group c=Receiver.draw();
        Group g=(Group)controller.getSelectdPane().getChildren().get(0);
        g.getChildren().remove(c);
        g.getChildren().add(c);
    }

    @Override
    public void undo(MainPanelController controller) {
        
        Transition t=(Transition)Receiver;
        oldstartlinktrans=(LinkTrans)t.getEnterstate().getlinktrans(t,"start").clone();
        oldendlinktrans=(LinkTrans)t.getEndState().getlinktrans(t,"end").clone();
        
        t.getEnterstate().removeTrans(t,"start");
        t.getEndState().removeTrans(t, "end");
        controller.getstateDiagram().remove(Receiver);
        Group c=Receiver.draw();
        Group g=(Group)deletedpane.getChildren().get(0);
        g.getChildren().remove(c);
    }
    
    @Override
    public void redo(MainPanelController controller) {
        Transition t=(Transition)Receiver;
        t.getEnterstate().linkTrans(oldstartlinktrans);
        t.getEndState().linkTrans(oldendlinktrans);
        
        double X=t.getEnterstate().draw().getChildren().get(0).getLayoutX()+oldstartlinktrans.getX();
        double Y=t.getEnterstate().draw().getChildren().get(0).getLayoutY()+oldstartlinktrans.getY();
        
        t.setstartpoint(X, Y);
        X=t.getEndState().draw().getChildren().get(0).getLayoutX()+oldendlinktrans.getX();
        Y=t.getEndState().draw().getChildren().get(0).getLayoutY()+oldendlinktrans.getY();
        t.setendpoint(X, Y);
        
        
        controller.getstateDiagram().add(Receiver);
        Group c=Receiver.draw();
        Group g=(Group)deletedpane.getChildren().get(0);
        g.getChildren().add(c);
    }

    
    
}
