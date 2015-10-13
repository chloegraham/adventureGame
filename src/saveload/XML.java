package saveload;

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

import convertors.Msgs;

public class XML {
	
	private final static String fileName = "continue.xml";
	private final static String tag = "GameState";
	
	public static String load() {
        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the    
            // XML file
            dom = db.parse(fileName);

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
	        e = dom.createElement(tag);
	        e.appendChild(dom.createTextNode(gameState));
	        rootEle.appendChild(e);

	        dom.appendChild(rootEle);

	        try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");

	            // send DOM to file
	            tr.transform(new DOMSource(dom), 
	            new StreamResult(new FileOutputStream(fileName)));
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
	
	public static String newGame(){
		// Level looks like = (row = 'char', rowspacer = '%', layer spacer = '@', 1234 + @ = levelID, level tag = <Level>)
		//                    www%eee%@www%eee%@1234@<Level>
		// Player looks like = (userID, levelID, keyAmount, boulder, directionfacing, x, y, <Player>)
		//					   101 % 1234 % 2 % 1 % 2 % 3 % 3 %@<Player>
		
		String level900 = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnnxnn%nnsnnnn%nnnnnnn%Mnnnnnn%ncnnnnn%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnnnnnn%@900@<Level><Split>";
		String level901 = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnnxnn%nnsnnnn%nnnnnzn%Mnnnnnn%nnnncnn%@nnnnnnn%nnnnnnn%nnnlnnb%nnnnnnn%nnnnnnn%@901@2%5%0%4%@<Level><Split>";
		String level902 = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnnxnn%nnsnnnn%nnnnnnn%Mnnnnnn%nnnnnnz%@nnnnnnn%nnbnnnn%nnnlnnn%nnnnnnn%nnbnnnn%@902@4%6%0%4%@<Level><Split>";
		String level903 = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnnxnn%nnnsnnn%nnnnnnn%Mnnnnnn%nnnncnz%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnbnnnn%@903@4%6%0%4%@<Level><Split>";
		
		
		String playerOne = "101%901%0%0%1%3%3%@<Player><Split>";
		String playerTwo = "202%901%1%1%1%2%2%@<Player><Split>";
		
		String playerTwoDiffLvl = "202%901%0%0%1%2%2%@<Player><Split>";
		
		String testLevel = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnndnn%nnnsnnn%nnnnnnn%nnnnnnn%nnnncnz%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnbnnnn%@1234@<Level><Split>101%1234%1%0%2%3%3%@<Player><Split>202%1234%5%1%2%2%2%@<Player><Split>";
		
		String demo = level901 + level902 + level903 + playerOne + playerTwo;
		
		return STAGE1 + "<Split>" + STAGE2 + "<Split>" + PLAYER1 + "<Split>" + PLAYER2 + "<Split>";
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
	
	
	
	/*
	 *  Rooms for Stage 1 + Stage 1
	 */
	private static final String S1ROOM1 = "wwe%eee%eee%eee%eee%@" +
			  							  "nnn%nnc%czz%nnn%nnn%@" +
			  							  "nnn%bbn%nnn%nnn%nnn%@" +
			  							  "<Room>";
	
	private static final String S1ROOM2 = "eeee%ewww%eeee%eeee%@" +
										  "ssss%nnnn%nnzz%nnnn%@" +
										  "nnnn%nnnn%nnnn%nnbb%@" +
										  "<Room>";
	
	private static final String STAGE1 = S1ROOM1 + S1ROOM2 + "<Stage>";
	
	
	
	/*
	 *  Rooms for Stage 2 + Stage 2
	 */
	private static final String S2ROOM1 = "eee%eee%eee%eee%@" +
			  							  "nnn%nnn%sss%nnn%@" +
			  							  "nnn%bbb%nnn%nnn%@" +
			  							  "<Room>";

	private static final String S2ROOM2 = "eeee%eeee%wwew%eeee%@" +
										  "ssss%nnzz%nnnn%nnnn%@" +
										  "nnnn%nnnn%nnnn%nnbb%@" +
										  "<Room>";

	private static final String STAGE2 = S2ROOM1 + S2ROOM2 + "<Stage>";
	
	
	
	/*
	 *  Players
	 */
	private static final String PLAYER1 = Msgs.PLAYER_ONE + "%" +			// UserID
										  "1%" +							// StageID    Current Stage ('000' default start) 
										  "1%" +							// RoomID
										  "1%" +							// Point.x
										  "2%" + 							// Point.y
										  "1%" +							// Facing Direction
										  "0%" +							// # of Keys Player has
										  "0%" +							// Holding Boulder 0=false, 1=true
										  "@" +
										  "<Player>";
	
	private static final String PLAYER2 = Msgs.PLAYER_TWO + "%" +			// UserID
										  "1%" +							// StageID    Current Stage ('000' default start) 
										  "1%" +							// RoomID
										  "2%" +							// Point.x
										  "1%" + 							// Point.y
										  "1%" +							// Facing Direction
										  "0%" +							// # of Keys Player has
										  "0%" +							// Holding Boulder 0=false, 1=true
										  "@" +
										  "<Player>";
}
