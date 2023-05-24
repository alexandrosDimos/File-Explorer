/*
 *       Filename:  BorderLayoutFrame.java
 *
 *    Description:  14.41 - Demonstrating BorderLayout
 *
 *        Created:  20/12/15 16:18:04
 *       Revision:  none
 *
 *        @Author:  Siidney Watson - siidney.watson.work@gmail.com
 *       @Version:  1.0
 *
 * =====================================================================================
 */
 import java.awt.event.MouseListener; 
import java.awt.event.MouseEvent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.stream.XMLStreamException;

public class FileBrowserFrame extends JFrame{
    JPanel centerPanel,northPanel,subCenterPanel, favouritesPanel, searchPathContainer, path, container, search, cont, top,favouritesTop;
    private static final String[] fileOptions = {"new Window","Exit"},editOptions={""};
    private BorderLayout layout;
    static JToolBar toolBar;
    private JLabel pathDiv;
    private JButton File,Edit,Search,Back,newWindow,Exit, Cut ,Copy,Paste,Rename,Delete,addToFavourites,properties,Back2,searchInPath;
    File fileToCopy,fileToCut;
    JCheckBox permissions[];
    JList searchList;
    JScrollPane list,scroller;
    JPopupMenu filePopup, editPopup;
    JTextField searchBar;
    JFrame propertiesFrame = null;
    PathButton[] pathButton;
    ContainerButton[] containerButtons; 
    ContainerJLabel[] cotainerLabels;
    pathButtonHandler handler;
    String[] finResults;
    boolean searchRemoved = true;
    EditListener editHandler;
    List<favouritesLabels> favouritesButtons = new ArrayList<>();
    int srcDir;
    favouritesButtonHandler favouritesHandler = new favouritesButtonHandler();
    MouseHandler contMouseHandler = new MouseHandler();
    Favourites XMLController;
    boolean favouritesActive = false;
    String FileSeparator ; 
    
    public FileBrowserFrame(){
        super("My Filebrowser");
        
        centerPanel = new JPanel();
        subCenterPanel = new JPanel();
        northPanel = new JPanel();
        searchPathContainer = new JPanel();
        path = new JPanel();
        container = new JPanel();
        search = new JPanel();
        cont = new JPanel();
        favouritesPanel = new JPanel();
        toolBar = new JToolBar();
        pathDiv = new JLabel(">");
        File = new JButton("File");
        Edit = new JButton("Edit");
        Search =  new JButton("Search");
        searchInPath = new JButton("Search");
        searchBar = new JTextField(30);
        FlowLayout pathLayout = new FlowLayout();
        listListener searchResultsListener = new listListener(); 
        
        
        Back = new JButton("Back");
        newWindow = new JButton("New Window");
        Exit = new JButton("Exit");
        filePopup = new  JPopupMenu();
        filePopup.setLayout(new GridLayout(3,1));
        filePopup.add(newWindow);
        filePopup.add(Exit);
        filePopup.add(Back);
        
        newWindow.addActionListener((new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                Paste.setEnabled(false);
                String homePath = System.getProperty("user.home");
                FileSeparator = System.getProperty("file.separator");
                File homefavourites = new File(homePath);
                File favsCheck = new File(homePath+FileSeparator+".java-file-browser");
                if(favouritesActive == false){
                    if(favsCheck.exists()){
                        XMLController = new Favourites(homePath+FileSeparator+".java-file-browser");
                        updateFavourites(XMLController);
                    }
                    else{
                        XMLController = new Favourites(homePath+FileSeparator+".java-file-browser");
                        favouritesButtons.add(new favouritesLabels(homePath,homefavourites.getName()));
                        favouritesButtons.get(0).addMouseListener(favouritesHandler);
                        favouritesPanel.add(favouritesButtons.get(0));
                    }
                    favouritesActive = true;
                }
                homePathButtons(homePath);
                File f = new File(homePath);
                File[] files = f.listFiles();
                handler = new pathButtonHandler();
                homePathButtons(homePath);
                pathLayout.setAlignment(FlowLayout.LEFT);
                path.removeAll();
                path.setLayout(pathLayout);
                for(int i = 0; i < pathButton.length;i++){
                    path.add(pathButton[i]);
                }
                pathButton[pathButton.length-1].doClick();
                filePopup.setVisible(false);
                path.setVisible(false);
                path.setVisible(true);

            } 
        }));
        Back.addActionListener((new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                filePopup.setVisible(false);
            } 
        }));
        Exit.addActionListener((new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                System.exit(0);
            } 
        }));
        
        editHandler = new EditListener();
        Cut = new JButton("Cut");
        Cut.addActionListener(editHandler);
        Copy = new JButton("Copy");
        Copy.addActionListener(editHandler);
        Paste = new JButton("Paste");
        Paste.addActionListener(editHandler);
        Rename = new JButton("Rename");
        Rename.addActionListener(editHandler);
        Delete = new JButton("Delete");
        Delete.addActionListener(editHandler);
        addToFavourites = new JButton("Add To Favourites");
        addToFavourites.addActionListener(editHandler);
        properties = new JButton("Properties");
        properties.addActionListener(editHandler);
        Back2 = new JButton("Back");
        editPopup = new  JPopupMenu();
        editPopup.setLayout(new GridLayout(8,1));
        editPopup.add(Cut);
        editPopup.add(Copy);
        editPopup.add(Paste);
        editPopup.add(Rename);
        editPopup.add(Delete);
        editPopup.add(addToFavourites);
        editPopup.add(properties);
        editPopup.add(Back2);
        
        Back2.addActionListener((new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                  editPopup.setVisible(false);
                  Cut.setEnabled(true);
                  Rename.setEnabled(true);
                  Copy.setEnabled(true);
                  properties.setEnabled(true);
                  addToFavourites.setEnabled(true);
            } 
        }));
        
        layout = new BorderLayout(5, 5);
        setLayout(layout);
        centerPanel.setLayout(new BorderLayout(2,2));
        centerPanel.setPreferredSize(new Dimension(1280, 720));
        
        toolBar.add(File);
        toolBar.add(Edit);
        toolBar.add(Search);
        
        File.addActionListener((new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                filePopup.setVisible(true);
            } 
        }));
        Edit.addActionListener((new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                editPopup.setVisible(true);
            } 
        }));
        Search.addActionListener((new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                if(search.isVisible() && searchRemoved == false){
                   BorderLayout layout = (BorderLayout)search.getLayout();
                    search.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
                    searchRemoved = true;
                }
                if(search.isVisible()){
                    search.setVisible(false);
                }
                else{
                    search.setVisible(true);
                }
            } 
        }));
        
        
        favouritesPanel.setPreferredSize(new Dimension(200,100));
        favouritesPanel.setLayout(new BoxLayout(favouritesPanel,BoxLayout.Y_AXIS));
        
        
        centerPanel.add(favouritesPanel, BorderLayout.WEST);
        centerPanel.add(searchPathContainer, BorderLayout.CENTER);
        
        searchPathContainer.setLayout(new BorderLayout(3, 3));
        searchPathContainer.add(search,BorderLayout.NORTH);
        searchPathContainer.add(container,BorderLayout.CENTER);

        container.setLayout(new BorderLayout(3,3));
        search.setLayout(new BorderLayout(3,3));
        search.add(searchBar,BorderLayout.CENTER);
        search.add(searchInPath,BorderLayout.EAST);
        searchInPath.addActionListener((new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
               String txt = searchBar.getText();
               System.out.println("Text in searchbar "+txt);
                if(search.isVisible() && searchRemoved == false){
                   BorderLayout layout = (BorderLayout)search.getLayout();
                    search.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
                    searchRemoved = true;
                }
               searchRemoved = false;
               SearchFiles(txt);
               searchList = new JList(finResults);
               searchList.addListSelectionListener(searchResultsListener);
               searchList.setVisibleRowCount(5);
               searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
               list = new JScrollPane(searchList);
               list.add(searchList);
               search.add(new JScrollPane(searchList),BorderLayout.SOUTH);
               search.revalidate();
            } 
        }));
        search.setVisible(false);
        
        container.add(path,BorderLayout.NORTH);
        container.add(cont,BorderLayout.CENTER);
        
        add(toolBar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
               
    }
    
    public class pathButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event) {
            JPanel pathTop = new JPanel();
            int numberOfIcons, panelHeight,i;
            
            if(search.isVisible() && searchRemoved == false){
                BorderLayout layout = (BorderLayout)search.getLayout();
                search.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
                searchRemoved = true;
            }
            top = new JPanel();
            top.addMouseListener(contMouseHandler);
            for(PathButton pbutton:pathButton){
                cont.removeAll();
                cont.setLayout(new BorderLayout());
                FlowLayout topLayout = new FlowLayout();
                topLayout.setAlignment(FlowLayout.LEFT);
                top.setLayout(topLayout);
                
                if(event.getSource() == pbutton){
                    File pbuttonDir = new File(pbutton.path);
                    File[] list = pbuttonDir.listFiles();
                    pbutton.files = list;
                    for(int k = 0;k < pathButton.length;k++){
                        if(pbutton.path == null ? pathButton[k].path == null : pbutton.path.equals(pathButton[k].path)){
                            PathButton[] tempArr = new PathButton[k+1];
                            System.arraycopy(pathButton,0,tempArr,0,k+1);
                            pathButton = new PathButton[k+1];
                            System.arraycopy(tempArr,0,pathButton,0,k+1);
                        }
                    }
                    
                    path.removeAll();
                    path.setLayout(new BorderLayout());
                    path.add(pathTop,BorderLayout.CENTER);
                    FlowLayout pathTopLayout = new FlowLayout();
                    pathTop.setLayout(pathTopLayout);
                    pathTopLayout.setAlignment(FlowLayout.LEFT);
                    pathTop.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    for(int j = 0;j < pathButton.length;j++){
                        pathTop.add(pathButton[j]);
                    }
                    cotainerLabels = null;
                    cotainerLabels = new ContainerJLabel[pbutton.files.length];
                    numberOfIcons = cotainerLabels.length;
                    panelHeight = (numberOfIcons/2)*100;
                    top.setPreferredSize(new Dimension(100,panelHeight));
                    
                    for(i = 0;i < pbutton.files.length;i++){
                      
                        
                        ContainerJLabel tempLabel = new ContainerJLabel(pbutton.files[i].getName(),pbutton.files[i]);
                        top.add(tempLabel);
                        tempLabel.addMouseListener(contMouseHandler);
                        tempLabel.setBackground(Color.green);
                        cotainerLabels[i] = tempLabel;
                        
                    }
                }
                scroller = new JScrollPane(top);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                cont.add(scroller,BorderLayout.CENTER);
                cont.revalidate();
                cont.repaint();
            }
        }
    }
    
    public class MouseHandler implements MouseListener{
        int mouseClicked = 0;
        
        @Override
        public void mouseClicked(MouseEvent me) {
            
            if(SwingUtilities.isLeftMouseButton(me)){
            if(top == me.getSource()){
                for(ContainerJLabel cLabel:cotainerLabels){
                    cLabel.setBorder(null);
                    cLabel.clicked = 0;
                }
                if(editPopup.isVisible()){
                    editPopup.setVisible(false);
                }
            }
            for(ContainerJLabel cLabel:cotainerLabels){
                if(me.getSource() == cLabel){
                    if(editPopup.isVisible()){
                        editPopup.setVisible(false);
                    }
                    cLabel.clicked++;
                    if(cLabel.clicked>1){
                        String name = cLabel.name;
                        System.out.println(name);
                        String pathSoFar  = pathButton[pathButton.length-1].path;
                        String newPath = pathSoFar.concat(FileSeparator);
                        String concat = newPath.concat(name);
                        System.out.println(concat);

                        if(cLabel.file.isDirectory()){
                            List<PathButton> list = new ArrayList<>( Arrays.asList(pathButton)); 

                         list.add(pathButton.length, new PathButton(name,concat)); 

                         pathButton = list.toArray(pathButton);
                         path.add(pathButton[pathButton.length-1]);
                         pathButton[pathButton.length-1].addActionListener(handler);
                         pathButton[pathButton.length-1].doClick();
                         if(search.isVisible() && searchRemoved == false){
                            BorderLayout layout = (BorderLayout)search.getLayout();
                            search.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
                            searchRemoved = true;
                         }
                         path.setVisible(false);
                         path.setVisible(true);
                        }
                        else{
                            try {
                        
                                if (Desktop.isDesktopSupported()) {
                                    Desktop.getDesktop().open(cLabel.file);
                                }
                                else{
                                    System.out.println("not supported");
                                }
                             } catch (Exception ex) {
                                 System.out.println("Cannot open with a desktop program");
                             }
                        }
                        cLabel.clicked = 0;
                    }
                    Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
                    cLabel.setBorder(border); 
                
                }
                else{
                    cLabel.setBorder(null);
                    cLabel.clicked = 0;
                }    
            }
          
            }
            else if(SwingUtilities.isRightMouseButton(me)){
                for(ContainerJLabel cLabel:cotainerLabels){
                    if(me.getSource() == cLabel){
                        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
                        cLabel.setBorder(border);
                        cLabel.clicked++;
                        editPopup.setVisible(true);
                    }
                    else{
                        cLabel.setBorder(null);
                        cLabel.clicked = 0;
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent me){
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            for(ContainerJLabel cLabel:cotainerLabels){
                if(me.getSource() == cLabel){
                    if(cLabel.clicked == 0){
                        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
                        cLabel.setBorder(border);
                    }
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent me) {
            for(ContainerJLabel cLabel:cotainerLabels){
                if(me.getSource() == cLabel){
                    if(cLabel.clicked==0){
                        cLabel.setBorder(null); 
                    }           
                }
            }
        }
        
    }
    
    public class listListener implements ListSelectionListener{
        String choice;
        @Override
        public void valueChanged(ListSelectionEvent lse) {
            if(!lse.getValueIsAdjusting()){
                
                String file = searchList.getSelectedValue().toString();
                File newFile = new File(file);
                if(newFile.isDirectory()){
                    List<PathButton> list = new ArrayList<>( Arrays.asList(pathButton)); 
                    String fileName = newFile.getName();
                    list.add(pathButton.length, new PathButton(fileName,file)); 

                    pathButton = list.toArray(pathButton);
                    path.add(pathButton[pathButton.length-1]);
                    pathButton[pathButton.length-1].addActionListener(handler);
                    pathButton[pathButton.length-1].doClick();
                     if(search.isVisible() && searchRemoved == false){
                        BorderLayout layout = (BorderLayout)search.getLayout();
                        search.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
                        searchRemoved = true;
                    }
                    path.setVisible(false);
                    path.setVisible(true);
                }
                else{
                    try {
                      
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(newFile);
                        }
                        else{
                            System.out.println("not supported");
                        }

                        } catch (Exception ex) {
                            System.out.println("not supported");
                        }
                }
            }
        }
        
    }

    public void SearchFiles(String text){
        int space,typeInd ;
        String type,name;
        String[] searchResults = new String[1];
        
        space = text.indexOf(" ");
        typeInd = text.indexOf("type");
        if(typeInd != -1){
            type = text.substring(typeInd +5);
            if(type.equals("dir")){
                type = "folder";
            }
            name = text.substring(0, space);
        }
        else{
            if(space != -1){
              name = text.substring(0, space-1);  
            }
            else{
                name = text;
            }
            type = null;
        }
        for(int i = 0;i < cotainerLabels.length;i++){
            if(type != null){
                if(type.equalsIgnoreCase(cotainerLabels[i].type)){
                    String temp = cotainerLabels[i].name;
                    temp = temp.substring(0, name.length());
                    if(name.equalsIgnoreCase(temp)){
                         List<String> arrlist = new ArrayList<>( Arrays.asList(searchResults)); 
                         arrlist.add(cotainerLabels[i].file.toString());
                         searchResults = arrlist.toArray(searchResults);  
                         
                    }
                }
            }
            else{
                if(name.length() <= cotainerLabels[i].name.length()){
                 String temp = cotainerLabels[i].name;
                temp = temp.substring(0, name.length());
                if(name.equalsIgnoreCase(temp)){
                    List<String> arrlist = new ArrayList<>( Arrays.asList(searchResults)); 
                    arrlist.add(cotainerLabels[i].file.toString());
                    searchResults = arrlist.toArray(searchResults);   
                }
                }
            }
        }
            
            
         int len = searchResults.length -1;
        containerButtons = new ContainerButton[len];
        finResults = new String[len];
        for(int j = 1;j<searchResults.length;j++){
             ContainerButton tempCButton = new ContainerButton(searchResults[j]);
             containerButtons[j-1] = tempCButton;
             finResults[j-1] = searchResults[j];
        }
    }
    
    public void homePathButtons(String homepath){
        int index,count = 0, j = 0;
        String homePath = homepath, filePath = "",temp = null;
        
        
        index = homepath.indexOf(FileSeparator);
        
        char[] fileSep = FileSeparator.toCharArray();
        for (int i = 0; i < homepath.length(); i++) {
            if (homepath.charAt(i) == fileSep[0]) {
                count++;
            }
        }
        pathButton = new PathButton[count+1];
         while(index!=-1){
            
            temp = homePath.substring(0, index);
            
            homePath = homePath.substring(index+1,homePath.length());
            
            filePath = filePath.concat(temp);
            if(j == 0){
                pathButton[j] = new PathButton(temp,filePath+FileSeparator);
            }
            else{
                pathButton[j] = new PathButton(temp,filePath);
            }
            pathButton[j].addActionListener(handler);
            j++;
            index = homePath.indexOf(FileSeparator);
            filePath = filePath.concat(FileSeparator);
         }   
       
         filePath = filePath.concat(homePath);
         pathButton[j] = new PathButton(homePath,filePath);
         pathButton[j].addActionListener(handler);
        
    }
    
    public class EditListener implements ActionListener{
        File f;
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == Rename){
                String type;
                for(int i =0;i<cotainerLabels.length;i++){
                    if(cotainerLabels[i].clicked != 0){
                       type = cotainerLabels[i].type;
                       String filePath = cotainerLabels[i].file.toString();
                       int index = filePath.lastIndexOf(FileSeparator);
                        String path = filePath.substring(0, index);
                         path= path+FileSeparator;
                        String newName = JOptionPane.showInputDialog("Enter new name");
                        if(newName != null){
                            if(type.equals("folder")){
                                path= path+newName;
                            }
                            else{
                                path= path+newName+"."+type;
                            }
                            File dest = new File(path);
                            File tempFile = pathButton[pathButton.length-1].files[i];
                            if(tempFile.renameTo(dest)){
                                System.out.println("File renamed");
                            }
                            path = filePath.substring(0, index);
                            File f = new File(path);
                            File[] files = f.listFiles();
                            pathButton[pathButton.length-1].files = files;
                        }
                    }
                }
                editPopup.setVisible(false);
                pathButton[pathButton.length-1].doClick();
            }
            if(ae.getSource() == Delete){
                if(!properties.isEnabled()){
                     for(int j = 0; j < favouritesButtons.size();j++){
                         if(favouritesButtons.get(j).clicked != 0){
                             favouritesPanel.remove(favouritesButtons.get(j));
                             favouritesButtons.remove(j);
                             
                         }
                     }
                     try {
                        XMLController.createNewFavFile(favouritesButtons);
                    } catch (XMLStreamException ex) {
                        System.out.println("XMLStreamException thrown");
                    } catch (FileNotFoundException ex) {
                        System.out.println("FileNotFoundException thrown");
                    }
                    editPopup.setVisible(false);
                    Cut.setEnabled(true);
                    Rename.setEnabled(true);
                    Copy.setEnabled(true);
                    //Paste.setEnabled(true);
                    properties.setEnabled(true);
                    addToFavourites.setEnabled(true);
                    favouritesPanel.setVisible(false);
                    favouritesPanel.setVisible(true);
                }
                else{
                    for(int i =0;i<cotainerLabels.length;i++){
                        if(cotainerLabels[i].clicked != 0){
                            boolean finalConfirmation;
                           int confirmation = JOptionPane.showConfirmDialog(null, "Do you want to delete this file","Confirm delete", JOptionPane.OK_CANCEL_OPTION);
                           if(confirmation == 0){
                               File f =  cotainerLabels[i].file;
                               if(f.isDirectory()){
                                   if(deleteDirectory(f)){
                                       System.out.println("I deleted the designated directory");
                                   }
                               } 
                               if(f.delete()){
                                     System.out.println("I deleted the file");
                                 }
                                String path =  f.getAbsolutePath();

                                File pathF = new File(path);
                                pathF = pathF.getParentFile();
                                File[] files = pathF .listFiles();
                                pathButton[pathButton.length-1].files = files;
                           }
                           else if(confirmation == 2){
                               System.out.println("Not deleted");
                           }
                        }  
                    }
                    pathButton[pathButton.length-1].doClick();
                }
                editPopup.setVisible(false);
                
            }
            if(ae.getSource() == properties){
                
                permissions = new JCheckBox[3];
                permissions[0] = new JCheckBox("read");
                permissions[1] = new JCheckBox("write");
                permissions[2] = new JCheckBox("Execute");
                long size = 0;
                String name = null, path = null;
                
                
                
                for(int i =0;i<cotainerLabels.length;i++){
                    if(cotainerLabels[i].clicked != 0){
                        
                        name = cotainerLabels[i].name;
                        path = cotainerLabels[i].file.toString();
                        if(cotainerLabels[i].file.isDirectory()){
                            size = folderSize(cotainerLabels[i].file);
                        }
                        else{
                            size = cotainerLabels[i].file.length();
                        }
                        if(cotainerLabels[i].file.canRead()){
                           permissions[0].setSelected(true);
                        }
                        if(cotainerLabels[i].file.canWrite()){
                            permissions[1].setSelected(true);
                        }
                        if(cotainerLabels[i].file.canExecute()){
                            permissions[2].setSelected(true);
                        }
                
                        break;
                    }
                    else if(cotainerLabels[i].clicked == 0 && i == cotainerLabels.length-1 ){
                        name = pathButton[pathButton.length -1].name;
                        path = pathButton[pathButton.length -1].path;
                        File f = new File(path);
                        size = folderSize(f);
                    }
                }
                editPopup.setVisible(false);
                
                
                propertiesFrame = new JFrame();
                JPanel propertiesPanel = new JPanel();
                propertiesFrame.add(propertiesPanel);
                propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
                propertiesPanel.add(new JLabel("Name: "+name));
                propertiesPanel.add(new JLabel("Path: "+path));
                propertiesPanel.add(new JLabel("Size: "+size+"Bytes"));
                JPanel permPanel = new JPanel();
                FlowLayout permLayout = new FlowLayout();
                permLayout.setAlignment(FlowLayout.LEFT);
                permPanel.setLayout(permLayout);
                permPanel.add(permissions[0]);
                permPanel.add(permissions[1]);
                permPanel.add(permissions[2]);
                propertiesPanel.add(permPanel);
                JButton apply = new JButton("Appy changes");
                permListener perms = new permListener(); 
                apply.addActionListener(perms);
                propertiesPanel.add(apply);
                propertiesFrame.pack();
                propertiesFrame.setVisible(true);
                
            }
            if(ae.getSource() == Copy){
                fileToCut = null;
                Paste.setEnabled(true);
                for(int i =0;i<cotainerLabels.length;i++){
                    if(cotainerLabels[i].clicked != 0){
                        fileToCopy = cotainerLabels[i].file;
                        System.out.println("I want to copy "+fileToCopy);
                        editPopup.setVisible(false);
                    }
                }
            }
            if(ae.getSource() == Cut){
                fileToCopy = null;
                Paste.setEnabled(true);
                for(int i =0;i<cotainerLabels.length;i++){
                    if(cotainerLabels[i].clicked != 0){
                        fileToCut = cotainerLabels[i].file;
                        System.out.println("I want to cut "+fileToCut);
                        srcDir = pathButton.length -1 ;
                        editPopup.setVisible(false);
                    }
                }
            }
            if(ae.getSource() == Paste){
                File destFile;
                String destPath = null;
                for(int i =0;i<cotainerLabels.length;i++){
                    if(cotainerLabels[i].clicked != 0){
                        destPath = cotainerLabels[i].file.toString();
                    }
                }
                if(destPath == null){
                    destPath = pathButton[pathButton.length - 1].path;
                }
                if(fileToCopy != null){ 
                    String file = fileToCopy.getName();
                    destPath = destPath+FileSeparator+file;
                    System.out.println("I will paste "+destPath);
                    destFile = new File(destPath);
                    boolean conf = false;
                    conf = copyFiles(fileToCopy,destFile);
                    fileToCopy = null;
                    pathButton[pathButton.length - 1].doClick();
                }
                if(fileToCut != null){ 
                    String file = fileToCut.getName();
                    destPath = destPath+FileSeparator+file;
                    System.out.println("I will paste "+destPath);
                    destFile = new File(destPath);
                    boolean conf = false;
                    conf = moveFiles(fileToCut,destFile);
                    fileToCut = null;
                    pathButton[pathButton.length - 1].doClick();
                }
                Paste.setEnabled(false);
            }
            if(ae.getSource() == addToFavourites){
                boolean dirExists = false;
                for(int i =0;i<cotainerLabels.length;i++){
                    if(cotainerLabels[i].clicked != 0){
                        if(cotainerLabels[i].file.isDirectory()){
                            for(int j = 0 ;j < favouritesButtons.size();j++ ){
                                if(cotainerLabels[i].name.equals(favouritesButtons.get(j).name)){
                                    dirExists = true;
                                    JOptionPane.showConfirmDialog(null, "File"+cotainerLabels[i].name+" already exists in favourites","File already exists", JOptionPane.OK_CANCEL_OPTION);
                                }
                            }
                            if(dirExists == false){
                                favouritesButtons.add(new favouritesLabels(cotainerLabels[i].file.toString(),cotainerLabels[i].name));
                                favouritesButtons.get(favouritesButtons.size() - 1).addMouseListener(favouritesHandler);
                                favouritesPanel.add(favouritesButtons.get(favouritesButtons.size() - 1));
                            }
                        }
                        else{
                            JOptionPane.showConfirmDialog(null, "The file you chose is not a directory","Wrong type of file", JOptionPane.OK_CANCEL_OPTION);
                        }
                        dirExists = false;
                    }
                    try {
                        XMLController.createNewFavFile(favouritesButtons);
                    } catch (XMLStreamException ex) {
                            System.out.println("XMLStreamException thrwon");
                    } catch (FileNotFoundException ex) {
                            System.out.println("FileNotFoundException thrwon");
                    }
                    editPopup.setVisible(false);
                    favouritesPanel.revalidate();
                    favouritesPanel.repaint();
                }
            }
            
        }
    }
   
    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
}
    
    public class permListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            int i;
            Set<PosixFilePermission> perms = new HashSet<>();
            boolean read,write,exec;
            for(i = 0;i < cotainerLabels.length;i++){
                if(cotainerLabels[i].clicked != 0){
                    break;
                }
            }
            
            if(permissions[0].isSelected()){
                read = cotainerLabels[i].file.setReadable(true);
                System.out.println("I enable read "+read);
            }
            else{
                read = cotainerLabels[i].file.setReadable(false); 
                System.out.println("I disable read "+read);
            }
            if(permissions[1].isSelected()){
                write = cotainerLabels[i].file.setWritable(true);
                System.out.println("I enable write "+write);
            }
            else{
                write = cotainerLabels[i].file.setWritable(false);
                System.out.println("I disable write "+write);
            }
            if(permissions[2].isSelected()){
                exec = cotainerLabels[i].file.setExecutable(true);
                System.out.println("I enable exec "+exec);
            }
            else{    
                exec = cotainerLabels[i].file.setExecutable(false);
                System.out.println("I disable exec "+exec);
            }
            propertiesFrame.dispose();
        }
        
    }
    
    public long folderSize(File folder){
        long i;
        long size = 0;
        File[] filesInDir;
        
        try{
            filesInDir = folder.listFiles();
            for(File f:filesInDir){
                if(f.exists()){
                    if(f.isDirectory()){
                        size = size + folderSize(f);
                    }
                    else{
                        size = size + f.length();
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Couldn't determine the size of: "+folder.toString());
        }
        
        
        
        
        return size;
    }
    
    public boolean copyFiles(File srcFile,File destFile){
        int confirmation = 1;
        File[] FileList;
        
        if(srcFile.isDirectory()){
            FileList = srcFile.listFiles();
            try{
                if(destFile.exists()){
                    confirmation = JOptionPane.showConfirmDialog(null, "The directory "+destFile.getName()+" already exists.Do you want to replace it?","Confirm Replace", JOptionPane.OK_CANCEL_OPTION);
                
                    if(confirmation == 0){
                        deleteDirectory(destFile);
                        Files.copy(Paths.get(srcFile.getPath()), Paths.get(destFile.getPath()));
                        for(File f:FileList){
                            String dest = destFile.toString()+FileSeparator+f.getName();
                            File newDestFile = new File(dest);
                            copyFiles(f,newDestFile);
                        }
                    }
                    else if(confirmation == 2){
                        System.out.println("Not replaced");
                        return false;     
                    }

                }
                else{
                    Files.copy(Paths.get(srcFile.getPath()), Paths.get(destFile.getPath()));
                    for(File f:FileList){
                        String dest = destFile.toString()+FileSeparator+f.getName();
                        File newDestFile = new File(dest);
                        copyFiles(f,newDestFile);
                    }
                }
               // return true;
            }
            catch(IOException e){
                return false;
            }
            
        }
        else{
            try{
                if(destFile.exists()){
                    confirmation = JOptionPane.showConfirmDialog(null, "The file "+destFile.getName()+" already exists.Do you want to replace it?","Confirm Replace", JOptionPane.OK_CANCEL_OPTION);
                    if(confirmation == 0){
                        Files.copy(Paths.get(srcFile.getPath()), Paths.get(destFile.getPath()),StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                else{
                    Files.copy(Paths.get(srcFile.getPath()), Paths.get(destFile.getPath()));
                }
                return true;
            }
            catch(IOException e){
                return false;
            }
        }
        return false;
    }
    
    public boolean moveFiles(File srcFile,File destFile){
        int confirmation = 1;
        
        try{
            if(destFile.exists()){
                confirmation = JOptionPane.showConfirmDialog(null, "The file "+destFile.getName()+" already exists.Do you want to replace it?","Confirm Replace", JOptionPane.OK_CANCEL_OPTION);
                if(confirmation == 0){
                    deleteDirectory(destFile);
                    Files.move(Paths.get(srcFile.getPath()), Paths.get(destFile.getPath()),StandardCopyOption.REPLACE_EXISTING);
                }
            }
            else{
                Files.move(Paths.get(srcFile.getPath()), Paths.get(destFile.getPath()));
            }
            return true;
        }
        catch(IOException e){
            return false;
        }
       
    } 
    
    public class favouritesButtonHandler implements MouseListener{

        public void mouseClicked(MouseEvent me) {
            int panelHeight,numberOfIcons;
            
            if(SwingUtilities.isLeftMouseButton(me)){
                if(favouritesPanel == me.getSource()){
                    for(int i = 0; i < favouritesButtons.size();i++){
                        favouritesButtons.get(i).setBorder(null);
                        favouritesButtons.get(i).clicked = 0;
                    }
                }
                else{
                    for(int i = 0; i < favouritesButtons.size();i++){
                        if(me.getSource() == favouritesButtons.get(i)){
                                Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
                                favouritesButtons.get(i).setBorder(border);
                                favouritesButtons.get(i).clicked = 1;
                                homePathButtons(favouritesButtons.get(i).path);
                                JPanel pathTop = new JPanel();
                                path.removeAll();
                                path.setLayout(new BorderLayout());
                                path.add(pathTop,BorderLayout.CENTER);
                                FlowLayout pathTopLayout = new FlowLayout();
                                pathTop.setLayout(pathTopLayout);
                                pathTopLayout.setAlignment(FlowLayout.LEFT);
                                pathTop.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                                for(int j = 0;j < pathButton.length;j++){
                                    pathTop.add(pathButton[j]);
                                }
                                top = new JPanel();
                                top.addMouseListener(contMouseHandler);
                                cont.removeAll();
                                cont.setLayout(new BorderLayout());
                                FlowLayout topLayout = new FlowLayout();
                                topLayout.setAlignment(FlowLayout.LEFT);
                                top.setLayout(topLayout);
                                numberOfIcons = cotainerLabels.length;
                                panelHeight = (numberOfIcons/2)*100;
                                top.setPreferredSize(new Dimension(100,panelHeight));
                                cotainerLabels = null;
                                cotainerLabels = new ContainerJLabel[favouritesButtons.get(i).files.length];
                    
                                for(int j = 0;j < favouritesButtons.get(i).files.length;j++){
                                    ContainerJLabel tempLabel = new ContainerJLabel(favouritesButtons.get(i).files[j].getName(),favouritesButtons.get(i).files[j]);
                                    top.add(tempLabel);
                                    tempLabel.addMouseListener(contMouseHandler);
                                    tempLabel.setBackground(Color.green);
                                    cotainerLabels[j] = tempLabel;
                                }
                                scroller = new JScrollPane(top);
                                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                                cont.add(scroller,BorderLayout.CENTER);
                                cont.revalidate();
                                cont.repaint();
                        }
                        else{
                            favouritesButtons.get(i).setBorder(null);
                            favouritesButtons.get(i).clicked = 0;
                        }
                    }
                }
            }
            else if(SwingUtilities.isRightMouseButton(me)){
                for(int i = 0; i < favouritesButtons.size();i++){
                    if(me.getSource() == favouritesButtons.get(i)){
                            Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
                            favouritesButtons.get(i).setBorder(border);
                            favouritesButtons.get(i).clicked = 1;
                            editPopup.setVisible(true);
                            Cut.setEnabled(false);
                            Rename.setEnabled(false);
                            Copy.setEnabled(false);
                            Paste.setEnabled(false);
                            properties.setEnabled(false);
                            addToFavourites.setEnabled(false);
                    }
                    else{
                        favouritesButtons.get(i).setBorder(null);
                        favouritesButtons.get(i).clicked = 0;
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent me) {
           //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            ListIterator<favouritesLabels> iterator = favouritesButtons.listIterator();
            for (favouritesLabels favouritesButton : favouritesButtons) {
                if (me.getSource() == favouritesButton) {
                    if (favouritesButton.clicked == 0) {
                        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
                        favouritesButton.setBorder(border);
                    }
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }

        @Override
        public void mouseExited(MouseEvent me) {
            ListIterator<favouritesLabels> iterator = favouritesButtons.listIterator();
            for(int i = 0; i < favouritesButtons.size();i++){
                if(me.getSource() == favouritesButtons.get(i)){
                    if(favouritesButtons.get(i).clicked == 0){
                        favouritesButtons.get(i).setBorder(null);
                   }
                }
            }
        }
        
    }
    
    public void updateFavourites(Favourites conroller){
        favouritesButtons = conroller.searchFavouritesXML();
        for(int j = 0; j < favouritesButtons.size();j++){
            favouritesButtons.get(j).addMouseListener(favouritesHandler);
            favouritesPanel.add(favouritesButtons.get(j));
        }
    }
}
    