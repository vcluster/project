package vcluster.engine.groupexecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vcluster.plugman.Plugin;
import vcluster.plugman.PluginManager;
import vcluster.ui.Command;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class PlugmanExecutor {

	//static List<String> cloudPluginsList = PluginManager.getCloudPluginList();
	//static List<String> batchPluginsList = PluginManager.getBatchPluginList();	
	//static StringTokenizer st = new StringTokenizer("");
	
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
		
		if(Command.HELP.contains(para)){
			System.out.println(getUsage());
			return false;			
		}		
		return false;			
	}
	
	
	public static boolean list(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		boolean cfilter =true;
		boolean bfilter =true;

	if(st.hasMoreTokens()){
		String para2 = st.nextToken().trim();
		if(Command.TYPE_BATCH.contains(para2)){
			cfilter=false;
		}else if(Command.TYPE_CLOUD.contains(para2)){
			bfilter=false;
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

	public static boolean load(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		
		String pluginPath = "";
		if(!st.hasMoreTokens()){
			PrintMsg.print(DMsgType.ERROR, "expected a plugin type!");
			System.out.println("[USAGE] : plugman <load -pluginType pluginName>");
			return false;
		}
		String pluginType = st.nextToken().trim();
		if(Command.TYPE_BATCH.contains(pluginType)){
			File dir = new File(System.getProperty("user.dir") + File.separator
					+ PluginManager.BATCH_PLUGIN_DIR);
			pluginPath = dir.getPath();
		}else if(Command.TYPE_CLOUD.contains(pluginType)){
			File dir = new File(System.getProperty("user.dir") + File.separator
					+ PluginManager.CLOUD_PLUGIN_DIR);
			pluginPath = dir.getPath();
		}

		List<String> pluginNames = new ArrayList<String>();
		if(st.hasMoreTokens()) {
			    pluginNames.add(st.nextToken().trim());

		}else{
			PrintMsg.print(DMsgType.ERROR, "expected a plugin name!");
			System.out.println("[USAGE] : plugin <register plugin_name | list>");
			return false;
		}
	    if(Command.TYPE_CLOUD.contains(pluginType)){		    	
	    
			while(st.hasMoreTokens()){
			    pluginNames.add(st.nextToken().trim());
			}
		
	    }else if(Command.TYPE_BATCH.contains(pluginType)&st.hasMoreTokens()){
	    	System.out.println("Only one batch plugin can be loaded at the same time, the rest will be ignored!");
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
			
			if(st.hasMoreTokens()) {
				PrintMsg.print(DMsgType.ERROR, "unexpected token \""+st.nextToken()+"\" found!");
				return false;
			}
			return true;
	}

	public static boolean unload(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		
		if(!st.hasMoreTokens()){
			PrintMsg.print(DMsgType.ERROR, "expected a plugin name!");
			System.out.println("[USAGE] : plugman <unload pluginName>");
			return false;
		}
		while(st.hasMoreTokens()){
			try {
				PluginManager.UnloadPlugin(st.nextToken());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				return false;
			}
		}
		if(st.hasMoreTokens()) {
			PrintMsg.print(DMsgType.ERROR, "unexpected token \""+st.nextToken()+"\" found!");
			return false;
		}
		return false;			
	}

	public static boolean getInfo(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		
		if(!st.hasMoreTokens()){
			PrintMsg.print(DMsgType.ERROR, "expected a plugin name!");
			System.out.println("[USAGE] : plugman <unload pluginName>");
			return false;
		}
		String pluginName = st.nextToken();
		
		System.out.println(PluginManager.getInfo(pluginName));		
		if(st.hasMoreTokens()) {
			PrintMsg.print(DMsgType.ERROR, "unexpected token \""+st.nextToken()+"\" found!");
			return false;
		}
		return false;
	}
	
	public static boolean undefined(String cmdLine){
		PrintMsg.print(DMsgType.ERROR, "no such a parameter like \""+cmdLine+"\" !");
		System.out.println(getUsage());
		return false;
		
		
	}
	
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
