/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import controller.MainPanelController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.Group;
import javafx.stage.FileChooser;

/**
 *
 * @author 正文
 */
public class FileProxy implements Proxy{
    private MainPanelController maincontroller;
    private ArrayList<LocalFile> localfiles=new ArrayList<>();
    private boolean modified=false;
    private String extension="";
    public FileProxy(MainPanelController controller){
        maincontroller=controller;
    }
    public void openfile(File file){
        if (file!=null){
            String filename=file.getName();
            try{
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objInputStream = new ObjectInputStream(fileInputStream); 

                maincontroller.getTabPane().getSelectionModel().getSelectedItem().setText(filename.substring(0, filename.length()-4));
                StateDiagram opensd=new StateDiagram();
                DiagramElement d;
                while(fileInputStream.available() > 0) {

                    d=(DiagramElement)objInputStream.readObject();
                        if (d instanceof State){
                            maincontroller.setStateEvent((State)d);

                        }else if(d instanceof Transition ){
                            maincontroller.setTransEvent((Transition) d);

                        }
                        maincontroller.getstateDiagram().add(d);
                        Group g=(Group)maincontroller.getSelectdPane().getChildren().get(0);
                        g.getChildren().add(d.draw());

                }
                objInputStream.close(); 
            }catch(ClassNotFoundException ex) { 
                ex.printStackTrace(); 

            }catch(IOException ex) { 
                ex.printStackTrace(); 
            }
        }
        LocalFile lf=new LocalFile();
        lf.setfile(file);  
        localfiles.add(lf);
    }
    
    @Override
    public void savefile(StateDiagram sd) {
        int index=maincontroller.getTabPane().getSelectionModel().getSelectedIndex();

        if (!isSave()){
            localfiles.get(index).savefile(sd);
            System.out.println("Save opening statediagram :" +index);
        }else{

            FileChooser filechooser=new FileChooser();
            filechooser.setTitle("Save as");
            filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("StateDiagram",".sde"));
            filechooser.setInitialFileName(maincontroller.getTabPane().getSelectionModel().getSelectedItem().getText());
            filechooser.initialFileNameProperty().addListener((observable, oldValue, newValue)->{
                extension=".sde";
            });
            File f=filechooser.showSaveDialog(maincontroller.getmainpage().getScene().getWindow());
            if (f!=null){
                f=new File(f.getAbsolutePath()+extension);
                LocalFile lf=localfiles.get(index);
                lf.setfile(f);
                lf.savefile(sd);
                String filename=f.getName();
                maincontroller.getTabPane().getSelectionModel().getSelectedItem().setText(filename.substring(0, filename.length()-4));
                System.out.println("Save as new file : "+f.getAbsolutePath());
            }
        }
        extension="";

    }
    

    public boolean isOpen(File f){
        boolean o=false;
        int index=0;
        Iterator<LocalFile> itr=localfiles.iterator();
        while (itr.hasNext()){
            LocalFile lfs=itr.next();
            o=lfs.isOpen(f);
            if (o==true){
                System.out.println("File is open");
                maincontroller.getTabPane().getSelectionModel().select(index);
                break;
            }
            index++;
        }
        return o;

    }
    public void closefile(){
        int index=maincontroller.getTabPane().getSelectionModel().getSelectedIndex();
        
        localfiles.remove(index);
        System.out.println("Close file : "+index+" loacalfile number: "+localfiles.size());
    }
    public boolean isSave(){
        return localfiles.get(maincontroller.getTabPane().getSelectionModel().getSelectedIndex()).isSave();
    }
    public void SaveAsNew(StateDiagram sd){
        localfiles.get(maincontroller.getTabPane().getSelectionModel().getSelectedIndex()).setfile(null);
        savefile(sd);
    }
}
