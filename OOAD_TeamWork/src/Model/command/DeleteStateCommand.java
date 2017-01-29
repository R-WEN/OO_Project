/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.command;

import Model.*;
import controller.MainPanelController;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

/**
 *
 * @author 正文
 */
public class DeleteStateCommand extends Command {
    
    private ArrayList<LinkTrans> oldlinktrans=new ArrayList<>();
    private ArrayList<LinkTrans> reverseoldlinktrans=new ArrayList<>();
    private Pane deletedpane;
    public DeleteStateCommand(DiagramElement e){
        super(e);
    }
    @Override
    public void Execute(MainPanelController controller) {
        State s=(State)Receiver;
        deletedpane=controller.getSelectdPane();
        s.getlinktrans().forEach(l->{
            oldlinktrans.add((LinkTrans)l.clone());
            if (l.getType().equals("start")){
                reverseoldlinktrans.add((LinkTrans)l.getTrans().getEndState().getlinktrans(l.getTrans(), "end").clone());
            }else{
                reverseoldlinktrans.add((LinkTrans)l.getTrans().getEnterstate().getlinktrans(l.getTrans(), "start").clone());
            } 
        });
        //System.out.println(oldlinktrans.size()+" "+reverseoldlinktrans.size());
        oldlinktrans.forEach(link->{
            link.getTrans().getEnterstate().removeTrans(link.getTrans(),"start");
            link.getTrans().getEndState().removeTrans(link.getTrans(), "end");
            controller.getstateDiagram().remove(link.getTrans());
            Group tc=link.getTrans().draw();
            Group g=(Group)controller.getSelectdPane().getChildren().get(0);
            g.getChildren().remove(tc);
        });
        controller.getstateDiagram().remove(Receiver);
        Group c=Receiver.draw();
        Group g=(Group)controller.getSelectdPane().getChildren().get(0);
        g.getChildren().remove(c);
        
        System.out.println("DeleteState after statediagram:"+controller.getstateDiagram().getAlldiagram().size());
    }

    @Override
    public void undo(MainPanelController controller) {
        controller.getstateDiagram().add(Receiver);
        Group c=Receiver.draw();
        Group g=(Group)deletedpane.getChildren().get(0);
        g.getChildren().add(c);
        reverseoldlinktrans.forEach(l->{
            if (l.getType().equals("start")){
                l.getTrans().getEnterstate().linkTrans(l);
            }else{
                l.getTrans().getEndState().linkTrans(l);
            }
        });
        oldlinktrans.forEach(t->{
            
            controller.getstateDiagram().add(t.getTrans());
            Group tc=t.getTrans().draw();
            g.getChildren().add(tc);
        });
        
        ArrayList<LinkTrans> ls=((State)Receiver).getlinktrans();
        ls.removeAll(ls);
        oldlinktrans.forEach(olink->{
            ls.add(olink);
        });
    }

    @Override
    public void redo(MainPanelController controller) {
        oldlinktrans.removeAll(oldlinktrans);
        reverseoldlinktrans.removeAll(reverseoldlinktrans);
        State s=(State)Receiver;
        deletedpane=controller.getSelectdPane();
        s.getlinktrans().forEach(l->{
            oldlinktrans.add((LinkTrans)l.clone());
            if (l.getType().equals("start")){
                reverseoldlinktrans.add((LinkTrans)l.getTrans().getEndState().getlinktrans(l.getTrans(), "end").clone());
            }else{
                reverseoldlinktrans.add((LinkTrans)l.getTrans().getEnterstate().getlinktrans(l.getTrans(), "start").clone());
            } 
        });
        //System.out.println(oldlinktrans.size()+" "+reverseoldlinktrans.size());
        oldlinktrans.forEach(link->{
            link.getTrans().getEnterstate().removeTrans(link.getTrans(),"start");
            link.getTrans().getEndState().removeTrans(link.getTrans(), "end");
            controller.getstateDiagram().remove(link.getTrans());
            Group tc=link.getTrans().draw();
            Group g=(Group)controller.getSelectdPane().getChildren().get(0);
            g.getChildren().remove(tc);
        });
        controller.getstateDiagram().remove(Receiver);
        Group c=Receiver.draw();
        Group g=(Group)controller.getSelectdPane().getChildren().get(0);
        g.getChildren().remove(c);   
    }
    
}
