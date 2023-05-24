/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.JButton;

/**
 *
 * @author arc68
 */
public class ContainerButton extends JButton{
    String name;
    boolean clicked;
    
    public ContainerButton(String name){
        super(name);
        clicked = false;
        this.name = name;
    }
    
}
