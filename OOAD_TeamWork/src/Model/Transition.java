/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import Model.command.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.event.EventHandler;
import javafx.scene.effect.Light.Point;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.QuadCurve;
import javax.swing.SwingUtilities;


/**
 *
 * @author 正文
 */
public class Transition implements DiagramElement {
    private static final long serialVersionUID = 030L;
    private transient Path arrow;
    private transient QuadCurve line=new QuadCurve();
    private transient Group trans=new Group();
    private transient Canvas startpoint=new Canvas(17,17);
    private transient Canvas endpoint=new Canvas(17,17);
    private transient Canvas turnpoint=new Canvas(17,17);
    private transient TextField text=new TextField();
    private State enterstate=null;
    private State endstate=null;
    private boolean isturnsetted=false;
    private Command bufferdrag;
    public Transition(){
        
        line.setStartX(50);
        line.setStartY(50);
        line.setEndX(100);
        line.setEndY(500);
        line.setControlX(Math.min(line.getStartX(), line.getEndX())+Math.abs(line.getStartX()-line.getEndX())/2);
        line.setControlY(Math.min(line.getStartY(), line.getEndY())+Math.abs(line.getStartY()-line.getEndY())/2);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(1);
        line.setFill( null);
        
        inipoint(startpoint);
        inipoint(endpoint);
        inipoint(turnpoint);
        
        startpoint.setLayoutX(line.getStartX()-startpoint.getWidth()/2);
        startpoint.setLayoutY(line.getStartY()-startpoint.getHeight()/2);
        endpoint.setLayoutX(line.getEndX()-startpoint.getWidth()/2);
        endpoint.setLayoutY(line.getEndY()-startpoint.getHeight()/2);
        turnpoint.setLayoutX(line.getControlX()-startpoint.getWidth()/2);
        turnpoint.setLayoutY(line.getControlY()-startpoint.getHeight()/2);
        
        iniarrow();
        
        text.setText("Transition");
        
        text.setMouseTransparent(false);
        text.setStyle("-fx-text-box-border:transparent;-fx-background-color:transparent;");
        text.setEditable(false);
        text.setPrefSize(80.0, 12.0);
        text.setAlignment(Pos.CENTER);
        text.setLayoutX(turnpoint.getLayoutX()-text.getPrefWidth()/2);
        text.setLayoutY(turnpoint.getLayoutY()-20-text.getPrefHeight()/2);
        
        trans.getChildren().addAll(startpoint,endpoint,turnpoint,line,arrow,text);
    }
    
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeUTF(text.getText());
        s.writeDouble(startpoint.getLayoutX()+startpoint.getWidth()/2);
        s.writeDouble(startpoint.getLayoutY()+startpoint.getHeight()/2);
        s.writeDouble(endpoint.getLayoutX()+endpoint.getWidth()/2);
        s.writeDouble(endpoint.getLayoutY()+endpoint.getHeight()/2);
        s.writeDouble(turnpoint.getLayoutX());
        s.writeDouble(turnpoint.getLayoutY());
    }
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        
        line=new QuadCurve();
        trans=new Group();
        startpoint=new Canvas(17,17);
        endpoint=new Canvas(17,17);
        turnpoint=new Canvas(17,17);
        
        line.setStartX(50);
        line.setStartY(50);
        line.setEndX(100);
        line.setEndY(500);
        line.setControlX(Math.min(line.getStartX(), line.getEndX())+Math.abs(line.getStartX()-line.getEndX())/2);
        line.setControlY(Math.min(line.getStartY(), line.getEndY())+Math.abs(line.getStartY()-line.getEndY())/2);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(1);
        line.setFill( null);
        
        inipoint(startpoint);
        inipoint(endpoint);
        inipoint(turnpoint);
        iniarrow();
        
        text=new TextField();
        text.setText(s.readUTF());
        text.setMouseTransparent(false);
        text.setStyle("-fx-text-box-border:transparent;-fx-background-color:transparent;");
        text.setEditable(false);
        text.setPrefSize(80.0, 12.0);
        text.setAlignment(Pos.CENTER);
        text.setLayoutX(turnpoint.getLayoutX()-text.getPrefWidth()/2);
        text.setLayoutY(turnpoint.getLayoutY()-20-text.getPrefHeight()/2);
        setstartpoint(s.readDouble(),s.readDouble());
        setendpoint(s.readDouble(),s.readDouble());
        setTurnlayoutpoint(s.readDouble(),s.readDouble());
        trans.getChildren().addAll(startpoint,endpoint,turnpoint,line,text);
    }
    
    public TextField getTextfield(){
        return text;
    }
    public void select(){
        
        text.setEditable(true);
        changeablepoint(startpoint);
        changeablepoint(endpoint);
        changeablepoint(turnpoint);
    }
    public void unselected(){
        text.setEditable(false);
        
        inipoint(startpoint);
        inipoint(endpoint);
        inipoint(turnpoint);
    }
    public Group draw(){
        return trans;
    }
    public State getEnterstate(){
        return enterstate;
    }
    public void setEnterstate(State s,String type){
        enterstate=s;
        s.linkTrans(this,type);
    }
    public void removeEnterstate(){
        enterstate=null;
    }
    public State getEndState(){
        return endstate;
    }
    public void setEndstate(State s,String type){
        endstate=s;
        s.linkTrans(this,type);
    }
    public void removeEndstate(){
        endstate.removeTrans(this,"end");
        endstate=null;
    }
    public void setstartpoint(double X,double Y){
        startpoint.setLayoutX(X-startpoint.getWidth()/2);
        startpoint.setLayoutY(Y-startpoint.getHeight()/2);
        line.setStartX(X);
        line.setStartY(Y);
        resetarrow();
        
    }
    public void setendpoint(double X,double Y){
        endpoint.setLayoutX(X-endpoint.getWidth()/2);
        endpoint.setLayoutY(Y-endpoint.getHeight()/2);
        line.setEndX(X);
        line.setEndY(Y);
        resetarrow();
        
    }
    private void resetarrow(){
        arrow.getElements().removeAll(arrow.getElements());
        iniarrow();
        trans.getChildren().remove(arrow);
        trans.getChildren().add(arrow);
    }
    public void resetturnpoint(){
        line.setControlX(Math.min(line.getStartX(), line.getEndX())+Math.abs(line.getStartX()-line.getEndX())/2);
        line.setControlY(Math.min(line.getStartY(), line.getEndY())+Math.abs(line.getStartY()-line.getEndY())/2);
        turnpoint.setLayoutX(line.getControlX()-turnpoint.getWidth()/2);
        turnpoint.setLayoutY(line.getControlY()-turnpoint.getHeight()/2);
        text.setLayoutX(turnpoint.getLayoutX()-text.getPrefWidth()/2);
        text.setLayoutY(turnpoint.getLayoutY()-20-text.getPrefHeight()/2);
        
        isturnsetted=false;
    }
    public boolean isturnsetted(){
        return isturnsetted;
    }
    public void setturnsetted(boolean b){
        isturnsetted=b;
    }
    /**
     * Evaluate the cubic curve at a parameter 0<=t<=1, returns a Point2D
     * @param c the CubicCurve 
     * @param t param between 0 and 1
     * @return a Point2D 
     */
    private Point2D eval(QuadCurve c, float t){
        Point2D p=new Point2D(Math.pow(1-t,3)*c.getStartX()+
                3*t*Math.pow(1-t,2)*c.getControlX()+
                3*(1-t)*t*t*c.getControlX()+
                Math.pow(t, 3)*c.getEndX(),
                Math.pow(1-t,3)*c.getStartY()+
                3*t*Math.pow(1-t, 2)*c.getControlY()+
                3*(1-t)*t*t*c.getControlY()+
                Math.pow(t, 3)*c.getEndY());
        return p;
    }

    /**
     * Evaluate the tangent of the cubic curve at a parameter 0<=t<=1, returns a Point2D
     * @param c the CubicCurve 
     * @param t param between 0 and 1
     * @return a Point2D 
     */
    private Point2D evalDt(QuadCurve c, float t){
        Point2D p=new Point2D(-3*Math.pow(1-t,2)*c.getStartX()+
                3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlX()+
                3*((1-t)*2*t-t*t)*c.getControlX()+
                3*Math.pow(t, 2)*c.getEndX(),
                -3*Math.pow(1-t,2)*c.getStartY()+
                3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlY()+
                3*((1-t)*2*t-t*t)*c.getControlY()+
                3*Math.pow(t, 2)*c.getEndY());
        return p;
    }
    private void iniarrow(){
        double size=Math.max(line.getBoundsInLocal().getWidth(),
                         line.getBoundsInLocal().getHeight());
        double scale=size/4d;

        Point2D ori=eval(line,0);
        Point2D tan=evalDt(line,0).normalize().multiply(scale);

        ori=eval(line,1);
        tan=evalDt(line,1).normalize().multiply(scale);
        arrow=new Path();
        arrow.getElements().add(new MoveTo(ori.getX()-0.2*tan.getX()-0.2*tan.getY(),
                                            ori.getY()-0.2*tan.getY()+0.2*tan.getX()));
        arrow.getElements().add(new LineTo(ori.getX(), ori.getY()));
        arrow.getElements().add(new LineTo(ori.getX()-0.2*tan.getX()+0.2*tan.getY(),
                                            ori.getY()-0.2*tan.getY()-0.2*tan.getX()));
    }
    private void inipoint(Canvas c){
        GraphicsContext gc=c.getGraphicsContext2D();
        gc.setFill(Color.TRANSPARENT);
        gc.clearRect(0, 0, 17, 17);
        gc.fillRoundRect(0,0,15.0,15.0,8.0,8.0);
        c.setMouseTransparent(true);
        
    }
    private void changeablepoint(Canvas c){
        GraphicsContext gc=c.getGraphicsContext2D();
        gc.setFill(Color.POWDERBLUE);
        gc.clearRect(0, 0, 17, 17);
        gc.fillRoundRect(0,0,15.0,15.0,8.0,8.0);
        c.setMouseTransparent(false);
    }
    public void setpointevent(SingleSelected selectmodel,Invoker invoker){
            /*startpoint.setOnDragDetected((MouseEvent e) -> {
                
                Dragboard db = startpoint.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content=new ClipboardContent();
                content.putString("start");
                
                //content.put(start, this);
                db.setContent(content);
                e.setDragDetect(false);
                //e.consume();
                
            });*/

            startpoint.setOnMouseDragged((MouseEvent e) -> {
                startpoint.mouseTransparentProperty().set(false);
                //e.setDragDetect(false);
                
                if (e.isPrimaryButtonDown()){
                    
                    if (bufferdrag==null){
                        Command ds=new TransDraggedCommand(this);
                        
                        bufferdrag=ds;
                    }
                
                    double X=e.getSceneX()-ErrorPosition.X;
                    double Y=e.getSceneY()-ErrorPosition.Y;
                    if (enterstate != null){      
                        enterstate.pointmove(e);
                        X=enterstate.getpointX();
                        Y=enterstate.getpointY();
                        enterstate.changelinkpoint(this,"start");
                    }
                    startpoint.setLayoutX(X-startpoint.getWidth()/2);
                    startpoint.setLayoutY(Y-startpoint.getHeight()/2);
                    line.setStartX(X);
                    line.setStartY(Y);
                    resetturnpoint();
                    resetarrow();
                }

            });
            
            endpoint.setOnMouseDragged(e->{
                
                    if (e.isPrimaryButtonDown()){
                        if (bufferdrag==null){
                        Command ds=new TransDraggedCommand(this);
                        
                        bufferdrag=ds;
                        }
                        double X=e.getSceneX()-ErrorPosition.X;
                        double Y=e.getSceneY()-ErrorPosition.Y;
                        if (endstate != null){       
                            endstate.pointmove(e);
                            X=endstate.getpointX();
                            Y=endstate.getpointY();
                            endstate.changelinkpoint(this,"end");
                        }
                        endpoint.setLayoutX(X-endpoint.getWidth()/2);
                        endpoint.setLayoutY(Y-endpoint.getHeight()/2);
                        line.setEndX(X);
                        line.setEndY(Y);
                        resetturnpoint();
                        resetarrow();
                    }
                
            });
            
            turnpoint.setOnMouseDragged(e->{
   
                if (e.isPrimaryButtonDown()){
                    if (bufferdrag==null){
                        Command ds=new TransDraggedCommand(this);

                        bufferdrag=ds;
                    }
                    turnpoint.setLayoutX(line.getControlX()-turnpoint.getWidth()/2);
                    turnpoint.setLayoutY(line.getControlY()-turnpoint.getHeight()/2);
                    line.setControlX(e.getSceneX()-ErrorPosition.X);
                    line.setControlY(e.getSceneY()-ErrorPosition.Y);
                    resetarrow();
                    moveText();
                    isturnsetted=true;
                }
                
            });
            turnpoint.setOnMouseReleased(e->{
                if(bufferdrag!=null){
                    invoker.addcommand(bufferdrag);
                    invoker.Execute();
                    bufferdrag=null;
                }
                moveText();
                selectmodel.select(this);
            });

            endpoint.setOnMouseReleased(e->{
                if(bufferdrag!=null){
                    invoker.addcommand(bufferdrag);
                    invoker.Execute();
                    bufferdrag=null;
                }
                selectmodel.select(this);
            });
            startpoint.setOnMouseReleased(e->{
                if(bufferdrag!=null){
                    invoker.addcommand(bufferdrag);
                    invoker.Execute();
                    bufferdrag=null;
                }
                selectmodel.select(this);
            });
            
    }
    public void moveText(){
        double XX=(startpoint.getLayoutX()+startpoint.getWidth()/2)+( ((endpoint.getLayoutX()+endpoint.getWidth()/2) - (startpoint.getLayoutX()+startpoint.getWidth()/2)) /2);
        double YY=(startpoint.getLayoutY()+startpoint.getHeight()/2)+(((endpoint.getLayoutY()+endpoint.getHeight()/2) - (startpoint.getLayoutY()+startpoint.getHeight()/2)) /2);

        double X=XX+((turnpoint.getLayoutX()+turnpoint.getWidth()/2)-XX)/2;
        double Y=YY+((turnpoint.getLayoutY()+turnpoint.getHeight()/2)-YY)/2;
        text.setLayoutX(X-text.getPrefWidth()/2);
        text.setLayoutY(Y-text.getPrefHeight()/2-30);
        resetarrow();
    }
    public Point getTrunlayoutpoint(){
        Point p=new Point();
        p.setX(turnpoint.getLayoutX());
        p.setY(turnpoint.getLayoutY());
        return p;
    }
    public void setTurnlayoutpoint(double X,double Y){
        turnpoint.setLayoutX(X);
        turnpoint.setLayoutY(Y);
        line.setControlX(X+turnpoint.getWidth()/2);
        line.setControlY(Y+turnpoint.getHeight()/2);
        moveText();
        resetarrow();
    }

}

