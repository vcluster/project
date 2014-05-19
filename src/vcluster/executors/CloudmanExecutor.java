package vcluster.executors;

import java.util.StringTokenizer;
import java.util.TreeMap;

import vcluster.managers.CloudManager;
import vcluster.simulators.Cloud;
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
	public static String register(String cmdLine) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		String token = null;
		if (!st.hasMoreTokens()) {
			str.append("[USAGE] : cloudman register <cloudelement conf file>");
			vcluster.util.Util.print(str);
			return str.toString();
		}
		token = st.nextToken().trim();
		//vcluster.util.Util.print(token);
		return CloudManager.registerCloud(token);

	}

	
	/**
	 * Load a cloud into vcluster. command line would be analyzed and the corresponding function in cloud manager would be invoked.
	 */
	public static String load(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		String cmd = st.nextToken();
		if (!st.hasMoreTokens()) {
			vcluster.util.Util.print("[ERROR : ] Expect a cloud name!");
			return "[ERROR : ] Expect a cloud name!";
		}
		else{			
			String [] arg = cmdLine.replace(cmd, "").trim().split(" ");	
			
			return CloudManager.loadCloud(arg);
		}
	}

	/**
	 * list up all the registered clouds, the relevant information would be list up.  
	 */
	public static TreeMap<String,Cloud> getCloudList() {
		// TODO Auto-generated method stub
		return CloudManager.getCloudList();
	}

	public static boolean undefined(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			vcluster.util.Util.print("[USAGE] : cloudman dump [<private | public>>]");
			vcluster.util.Util.print("        : cloudman register <cloudelement conf file>");
			vcluster.util.Util.print("        : cloudman set <private | public> <cloud num>");			
			return false;
		}		
		return true;
	}

	/**
	 *unload a cloud from vcluster. the cloud status would be change to "unloaded", 
	 *and the connection between vcluster and related real cloud system would be cut off.
	 */
	public static boolean unload(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		String cmd = st.nextToken();
		if (!st.hasMoreTokens()) {
			vcluster.util.Util.print("[ERROR : ] Expect a cloud name!");
			return false;
		}else{			
			String [] arg = cmdLine.replace(cmd, "").trim().split(" ");	
			
			return CloudManager.unLoadCloud(arg);
		}
		
	}

	/**
	 *Turn on a physical host 
	 */
	public static boolean hoston(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		if (!st.hasMoreTokens()) {
			vcluster.util.Util.print("[ERROR : ] Expect a cloud name!");
			return false;
		}
		String cloudname = st.nextToken().trim();
		if (!st.hasMoreTokens()) {
			vcluster.util.Util.print("[ERROR : ] Expect a host id!");
			return false;
		}
		String hostID = st.nextToken().trim();
		Cloud cloud = CloudManager.getCloudList().get(cloudname);
		
		
		return cloud.hoston(hostID);
		
		}

	/**
	 * Shut down a physical host.
	 */
	public static boolean hostoff(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		if (!st.hasMoreTokens()) {
			vcluster.util.Util.print("[ERROR : ] Expect a cloud name!");
			return false;
		}
		String cloudname = st.nextToken().trim();
		if (!st.hasMoreTokens()) {
			vcluster.util.Util.print("[ERROR : ] Expect a host id!");
			return false;
		}
		String hostID = st.nextToken().trim();
		
		Cloud cloud = CloudManager.getCloudList().get(cloudname);
		
		
		return cloud.hostoff(hostID);
		
	}



}
