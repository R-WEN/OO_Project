/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


import Model.Button.Transbtn;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;


public class State implements DiagramElement,Phototype{
    private static final long serialVersionUID = 020L;
    private final Long ID=System.currentTimeMillis();
    private transient Group state=new Group();
    private transient Pane state_pane=new Pane();
    private transient Canvas cn=new Canvas(100,100);
    private transient TextField text=new TextField();

    private transient Canvas point=new Canvas(3,3);
    private ArrayList<LinkTrans> linktrans=new ArrayList<>();
    
    public State(){
        GraphicsContext gct=point.getGraphicsContext2D();
        gct.setFill(Color.POWDERBLUE);
        gct.fillRoundRect(0,0,3.0,3.0,8.0,8.0);
 
        state_pane.prefHeight(100.0);
        state_pane.prefWidth(100.0);
        //Draw state canvas
        
        GraphicsContext gc=cn.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeOval(3, 3, 97 , 97);
        state_pane.getChildren().add(cn);
        //Text for state
        text.setText("State");
        
        text.setMouseTransparent(true);
        text.setStyle("-fx-text-box-border:transparent;-fx-background-color:transparent;");
    
        text.setPrefSize(80.0, 12.0);
        text.setAlignment(Pos.CENTER);
        text.setLayoutX(50-text.getPrefWidth()/2);
        text.setLayoutY(50-text.getPrefHeight()/2);
        
        state_pane.getChildren().add(text);
        
        state_pane.getChildren().add(point);
        
        state.getChildren().add(state_pane);
        
    }
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeUTF(text.getText());
        s.writeDouble(state_pane.getLayoutX());
        s.writeDouble(state_pane.getLayoutY());
        System.out.println("writing linktrans size:"+linktrans.size());
    }
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        state=new Group();
        state_pane=new Pane();
        cn=new Canvas(100,100);
        text=new TextField();
        point=new Canvas(3,3);
        //ini
        GraphicsContext gct=point.getGraphicsContext2D();
        gct.setFill(Color.POWDERBLUE);
        gct.fillRoundRect(0,0,3.0,3.0,8.0,8.0);
        text.setText(s.readUTF());
        text.setMouseTransparent(true);
        state_pane.prefHeight(100.0);
        state_pane.prefWidth(100.0);
        state_pane.setLayoutX(s.readDouble());
        state_pane.setLayoutY(s.readDouble());
        GraphicsContext gc=cn.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeOval(3, 3, 97 , 97);
        state_pane.getChildren().add(cn);
        
        
        text.setStyle("-fx-text-box-border:transparent;-fx-background-color:transparent;");
    
        text.setPrefSize(80.0, 12.0);
        text.setAlignment(Pos.CENTER);
        text.setLayoutX(50-text.getPrefWidth()/2);
        text.setLayoutY(50-text.getPrefHeight()/2);
        
        state_pane.getChildren().add(text);
        
        state_pane.getChildren().add(point);
        
        state.getChildren().add(state_pane);
        System.out.println(this+"linktrans size:"+linktrans.size());
    }
    public void linkTrans(Transition t,String type){
        LinkTrans nlink=null;
        for (LinkTrans link :linktrans){
            if(link.getTrans()==t && type.equals(link.getType())){
               nlink=link;
            }
        }
        if (nlink == null){
            nlink=new LinkTrans(t,type,getpointX()-state_pane.getLayoutX(),getpointY()-state_pane.getLayoutY());
            linktrans.add(nlink);
        }else{
            nlink.setX(getpointX()-state_pane.getLayoutX());
            nlink.setY(getpointY()-state_pane.getLayoutY());
        }
    }

    public void linkTrans(LinkTrans obj){
        LinkTrans nlink=null;
        for (LinkTrans link :linktrans){
            if(link.getTrans()==obj.getTrans() && obj.getType().equals(link.getType())){
               nlink=link;
            }
        }
        linktrans.remove(nlink);
        linktrans.add(obj);
    }
    public void linkTrans(ArrayList<LinkTrans> objs){
        linktrans=objs;
    }
    public void removeTrans(Transition t,String type){
        Iterator<LinkTrans> itr=linktrans.iterator();
        while (itr.hasNext()){
            LinkTrans link=itr.next();
                if (link.getTrans()==t && type.equals(link.getType())){
                    itr.remove();
                }
        }
    }
    public void changelinkpoint(Transition t,String type){
        linktrans.forEach(link->{
            if (type.equals(link.getType()) && link.getTrans()==t){
                link.setX(getpointX()-state_pane.getLayoutX());
                link.setY(getpointY()-state_pane.getLayoutY());
            }
        });
    }
    //
    
    public Group draw(){
        
        return state;
    }
    public double getCenterX(){
        return state_pane.getLayoutX()+state_pane.getPrefWidth();
    };
    public double getCenterY(){
        return state_pane.getLayoutY()+state_pane.getPrefHeight();
    }
    public void setX(double X){
        state_pane.setLayoutX(X-ErrorPosition.X-50);
    }
    public void setY(double Y){
        state_pane.setLayoutY(Y-ErrorPosition.Y-50);
    }
    
    public double getpointX(){
        return point.getLayoutX()+point.getWidth()/2+state_pane.getLayoutX();
    }
    public double getpointY(){
        return point.getLayoutY()+point.getHeight()/2+state_pane.getLayoutY();
    }
    
    public void setEvent(Transbtn transbtn){
        
        state.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
            if (transbtn.isDrawing()==false){
                //System.out.println(e.isDragDetect());
                if (e.getButton()==e.getButton().PRIMARY && !e.isDragDetect()){
                    state_pane.setLayoutX(e.getSceneX()-ErrorPosition.X-state_pane.getWidth()/2);
                    state_pane.setLayoutY(e.getSceneY()-ErrorPosition.Y-state_pane.getHeight()/2);
                    linktrans.forEach(t->{
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
        });
    }
    public void select(){
        text.setMouseTransparent(false);
        String cssDefault = "-fx-border-color: black;\n"
        + "-fx-border-width: 1;\n"
        + "-fx-border-style: dashed;\n";
        state_pane.setStyle(cssDefault);
  
    }
    public void unselected(){
        text.setMouseTransparent(true);
        state_pane.setStyle("");
    }
 
    
    
    public void pointmove(MouseEvent e){
            double degree= Math.toDegrees(Math.atan2( e.getSceneY()-ErrorPosition.Y-(state_pane.getLayoutY()+50),e.getSceneX()-ErrorPosition.X-(state_pane.getLayoutX()+50) ));
            if (degree < 0) {

                degree= Math.abs(degree);
            }else{
                degree-=180;
                degree= Math.abs(degree);
                degree+=180;
            }

            double rX=(Math.abs(Math.cos(Math.toRadians(degree))*48));
            double rY=(Math.abs(Math.sin(Math.toRadians(degree))*48));

            if (degree <= 90){

                point.setLayoutX(50+rX-point.getWidth()/2);
                point.setLayoutY(50-rY-point.getHeight()/2);
            }else if(degree <=180){
                point.setLayoutX(50-rX-point.getWidth()/2);
                point.setLayoutY(50-rY-point.getHeight()/2);
            }else if(degree <= 270){
                point.setLayoutX(50-rX-point.getWidth()/2);
                point.setLayoutY(50+rY-point.getHeight()/2);
            }else{
                point.setLayoutX(50+rX-point.getWidth()/2);
                point.setLayoutY(50+rY-point.getHeight()/2);
            }
    }
    
    public void pointmove(DragEvent e){
            double degree= Math.toDegrees(Math.atan2( e.getSceneY()-ErrorPosition.Y-(state_pane.getLayoutY()+50),e.getSceneX()-ErrorPosition.X-(state_pane.getLayoutX()+50) ));
            if (degree < 0) {

                degree= Math.abs(degree);
            }else{
                degree-=180;
                degree= Math.abs(degree);
                degree+=180;
            }

            double rX=(Math.abs(Math.cos(Math.toRadians(degree))*48));
            double rY=(Math.abs(Math.sin(Math.toRadians(degree))*48));

            if (degree <= 90){

                point.setLayoutX(50+rX-point.getWidth()/2);
                point.setLayoutY(50-rY-point.getHeight()/2);
            }else if(degree <=180){
                point.setLayoutX(50-rX-point.getWidth()/2);
                point.setLayoutY(50-rY-point.getHeight()/2);
            }else if(degree <= 270){
                point.setLayoutX(50-rX-point.getWidth()/2);
                point.setLayoutY(50+rY-point.getHeight()/2);
            }else{
                point.setLayoutX(50+rX-point.getWidth()/2);
                point.setLayoutY(50+rY-point.getHeight()/2);
            }
    }
    public ArrayList<LinkTrans> getlinktrans(){
        return linktrans;
    }
    public LinkTrans getlinktrans(Transition t,String type){
        LinkTrans obj=null;
        Iterator<LinkTrans> itr=linktrans.iterator();
        while (itr.hasNext()){
            LinkTrans link=itr.next();
            if ((link.getTrans()==t) && type.equals(link.getType())){
                obj=link;
            }
        }
        return obj;
    }
    public ArrayList<Transition> getlinked(){
        ArrayList<Transition> o=new ArrayList<>();
        linktrans.forEach(link->{
            o.add(link.getTrans());
        });
        return o;
    }
    public TextField getTextfield(){
        return text;
    }
    public Object clone(){
        State obj=new State();
        obj.state_pane.setLayoutX(state_pane.getLayoutX());
        obj.state_pane.setLayoutY(state_pane.getLayoutY());
        obj.text.setText(text.getText());
        linktrans.forEach(link->{
            obj.linkTrans((LinkTrans)link.clone());
        });
        
        return obj;
    }
}

