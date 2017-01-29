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
public class statebtn_default extends StatebtnState {

    private static statebtn_default defaultstate=new statebtn_default();
    public static statebtn_default getInstance(){
        return defaultstate;
    }
    @Override
    public void change(Statebtn b) {
        b.setstate(Statebtn_drawing.getInstance());
    }

    @Override
    public void changecursor(Pane mainpage) {
        mainpage.setCursor(Cursor.DEFAULT);
    }
    
}
