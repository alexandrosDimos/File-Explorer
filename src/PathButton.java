/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Component;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;

/**
 *
 * @author arc68
 */
public class PathButton extends JButton{
    File[] files;
    String path,name;
    
    public PathButton(String name,String path){
        super(name);
        this.path = path;
        this.name = name;
        File f = new File(path);
        files = f.listFiles();
    }
}
