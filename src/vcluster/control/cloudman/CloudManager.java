package vcluster.control.cloudman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import vcluster.global.Config;
import vcluster.global.Config.CloudType;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class CloudManager  {

	/* later it may be a thread */
	
	
	public CloudManager() {
		privateCloudList = new Vector <Cloud>();
		publicCloudList = new Vector <Cloud>();
		
	}
	
	
	public void dump(String type)
	{
		String cName = String.format("%-12s", "Name");
		String cInterface =String.format("%-20s", "Interface");
		String cType = String.format("%-12s", "Type");
		String cVMs = String.format("%-16s", "VMs");
		System.out.println("-----------------------------------------------");
		System.out.println(cName+cInterface+cType+cVMs);
		System.out.println("-----------------------------------------------");

		for(Cloud cloud:this.getCloudList().values()){
			if(cloud.getCloudType().toString().equalsIgnoreCase(type)){
				String fName = String.format("%-12s", cloud.getCloudName());
				String fInterface =String.format("%-20s", cloud.getCloudpluginName());
				String fType = String.format("%-12s", cloud.getCloudType());
				String fVMs = String.format("%-16s", cloud.getVmList().size());
				System.out.println(fName+fInterface+fType+fVMs);
			}
		}

		System.out.println("-----------------------------------------------");
		

	}
	public void dump()
	{
		String cName = String.format("%-12s", "Name");
		String cInterface =String.format("%-20s", "Interface");
		String cType = String.format("%-12s", "Type");
		String cVMs = String.format("%-16s", "VMs");
		System.out.println("-----------------------------------------------");
		System.out.println(cName+cInterface+cType+cVMs);
		System.out.println("-----------------------------------------------");

		for(Cloud cloud:this.getCloudList().values()){
			String fName = String.format("%-12s", cloud.getCloudName());
			String fInterface =String.format("%-20s", cloud.getCloudpluginName());
			String fType = String.format("%-12s", cloud.getCloudType());
			String fVMs = String.format("%-16s", cloud.getVmList().size());
			System.out.println(fName+fInterface+fType+fVMs);
			
		}

		System.out.println("-----------------------------------------------");
	}
	
	/*
	 * things to consider
	 * 
	 * 1. find a cloud system which there is a room for requested vms
	 *    -  a single cloud system or multiple cloud system?
	 *  
	 */
	public Cloud findCloudSystem(int vms) {
		
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


	public void setCurrentCloud(Cloud cloud)
	{
		currentCloud = cloud;
	}

	public Cloud getCurrentCloud() 
	{
		return currentCloud;
	}
	
	public boolean addCloudElement(Cloud e)
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
	
	public void incCurrentVMs(Cloud e, int vms) 
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

	public boolean loadCloudElments(String confFile, CloudManager cman) 
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(confFile));
			
			String aLine = "";
			List<String> conf = new ArrayList<String>() ;
			String cName = String.format("%-12s", "Name");
			String cInterface =String.format("%-20s", "Interface");
			String cType = String.format("%-12s", "Type");
			String cVMs = String.format("%-16s", "VMs");
			System.out.println("-----------------------------------------------");
			System.out.println(cName+cInterface+cType+cVMs);
			System.out.println("-----------------------------------------------");
			while ((aLine = br.readLine()) != null) {
				if (aLine.equalsIgnoreCase("[cloudelement]")) {
					if(!conf.isEmpty()){
						String name = conf.get(0).split("=")[1].trim();

						boolean flag = true;
						for(Cloud c : getCloudList().values()){
							if(name.equals(c.getCloudName())){
								System.out.println("There is already a cloud named "+name+" exists!");
								flag = false;
							}
						}
						if(flag){
							Cloud cloud = new Cloud(conf);
							cman.addCloudElement(cloud);
						}
						conf = new ArrayList<String>();
					}
				}else{					
					conf.add(aLine);
				}
				
			}
			String name = conf.get(0).split("=")[1].trim();

			boolean flag = true;
			for(Cloud c : getCloudList().values()){
				if(name.equals(c.getCloudName())){
					System.out.println("There is already a cloud named "+name+" exists!");
					flag = false;
				}
			}
			if(flag){
				Cloud cloud = new Cloud(conf);
				cman.addCloudElement(cloud);
			}
			System.out.println("-----------------------------------------------");			
			br.close();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	PrintMsg.print(DMsgType.ERROR, e.getMessage());
	    	System.out.println("mark 1");
	    	return false;
	    } 
		return true;
	}
	
	public HashMap<String, Cloud> getCloudList(){
		HashMap<String,Cloud> cl = new HashMap<String,Cloud>();
		for(Cloud cloud : privateCloudList){
			cl.put(cloud.getCloudName(), cloud);
		}
		for(Cloud cloud : publicCloudList){
			cl.put(cloud.getCloudName(), cloud);
		}
		return cl;
		
	}
	
	private Vector <Cloud> privateCloudList;
	private Vector <Cloud> publicCloudList;
	
	/* this is only used when executing commands from command line */
	private static Cloud currentCloud = null;

    
}

