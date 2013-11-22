package vcluster.control.cloudman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import vcluster.control.VMelement;
import vcluster.global.Config;
import vcluster.util.HandleXML;

public class CloudManager  {

	/* later it may be a thread */

	public static boolean printArchitecture(){
		for(Cloud c : cloudList.values()){
			if(c.isLoaded()){
			System.out.println("---------------------------------------");
			System.out.println(c.getCloudName());
			for(Host h : c.getHostList().values()){
				System.out.println("      "+h.getId()+"  : "+h.getMaxVmNum()+"/" + h.getVmList().size());
				System.out.println("        ID       Activity");	
				for(VMelement v : h.getVmList().values()){
					System.out.println("        "+v.getId()+"   "+v.isIdle());					
				}				
			}
			System.out.println("---------------------------------------");
		
			}
		}
		return true;
	}
	
	public static void dump(String type)
	{
		String cName = String.format("%-12s", "Name");
		String cInterface =String.format("%-20s", "Interface");
		String cType = String.format("%-12s", "Type");
		String cVMs = String.format("%-16s", "VMs");
		System.out.println("-----------------------------------------------");
		System.out.println(cName+cInterface+cType+cVMs);
		System.out.println("-----------------------------------------------");

		for(Cloud cloud:cloudList.values()){
			if(cloud.getCloudType().toString().equalsIgnoreCase(type)){
				String vms = "";
				if(cloud.isLoaded()){
					vms=cloud.getVmList().size()+"";
				}else{
					vms="undeployed";
				}
				
				String fName = String.format("%-12s", cloud.getCloudName());
				String fInterface =String.format("%-20s", cloud.getCloudpluginName());
				String fType = String.format("%-12s", cloud.getCloudType());
				String fVMs = String.format("%-16s", vms);
				System.out.println(fName+fInterface+fType+fVMs);
			}
		}

		System.out.println("-----------------------------------------------");

	}
	public static void dump()
	{
		String cName = String.format("%-12s", "Name");
		String cInterface =String.format("%-20s", "Interface");
		String cType = String.format("%-12s", "Type");
		String cVMs = String.format("%-16s", "VMs");
		System.out.println("-------------------------------------------------------");
		System.out.println(cName+cInterface+cType+cVMs);
		System.out.println("-------------------------------------------------------");

		for(Cloud cloud:cloudList.values()){
			String vms = "";
			if(cloud.isLoaded()){
				vms=cloud.getVmList().size()+"";
			}else{
				vms="undeployed";
			}
			String fName = String.format("%-12s", cloud.getCloudName());
			String fInterface =String.format("%-20s", cloud.getCloudpluginName());
			String fType = String.format("%-12s", cloud.getCloudType());
			String fVMs = String.format("%-16s", vms);
			System.out.println(fName+fInterface+fType+fVMs);
			
		}

		System.out.println("-------------------------------------------------------");
	}
	
	/*
	 * things to consider
	 * 
	 * 1. find a cloud system which there is a room for requested vms
	 *    -  a single cloud system or multiple cloud system?
	 *  
	 */	

	public static void setCurrentCloud(Cloud cloud)
	{
		currentCloud = cloud;
	}

	public static Cloud getCurrentCloud() 
	{
		return currentCloud;
	}

	private static boolean chkConf(ArrayList<String> conf){
		boolean flag = true;
		if(conf.size()<3){
			System.out.println("[ERROR : ] Configuration file format is incorrect,please check the format! ==> "+conf.get(0));
			flag = false;
			return false;
		}
		String[] l1 = conf.get(0).split("=");
		String[] l2 = conf.get(1).split("=");
		String[] l3 = conf.get(2).split("=");
		if(l1.length<2||l2.length<2||l3.length<2){
			System.out.println("[ERROR : ] Configuration file format is incorrect,please check the format! ==> "+conf.get(0));
			flag = false;
			return flag;
		}
		if(!l1[0].trim().equalsIgnoreCase("name")||!l2[0].trim().equalsIgnoreCase("interface")||!l3[0].trim().equalsIgnoreCase("type")){
			System.out.println("[ERROR : ] Configuration file format is incorrect,please check the format! ==> "+conf.get(0));
			flag = false;
			return flag;
		}
		return flag;
	}
	
	private static ArrayList<ArrayList<String>> handleConf(String confFile){

		
		ArrayList<ArrayList<String>> confList = new  ArrayList<ArrayList<String>>();		
		BufferedReader br ;
		try{
			br = new BufferedReader(new FileReader(confFile));
			String aLine = "";
			ArrayList<String> conf = new ArrayList<String>() ;
			while ((aLine = br.readLine()) != null) {
				if (aLine.equalsIgnoreCase("[cloudelement]")) {
					if(!conf.isEmpty()){
						if(chkConf(conf)){
							confList.add(conf);
						}
						conf = new ArrayList<String>();	
					}
				
				}else{
					if(!aLine.trim().isEmpty())conf.add(aLine);
				}				
			}
			if(chkConf(conf)){
				confList.add(conf);
			}
			br.close();
			
		}catch(Exception e){
			System.out.println("Configuration file doesn't exist ,please check it!");
			return confList;
		}
		
		return confList;
	}

	public static boolean loadCloudElments(String confFile) 
	{			
		ArrayList<ArrayList<String>> confList = handleConf(confFile);
		ArrayList<Cloud> tempCL = new ArrayList<Cloud>();
		for(ArrayList<String> conf:confList){
			String name = conf.get(0).split("=")[1].trim();
			boolean flag = false;
			if(cloudList!=null&&!cloudList.isEmpty()){
				for(Cloud c : getCloudList().values()){
					if(name.equals(c.getCloudName())){
						//System.out.println("There is already a cloud named "+name+" exists!");
						flag = true;
						break;
					}
				}
				if(flag)continue;
			}
			Cloud cloud = new Cloud(conf);
			if(cloud.getCloudName()!=null&cloud.getCloudpluginName()!=null&cloud.getCloudType()!=null)
			cloudList.put(cloud.getCloudName(), cloud);
			HandleXML.addCloudElement(conf);
			tempCL.add(cloud);
		}
		String cName = String.format("%-12s", "Name");
		String cInterface =String.format("%-20s", "Interface");
		String cType = String.format("%-12s", "Type");
		String cVMs = String.format("%-16s", "VMs");
		System.out.println("-------------------------------------------------------");
		System.out.println(cName+cInterface+cType+cVMs);
		System.out.println("-------------------------------------------------------");

		for(Cloud cloud:tempCL){
			String vmnum = "";
			if(cloud.isLoaded()){
				vmnum = cloud.getVmList().size()+"";
			}else{
				vmnum = "undeployed";
			}
			String fName = String.format("%-12s", cloud.getCloudName());
			String fInterface =String.format("%-20s", cloud.getCloudpluginName());
			String fType = String.format("%-12s", cloud.getCloudType());
			String fVMs = String.format("%-16s", vmnum);
			System.out.println(fName+fInterface+fType+fVMs);
			
		}

		System.out.println("-------------------------------------------------------");
		
		return true;
	}
	
	public static TreeMap<String, Cloud> getCloudList(){
		//Config.vmMan.listVM("vmman list -refresh");
		return cloudList;		
	}
	

	
	public static boolean deploy(String[] name) {
		// TODO Auto-generated method stub
		boolean flag = true;
		String cName = String.format("%-12s", "Name");
		String cInterface =String.format("%-20s", "Interface");
		String cType = String.format("%-12s", "Type");
		String cVMs = String.format("%-16s", "VMs");
		System.out.println("-------------------------------------------------------");
		System.out.println(cName+cInterface+cType+cVMs);
		System.out.println("-------------------------------------------------------");
		for(int i = 0;i<name.length;i++){
			try {
				flag = cloudList.get(name[i].trim()).load();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				
				System.out.println(name[i]+" Cloud doesn't exist!");
				e.printStackTrace();
			}
		}
		System.out.println("-------------------------------------------------------");
		return flag;
	}



	/* this is only used when executing commands from command line */
	private static Cloud currentCloud = null;
	private static TreeMap<String,Cloud> cloudList = new TreeMap<String,Cloud>();
    
}