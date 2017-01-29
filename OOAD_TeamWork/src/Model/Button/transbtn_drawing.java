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
public class transbtn_drawing extends TransbtnState {

    private static transbtn_drawing drawingstate=new transbtn_drawing();
    public static transbtn_drawing getInstance(){
        return drawingstate;
    }
    @Override
    public void change(Transbtn b) {
        b.setstate(transbtn_default.getInstance());
    }

    @Override
    public void changecursor(Pane mainpage) {
        mainpage.setCursor(Cursor.CROSSHAIR);
    }
    
}
