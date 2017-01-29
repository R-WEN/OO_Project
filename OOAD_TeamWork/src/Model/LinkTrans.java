/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author 正文
 */
public class LinkTrans implements Phototype,Serializable {
    private static final long serialVersionUID = 3L;
    private Transition trans;
    private String linktype;
    private double X;
    private double Y;
    public LinkTrans(Transition t,String s,double X,double Y){
        trans=t;
        linktype=s;
        this.X=X;
        this.Y=Y;
    }

    public Transition getTrans(){
        return trans;
    }
    public String getType(){
        return linktype;
    }
    public void setX(double X){
        this.X=X;
    }
    public double getX(){
        return X;
    }
    public void setY(double Y){
        this.Y=Y;
    }
    public double getY(){
        return Y;
    }
    public Object clone(){
        String lt=linktype;
        double x=X;
        double y=Y;
        return new LinkTrans(trans,lt,x,y);
    }
}
