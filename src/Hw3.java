/*
 *       Filename:  BorderLayoutDemo.java
 *
 *    Description:  14.42 - Testing BorderLayoutFrame
 *
 *        Created:  20/12/15 16:24:23
 *       Revision:  none
 *
 *        @Author:  Siidney Watson - siidney.watson.work@gmail.com
 *       @Version:  1.0
 *
 * =====================================================================================
 */
import javax.swing.JFrame;

public class Hw3{
    public static void main(String[] args){
        FileBrowserFrame borderLayoutFrame = new FileBrowserFrame();
        borderLayoutFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        borderLayoutFrame.pack();
        borderLayoutFrame.setVisible(true);
    }
}
