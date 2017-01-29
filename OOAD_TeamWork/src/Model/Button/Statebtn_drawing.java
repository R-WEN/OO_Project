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
public class Statebtn_drawing extends StatebtnState {

    private static Statebtn_drawing drawingstate=new Statebtn_drawing();
    public static Statebtn_drawing getInstance(){
        return drawingstate;
    }
    @Override
    public void change(Statebtn b) {
        b.setstate(statebtn_default.getInstance());
    }

    @Override
    public void changecursor(Pane mainpage) {
        mainpage.setCursor(Cursor.CROSSHAIR);
    }
    
}
