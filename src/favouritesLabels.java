/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author arc68
 */
public class favouritesLabels extends JLabel{
    File[] files;
    String path,name;
    int clicked;
    
    public favouritesLabels(String path,String name){
        super(name);
        this.path = path;
        this.name = name;
        File f = new File(path);
        files = f.listFiles();
        clicked = 0;
    }
}
