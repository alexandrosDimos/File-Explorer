/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 *
 * @author arc68
 */
public class ContainerJLabel extends JLabel{
     String name;
     File file;
     ImageIcon image = null;
     int clicked;
     String type;
     String FileSeparator;
    
    public ContainerJLabel(String name,File file){
        super(name);
        super.setHorizontalTextPosition(SwingConstants.CENTER);
        super.setVerticalTextPosition(SwingConstants.BOTTOM);
        FileSeparator = System.getProperty("file.separator");
        this.file = file;
        this.name = name;
        clicked = 0;
       
        image = null;
        /*File img = new File("."+FileSeparator+"icons");*/
        File img = new File("src\\icons");
        File[] list = img.listFiles();

        String iconPath = null ;
        if(file.isDirectory()){
            type = "folder";
            iconPath = type+".png";
        }
        else{
            type = getFileExtension(this.name);
            for(int i = 0; i < list.length;i++){
                String temp = type+".png";
                if(temp.equals(list[i].getName())){
                    iconPath = temp;
                    break;
                } else {
                }   
            }
            if(iconPath == null){
                iconPath = "question.png";
            }
        }

       image = new ImageIcon("src\\icons\\"+iconPath);
        if(image == null){
            image = new ImageIcon("."+FileSeparator+"question.png");
        }
       this.setAlignmentY(TOP_ALIGNMENT);
        this.setIcon(image);
    }
    
    public static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
