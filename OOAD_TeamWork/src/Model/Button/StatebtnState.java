/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Button;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author 正文
 */
public abstract class StatebtnState {
    public abstract void change(Statebtn b);
    public abstract void changecursor(Pane mainpage);
}
