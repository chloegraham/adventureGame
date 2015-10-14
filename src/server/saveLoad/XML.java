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
	
	private final static String saveFileName = "continue.xml";
	private final static String saveTag = "GameState";
	private final static String newGameFileName = "newgame.xml";
	private final static String newGameTag = "NewGame";
	
	public static String load() {
        return load(saveFileName, saveTag);
    }
	
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
	
	
	
	private static String getTextValue(String def, Element doc, String tag) {
	    String value = def;
	    NodeList nl;
	    nl = doc.getElementsByTagName(tag);
	    if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
	        value = nl.item(0).getFirstChild().getNodeValue();
	    }
	    return value;
	}
	
	
	
	
	public static String newGame(){
		return load(newGameFileName, newGameTag);
	}
	
	
	
	/*
	 *  Rooms for Stage 1 + Stage 1
	 *  -- s1room1 = stage1 room1 etc
	 */
	private static final String S1ROOM1 = "wee%wee%wee%wee%wee%wee%wee%wee%wee%wee%wee%wee%@" +
			  							  "nnn%nnc%nnc%nns%nns%nnz%nDz%nnn%nnn%nnn%nnn%nnn%@" +
			  							  "nnn%nnn%nnn%nnn%nnn%nnn%nnn%nnb%nnn%nnn%nnn%nnn%@" +
			  							  "<Room>";
	
	private static final String S1ROOM2 = "eeeeeeeee%wweeeeeee%wweeeeeee%eeeeeeeee%@" +
										  "nnnnnsssD%nnnnnnnnn%nnnnnnnzz%nnnnnnnnn%@" +
										  "nnnnnnnnn%nnnnnnnnn%nnnnnnnnn%nnbbnnnnn%@" +
										  "<Room>";
	
	private static final String STAGE1 = S1ROOM1 + S1ROOM2 + "<Stage>";
	
	
	
	/*
	 *  Rooms for Stage 2 + Stage 2
	 */
	private static final String TROOM1 = "wwwww%weeee%weeee%weeee%weeee%@" +
			  							 "nnndn%nnnnn%nnnnn%nnnnn%nnnnn%@" +
			  							 "nnnnn%nnnnn%nnnnn%nnnnn%nnnnn%@" +
			  							 "<Room>";

	private static final String TROOM2 = "eeee%ewwe%ewwe%ewee%eeee%@" +
										 "cnnn%nnnn%nnnn%nndn%nnnD%@" +
										 "nnnn%nnnn%nnnn%nnnn%nnnn%@" +
										 "<Room>";
	
	private static final String TROOM3 = "eeeeew%eeeeee%eeeeew%@" +
										 "nnnnnn%nnDnnd%znnnnn%@" +
			  							 "bnnnnn%nnnnnn%nnnnnn%@" +
			  							 "<Room>";

	private static final String TROOM4 = "eeeeeeeeee%@" +
			  							 "Dsnsnsnsny%@" +
			  							 "nnnnnnnnnn%@" +
			  							 "<Room>";

	private static final String TUTORIAL = TROOM1 + TROOM2 + TROOM3 + TROOM4 +"<Stage>";
	
	
	
	/*
	 *  Players
	 */
	private static final String PLAYER1 = Msgs.PLAYER_ONE + "%" +			// UserID
										  "0%" +							// StageID    Current Stage ('000' default start) 
										  "0%" +							// RoomID
										  "2%" +							// Point.x
										  "4%" + 							// Point.y
										  "1%" +							// Facing Direction
										  "0%" +							// # of Keys Player has
										  "0%" +							// Holding Boulder 0=false, 1=true
										  "@" +								// a delimiter which i don't think we actually need anymore
										  "<Player>";
	
	private static final String PLAYER2 = Msgs.PLAYER_TWO + "%" +			// UserID
										  "0%" +							// StageID    Current Stage ('000' default start) 
										  "0%" +							// RoomID
										  "2%" +							// Point.x
										  "5%" + 							// Point.y
										  "1%" +							// Facing Direction
										  "0%" +							// # of Keys Player has
										  "0%" +							// Holding Boulder 0=false, 1=true
										  "@" +								// a delimiter which i don't think we actually need anymore
										  "<Player>";
	
	/*
	 *  Players
	 */
	private static final String TUTORIALPLAYER1 = Msgs.PLAYER_ONE + "%" +			// UserID
												  "0%" +							// StageID    Current Stage ('000' default start) 
												  "0%" +							// RoomID
												  "2%" +							// Point.x
												  "3%" + 							// Point.y
												  "3%" +							// Facing Direction
												  "1%" +							// # of Keys Player has
												  "0%" +							// Holding Boulder 0=false, 1=true
												  "@" +								// a delimiter which i don't think we actually need anymore
												  "<Player>";
	
	private static final String TUTORIALPLAYER2 = Msgs.PLAYER_TWO + "%" +			// UserID
												  "0%" +							// StageID    Current Stage ('000' default start) 
												  "0%" +							// RoomID
												  "3%" +							// Point.x
												  "3%" + 							// Point.y
												  "1%" +							// Facing Direction
												  "0%" +							// # of Keys Player has
												  "0%" +							// Holding Boulder 0=false, 1=true
												  "@" +								// a delimiter which i don't think we actually need anymore
												  "<Player>";
	
	/*
	 *  Rooms for Stage 2 + Stage 2
	 */
	private static final String ZROOM1 = "eeeeee%eeeeee%eeeeee%wwwwww%wwwwww%eeeeee%eeeeee%eeeeee%wwwwww%@" +
			  							 "cnnnnn%nnnnnn%nnnnnn%nnndnn%nnndnn%nnnnnn%nnnnnn%nnnnnz%nnndnn%@" +
			  							 "nnnnnn%nnnnnn%nnnnnn%nnnnnn%nnnnnn%nnnnnn%nnnnnn%nnnnnn%nnnnnn%@" +
			  							 "<Room>";

	private static final String ZROOM2 = "eeeeeeeew%eeeeeeeee%eeeeeeeew%@" +
										 "ssssncssn%Dssnsnsnd%snsnssnsn%@" +
										 "nnnnnnnnn%nnnnnnnnn%nnnnnnnnn%@" +
										 "<Room>";
	
	private static final String ZROOM3 = "wwwwwwwwwwwwweeeeeww%weeeeeeeeeeeeewweeww%eeeeeeeeeeeeeeeeeeee%eeeeeeeeeeeeewwwwwww%@" +
										 "nnnnnnnnnnnnndddndnn%nzzzzzzzzzzzzdnnndnn%Dnnnnnnnnnnnndddnddy%nnnnnnnnnnnnnnnnnnnn%@" +
			  							 "nnnnnnnnnnnnnnnnnnnn%nnnnnnnnnnnnnnnnnnnn%nbbbbbbbbbbbbnnnnnnn%nnnnnnnnnnnnnnnnnnnn%@" +
			  							 "<Room>";

	private static final String ZEBRA = ZROOM1 + ZROOM2 + ZROOM3 + "<Stage>";
	
	/*
	 *  Players
	 */
	private static final String ZEBRAPLAYER1 = Msgs.PLAYER_ONE + "%" +			// UserID
												  "0%" +							// StageID    Current Stage ('000' default start) 
												  "0%" +							// RoomID
												  "0%" +							// Point.x
												  "6%" + 							// Point.y
												  "1%" +							// Facing Direction
												  "0%" +							// # of Keys Player has
												  "0%" +							// Holding Boulder 0=false, 1=true
												  "@" +								// a delimiter which i don't think we actually need anymore
												  "<Player>";
	
	private static final String ZEBRAPLAYER2 = Msgs.PLAYER_TWO + "%" +			// UserID
												  "0%" +							// StageID    Current Stage ('000' default start) 
												  "0%" +							// RoomID
												  "3%" +							// Point.x
												  "2%" + 							// Point.y
												  "1%" +							// Facing Direction
												  "1%" +							// # of Keys Player has
												  "0%" +							// Holding Boulder 0=false, 1=true
												  "@" +								// a delimiter which i don't think we actually need anymore
												  "<Player>";
}
