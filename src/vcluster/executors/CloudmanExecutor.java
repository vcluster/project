package vcluster.executors;

import java.util.ArrayList;
import java.util.TreeMap;

import vcluster.Vcluster.uiType;
import vcluster.elements.Cloud;
import vcluster.managers.CloudManager;
import vcluster.ui.CmdComb;
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
	public static String register(CmdComb cmd) {
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
	public static String load(CmdComb cmd) {
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

	public static boolean undefined(CmdComb cmd) {
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
	public static boolean unload(CmdComb cmd) {
		// TODO Auto-generated method stub
		
		if (cmd.getParaset().size()==0) {
			System.out.println("[ERROR : ] Expect a cloud name!");
			return false;
		}else{			
						
			boolean result =  CloudManager.unLoadCloud(cmd.getParaset());
			return result;
		}
		
	}

	/**
	 *Turn on a physical host 
	 */
	public static boolean hoston(CmdComb cmd) {
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
	public static boolean hostoff(CmdComb cmd) {
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


	public static String dump(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		if(cmd.getParaset().size()==0){
			return str.append("[ERROR : ] Expect a Cloud name!").toString();
		}
		Cloud c = CloudManager.getCloudList().get(cmd.getParaset().get(0));
		if(c.getHostList().size()==0||c.getHostList()==null)return "";
		String tId=  String.format("%-8s","ID");
		String tName=  String.format("%-12s", "Name");
		String tMax = String.format("%-6s", "Max");
		String tStat =  String.format("%-6s","stat");
		String tip = String.format("%-20s","Private IP");
		str.append("-------------------------------------------------------"+System.getProperty("line.separator"));
		str.append(tId+tName+tMax+tStat+tip+System.getProperty("line.separator"));
		str.append("-------------------------------------------------------"+System.getProperty("line.separator"));
		for(vcluster.elements.Host h : c.getHostList().values()){
			String id = String.format("%-8s", h.getId());
			String name = String.format("%-12s",h.getName());
			String max = String.format("%-6s",h.getMaxVmNum()+"");
			String stat = String.format("%-6s","ON");
			String ip = String.format("%-20s", h.getIpmiID());
			if(h.getPowerStat()==0)
			stat = String.format("%-6s","OFF");
			str.append(id+name+max+stat+ip+System.getProperty("line.separator"));
		}
		str.append("-------------------------------------------------------"+System.getProperty("line.separator"));
		if(cmd.getUi()==uiType.CMDLINE)System.out.println(str);
		return str.toString();
	}



}
