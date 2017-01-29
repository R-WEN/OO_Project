/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author 正文
 */
public class SingleSelected {
    private DiagramElement item=null;
    public void select(DiagramElement e){
        if (item!=null){
            unselect();
        }
        item=e;
        e.select();
    }
    public void unselect(){
        if (item!=null){
            item.unselected();
        }
        
        item=null;
    }
    public DiagramElement getSelectedItem(){
        return item;
    }
}
