package vcluster.executors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vcluster.managers.PluginManager;
import vcluster.simulators.Plugin;
import vcluster.ui.Command;
/**
 * A class representing a plug-in manager executor.In this class involves the functions which are charge of manage the plug-ins.
 */
public class PlugmanExecutor {

	
	public static boolean plugman(String cmdLine){
		StringTokenizer st = new StringTokenizer(cmdLine);

		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			vcluster.util.Util.print(getUsage());
	
			return false;
		}
		
		/* get a token to set */
		String para = st.nextToken().trim();
		
		if(para.equalsIgnoreCase("-h")){
			vcluster.util.Util.print(getUsage());
			return false;			
		}		
		return false;			
	}
	
	/**
	 *List up all the plug-ins located in plug-in folder 
	 */
	public static boolean list(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		if(st.hasMoreTokens()){
		String para2 = st.nextToken().trim();
		if(para2.equals("-b")){
		}else if(para2.equalsIgnoreCase("-c")){
		}else{
			vcluster.util.Util.print("[ERROR : ] Wrong parameter!");
		}	
	}
		String cName = String.format("%-20s", "Name");
		String cStat =String.format("%-12s", "Status");
		String cType = String.format("%-12s", "Type");
		vcluster.util.Util.print("List the plugins in plugin directory :");		
		vcluster.util.Util.print("----------------------------------------");
		vcluster.util.Util.print(cName+cStat+cType);
		vcluster.util.Util.print("----------------------------------------");		

		for(Plugin plugin : PluginManager.pluginList.values()){
			String name = String.format("%-20s", plugin.getPluginName());
			String stat=String.format("%-12s", plugin.getPluginStatus());
			String type=String.format("%-12s", plugin.getPluginType());
			vcluster.util.Util.print(name+stat+type);
		}
		vcluster.util.Util.print("----------------------------------------");
		return true;
	}

	
	/**
	 *Load a plug-in into vcluster runtime, the plug-in package would stay in the memory. 
	 */
	public static boolean load(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		
		String pluginPath = "";
		if(!st.hasMoreTokens()){
			vcluster.util.Util.print( "expected a plugin type!");
			vcluster.util.Util.print("[USAGE] : plugman <load -pluginType pluginName>");
			return false;
		}
		String pluginType = st.nextToken().trim();
		if(pluginType.equalsIgnoreCase("-b")){
			File dir = new File(System.getProperty("user.dir") + File.separator
					+ PluginManager.BATCH_PLUGIN_DIR);
			pluginPath = dir.getPath();
		}else if(pluginType.equalsIgnoreCase("-c")){
			File dir = new File(System.getProperty("user.dir") + File.separator
					+ PluginManager.CLOUD_PLUGIN_DIR);
			pluginPath = dir.getPath();
		}else if(pluginType.equalsIgnoreCase("-l")){
			File dir = new File(System.getProperty("user.dir") + File.separator
					+ PluginManager.LOADBALANCER_PLUGIN_DIR);
			pluginPath = dir.getPath();
		}

		List<String> pluginNames = new ArrayList<String>();
		if(st.hasMoreTokens()) {
			    pluginNames.add(st.nextToken().trim());

		}else{
			vcluster.util.Util.print( "expected a plugin name!");
			vcluster.util.Util.print("[USAGE] : plugin <register plugin_name | list>");
			return false;
		}
	    if(pluginType.equalsIgnoreCase("-c")){		    	
	    
			while(st.hasMoreTokens()){
			    pluginNames.add(st.nextToken().trim());
			}
		
	    }
			for(String pluginName:pluginNames){
				//vcluster.util.Util.print(pluginName);
				if(PluginManager.pluginList.keySet().contains(pluginName)){

					try {
						//vcluster.util.Util.print(pluginPath + File.separator + pluginName);
						PluginManager.LoadPlugin(pluginPath + File.separator + pluginName+".jar",pluginType);

					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						vcluster.util.Util.print("[ERROR:] No such a plugin,please check your input!");
						return false;
					}
					
				}else{
					vcluster.util.Util.print("[ERROR:] No such a plugin,please check your input!");
				}
			}
			
			if(st.hasMoreTokens()) {
				vcluster.util.Util.print( "unexpected token \""+st.nextToken()+"\" found!");
				return false;
			}
			return true;
	}

	/**
	 *Unload a plug-in from vcluster, all the references to the instance of plug-in would be removed, 
	 *the plug-in would be removed from memory by garbage collection  
	 */
	public static boolean unload(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		
		if(!st.hasMoreTokens()){
			vcluster.util.Util.print( "expected a plugin name!");
			vcluster.util.Util.print("[USAGE] : plugman <unload pluginName>");
			return false;
		}
		while(st.hasMoreTokens()){
			try {
				PluginManager.UnloadPlugin(st.nextToken());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				vcluster.util.Util.print(e.getMessage());
				return false;
			}
		}
		if(st.hasMoreTokens()) {
			vcluster.util.Util.print( "unexpected token \""+st.nextToken()+"\" found!");
			return false;
		}
		return false;			
	}

	/**
	 *Get the introduction of the plug-in
	 * 
	 */
	public static boolean getInfo(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		
		if(!st.hasMoreTokens()){
			vcluster.util.Util.print( "expected a plugin name!");
			vcluster.util.Util.print("[USAGE] : plugman <unload pluginName>");
			return false;
		}
		String pluginName = st.nextToken();
		
		vcluster.util.Util.print(PluginManager.getInfo(pluginName));		
		if(st.hasMoreTokens()) {
			vcluster.util.Util.print( "unexpected token \""+st.nextToken()+"\" found!");
			return false;
		}
		return false;
	}
	
	public static boolean undefined(String cmdLine){
		vcluster.util.Util.print( "no such a parameter like \""+cmdLine+"\" !");
		vcluster.util.Util.print(getUsage());
		return false;
		
		
	}
	/**
	 *The information about command line for plug-ins
	 *@return A string of help information. 
	 */
	public static String getUsage(){
		StringBuffer usage = new StringBuffer();
		usage.append("[USAGE] :plugman"+System.getProperty("line.separator"));
		usage.append("                    -h : list all options and usages"+System.getProperty("line.separator"));
		usage.append("                  list : list all the plugins"+System.getProperty("line.separator"));
		usage.append("                        -c : list all cloud plugins "+System.getProperty("line.separator"));		
		usage.append("                        -b : list all batch plugins "+System.getProperty("line.separator"));		
		usage.append("                        -l : list all loaded plugins "+System.getProperty("line.separator"));
		usage.append("                  load : "+System.getProperty("line.separator"));	
		usage.append("                        -c pluginname-1,pluginname-2......,pluginname-n : load n cloud plugins "+System.getProperty("line.separator"));
		usage.append("                        -b pluginname : load a batch plugin "+System.getProperty("line.separator"));	
		usage.append("                unload : unload plugins"+System.getProperty("line.separator"));	
		usage.append("                  info : prints the information about a plugin "+System.getProperty("line.separator"));	
		return usage.toString();
		
	}

}
