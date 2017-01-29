/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Button;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class Statebtn {
    private Button statebtn;
    private StatebtnState state;
    public Statebtn(Button btn,StatebtnState defaultstate){
        statebtn=btn;
        state=defaultstate;
    }
    public void setstate(StatebtnState state){
        this.state=state;
    }
    public void changecursor(Pane mainpage){
        state.changecursor(mainpage);
    }
    public void change(){
        state.change(this);
    }
    
    public Button getButton(){
        return statebtn;
    }
    public void inithisButton(){
        state=statebtn_default.getInstance();
    }
}
