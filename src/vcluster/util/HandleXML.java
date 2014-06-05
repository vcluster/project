package vcluster.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import vcluster.elements.Cloud;
import vcluster.executors.PlugmanExecutor;
import vcluster.managers.CloudManager;
import vcluster.ui.CmdComb;

public class HandleXML {
	private static DocumentBuilderFactory factory;
	private static DocumentBuilder builder;
	private static Document document;
	private static Element cloudman;	
	private static Element root;
	private static Element time;
	private static File file;
	private static Element batchPlugin;
	
	public static void main(String[] arg){
		
		//it.getElement(e, name, attr, attrName)
	}

	static {
		try {
			file = new File("vclusterStatus.xml");
		 if(!file.exists()){
			 file.createNewFile();
			 firstTime();
		 }
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.parse(file);
			document.setXmlVersion("1.0");
			root = (Element) document.getElementsByTagName("root").item(0);
			cloudman = (Element) document.getElementsByTagName("cloudman").item(0);
			time = (Element) document.getElementsByTagName("LastModifiedTime").item(0);
			batchPlugin = (Element)document.getElementsByTagName("batchPlugin").item(0);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean restore(){
				
		/*restore clouds status */
		
		for(int i = 0; i<cloudman.getChildNodes().getLength();i++){
			ArrayList<String> conf = new ArrayList<String> ();
			Element e = (Element) cloudman.getChildNodes().item(i);
			for(int j = 0 ; j<e.getAttributes().getLength();j++){
				String str = e.getAttributes().item(j).getNodeName() + "="+e.getAttributes().item(j).getNodeValue();
				conf.add(str);
			}
			Cloud cloud = new Cloud(conf);
			boolean isloaded = Boolean.parseBoolean(e.getAttribute("isloaded"));
			cloud.setIsLoaded(isloaded);
			CloudManager.getCloudList().put(cloud.getCloudName(), cloud);
			if(cloud.isLoaded())cloud.load();		
			
		}
		
		/*restore batchPlugin status */	
		String name = batchPlugin.getAttribute("name");
		if(!name.equals("")&&name!=null){
			PlugmanExecutor.load(new CmdComb("plugman load -b "+name));	
		}
		
		
		return true;
	}
	
	public static boolean loadBatchPlugin(String name){
		batchPlugin.setAttribute("name", name);
		writeXML();
		return true;
	}
	public static boolean unloadBatchPlugin(){
		batchPlugin.removeAttribute("name");
		writeXML();
		return true;
	}
	
	public static boolean addCloudElement(List<String> conf){
		String name = conf.get(0).split("=")[1].trim();
		Element e = document.createElement(name);
		for(String item : conf){
			String str1 = item.split("=")[0].trim();
			String str2 = item.split("=")[1].trim();
			e.setAttribute(str1, str2);
		}
		e.setAttribute("isLoaded", "false");
		e.setAttribute("vms", "0");
		cloudman.appendChild(e);
		writeXML();
		return true;
	}
	
	public static boolean setCloudAttribute(String name,String att,String value){
		Element e = (Element)cloudman.getElementsByTagName(name).item(0);
		e.setAttribute(att, value);
		writeXML();
		return true;
	}
	
	public static boolean firstTime(){
		factory = DocumentBuilderFactory.newInstance();
		try {
			file.createNewFile();
			builder = factory.newDocumentBuilder();
		} catch (IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		document = builder.newDocument();
		document.setXmlVersion("1.0");
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			Date dt = cal.getTime();
			String t = format1.format(dt);
			
			root = document.createElement("vCluster");
			cloudman = document.createElement("cloudman");	
			batchPlugin = document.createElement("batchPlugin");
			time = document.createElement("LastModifiedTime");
			time.setAttribute("value", t);
			document.appendChild(root);
			root.appendChild(time);
			root.appendChild(cloudman);
			root.appendChild(batchPlugin);
					
		return creaXML();
	}
	
	
	
	public static boolean creaXML(){
		try {			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transFormer = transFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			 FileOutputStream out = new FileOutputStream(file);
			 StreamResult xmlResult = new StreamResult(out);
			 transFormer.transform(domSource, xmlResult);


		} catch (IOException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;		
	}
	public static boolean readXML(){
		
		return true;
	}
	public static boolean writeXML(){
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Date dt = cal.getTime();
		String t = format1.format(dt);
		time.setAttribute("value", t);
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transFormer = null;
		try {
			transFormer = transFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		DOMSource domSource = new DOMSource(document);

		 if(!file.exists()){
			 try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		 }
		 FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		 StreamResult xmlResult = new StreamResult(out);
		 try {
			transFormer.transform(domSource, xmlResult);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		 
		return true;
	}

}
