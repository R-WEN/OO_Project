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
public class NewStateCommand extends Command {
    private Pane deletedpane;
    public NewStateCommand(DiagramElement e){
        super(e);
    }
    @Override
    public void Execute(MainPanelController controller) {
        Group gc=Receiver.draw();
        deletedpane=controller.getSelectdPane();
        Group g=(Group)deletedpane.getChildren().get(0);
        g.getChildren().add(gc);
    }

    @Override
    public void undo(MainPanelController controller) {
        Group gc=Receiver.draw();
        Group g=(Group)deletedpane.getChildren().get(0);
        g.getChildren().remove(gc);
    }

    @Override
    public void redo(MainPanelController controller) {
        Group gc=Receiver.draw();
        Group g=(Group)deletedpane.getChildren().get(0);
        g.getChildren().add(gc);
    }
    
}
