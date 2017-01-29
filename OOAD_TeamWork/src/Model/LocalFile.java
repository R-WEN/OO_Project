/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author 正文
 */
public class LocalFile implements Proxy {
    private File file=null;
    
    public void setfile(File f){
        file=f;
    }
    @Override
    public void savefile(StateDiagram sd) {
        if (file!=null){
            BufferedWriter writer=null;
            
            try{    
                FileOutputStream fs = new FileOutputStream(file);
                ObjectOutputStream os =  new ObjectOutputStream(fs);
                //System.out.println(DiagramEdit.getSelectionModel().getSelectedIndex());
                sd.getAlldiagram().forEach(de->{
                    try{
                        if (de instanceof Transition){
                            if ((((Transition)de).getEnterstate()!=null) && (((Transition)de).getEndState()!=null)){
                                os.writeObject(de);
                            }
                        }else{
                            os.writeObject(de);
                        }
                        
                    }catch(IOException we){
                        we.printStackTrace(); 
                    }

                });
                fs.flush();

                os.flush();
                os.close();
            }catch(Exception ex){ 
                ex.printStackTrace();    
            } 
            System.out.println("Save file : "+file.getAbsolutePath());
        }
    }
    public boolean isOpen(File f){
        boolean o=false;
        if (file!=null){
            o=file.getAbsolutePath().equals(f.getAbsolutePath());
        }
        return o;
    }

    @Override
    public void openfile(File f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void closefile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSave() {
        return file==null;
    }

    @Override
    public void SaveAsNew(StateDiagram sd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
