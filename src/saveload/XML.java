package saveload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.w3c.dom.*;

import serverclient.GameState;

public class XML {
	
	//private static String row1 = "5 7";
	private static String row2 = "w w w w d w w";
	private static String row3 = "w e e s e e e";
	private static String row4 = "w e e e e e e";
	private static String row5 = "w e e e e e e";
	private static String row6 = "w e e e c e z";
	
	private static ArrayList<String> rolev;
	private GameState happyCLAM;
	private static String happyClam;
	
	public static void main(String[] args){
		BufferedWriter output = null;
		happyClam = "";
		saveToXML("xml.xml");
		if (readXML("xml.xml") == true){
			for (String s : rolev){
				happyClam += s;
				happyClam += "%";    
			}
			happyClam += "@";
			for (String s : rolev){
				happyClam += s;
				happyClam += "%";    
			}
			happyClam += "@";
			for (String s : rolev){
				happyClam += s;
				happyClam += "%";
			}
			happyClam += "@";
		}
		try {
			File file = new File("Level1HappyClam.txt");
			output = new BufferedWriter(new FileWriter(file));
			output.write(happyClam);
		} catch ( IOException e ) {
			e.printStackTrace();
		}  
		if ( output != null )
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static boolean readXML(String xml) {
        rolev = new ArrayList<String>();
        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the    
            // XML file
            dom = db.parse(xml);

            Element doc = dom.getDocumentElement();

//            row1 = getTextValue(row1, doc, "row1");
//            if (row1 != null) {
//                if (!row1.isEmpty())
//                    rolev.add(row1);
//            }
            row2 = getTextValue(row2, doc, "row2");
            if (row2 != null) {
                if (!row2.isEmpty())
                    rolev.add(row2);
            }
            row3 = getTextValue(row3, doc, "row3");
            if (row3 != null) {
                if (!row3.isEmpty())
                    rolev.add(row3);
            }
            row4 = getTextValue(row4, doc, "role4");
            if ( row4 != null) {
                if (!row4.isEmpty())
                    rolev.add(row4);
            }
            row5 = getTextValue(row5, doc, "role4");
            if ( row5 != null) {
                if (!row5.isEmpty())
                    rolev.add(row5);
            }
            row6 = getTextValue(row6, doc, "role4");
            if ( row6 != null) {
                if (!row6.isEmpty())
                    rolev.add(row6);
            }
            return true;

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

        return false;
    }
	
	public static void saveToXML(String xml) {
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
	        Element rootEle = dom.createElement("Level_1");

	        // create data elements and place them under root
//	        e = dom.createElement("row1");
//	        e.appendChild(dom.createTextNode(row1));
//	        rootEle.appendChild(e);

	        e = dom.createElement("row2");
	        e.appendChild(dom.createTextNode(row2));
	        rootEle.appendChild(e);

	        e = dom.createElement("row3");
	        e.appendChild(dom.createTextNode(row3));
	        rootEle.appendChild(e);

	        e = dom.createElement("row4");
	        e.appendChild(dom.createTextNode(row4));
	        rootEle.appendChild(e);
	        
	        e = dom.createElement("row5");
	        e.appendChild(dom.createTextNode(row5));
	        rootEle.appendChild(e);
	        
	        e = dom.createElement("row6");
	        e.appendChild(dom.createTextNode(row6));
	        rootEle.appendChild(e);

	        dom.appendChild(rootEle);

	        try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            //tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            //tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            //tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
	            //tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	            // send DOM to file
	            tr.transform(new DOMSource(dom), 
	            new StreamResult(new FileOutputStream(xml)));

	        } catch (TransformerException te) {
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
	            System.out.println(ioe.getMessage());
	        }
	    } catch (ParserConfigurationException pce) {
	        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
	    }
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
	
	public GameState getGameState(){
		happyCLAM = new GameState(happyClam);
		return happyCLAM;
	}
}
