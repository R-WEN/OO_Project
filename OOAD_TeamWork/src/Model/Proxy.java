/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;

/**
 *
 * @author 正文
 */
public interface Proxy {
    public void openfile(File f);
    public void savefile(StateDiagram sd);
    public void SaveAsNew(StateDiagram sd);
    public boolean isOpen(File f);
    public boolean isSave();
    public void closefile();
    
}
