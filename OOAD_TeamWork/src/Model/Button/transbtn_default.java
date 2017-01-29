/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Button;


import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

/**
 *
 * @author 正文
 */
public class transbtn_default extends TransbtnState {

    private static transbtn_default defaultstate=new transbtn_default();
    public static transbtn_default getInstance(){
        return defaultstate;
    }
    @Override
    public void change(Transbtn b) {
        b.setstate(transbtn_drawing.getInstance());
    }

    @Override
    public void changecursor(Pane mainpage) {
        mainpage.setCursor(Cursor.DEFAULT);
    }
    
}
