/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javafx.scene.Group;
import javafx.scene.control.TextField;


/**
 *
 * @author 正文
 */
public interface DiagramElement extends Serializable{
    static final long serialVersionUID = 100L;
    public Group draw();
    public void select();
    public void unselected();
    public TextField getTextfield();
}
