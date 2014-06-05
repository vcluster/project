package vcluster.executors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vcluster.Vcluster.uiType;
import vcluster.elements.Plugin;
import vcluster.managers.PluginManager;
import vcluster.ui.CmdComb;
/**
 * A class representing a plug-in manager executor.In this class involves the functions which are charge of manage the plug-ins.
 */
public class PlugmanExecutor {

	
	
	/**
	 *List up all the plug-ins located in plug-in folder 
	 */
	public static String list(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		if(cmd.getParaset().size()>0){
		String para2 = cmd.getParaset().get(0);
		if(para2.equals("-b")){
		}else if(para2.equalsIgnoreCase("-c")){
		}else{
			str.append("[ERROR : ] Wrong parameter!"+System.getProperty("line.separator"));
		}	
	}
		String cName = String.format("%-20s", "Name");
		String cStat =String.format("%-12s", "Status");
		String cType = String.format("%-12s", "Type");
		str.append("List the plugins in plugin directory :"+System.getProperty("line.separator"));		
		str.append("----------------------------------------"+System.getProperty("line.separator"));
		str.append(cName+cStat+cType+System.getProperty("line.separator"));
		str.append("----------------------------------------"+System.getProperty("line.separator"));		

		for(Plugin plugin : PluginManager.pluginList.values()){
			String name = String.format("%-20s", plugin.getPluginName());
			String stat=String.format("%-12s", plugin.getPluginStatus());
			String type=String.format("%-12s", plugin.getPluginType());
			str.append(name+stat+type+System.getProperty("line.separator"));
		}
		str.append("----------------------------------------"+System.getProperty("line.separator"));
		if(cmd.getUi()==uiType.CMDLINE)System.out.println(str);
		return str.toString();
	}

	
	/**
	 *Load a plug-in into vcluster runtime, the plug-in package would stay in the memory. 
	 */
	public static String load(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		String pluginPath = "";
		if(cmd.getParaset().size()==0){
			str.append( "expected a plugin type!"+System.getProperty("line.separator"));
			str.append("[USAGE] : plugman <load -pluginType pluginName>"+System.getProperty("line.separator"));
			return str.toString();
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
			str.append( "expected a plugin name!"+System.getProperty("line.separator"));
			str.append("[USAGE] : plugin <register plugin_name | list>"+System.getProperty("line.separator"));
			return str.toString();
		}
	    if(pluginType.equalsIgnoreCase("-c")){		    	
	    
	    	int i = 1;
			while(i<cmd.getParaset().size()){
			    pluginNames.add(cmd.getParaset().get(i));
			    i++;
			}
		
	    }
			for(String pluginName:pluginNames){
				//str.append(pluginName);
				if(PluginManager.pluginList.keySet().contains(pluginName)){

					try {
						//str.append(pluginPath + File.separator + pluginName);
						PluginManager.LoadPlugin(pluginPath + File.separator + pluginName+".jar",pluginType);

					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						str.append("[ERROR:] No such a plugin,please check your input!"+System.getProperty("line.separator"));
						return str.toString();
					}
					
				}else{
					str.append("[ERROR:] No such a plugin,please check your input!"+System.getProperty("line.separator"));
				}
			}
			
			if(cmd.getUi()==uiType.CMDLINE)System.out.println(str);
			return str.toString();
	}

	/**
	 *Unload a plug-in from vcluster, all the references to the instance of plug-in would be removed, 
	 *the plug-in would be removed from memory by garbage collection  
	 */
	public static String unload(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		if(cmd.getParaset().size()==0){
			str.append( "expected a plugin name!"+System.getProperty("line.separator"));
			str.append("[USAGE] : plugman <unload pluginName>"+System.getProperty("line.separator"));
			return str.toString();
		}
		int i = 0;
		while(i<cmd.getParaset().size()){
			try {
				PluginManager.UnloadPlugin(cmd.getParaset().get(i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				str.append(e.getMessage()+System.getProperty("line.separator"));
				return str.toString();
			}
		}
		if(cmd.getUi()==uiType.CMDLINE)System.out.println(str);
		return str.toString();			
	}

	/**
	 *Get the introduction of the plug-in
	 * 
	 */
	public static String getInfo(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		if(cmd.getParaset().size()==0){
			str.append( "expected a plugin name!"+System.getProperty("line.separator"));
			str.append("[USAGE] : plugman <unload pluginName>"+System.getProperty("line.separator"));
			return str.toString();
		}
		String pluginName = cmd.getParaset().get(0);
		
		str.append(PluginManager.getInfo(pluginName)+System.getProperty("line.separator"));		
		
		return str.toString();
	}
	
	public static String undefined(CmdComb cmd){
		StringBuffer str = new StringBuffer();
		str.append( "no such a parameter like \""+cmd.getParaset().get(0)+"\" !"+System.getProperty("line.separator"));
		str.append(getUsage()+System.getProperty("line.separator"));
		if(cmd.getUi()==uiType.CMDLINE)System.out.println(str);
		return str.toString();
		
		
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
