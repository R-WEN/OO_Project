/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Button;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 *
 * @author 正文
 */
public class Transbtn{
    private Button transbtn;
    private TransbtnState state;
    public Transbtn(Button btn,TransbtnState defaultstate){
        transbtn=btn;
        state=defaultstate;
    }
    public void setstate(TransbtnState state){
        this.state=state;
    }
    public void changecursor(Pane mainpage){
        state.changecursor(mainpage);
    }
    public void change(){
        state.change(this);
    }
    
    public Button getButton(){
        return transbtn;
    }
    public boolean isDrawing(){
        return (state == transbtn_drawing.getInstance()); 
    }
    public void inithisButton(){
        state=transbtn_default.getInstance();
    }
}
