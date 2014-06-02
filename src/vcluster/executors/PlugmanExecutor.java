package vcluster.executors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vcluster.elements.Plugin;
import vcluster.managers.PluginManager;
import vcluster.ui.CmdSet;
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
			System.out.println(getUsage());
	
			return false;
		}
		
		/* get a token to set */
		String para = st.nextToken().trim();
		
		if(para.equalsIgnoreCase("-h")){
			System.out.println(getUsage());
			return false;			
		}		
		return false;			
	}
	
	/**
	 *List up all the plug-ins located in plug-in folder 
	 */
	public static boolean list(CmdSet cmd) {
		// TODO Auto-generated method stub
		if(cmd.getParaset().size()>0){
		String para2 = cmd.getParaset().get(0);
		if(para2.equals("-b")){
		}else if(para2.equalsIgnoreCase("-c")){
		}else{
			System.out.println("[ERROR : ] Wrong parameter!");
		}	
	}
		String cName = String.format("%-20s", "Name");
		String cStat =String.format("%-12s", "Status");
		String cType = String.format("%-12s", "Type");
		System.out.println("List the plugins in plugin directory :");		
		System.out.println("----------------------------------------");
		System.out.println(cName+cStat+cType);
		System.out.println("----------------------------------------");		

		for(Plugin plugin : PluginManager.pluginList.values()){
			String name = String.format("%-20s", plugin.getPluginName());
			String stat=String.format("%-12s", plugin.getPluginStatus());
			String type=String.format("%-12s", plugin.getPluginType());
			System.out.println(name+stat+type);
		}
		System.out.println("----------------------------------------");
		return true;
	}

	
	/**
	 *Load a plug-in into vcluster runtime, the plug-in package would stay in the memory. 
	 */
	public static boolean load(CmdSet cmd) {
		// TODO Auto-generated method stub
		
		String pluginPath = "";
		if(cmd.getParaset().size()==0){
			System.out.println( "expected a plugin type!");
			System.out.println("[USAGE] : plugman <load -pluginType pluginName>");
			return false;
		}
		String pluginType = cmd.getParaset().get(0);
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
		if(cmd.getParaset().size()>1) {
			    pluginNames.add(cmd.getParaset().get(1));

		}else{
			System.out.println( "expected a plugin name!");
			System.out.println("[USAGE] : plugin <register plugin_name | list>");
			return false;
		}
	    if(pluginType.equalsIgnoreCase("-c")){		    	
	    
	    	int i = 1;
			while(i<cmd.getParaset().size()){
			    pluginNames.add(cmd.getParaset().get(i));
			    i++;
			}
		
	    }
			for(String pluginName:pluginNames){
				//System.out.println(pluginName);
				if(PluginManager.pluginList.keySet().contains(pluginName)){

					try {
						//System.out.println(pluginPath + File.separator + pluginName);
						PluginManager.LoadPlugin(pluginPath + File.separator + pluginName+".jar",pluginType);

					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("[ERROR:] No such a plugin,please check your input!");
						return false;
					}
					
				}else{
					System.out.println("[ERROR:] No such a plugin,please check your input!");
				}
			}
			
			
			return true;
	}

	/**
	 *Unload a plug-in from vcluster, all the references to the instance of plug-in would be removed, 
	 *the plug-in would be removed from memory by garbage collection  
	 */
	public static boolean unload(CmdSet cmd) {
		// TODO Auto-generated method stub
		
		if(cmd.getParaset().size()==0){
			System.out.println( "expected a plugin name!");
			System.out.println("[USAGE] : plugman <unload pluginName>");
			return false;
		}
		int i = 0;
		while(i<cmd.getParaset().size()){
			try {
				PluginManager.UnloadPlugin(cmd.getParaset().get(i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				return false;
			}
		}
		return true;			
	}

	/**
	 *Get the introduction of the plug-in
	 * 
	 */
	public static boolean getInfo(CmdSet cmd) {
		// TODO Auto-generated method stub
		
		if(cmd.getParaset().size()==0){
			System.out.println( "expected a plugin name!");
			System.out.println("[USAGE] : plugman <unload pluginName>");
			return false;
		}
		String pluginName = cmd.getParaset().get(0);
		
		System.out.println(PluginManager.getInfo(pluginName));		
		
		return true;
	}
	
	public static boolean undefined(CmdSet cmd){
		System.out.println( "no such a parameter like \""+cmd.getParaset().get(0)+"\" !");
		System.out.println(getUsage());
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
