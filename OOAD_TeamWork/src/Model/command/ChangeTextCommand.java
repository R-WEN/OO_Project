/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.command;

import Model.*;
import controller.MainPanelController;

/**
 *
 * @author 正文
 */
public class ChangeTextCommand extends Command {

    public ChangeTextCommand(DiagramElement e){
        super(e);

    }
    @Override
    public void Execute(MainPanelController controller) {
        //Receiver.getTextfield().getText();
    }

    @Override
    public void undo(MainPanelController controller) {
        Receiver.getTextfield().undo();
        //Receiver.getTextfield().setText(oldtext);
    }

    @Override
    public void redo(MainPanelController controller) {
        Receiver.getTextfield().redo();
        //Receiver.getTextfield().setText(newtext);
        
    }
    
}
