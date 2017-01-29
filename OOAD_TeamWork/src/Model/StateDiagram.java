/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.Group;
import javafx.scene.control.TextField;

/**
 *
 * @author 正文
 */
public class StateDiagram implements DiagramElement,Phototype {
    private ArrayList<DiagramElement> elements=new ArrayList<>();
    private static final long serialVersionUID = 010L;
    public Group draw(){
        return null;
    }
    public void add(DiagramElement e){
        elements.add(e);
        
    }
    public void remove(DiagramElement e){
        elements.remove(e);
    }
    public ArrayList<DiagramElement> getAlldiagram(){
        return elements;
    }
    public ArrayList<Transition> getAlltrans(){
        ArrayList<Transition> ot=new ArrayList<>();
        Iterator<DiagramElement> itr=elements.iterator();
        while(itr.hasNext()){
            DiagramElement e=itr.next();
            if (e instanceof Transition){
                ot.add((Transition)e);
            }
        }
        return ot;
    }
    public ArrayList<State> getAllstate(){
        ArrayList<State> ot=new ArrayList<>();
        Iterator<DiagramElement> itr=elements.iterator();
        while(itr.hasNext()){
            DiagramElement e=itr.next();
            if (e instanceof State){
                ot.add((State)e);
            }
        }
        return ot;
    }
    public void select(){}
    public void unselected(){}
    public TextField getTextfield(){
        return null;
    }
    public Object clone(){
        StateDiagram obj=new StateDiagram();
        elements.forEach(e->{
            obj.add((DiagramElement)((Phototype)e).clone());
        });
        return obj;
    }
}
