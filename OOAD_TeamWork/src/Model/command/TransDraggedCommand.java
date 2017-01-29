/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.command;

import Model.*;
import controller.MainPanelController;
import javafx.scene.effect.Light.Point;

/**
 *
 * @author 正文
 */
public class TransDraggedCommand extends Command{
    private LinkTrans oldstartlinktrans;
    private LinkTrans oldendlinktrans;
    private Point oldturnpoint=new Point();
    private LinkTrans newstartlinktrans;
    private LinkTrans newendlinktrans;
    private Point newturnpoint=new Point();
    private boolean oldturnsetted;
    private boolean newturnsetted;
    public TransDraggedCommand(DiagramElement e){
        super(e);
        Transition t=(Transition)Receiver;
        oldturnsetted=t.isturnsetted();
        oldturnpoint=t.getTrunlayoutpoint();
        oldstartlinktrans=(LinkTrans)t.getEnterstate().getlinktrans(t,"start").clone();
        oldendlinktrans=(LinkTrans)t.getEndState().getlinktrans(t,"end").clone();
    }

    @Override
    public void Execute(MainPanelController controller) {
        Transition t=(Transition)Receiver;
        newturnsetted=t.isturnsetted();
        newturnpoint=t.getTrunlayoutpoint();
        newstartlinktrans=(LinkTrans)t.getEnterstate().getlinktrans(t,"start").clone();
        newendlinktrans=(LinkTrans)t.getEndState().getlinktrans(t,"end").clone();
    }

    @Override
    public void undo(MainPanelController controller) {
        Transition t=(Transition)Receiver;
        t.getEnterstate().linkTrans(oldstartlinktrans);
        t.getEndState().linkTrans(oldendlinktrans);
        double X=t.getEnterstate().draw().getChildren().get(0).getLayoutX()+oldstartlinktrans.getX();
        double Y=t.getEnterstate().draw().getChildren().get(0).getLayoutY()+oldstartlinktrans.getY();
        t.setstartpoint(X, Y);
        X=t.getEndState().draw().getChildren().get(0).getLayoutX()+oldendlinktrans.getX();
        Y=t.getEndState().draw().getChildren().get(0).getLayoutY()+oldendlinktrans.getY();
        t.setendpoint(X, Y);
        t.setTurnlayoutpoint(oldturnpoint.getX(), oldturnpoint.getY());
        t.setturnsetted(oldturnsetted);
    }
    @Override
    public void redo(MainPanelController controller) {
        Transition t=(Transition)Receiver;
        t.getEnterstate().linkTrans(newstartlinktrans);
        t.getEndState().linkTrans(newendlinktrans);
        double X=t.getEnterstate().draw().getChildren().get(0).getLayoutX()+newstartlinktrans.getX();
        double Y=t.getEnterstate().draw().getChildren().get(0).getLayoutY()+newstartlinktrans.getY();
        t.setstartpoint(X, Y);
        X=t.getEndState().draw().getChildren().get(0).getLayoutX()+newendlinktrans.getX();
        Y=t.getEndState().draw().getChildren().get(0).getLayoutY()+newendlinktrans.getY();
        t.setendpoint(X, Y);
        t.setTurnlayoutpoint(newturnpoint.getX(), newturnpoint.getY());
        t.setturnsetted(newturnsetted);
    }

}
