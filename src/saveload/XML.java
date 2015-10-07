package saveload;

import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.w3c.dom.*;

public class XML {
	
	private final static String fileName = "continue.xml";
	private final static String tag = "Game State";
	
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

//	         create data elements and place them under root
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
		
		String Level1 = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnndnn%nnnnnnn%nnnnnnn%nnnnnnn%nnnnnnn%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnnnnnn%@1111@<Level><Split>101%1111%2%1%2%3%3%@<Player><Split>";
		String Level2 = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnndnn%nnnnnnn%nnnnnnn%nnnnnnn%nnnncnn%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnnnnnn%@2222@<Level><Split>101%2222%0%1%2%3%3%@<Player><Split>";
		String Level3 = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnndnn%nnnnnnn%nnnnnnn%nnnnnnn%nnnnnnz%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnbnnnn%@3333@<Level><Split>101%3333%0%1%2%3%3%@<Player><Split>";
		String Level4 = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnndnn%nnnsnnn%nnnnnnn%nnnnnnn%nnnncnz%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnbnnnn%@4444@<Level><Split>101%4444%0%1%2%3%3%@<Player><Split>";
		
		String multiLevel = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnndnn%nnnnnnn%nnnnnnn%nnnnnnn%nnnnnnn%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnnnnnn%@4567@<Level><Split>wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnndnn%nnnnnnn%nnnnnnn%nnnnnnn%nnnncnn%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnnnnnn%@5678@<Level><Split>101%4567%2%1%2%3%3%@<Player><Split>";
		//TODO: how to implement multiple levels?
		String multiPlayer = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnndnn%nnnnnnn%nnnnnnn%nnnnnnn%nnnncnn%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnnnnnn%@7899@<Level><Split>101%7899%0%1%2%3%3%@<Player><Split>202%7899%0%1%2%4%4%@<Player><Split>";
		//TODO: how to implement multiple players?
		
		String testLevel = "wwwweww%weeeeee%weeeeee%weeeeee%weeeeee%@nnnndnn%nnnsnnn%nnnnnnn%nnnnnnn%nnnncnz%@nnnnnnn%nnnnnnn%nnnlnnn%nnnnnnn%nnbnnnn%@1234@<Level><Split>101%1234%2%1%2%3%3%@<Player><Split>";
		return testLevel;
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
}
