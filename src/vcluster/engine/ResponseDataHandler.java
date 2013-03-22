package vcluster.engine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vcluster.global.Config;
import vcluster.ui.Command;

public class ResponseDataHandler {
	
	public static void handleResponse(Command command, InputStream is) throws Exception 
	{
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

		switch(command) {
		case DESCRIBE_INSTANCE:
			describeInstanceResponse(doc);
			break;
		case DESCRIBE_IMAGE:
			describeImageResponse(doc);
			break;
		}

		saveResponse(doc);
	}



	private static void describeInstanceResponse(Document doc) throws Exception 
	{

		Element reservSet = findFirstMatchedNode(doc, "reservationSet");

		if (reservSet == null) {
			System.out.println("[Error] : cannot find instanceSet element!");
			return;
		}

		//get a nodelist of  elements
		NodeList instNodeList = reservSet.getElementsByTagName("instancesSet");

		if(instNodeList == null || instNodeList.getLength() <= 0) {
			System.out.println("[Error] : but no item element!");
		}

		System.out.println("----------------------------------------");
		System.out.println("Inst ID\t\tStatus");
		System.out.println("----------------------------------------");

		String instanceId = "";
		String instanceState = "";
		for(int i = 0 ; i < instNodeList.getLength();i++) {
			//get an instanceId element

			//get a nodelist of  item elements
			Element anElement = (Element)instNodeList.item(i);
			NodeList itemNodeList = anElement.getElementsByTagName("item");
			for(int j = 0; j < itemNodeList.getLength(); j++) {
				Element anItem = (Element)itemNodeList.item(j);
				instanceId = getTextValue(anItem,"instanceId");
				if (instanceId == null || instanceId.equals("")) 
					continue;

				instanceState = getTextValue(anItem,"name");
				if (instanceState == null || instanceState.equals("")) 
					continue;
				
				System.out.println(instanceId+"\t\t"+instanceState);
				
			}
			
			
		}

		System.out.println("----------------------------------------");

		//saveResponse(doc);
	}

	private static void describeImageResponse(Document doc) throws Exception 
	{
		
		Element imgSetEle = findFirstMatchedNode(doc, "imagesSet");

		if (imgSetEle == null) {
			System.out.println("[Error] : cannot find imageSet element!");
			return;
		}
		
		//get a nodelist of  elements
		NodeList nl = imgSetEle.getElementsByTagName("item");

		if(nl == null || nl.getLength() <= 0) {
			System.out.println("[Error] : but no item element!");
		}

		System.out.println("----------------------------------------");
		System.out.println("Img Id\t\tOwner\t\tisPublic?");
		System.out.println("----------------------------------------");

		for(int i = 0 ; i < nl.getLength();i++) {

			String imageId = "";
			String ownerId = "";
			String isPublic = "";

			/* get an item element */
			Element el = (Element)nl.item(i);

			/* get image id */
			imageId = getTextValue(el,"imageId");

			if (imageId == null || imageId.equals(""))
				continue;
			
			/* get owner id */
			ownerId = getTextValue(el,"imageOwnerId");
			
			if (ownerId == null || ownerId.equals("")) 
				continue;
			
			/* get owner id */
			isPublic = getTextValue(el,"isPublic");
			
			if (isPublic == null || isPublic.equals("")) 
				continue;

			System.out.println(imageId+"\t"+ownerId+"\t\t"+isPublic);
		}

		System.out.println("----------------------------------------");

		//saveResponse(doc);
	}
	
	
	private static Element findFirstMatchedNode(Document doc, String nodeName)
	{
		/* got a root element */
		Element docEle = doc.getDocumentElement();

		NodeList nl = docEle.getElementsByTagName(nodeName);
		return (Element)nl.item(0);
	}
	
	public static void handlError(InputStream is) throws Exception 
	{
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

		/* get the root element */
		Element docEle = doc.getDocumentElement();

		/* get a nodelist of  elements */
		NodeList nl = docEle.getElementsByTagName("Error"); 

		if(nl == null || nl.getLength() <= 0) {
			System.out.println("[Error] : hard to explain....");
			System.out.println("        : copy and paste the below end point and see..why it is difficult to explain....");
			// System.out.println("        : " + QueryExecutor.getEndPoint());
		} else {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get an error element
				Element el = (Element)nl.item(i);
				String msg = getTextValue(el,"Message");
				System.out.println("[Error] : "+msg);
			}
		}

		saveResponse(doc);
	}

	private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	
   
	private static void saveResponse(Document doc) throws Exception
	{
		
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();

        try {
			FileWriter outFile = new FileWriter(Config.xmlFile);
			PrintWriter out = new PrintWriter(outFile);
			out.println(xmlString);
			outFile.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
