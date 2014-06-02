package vcluster.executors;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

import vcluster.Vcluster.uiType;
import vcluster.elements.Cloud;
import vcluster.managers.CloudManager;
import vcluster.ui.CmdSet;
/**
 *A class representing Cloud manager executor. Cloud-related commands would be sent to this class and the commands will be analyzed,
 *then corresponding functions will be invoked according the commands.
 *
 */
public class CloudmanExecutor {


	/**
	 *Execute the command of register clouds on vcluster
	 *step 1. analyze the command, extract the parameters  
	 *step 2. invoke the corresponding function in cloud manager class and send the parameters.
	 *@param cmdLine, the command line of register cloud from a conf file.
	 */
	public static String register(CmdSet cmd) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		if (cmd.getParaset().size()==0) {
			str.append("[USAGE] : cloudman register <cloudelement conf file>");
			System.out.println(str);
			return str.toString();
		}
		String confFile = cmd.getParaset().get(0);
		//System.out.println(token);
		String result =  CloudManager.registerCloud(confFile);
		if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(result);
		return result;
	}

	
	/**
	 * Load a cloud into vcluster. command line would be analyzed and the corresponding function in cloud manager would be invoked.
	 */
	public static String load(CmdSet cmd) {
		// TODO Auto-generated method stub
		if (cmd.getParaset().size()==0) {
			System.out.println("[ERROR : ] Expect a cloud name!");
			return "[ERROR : ] Expect a cloud name!";
		}
		else{	
			ArrayList<String> para = cmd.getParaset();
			int size = para.size();
			String [] arg =(String [])para.toArray(new String[size]);	
			
			String result =  CloudManager.loadCloud(arg);
			if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(result);
			return result;
		}
	}

	/**
	 * list up all the registered clouds, the relevant information would be list up.  
	 */
	public static TreeMap<String,Cloud> getCloudList() {
		// TODO Auto-generated method stub
		return CloudManager.getCloudList();
	}

	public static boolean undefined(CmdSet cmd) {
		// TODO Auto-generated method stub
		/* skip the command */
		
		if (cmd.getParaset().size()==0) {
			System.out.println("[USAGE] : cloudman dump [<private | public>>]");
			System.out.println("        : cloudman register <cloudelement conf file>");
			System.out.println("        : cloudman set <private | public> <cloud num>");			
			return false;
		}		
		return true;
	}

	/**
	 *unload a cloud from vcluster. the cloud status would be change to "unloaded", 
	 *and the connection between vcluster and related real cloud system would be cut off.
	 */
	public static boolean unload(CmdSet cmd) {
		// TODO Auto-generated method stub
		
		if (cmd.getParaset().size()==0) {
			System.out.println("[ERROR : ] Expect a cloud name!");
			return false;
		}else{			
			String [] arg = (String[])cmd.getParaset().toArray();	
			
			boolean result =  CloudManager.unLoadCloud(arg);
			return result;
		}
		
	}

	/**
	 *Turn on a physical host 
	 */
	public static boolean hoston(CmdSet cmd) {
		// TODO Auto-generated method stub
		
		if (cmd.getParaset().size()==0) {
			System.out.println("[ERROR : ] Expect a cloud name!");
			return false;
		}
		String cloudname = cmd.getParaset().get(0);
		if (cmd.getParaset().size()<2) {
			System.out.println("[ERROR : ] Expect a host id!");
			return false;
		}
		String hostID = cmd.getParaset().get(1).trim();
		Cloud cloud = CloudManager.getCloudList().get(cloudname);
		
		
		return cloud.hoston(hostID);
		
		}

	/**
	 * Shut down a physical host.
	 */
	public static boolean hostoff(CmdSet cmd) {
		// TODO Auto-generated method stub
		if (cmd.getParaset().size()==0) {
			System.out.println("[ERROR : ] Expect a cloud name!");
			return false;
		}
		String cloudname = cmd.getParaset().get(0).trim();
		if (cmd.getParaset().size()<2) {
			System.out.println("[ERROR : ] Expect a host id!");
			return false;
		}
		String hostID = cmd.getParaset().get(1).trim();
		
		Cloud cloud = CloudManager.getCloudList().get(cloudname);
		
		
		return cloud.hostoff(hostID);
		
	}



}
