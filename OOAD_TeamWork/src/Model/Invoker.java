/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import Model.command.*;
import controller.MainPanelController;
import java.util.EmptyStackException;
import java.util.Stack;
import javafx.scene.control.Menu;

public class Invoker {
    private MainPanelController mainpagecontroller;
    private Menu editmenu;
    private Command command;
    private Stack<Command> undostack=new Stack<>();
    private Stack<Command> redostack=new Stack<>();
    public Invoker(MainPanelController c,Menu edit){
        mainpagecontroller=c;
        editmenu=edit;
    }
    public void addcommand(Command c){
        command=c;
    }
    public void Execute(){
        command.Execute(mainpagecontroller);
        undostack.push(command);
        editmenu.getItems().get(0).setDisable(false);
        command=null;
        redostack.clear();
        editmenu.getItems().get(1).setDisable(true);
    };
    public void undo(){
        Command c;
        if (canUndo()){
            c=undostack.pop();
            if (!canUndo()){editmenu.getItems().get(0).setDisable(true);}
            c.undo(mainpagecontroller);
            redostack.push(c);
            editmenu.getItems().get(1).setDisable(false);
        }
    }
    public void redo(){
        Command c;
        if (canRedo()){
            c=redostack.pop();
            if (!canRedo()){editmenu.getItems().get(1).setDisable(true);}
            c.redo(mainpagecontroller);
            undostack.push(c);
            editmenu.getItems().get(0).setDisable(false);
        }
    }
    public boolean canUndo(){
        return !undostack.isEmpty();
    }
    public boolean canRedo(){
        return !redostack.isEmpty();
    }
    public void clearAll(){
        undostack.clear();
        redostack.clear();
        editmenu.getItems().get(0).setDisable(true);
        editmenu.getItems().get(1).setDisable(true);
    }
}
