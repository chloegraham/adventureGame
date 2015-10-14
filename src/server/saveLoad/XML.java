package server.saveLoad;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import server.helpers.Msgs;

public class XML {
	
	//final constant fields for saving and loading a saved game
	private final static String saveFileName = "continue.xml";
	private final static String saveTag = "GameState";
	
	//final constant fields for creating the new game
	private final static String newGameFileName = "newgame.xml";
	private final static String newGameTag = "NewGame";
	
	
	/**
	 * Call load on continue.xml and returns the saved gameState to the server
	 * @return the string from continue.xml representing the last saved gameState
	 */
	public static String load() {
        return load(saveFileName, saveTag);
    }
	
	/**
	 * Call load on newgame.xml and returns a new gameState to the server
	 * @return the string from newgame.xml representing a new level
	 */
	public static String newGame(){
		return load(newGameFileName, newGameTag);
	}
	
	/**
	 * Actual functionality of load, handling the changing of the contents of a xml file into
	 * a string which the server receives as the gameState. Handles error checking for file not
	 * found and passing illegal arguments.
	 * @param file the name of the file to save to
	 * @param tag displaying some information about the contents stored
	 * @return the String which is passed to newGame() or load()
	 */
	public static String load(String file, String tag) {
        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the    
            // XML file
            dom = db.parse(file);

            Element doc = dom.getDocumentElement();
            
            String gameState = null;
            gameState = getTextValue(gameState, doc, tag);
            if (gameState != null) {
                if (!gameState.isEmpty()){
                	return gameState;
                }
            }

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return null;
    }
	
	/**
	 * Receives the GameState as a string from the server and saves it to continue.xml.
	 * Handles error checking such as file not found and illegal arguments.
	 * @param gameState a string received from server representing the current GameState.
	 * @return true for success, false for fail to save.
	 */
	public static boolean save(String gameState) {
	    Document dom;
	    Element e = null;

	    // instance of a DocumentBuilderFactory
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    try {
	        // use factory to get an instance of document builder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        // create instance of DOM
	        dom = db.newDocument();

	        // create the root element
	        Element rootEle = dom.createElement("Continue");

	        // create data elements and place them under root
	        e = dom.createElement(saveTag);
	        e.appendChild(dom.createTextNode(gameState));
	        rootEle.appendChild(e);

	        dom.appendChild(rootEle);

	        try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");

	            // send DOM to file
	            tr.transform(new DOMSource(dom), 
	            new StreamResult(new FileOutputStream(saveFileName)));
	            return true;

	        } catch (TransformerException te) {
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
	            System.err.println(ioe.getMessage());
	        }
	    } catch (ParserConfigurationException pce) {
	        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
	    }
	    return false;
	}
	
	
	
	/**
	 * @param def the String to add the contents 
	 * @param doc
	 * @param tag
	 * @return
	 */
	private static String getTextValue(String def, Element doc, String tag) {
	    String value = def;
	    NodeList nl;
	    nl = doc.getElementsByTagName(tag);
	    if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
	        value = nl.item(0).getFirstChild().getNodeValue();
	    }
	    return value;
	}
	
}
