package vcluster.control.cloudman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import vcluster.global.Config.CloudType;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class CloudManager  {

	/* later it may be a thread */
	
	
	public CloudManager() {
		privateCloudList = new Vector <CloudElement>();
		publicCloudList = new Vector <CloudElement>();
	}
	
	
	public void dump(String type)
	{
		System.out.println("----------------------------------------");
		System.out.println("\tCloud Manager");
		System.out.println("----------------------------------------");

		if(type.equalsIgnoreCase("private")) {
			for(int i = 0; i < privateCloudList.size(); i++) {
				CloudElement e = privateCloudList.elementAt(i);
				System.out.printf("[private cloude element] [%d]\n", i);
				e.dump();
				System.out.println();
			}
		} else if (type.equalsIgnoreCase("public")) {
			for(int i = 0; i < publicCloudList.size(); i++) {
				CloudElement e = publicCloudList.elementAt(i);
				System.out.printf("[public cloude element] [%d]\n", i);
				e.dump();
				System.out.println();
			}
		}
		System.out.println("----------------------------------------");

	}
	public void dump()
	{
		System.out.println("----------------------------------------");
		System.out.println("\tCloud Manager");
		System.out.println("----------------------------------------");

		for(int i = 0; i < privateCloudList.size(); i++) {
			CloudElement e = privateCloudList.elementAt(i);
			System.out.printf("[private cloude element] [%d]\n", i);
			e.dump();
			System.out.println();
		}

		System.out.println("----------------------------------------");

		for(int i = 0; i < publicCloudList.size(); i++) {
			CloudElement e = publicCloudList.elementAt(i);
			System.out.printf("[public cloude element] [%d]\n", i);
			e.dump();
			System.out.println();
		}

		System.out.println("----------------------------------------");
	}
	
	/*
	 * things to consider
	 * 
	 * 1. find a cloud system which there is a room for requested vms
	 *    -  a single cloud system or multiple cloud system?
	 *  
	 */
	public CloudElement findCloudSystem(int vms) {
		
		/* this function has to be intelligent to find an available cloud system. 
		 * at this moment, it just returns predefined one
		 */
		if (privateCloudList.size() == 0) {
			PrintMsg.print(DMsgType.ERROR, "No available cloud system found!");
			return null;
		}
		
		//return privateCloudList.elementAt(0);
		return publicCloudList.elementAt(0);
	}


	public void setCurrentCloud(CloudType ctype, int no)
	{
		switch (ctype) {
		case PRIVATE:
			if (privateCloudList.size() < no) {
				PrintMsg.print(DMsgType.ERROR, "exceed the size of private cloud list, [no]="+no);
				return;
			}
			currentCloud = privateCloudList.elementAt(no);
			break;
			
		case PUBLIC:
			if (publicCloudList.size() < no) {
				PrintMsg.print(DMsgType.ERROR, "exceed the size of public cloud list, [no]="+no);
				return;
			}
			currentCloud = publicCloudList.elementAt(no);
			break;
		}
	}

	public CloudElement getCurrentCloud() 
	{
		return currentCloud;
	}
	
	public boolean addCloudElement(CloudElement e)
	{
		CloudType ctype = e.getCloudType();
		if (ctype == CloudType.PRIVATE) {
			privateCloudList.add(e);
			
			PrintMsg.print(DMsgType.MSG, e.stringCloudType()+" added.");
			return true;
		}
		
		if (ctype == CloudType.PUBLIC) {
			publicCloudList.add(e);
			PrintMsg.print(DMsgType.MSG, e.stringCloudType()+" added.");
			return true;
		}
		
		PrintMsg.print(DMsgType.ERROR, "check the cloud type, "+e.stringCloudType());
		return false;
	}
	
	public void incCurrentVMs(CloudElement e, int vms) 
	{
		e.incCurrentVMs(vms);
	}

	/*
	public boolean addCloudElement(CloudType ctype, String epoint, int max) 
	{
		CloudElement element = new CloudElement(ctype, epoint, max);
		
		if (ctype == CloudType.PRIVATE) {
			privateCloudList.add(element);
			
			PrintMsg.print(DMsgType.MSG, element.stringCloudType()+" added.");
			return true;
		}
		
		if (ctype == CloudType.PUBLIC) {
			publicCloudList.add(element);
			PrintMsg.print(DMsgType.MSG, element.stringCloudType()+" added.");
			return true;
		}
		
		PrintMsg.print(DMsgType.ERROR, "check the cloud type, "+element.stringCloudType());
		return false;
	}
	*/

	public static boolean loadCloudElments(String confFile, CloudManager cman) 
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(confFile));
			
			String aLine = "";
			CloudElement e = null;
			List<String> conf = null ;
			while ((aLine = br.readLine()) != null) {
				if (aLine.equalsIgnoreCase("[cloudelement]")) {
					if (e != null) cman.addCloudElement(e);
					e = new CloudElement(conf);
				} else {
					conf.add(aLine);
				}
			}

			if (e != null) cman.addCloudElement(e);
			
	    } catch (Exception e) {
	    	PrintMsg.print(DMsgType.ERROR, e.getMessage());
	    	return false;
	    } 
		return true;
	}
	
	
	private Vector <CloudElement> privateCloudList;
	private Vector <CloudElement> publicCloudList;
	
	/* this is only used when executing commands from command line */
	private CloudElement currentCloud = null;

    
}

