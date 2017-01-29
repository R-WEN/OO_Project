/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.command;

import Model.DiagramElement;
import controller.MainPanelController;

/**
 *
 * @author 正文
 */
public abstract class Command {
    protected DiagramElement Receiver;
    public Command(DiagramElement e){
        Receiver=e;
    }
    public abstract void Execute(MainPanelController controller);
    public abstract void undo(MainPanelController controller);
    public abstract void redo(MainPanelController controller);
}
