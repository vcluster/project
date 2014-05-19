package vcluster.managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import vcluster.simulators.Cloud;


/**
 * A class that presents functions to manage the clouds. Since vcluster only has one cloudmanager, 
 * all it's functions are static.
 */
public class CloudManager  {

	/**
	 * Set the current activity cloud, all the operations should be performed on the current cloud.
	 * @param instance of cloud.
	 */
	public static void setCurrentCloud(Cloud cloud)
	{
		currentCloud = cloud;
	}

	/**
	 * Get the current activity cloud.
	 * @return An instance of a cloud. 
	 */
	public static Cloud getCurrentCloud() 
	{
		return currentCloud;
	}

	
	private static boolean chkConf(ArrayList<String> conf){
		boolean flag = true;
		if(conf.size()<3){
			vcluster.util.Util.print("[ERROR : ] Configuration file format is incorrect,please check the format! ==> "+conf.get(0));
			flag = false;
			return false;
		}
		String[] l1 = conf.get(0).split("=");
		String[] l2 = conf.get(1).split("=");
		String[] l3 = conf.get(2).split("=");
		if(l1.length<2||l2.length<2||l3.length<2){
			vcluster.util.Util.print("[ERROR : ] Configuration file format is incorrect,please check the format! ==> "+conf.get(0));
			flag = false;
			return flag;
		}
		if(!l1[0].trim().equalsIgnoreCase("name")||!l2[0].trim().equalsIgnoreCase("interface")||!l3[0].trim().equalsIgnoreCase("type")){
			vcluster.util.Util.print("[ERROR : ] Configuration file format is incorrect,please check the format! ==> "+conf.get(0));
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
					//vcluster.util.Util.print(aLine);
				}				
			}
			if(chkConf(conf)){
				confList.add(conf);
			}
			br.close();
			
		}catch(Exception e){
			vcluster.util.Util.print("Configuration file doesn't exist ,please check it!");
			return confList;
		}
		
		return confList;
	}
	
	/**
	 * Register a cloud on vcluster use the given configurations
	 * @param confFile, the configuration file path.
	 */
	public static String registerCloud(String confFile) 
	{		
		StringBuffer str = new StringBuffer();
		ArrayList<ArrayList<String>> confList = handleConf(confFile);
		ArrayList<Cloud> tempCL = new ArrayList<Cloud>();
		for(ArrayList<String> conf:confList){
			String name = conf.get(0).split("=")[1].trim();
			boolean flag = false;
			if(cloudList!=null&&!cloudList.isEmpty()){
				for(Cloud c : getCloudList().values()){
					if(name.equals(c.getCloudName())){
						flag = true;
						break;
					}
				}
				if(flag)continue;
			}
			Cloud cloud = new Cloud(conf);
			if(cloud.getCloudName()!=null&cloud.getCloudpluginName()!=null&cloud.getCloudType()!=null)
			cloudList.put(cloud.getCloudName(), cloud);
			tempCL.add(cloud);
		}
		String cName = String.format("%-12s", "Name");
		String cInterface =String.format("%-20s", "Interface");
		String cType = String.format("%-12s", "Type");
		String cVMs = String.format("%-16s", "VMs");
		str.append("-------------------------------------------------------"+System.getProperty("line.separator"));
		str.append(cName+cInterface+cType+cVMs+System.getProperty("line.separator"));
		str.append("-------------------------------------------------------"+System.getProperty("line.separator"));

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
			str.append(fName+fInterface+fType+fVMs+System.getProperty("line.separator"));
			
		}

		str.append("-------------------------------------------------------"+System.getProperty("line.separator"));
		
		return str.toString();
	}
	
	/**
	 *Get the registered clouds list  
	 */
	public static TreeMap<String, Cloud> getCloudList(){
		//Config.vmMan.listVM("vmman list -refresh");
		return cloudList;		
	}
	

	/**
	 * Load the given clouds in vcluster. vlcusetr would communicate with the real clouds system and get the virtual machines information.
	 * @param An array of the given clouds' names
	 */
	public static String loadCloud(String[] name) {
		// TODO Auto-generated method stub
		StringBuffer flag = new StringBuffer();
		String cName = String.format("%-12s", "Name");
		String cInterface =String.format("%-20s", "Interface");
		String cType = String.format("%-12s", "Type");
		String cVMs = String.format("%-16s", "VMs");
		flag.append("-------------------------------------------------------"+System.getProperty("line.separator"));
		flag.append(cName+cInterface+cType+cVMs+System.getProperty("line.separator"));
		flag.append("-------------------------------------------------------"+System.getProperty("line.separator"));
		for(int i = 0;i<name.length;i++){
			try {
				flag.append(cloudList.get(name[i].trim()).load()+System.getProperty("line.separator"));
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block				
				flag.append(name[i]+" doesn't exist!"+System.getProperty("line.separator"));
			}
		}
		flag.append("-------------------------------------------------------"+System.getProperty("line.separator"));
		return flag.toString();
	}



	/**
	 * Unload the given clouds from vcluster. the status of the unloaded clouds would be switched to "unloaded"
	 * the virtual machines that belongs to those clouds would be removed from data structure.
	 * @param  An array of the given clouds' names
	 */
	public static boolean unLoadCloud(String[] name) {
		// TODO Auto-generated method stub
		boolean flag = true;
		for(int i = 0;i<name.length;i++){			
			try {
				Cloud c = cloudList.get(name[i].trim());
				if(!c.getVmList().isEmpty()){
					vcluster.util.Util.print(name[i]+" still have vms are running!");
					return false;
				}
				CloudManager.cloudList.remove(name[i]);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				
				vcluster.util.Util.print(name[i]+" Cloud doesn't exist!");
				e.printStackTrace();
			}
		}
		return flag;
	}



	/* this is only used when executing commands from command line */
	private static Cloud currentCloud = null;
	private static TreeMap<String,Cloud> cloudList = new TreeMap<String,Cloud>();

    
}