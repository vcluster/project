package vcluster.plugin.ec2;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vcluster.elements.Vm;
import vcluster.elements.Vm.VMState;

public class ResponseDataHandler {
	
	public static ArrayList<Vm> handleResponse(Command command, InputStream is) throws Exception 
	{
		/*BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer(); 
		String line; 
		while ((line = rd.readLine()) != null) {
		sb.append(line);
		} 
		rd.close();	
	
	System.out.println(sb.toString()+"\n");*/
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		ArrayList<Vm> vmList = new ArrayList<Vm>();

		switch(command) {
		case DESCRIBE_INSTANCE:
			vmList =  describeInstanceResponse(doc);
			break;
		case DESCRIBE_IMAGE:
			describeImageResponse(doc);
			break;
		case RUN_INSTANCE:
			vmList =  operateResponse(doc);
			break;
		case TERMINATE_INSTANCE:
			vmList =  operateResponse(doc);
			break;
		case STOP_INSTANCE:
			vmList =  operateResponse(doc);
			break;
		case START_INSTANCE:
			vmList =  operateResponse(doc);
			break;
		default:
			break;		
		}
		
		saveResponse(doc);
		return vmList;
	}


	private static ArrayList<Vm> operateResponse(Document doc) {
		// TODO Auto-generated method stub
		 ArrayList<Vm> vmList = new ArrayList<Vm>();
		Element instNodeList = findFirstMatchedNode(doc, "instancesSet");

		if (instNodeList == null) {
			System.out.println("[Error] : cannot find instanceSet element!");
			
		}

		String instanceId = "";
		String instanceState = "";
		for(int i = 0; i < instNodeList.getElementsByTagName("item").getLength();i++) {
			Vm vm = new Vm();

			Element anItem = (Element)instNodeList.getElementsByTagName("item").item(i);

				instanceId = getTextValue(anItem,"instanceId");
				if (instanceId == null || instanceId.equals("")) 
					continue;
				
				instanceState = getTextValue(anItem,"name");
				if (instanceState == null || instanceState.equals("")) 
					continue;
				
				//System.out.println(instanceId+"\t\t"+instanceState);
				vm.setId(instanceId);
				if(instanceState.equalsIgnoreCase("running")){
					vm.setState(VMState.RUNNING);
				}else if(instanceState.equalsIgnoreCase("stoped")){
					vm.setState(VMState.STOP);
				}else if(instanceState.equalsIgnoreCase("Pending")){
					vm.setState(VMState.PENDING);
				}else if(instanceState.equalsIgnoreCase("Prolog")){
					vm.setState(VMState.PROLOG);
				}else if(instanceState.equalsIgnoreCase("Suspended")){
					vm.setState(VMState.SUSPEND);
				}else if(instanceState.equalsIgnoreCase("terminated")){
					vm.setState(VMState.FAILED);
				}
				else{
					vm.setState(VMState.NOT_DEFINED);
				}
				vmList.add(vm);			
		}
		//System.out.println("----------------------------------------");
		return vmList;
	}

	private static ArrayList<Vm> describeInstanceResponse(Document doc) throws Exception 
	{

		 ArrayList<Vm> vmList = new ArrayList<Vm>();
		Element reservSet = findFirstMatchedNode(doc, "reservationSet");

		if (reservSet == null) {
			System.out.println("[Error] : cannot find instanceSet element!");			
		}
		
		//get a nodelist of  elements
		NodeList instNodeList = reservSet.getElementsByTagName("instancesSet");

		if(instNodeList == null || instNodeList.getLength() <= 0) {
			System.out.println("[Error] : but no item element!");			
		}

		//System.out.println("----------------------------------------");
		//System.out.println("Inst ID\t\tStatus");
		//System.out.println("----------------------------------------");

		String instanceId = "";
		String instanceState = "";
		String privateIP = "";
		String publicIP = "";
		String lunTime ="";
		
		
		for(int i = 0 ; i < instNodeList.getLength();i++) {
			
			Element anElement = (Element)instNodeList.item(i);
			NodeList itemNodeList = anElement.getElementsByTagName("item");
			for(int j = 0; j < itemNodeList.getLength(); j++) {
				
				Element anItem = (Element)itemNodeList.item(j);
				Vm vm = new Vm();
				instanceId = getTextValue(anItem,"instanceId");
				if(instanceId==""||instanceId==null){
					continue;
				}
				String ips = getTextValue(anItem,"privateIpAddress");
				if(ips==null)continue;
				String [] ipStr = ips.split(",");
				privateIP = ipStr[0].trim();
				if(ipStr.length>1){publicIP = ipStr[1].trim();}
				else{try {
					publicIP =getTextValue(anItem,"ipAddress").split(",")[0].trim();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					publicIP = "";
				}}
				lunTime = getTextValue(anItem,"launchTime");
				if (instanceId == null || instanceId.equals("")) 
					continue;
				
				instanceState = getTextValue(anItem,"name");
				if (instanceState == null || instanceState.equals("")) 
					continue;
				
				vm.setId(instanceId);
				vm.setPrivateIP(privateIP);
				vm.setPubicIP(publicIP);
				vm.setTime(lunTime);
				vm.setHostname("host1");
				if(instanceState.equalsIgnoreCase("running")){
					vm.setState(VMState.RUNNING);
				}else if(instanceState.equalsIgnoreCase("stoped")){
					vm.setState(VMState.STOP);
				}else if(instanceState.equalsIgnoreCase("Pending")){
					vm.setState(VMState.PENDING);
				}else if(instanceState.equalsIgnoreCase("Prolog")){
					vm.setState(VMState.PROLOG);
				}else if(instanceState.equalsIgnoreCase("Suspended")){
					vm.setState(VMState.SUSPEND);
				}else if(instanceState.equalsIgnoreCase("terminated")){
					vm.setState(VMState.FAILED);
				}else{
					vm.setState(VMState.NOT_DEFINED);
				}
				vmList.add(vm);
			}			
		}
		//System.out.println("----------------------------------------");
		saveResponse(doc);
		return vmList;

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
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer(); 
			String line; 
			while ((line = rd.readLine()) != null) {
			sb.append(line);
			} 
			rd.close();			
		System.out.println(sb.toString()+"\n");	
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
			FileWriter outFile = new FileWriter("amazonResponse.xml");
			PrintWriter out = new PrintWriter(outFile);
			out.println(xmlString);
			outFile.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
