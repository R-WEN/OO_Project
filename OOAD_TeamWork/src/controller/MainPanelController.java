/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

//import javafx.scene.*;

import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.canvas.*;
import Model.*;
import Model.Button.*;
import Model.command.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class MainPanelController {
    private Scene mainscene;
    private BorderPane mainpage;
    private TabPane DiagramEdit=new TabPane();
    private ArrayList<StateDiagram> statediagram=new ArrayList<>();
    private ToolButtonController toolController;
    private Transition buffertrans=null;
    private State bufferstate=null;
    private SingleSelected selectmodel=new SingleSelected();
    private Invoker invoker;
    private Proxy files;
    private String extension="";
    public MainPanelController(Scene scene,BorderPane v){
        mainscene=scene;
        mainpage=v;
        MenuBar mb=(MenuBar)mainpage.getTop();
        iniMenubutton(mb);
        mainpage.setCenter(DiagramEdit);
        
        invoker=new Invoker(this,mb.getMenus().get(1));
        OpenNewDiagram();
        setkeyevent();
        files =new FileProxy(this);
        files.openfile(null);

    }
    
    public void setTool(ToolButtonController tool){
        toolController=tool;
    }
    private boolean checkTransbtnstate(){
        return toolController.getTransitionbtn().isDrawing();
    }
    private void OpenNewDiagram(){
        
        Tab tab=new Tab();
        tab.setText("untitlted");
        tab.setOnCloseRequest(e->{
            int index=DiagramEdit.getSelectionModel().getSelectedIndex();
            System.out.println("Close Tab :"+index);
            getSelectdPane().getChildren().removeAll(getSelectdPane().getChildren());  
            statediagram.remove(index);
            System.out.println("remove after statediagram :" + statediagram.size());
            files.closefile();
            invoker.clearAll();
        });
        DiagramEdit.getTabs().add(tab);
        statediagram.add(new StateDiagram());
        
        Pane Canvas=new Pane();
        
        Canvas.setStyle("-fx-background-color: #FFFFFF");
        
        
        Group Diagram=new Group();
        Canvas.getChildren().add(Diagram);
        Canvas.setCache(true);
        Canvas.setOnMouseClicked(e->{
            toolController.getTransitionbtn().inithisButton();
            TabPane tp=getTabPane();
            tp.getTabs().forEach(t-> {
                toolController.getTransitionbtn().changecursor((Pane)t.getContent());
            });
            
           
        });
        Canvas.setOnMousePressed(e->{
            selectmodel.unselect();
        });
        mainpage.setOnMouseClicked(e->{
            toolController.getTransitionbtn().inithisButton();
            TabPane tp=getTabPane();
            tp.getTabs().forEach(t-> {
                toolController.getTransitionbtn().changecursor((Pane)t.getContent());
            });
            selectmodel.unselect();
        });

        tab.setContent(Canvas);
        DiagramEdit.getSelectionModel().selectLast();
    }
    
    private void iniMenubutton(MenuBar mb){
        
        //File buttonlist
        Menu m=mb.getMenus().get(0);
        m.getItems().get(0).setText("New         Ctrl+N");
        m.getItems().get(0).setOnAction(e -> {
            OpenNewDiagram();
            files.openfile(null);
            if (checkTransbtnstate()){
                toolController.getTransitionbtn().changecursor((Pane)DiagramEdit.getSelectionModel().getSelectedItem().getContent());
            }
        });
        
        MenuItem newbtn=new MenuItem("Open        Ctrl+O");
        newbtn.setOnAction(e->{
            FileChooser filechooser=new FileChooser();
            filechooser.setTitle("Open");
            //filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("StateDiagram",".sde"));
            File file=filechooser.showOpenDialog(mainscene.getWindow());
            String filename=null;
            if (file!=null){
                filename=file.getName();
                
                if (!files.isOpen(file)){
                    //Open new in file
                    if (filename.substring(filename.length()-3, filename.length()).equals("sde")){
                        System.out.println("open file:"+file.getAbsolutePath());
                        OpenNewDiagram();
                        files.openfile(file);
                        System.out.println("statediagram diagram number:"+statediagram.get(DiagramEdit.getSelectionModel().getSelectedIndex()).getAlldiagram().size());
                    }else {
                        System.out.println("not select file");
                    }
                }
            }
        });
        m.getItems().add(newbtn);
        
        newbtn=new MenuItem("Save        Ctrl+S");
        newbtn.setOnAction(e->{
            if (!DiagramEdit.getSelectionModel().isEmpty()){
                int index=DiagramEdit.getSelectionModel().getSelectedIndex();
                selectmodel.unselect();
                files.savefile(statediagram.get(index));
            }
            
        });
        m.getItems().add(newbtn);
        
        newbtn=new MenuItem("Save as  Ctrl+Shift+S");
        newbtn.setOnAction(e->{
            if (!DiagramEdit.getSelectionModel().isEmpty()){
                int index=DiagramEdit.getSelectionModel().getSelectedIndex();
                selectmodel.unselect();
                files.SaveAsNew(statediagram.get(index));
            }
        });
        
        m.getItems().add(newbtn);
        
        newbtn=new MenuItem("Export Image");
        newbtn.setOnAction(e->{
            selectmodel.unselect();
            FileChooser filechooser=new FileChooser();
            filechooser.setTitle("Export Image");
            filechooser.setInitialFileName(DiagramEdit.getSelectionModel().getSelectedItem().getText());
            filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG",".png"));
            
            filechooser.initialFileNameProperty().addListener((observable, oldValue, newValue)->{
                extension=".png";
            });
                    
            File file=filechooser.showSaveDialog(mainscene.getWindow());
            
            
            if (file!=null){
                file=new File(file.getAbsolutePath()+extension);
                try{
                    WritableImage image=getSelectdPane().snapshot(new SnapshotParameters(), null);
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                }catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
                
            }
            extension="";
        });
        m.getItems().add(newbtn);
        // Edit button
        m=mb.getMenus().get(1);
        newbtn=new MenuItem("Undo  Ctrl+Z");
        newbtn.setDisable(true);
        newbtn.setOnAction(e->{
            invoker.undo();
        });
        
        m.getItems().add(newbtn);
        newbtn=new MenuItem("Redo  Ctrl+B");
        newbtn.setDisable(true);
        newbtn.setOnAction(e->{
            invoker.redo();
        });
        m.getItems().add(newbtn);
        
        newbtn=new MenuItem("Delete  Delete");
        newbtn.setOnAction(e->{
            if (selectmodel.getSelectedItem() !=null){
                    if (selectmodel.getSelectedItem() instanceof State){
                        Command delete=new DeleteStateCommand(selectmodel.getSelectedItem());
                        invoker.addcommand(delete);
                        invoker.Execute();
                    }else{
                        Command delete=new DeleteTransCommand(selectmodel.getSelectedItem());
                        invoker.addcommand(delete);
                        invoker.Execute();
                    }
            }
        });
        
        m.getItems().add(newbtn);
        m=mb.getMenus().get(2);
        //Help button
        newbtn=new MenuItem("Help");
        newbtn.setOnAction(e->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Help");
            alert.setContentText("Object-Oriented Software Engineering	\nTeamWork 2016/06/04 \nGroup 3\n\nDesign by\n     Dylan  Jimmy  Solomon  Heidi  Ray  Ashton");
            alert.show();
        });
        m.getItems().add(newbtn);
    }

    private void setkeyevent(){
        mainscene.setOnKeyPressed(e->{
            
            if (e.getCode()==KeyCode.DELETE){
                System.out.println("delete");
                if (selectmodel.getSelectedItem() !=null){
                    if (selectmodel.getSelectedItem() instanceof State){
                        Command delete=new DeleteStateCommand(selectmodel.getSelectedItem());
                        invoker.addcommand(delete);
                        invoker.Execute();
                    }else{
                        Command delete=new DeleteTransCommand(selectmodel.getSelectedItem());
                        invoker.addcommand(delete);
                        invoker.Execute();
                    }
                }
            }
            if ((e.getCode()==KeyCode.Z) && e.isControlDown()){
                System.out.println("undo");
                invoker.undo();
            }
            if ((e.getCode()==KeyCode.B) && e.isControlDown()){
                System.out.println("redo");
                invoker.redo();
            }
            if ((e.getCode()==KeyCode.N) && e.isControlDown() ){
                ((MenuBar)mainpage.getTop()).getMenus().get(0).getItems().get(0).fire();
            }
            if ((e.getCode()==KeyCode.O) && e.isControlDown() ){
                ((MenuBar)mainpage.getTop()).getMenus().get(0).getItems().get(1).fire();
            }
            if ((e.getCode()==KeyCode.S) && e.isControlDown() && e.isShiftDown()){
                ((MenuBar)mainpage.getTop()).getMenus().get(0).getItems().get(3).fire();
            }else if ((e.getCode()==KeyCode.S) && e.isControlDown()){
                 ((MenuBar)mainpage.getTop()).getMenus().get(0).getItems().get(2).fire();
            }

            
        });
  
    }
    private void setpanedragevent(){

        
        getSelectdPane().setOnDragOver(e->{
            e.acceptTransferModes(TransferMode.ANY);
            if(bufferstate!=null){bufferstate=null;};
            if (bufferstate == null){
                buffertrans.setendpoint(e.getSceneX()-ErrorPosition.X,e.getSceneY()-ErrorPosition.Y);
                buffertrans.resetturnpoint();
            }
            
            e.consume();
        });
        
        getSelectdPane().setOnDragDropped(e->{

            statediagram.get(DiagramEdit.getSelectionModel().getSelectedIndex()).remove(buffertrans);
            if (buffertrans.getEnterstate()!=null){ buffertrans.getEnterstate().removeTrans(buffertrans,"start");}
            if (buffertrans.getEndState()!=null){ buffertrans.getEndState().removeTrans(buffertrans, "end");}
            Group dc=buffertrans.draw();
            Group g=(Group)getSelectdPane().getChildren().get(0);
            g.getChildren().remove(dc);
            buffertrans=null;
            bufferstate=null;
            removepanedragevent();
        });
        
    }
    private void removepanedragevent(){
        getSelectdPane().removeEventHandler(DragEvent.DRAG_OVER, getSelectdPane().getOnDragOver());
        getSelectdPane().removeEventHandler(DragEvent.DRAG_DROPPED, getSelectdPane().getOnDragDropped());
    }
    public BorderPane getmainpage(){
        return mainpage;
    }
    public TabPane getTabPane(){
        return DiagramEdit;
    }
    public Pane getSelectdPane(){
        Tab selectedtab=DiagramEdit.getSelectionModel().getSelectedItem();
        return (Pane)selectedtab.getContent();
    }
    private Command bufferdragstate;
    public void setState(State s){
        
        statediagram.get(DiagramEdit.getSelectionModel().getSelectedIndex()).add(s);
        setStateEvent(s);
        Command newstate=new NewStateCommand(s);
        invoker.addcommand(newstate);
        invoker.Execute();

    }
    public void setStateEvent(State s){
        Group gc=s.draw();
        Pane c=(Pane)gc.getChildren().get(0);
  
        gc.setOnMouseReleased(e->{
            if (bufferdragstate!=null){
                invoker.addcommand(bufferdragstate);
                invoker.Execute();
                bufferdragstate=null;
            }
            
        });
        gc.setOnMouseMoved(e->{
            s.pointmove(e);
            if (!checkTransbtnstate()){
                ((Node) e.getSource()).setCursor(Cursor.HAND);
            }else {
                ((Node) e.getSource()).setCursor(Cursor.CROSSHAIR);
            }
        });
        gc.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
            
            if (checkTransbtnstate()){
                e.setDragDetect(true);
            }else{
                if (e.isPrimaryButtonDown()){
                    if (bufferdragstate==null){
                        Command ds=new StateDraggedCommand(s);
                        
                        bufferdragstate=ds;
                    }
                }
            }
        });
        
        gc.setOnDragDropped(e->{
            
            if (buffertrans.getEnterstate().draw() != ((Group)e.getSource())){
                if (bufferstate !=null){
                    buffertrans.setEndstate(bufferstate, "end");
                    System.out.println("state dropped");
                    buffertrans=null;
                    bufferstate=null;
                }
            }else{
                statediagram.remove(buffertrans);
                Group dc=buffertrans.draw();
                Group g=(Group)getSelectdPane().getChildren().get(0);
                g.getChildren().remove(dc);
                
                buffertrans=null;
                bufferstate=null;
            }
            removepanedragevent();
            e.consume();
        });

        gc.setOnDragDetected(e->{
            //c.setCursor(Cursor.CROSSHAIR);
            if(toolController.getTransitionbtn().isDrawing()){
                if (e.isPrimaryButtonDown()){
                    e.setDragDetect(true);
                    buffertrans=toolController.DrawTransition();

                    Dragboard db =c.startDragAndDrop(TransferMode.ANY);

                    if (!db.hasString()){
                        ClipboardContent content = new ClipboardContent();
                        content.putString("Transition");
                        db.setContent(content);
                    }


                    double X=(s.getpointX());
                    double Y=(s.getpointY());

                    buffertrans.setendpoint(e.getSceneX()-ErrorPosition.X,e.getSceneY()-ErrorPosition.Y);
                    buffertrans.setstartpoint(X, Y);
                    buffertrans.setEnterstate(s, "start");
                    buffertrans.resetturnpoint();
                    setTransition(buffertrans);
                    setpanedragevent();
                }
            }
            e.consume();
        });
        gc.setOnDragEntered(e->{
            
            s.pointmove(e);
            double X=s.getpointX();
            double Y=s.getpointY();
            bufferstate=null;
            if (buffertrans.getEnterstate().draw() != ((Group)e.getSource())){
                bufferstate=s;
                buffertrans.setendpoint(X, Y);
            }else{
                buffertrans.setendpoint(e.getSceneX()-ErrorPosition.X, e.getSceneY()-ErrorPosition.Y);
            }
            buffertrans.resetturnpoint();
            e.consume();
        });
        gc.setOnDragOver(e->{
            
            s.pointmove(e);
            double X=s.getpointX();
            double Y=s.getpointY();
            bufferstate=null;
            if (buffertrans.getEnterstate().draw() != ((Group)e.getSource())){
                bufferstate=s;
                buffertrans.setendpoint(X, Y);
            }else{
                buffertrans.setendpoint(e.getSceneX()-ErrorPosition.X, e.getSceneY()-ErrorPosition.Y);
            }
            buffertrans.resetturnpoint();
            e.acceptTransferModes(TransferMode.ANY);
            e.consume();
        });
        gc.setOnMouseClicked(e->{
            selectmodel.select(s);
        });
        s.getTextfield().textProperty().addListener((observable, oldValue, newValue)->{
            
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            Command changetext=new ChangeTextCommand(s);
            invoker.addcommand(changetext);
            invoker.Execute();
        });

        s.setEvent(toolController.getTransitionbtn());
    }

    public void setTransition(Transition t){
        
        setTransEvent(t);
        Command newtrans=new NewTransCommand(t);
        invoker.addcommand(newtrans);
        invoker.Execute();
        
        
    }
    public void setTransEvent(Transition t){
        Group c=t.draw();
        t.getTextfield().setOnMouseClicked(e->{
            selectmodel.select(t);
        });
 
        t.getTextfield().textProperty().addListener((observable, oldValue, newValue)->{

            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            Command changetext=new ChangeTextCommand(t);
            invoker.addcommand(changetext);
            invoker.Execute();
        });
        c.setOnMouseClicked(e->{
            
            selectmodel.select(t);
        });
        t.setpointevent(selectmodel,invoker);
    }
    public StateDiagram getstateDiagram(){
        return statediagram.get(DiagramEdit.getSelectionModel().getSelectedIndex());
    }
}
