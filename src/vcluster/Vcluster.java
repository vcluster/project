package vcluster;

import java.io.BufferedReader;
import java.io.FileReader;
/**
 *This class initiate the parameters of vcluster. 
 */
public class Vcluster {

	public static String CONDOR_IPADDR;
	public static int PORTNUM;
	public static String SERVER_ADDR;
	public static int SERVER_PORT1;
	public static int SERVER_PORT2;
	public static String configFile = "vcluster.conf";
	public static enum uiType {CMDLINE, REMOTECLIENT, WEBUI,LOADBALANCER};	
	public static uiType currUI;
	public static String xmlFile;
	
	/**
	 * When vcluser start this function will be involked, the parameters will be read from a configuration file.
	 */
	public static void init(){	
		BufferedReader br ;
		try{
			br = new BufferedReader(new FileReader(configFile));
			String aLine = "";
			while ((aLine = br.readLine()) != null) {
				if(aLine.contains("#")||!aLine.contains("="))continue;
				String[] pair = aLine.split("=");
				if (pair[0].trim().equalsIgnoreCase("CONDOR_IPADDR")) {
					CONDOR_IPADDR = pair[1].trim();				
				}else if(pair[0].trim().equalsIgnoreCase("CONDOR_PORTNUM")){
					PORTNUM = Integer.parseInt(pair[1].trim());
				}else if(pair[0].trim().equalsIgnoreCase("SERVER_IPADDR")){
					SERVER_ADDR = pair[1].trim();
				}else if(pair[0].trim().equalsIgnoreCase("SERVER_PORT1")){
					SERVER_PORT1 = Integer.parseInt(pair[1].trim());
				}else if(pair[0].trim().equalsIgnoreCase("SERVER_PORT2")){
					SERVER_PORT2 = Integer.parseInt(pair[1].trim());
				} 
			}
			
			br.close();
			
		}catch(Exception e){
			System.out.println("Configuration file doesn't exist ,please check it!");			
		}
	}
	
}