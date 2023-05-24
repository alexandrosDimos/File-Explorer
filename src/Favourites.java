/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.*;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
/**
 *
 * @author arc68
 */
public class Favourites {
    String path;
    String FileSeparator ; 
    
    public Favourites(String path){
        this.path = path;
        FileSeparator = System.getProperty("file.separator");
        File f = new File(path);
        if(!f.exists()){
            try {
                createNewFavFile(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void createNewFavFile(List<favouritesLabels> listOfFavs) throws XMLStreamException, FileNotFoundException{
        String favPath= path+FileSeparator+"favourites.xml";
        
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
            File favouritesXML = new File(favPath);
            try{
                favouritesXML.createNewFile();
            }
            catch(IOException ex){
                ex.getCause();
            }
        }
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        
        FileOutputStream favXML = new FileOutputStream(favPath);
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(favXML);
       
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        eventWriter.add(end);
        StartElement configStartElement = eventFactory.createStartElement("","", "favourites");
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        if(listOfFavs != null){
            for(int i =0;i < listOfFavs.size();i++){
                String name = listOfFavs.get(i).name;
                String path = listOfFavs.get(i).path;
                String favouriteInput = "name=\""+name+"\" path=\""+path+"\"";
                createNode(eventWriter, "directory", favouriteInput);
            }
            
        }
        eventWriter.add(eventFactory.createEndElement("", "", "favourites"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
        
    }

    public List<favouritesLabels> searchFavouritesXML(){
        int pos;
        List<favouritesLabels> favouritesButtons = new ArrayList<>();
        
        XMLInputFactory factory;
        XMLEventReader eventReader = null;
        try {
            File favXML = new File(path+FileSeparator+"favourites.xml");
            factory = XMLInputFactory.newInstance();
            
            eventReader = factory.createXMLEventReader(new FileReader(favXML));
        } catch (XMLStreamException ex) {
            Logger.getLogger(Favourites.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Favourites.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (eventReader.hasNext()) { 
            
            try {
                XMLEvent event = eventReader.nextEvent();
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart().equals("directory")) {
                            event = eventReader.nextEvent();
                            String  node = event.asCharacters().getData();
                            pos = node.indexOf("\"");
                            node = node.substring(pos+1);
                            pos = node.indexOf("\"");
                            String name = node.substring(0,pos);
                            node = node.substring(pos+1);
                            pos =  node.indexOf("\"");
                            node = node.substring(pos+1);
                            String path = node.substring(0,node.length()-1);
                           favouritesButtons.add(new favouritesLabels(path,name));
                            continue;
                        }
                    }
            } catch (XMLStreamException ex) {
                Logger.getLogger(Favourites.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return favouritesButtons;
    }
    
    private void createNode(XMLEventWriter eventWriter, String name,String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);

    }
}
