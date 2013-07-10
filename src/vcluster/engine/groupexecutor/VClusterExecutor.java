package vcluster.engine.groupexecutor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vcluster.control.VMManager;
import vcluster.control.cloudman.CloudElement;
import vcluster.control.cloudman.CloudManager;
import vcluster.global.Config;
import vcluster.global.Config.CloudType;
import vcluster.monitoring.MonitoringMan;
import vcluster.plugin.PluginManager;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;
import vcluster.util.Util;
import vcluster.ui.Command;

public class VClusterExecutor {
	public static boolean debug_mode(String cmdLine)
	{

		StringTokenizer st = new StringTokenizer(cmdLine);
		String token = null;
		
		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			System.out.println("[Usage] : debug <on | off | dump>");
			return false;
		}
		
		token = st.nextToken().trim();
		
		if (token.equalsIgnoreCase("on")) {
			Config.DEBUG_MODE = true;
			return false;
		} else if (token.equalsIgnoreCase("off")) {
			Config.DEBUG_MODE = false;
			return false;
		} else if (token.equalsIgnoreCase("dump")) {
			String onoff = null;
			if (Config.DEBUG_MODE == true) onoff = "on";
			else onoff = "off";
			
			System.out.println("Debug Mode : "+onoff);
			return false;
		}

		PrintMsg.print(DMsgType.ERROR, "unexpected token, "+token+" found.");

		return true;
	}
	
	public static boolean vmman(String cmdLine)
	{		
		StringTokenizer st = new StringTokenizer(cmdLine);
		String token = null;
		
		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			System.out.println("[USAGE] : vmman <start | stop | dump>");
			return false;
		}
		
		token = st.nextToken().trim();
		
		if (token.equalsIgnoreCase("stop")) {
			if (Config.vmMan == null) {
				PrintMsg.print(DMsgType.MSG, "vm manager is not running.");
			} else {
				Config.vmMan.shutDwon();
				PrintMsg.print(DMsgType.MSG, "monitoring is stopped.");
				Config.vmMan = null;
			}
			return false;
		}
		
		if (token.equalsIgnoreCase("dump")) {
			if (Config.vmMan == null) {
				PrintMsg.print(DMsgType.MSG, "vm manager is not running.");
			} else {
				Config.vmMan.dump();
			}
			return false;
		}
		
		
		
		if (!token.equalsIgnoreCase("start")) {
			PrintMsg.print(DMsgType.ERROR, "undefined command, "+token);
			return false;
		}

		/* from here, we have to handle start command */
		if (Config.vmMan !=  null) {
			PrintMsg.print(DMsgType.ERROR, "vm manager is running....");
			return false;
		}
		
		Config.vmMan = new VMManager();
		Config.vmMan.start();

		return true;
	}
	
	public static boolean cloudman(String cmdLine)
	{
		StringTokenizer st = new StringTokenizer(cmdLine);
		String token = null;
		
		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			System.out.println("[USAGE] : cloudman dump [<private | public>>]");
			System.out.println("        : cloudman register <cloudelement conf file>");
			System.out.println("        : cloudman set <private | public> <cloud num>");
			return false;
		}
		
		token = st.nextToken().trim();
		
		if (token.equalsIgnoreCase("dump")) {
			
			if (st.hasMoreTokens()) {
				token = st.nextToken().trim();
				
				if(token.equalsIgnoreCase("private") || token.equalsIgnoreCase("public"))
					Config.cloudMan.dump(token);
				else {
					System.out.println("[USAGE] : cloudman dump [<private | public>]");
					return false;
				}
				
			} else {
				Config.cloudMan.dump();
			}
			return true;
		}

		if (token.equalsIgnoreCase("register")) {
			
			if (!st.hasMoreTokens()) {
				System.out.println("[USAGE] : cloudman register <cloudelement conf file>");
				return false;
			}

			token = st.nextToken().trim();
			return CloudManager.loadCloudElments(token, Config.cloudMan);
		}
		
		if (token.equalsIgnoreCase("set")) {
			
			if (st.hasMoreTokens()) {
				
				token = st.nextToken().trim();
				
				if(token.equalsIgnoreCase("private")) {
					if (st.hasMoreTokens()) {
						token = st.nextToken();
						Config.cloudMan.setCurrentCloud(CloudType.PRIVATE, 
								Integer.parseInt(token.trim()));
					}
				} else if(token.equalsIgnoreCase("public")) {
					if (st.hasMoreTokens()) {
						token = st.nextToken();
						Config.cloudMan.setCurrentCloud(CloudType.PUBLIC, 
								Integer.parseInt(token.trim()));
					}
				}
				else {
					System.out.println("[USAGE] : cloudman set <private | public> <cloud num>");
					return false;
				}
			} else {
				Config.cloudMan.dump();
			}
			return true;
		}

		if (token.equalsIgnoreCase("current")) {
			CloudElement cloud = Config.cloudMan.getCurrentCloud();
			cloud.dump();
			return true;
		}

		
		
		PrintMsg.print(DMsgType.ERROR, "undefined attributes, "+token+", found");
		
		return false;
	}
	
	public static boolean monitor(String cmdLine)
	{
		
		StringTokenizer st = new StringTokenizer(cmdLine);
		String token = null;
		
		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			System.out.println("[USAGE] : mon <start | stop | dump>");
			return false;
		}
		
		token = st.nextToken().trim();
		
		if (token.equalsIgnoreCase("stop")) {
			if (Config.monMan == null) {
				PrintMsg.print(DMsgType.MSG, "monitoring is not running.");
			} else {
				Config.monMan.shutDwon();
				PrintMsg.print(DMsgType.MSG, "monitoring is stopped.");
				Config.monMan = null;
			}
			return false;
		}
		
		if (token.equalsIgnoreCase("dump")) {
			if (Config.monMan == null) {
				PrintMsg.print(DMsgType.MSG, "monitoring is not running.");
			} else {
				Config.monMan.dump();
			}
			return false;
		}
		
		
		
		if (!token.equalsIgnoreCase("start")) {
			PrintMsg.print(DMsgType.ERROR, "undefined command, "+token);
			return false;
		}

		/* from here, we have to handle start command */
		
		if (Config.monMan !=  null) {
			PrintMsg.print(DMsgType.ERROR, "Monitoring is running....");
			return false;
		}
		
		if (Config.vmMan != null) {
			Config.monMan = new MonitoringMan(Config.vmMan);
			Config.monMan.start();
		}
		else {
			PrintMsg.print(DMsgType.ERROR, "vmman is not running");
		}
		
		return true;
	}
	
	public static boolean show(String cmdLine)
	{

		StringTokenizer st = new StringTokenizer(cmdLine);

		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			System.out.println("[USAGE] : show <config>");
			return false;
		}
		
		/* what has to be shown */
		String showWhat = st.nextToken().trim();

		if(st.hasMoreTokens()) {
			PrintMsg.print(DMsgType.ERROR, "unexpected token \""+st.nextToken()+"\" found!");
			return false;
		}

		if (showWhat.equalsIgnoreCase("config"))
			Util.printConfig();
		
		return true;

	}
	
	public static boolean load(String cmdLine)
	{

		StringTokenizer st = new StringTokenizer(cmdLine);

		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			System.out.println("[USAGE] : load <conf file name>");
			return false;
		}
		
		/* what has to be shown */
		String configFile = st.nextToken().trim();

		if(st.hasMoreTokens()) {
			PrintMsg.print(DMsgType.ERROR, "unexpected token \""+st.nextToken()+"\" found!");
			return false;
		}

		Util.loadConfig(configFile);

		return true;
	}
	
	public static boolean set(String cmdLine)
	{

		StringTokenizer st = new StringTokenizer(cmdLine);

		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			System.out.println("[USAGE] : set <condorip | oneip> <ipaddress>");
			return false;
		}
		
		/* get a token to set */
		String setWhat = st.nextToken().trim();
		
		if(!st.hasMoreTokens()) {
			PrintMsg.print(DMsgType.ERROR, "value expected!");
			return false;
		}

		String value = st.nextToken().trim();
		
		if (setWhat.equalsIgnoreCase("condorip")) {
			Config.CONDOR_IPADDR = value;
		}
		else if (setWhat.equalsIgnoreCase("oneip")) {
			Config.ONE_IPADDR = value;
		}

		if(st.hasMoreTokens()) {
			PrintMsg.print(DMsgType.ERROR, "unexpected token \""+st.nextToken()+"\" found!");
			return false;
		}

		return true;
	}
	
	public static boolean engmode(String cmdLine)
	{


		/* remove the keyword engmode */
		String cmd = cmdLine.replaceFirst("engmode", "");
		// System.out.println("Command = "+cmd);
		
		
		
        Socket socket = null;
        BufferedReader in = null;
        DataOutputStream out = null;
        
        try {
        	//socket = new Socket("127.0.0.1", 9734);
        	socket = new Socket("ce03.sdfarm.kr", 9734);
        	
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            out.flush();
            /* make an integer to unsigned int */
            int userInput = 16;
            userInput <<= 8;
            userInput |=  1;
            userInput &= 0x7FFFFFFF;

            /*
             * easy and simple way is using writeInt() function, 
             * but writing an integer to socket through writeInt() 
             * causes "connection reset" problem because an additional data being
             * transmitted.
             * 
             * In order to resolve this problem, we use write() function after
             * converting the integer to byte[].
             */
            String s = Integer.toString(userInput);
            byte[] b = s.getBytes();
            
            out.write(b, 0, b.length);
            out.write(cmd.getBytes(), 0, cmd.getBytes().length);

            char[] cbuf = new char[1024];
            
            while (in.read(cbuf, 0, 1024) != -1) {
            	String str = new String(cbuf);
    	        str = str.trim();
    	        
    	        if (str.contains("Total") && !str.contains("Owner")) {
    	        	//PoolStatus.extractInfo(str);
    	        	//PoolStatus.printQStatus();
    	        	PluginManager.current_proxyExecutor.check_pool();
    	        }
    	        
    	        for(int i = 0; i< 1024; i++)
    	        	cbuf[i] = '\0';
            }
        } catch (UnknownHostException e) {
    		PrintMsg.print(DMsgType.ERROR, e.getMessage());
            closeStream(in, out, socket);
    		
    		return false;
        } catch (IOException e) {
    		PrintMsg.print(DMsgType.ERROR, e.getMessage());
            closeStream(in, out, socket);
            
            return false;
        }
        
        closeStream(in, out, socket);
 		
		return true;
	}
	
	public static boolean plugman(String cmdLine) {
		// TODO Auto-generated method stub

		PluginManager pm = new PluginManager();
		List<String> cloudPluginsList = pm.getCloudPluginList();
		List<String> batchPluginsList = pm.getBatchPluginList();		
		
		StringTokenizer st = new StringTokenizer(cmdLine);

		/* skip the command */
		st.nextToken();

		if (!st.hasMoreTokens()) {
			System.out.println(pm.getUsage());
	
			return false;
		}
		
		/* get a token to set */
		String para = st.nextToken().trim();
		
		if(Command.HELP.contains(para)){
			System.out.println(pm.getUsage());
			return false;
			
		}		
		else if(para.equalsIgnoreCase("list")){
			System.out.println("List the plugins in plugin directory :");
			if(!st.hasMoreTokens()) {
				System.out.println("Batch plugins : ");
				for(String batchplugin : pm.getBatchPluginList()){
					
					System.out.println("      " + batchplugin);
				}
				System.out.println("----------------------------------------");
				System.out.println("Cloud plugins : ");
				for(String cloudplugin : cloudPluginsList){
					
					System.out.println("      " + cloudplugin);
				}
				
				
				return true;
			}
			
			String para2 = st.nextToken().trim();
			if(Command.TYPE_BATCH.contains(para2)){
				System.out.println("Batch plugins : ");
				for(String batchplugin : pm.getBatchPluginList()){
					
					System.out.println("      " + batchplugin);
				}
			}else if(Command.TYPE_CLOUD.contains(para2)){
				System.out.println("Cloud plugins : ");
				for(String cloudplugin : cloudPluginsList){
					
					System.out.println("      " + cloudplugin);
				}
			}else if(Command.LOADED.contains(para2)){
				if(!st.hasMoreTokens()){
					System.out.println("Loaded Batch plugin : ");
					for(String loadedcloud : pm.GetLoadedBatchPlugins()){
						System.out.println("         " + loadedcloud);
					}
					System.out.println("Loaded Cloud plugins : ");
					for(String loadedcloud : pm.GetLoadedCloudPlugins()){
						System.out.println("         " + loadedcloud);
					}
				}else{
					String type = st.nextToken();
					if(Command.TYPE_BATCH.contains(type)){
						System.out.println("Loaded Batch plugin : ");
						for(String loadedcloud : pm.GetLoadedBatchPlugins()){
							System.out.println("         " + loadedcloud);
						}
					}
					else if(Command.TYPE_CLOUD.contains(type)){
						System.out.println("Loaded Cloud plugins : ");
						for(String loadedcloud : pm.GetLoadedCloudPlugins()){
							System.out.println("         " + loadedcloud);
						}
					}
					
				}
				
				
				
			}else {
				PrintMsg.print(DMsgType.ERROR, "no such a parameter like \""+para2+"\" !");
				return false;
			}
			
			if(st.hasMoreTokens()) {
				PrintMsg.print(DMsgType.ERROR, "unexpected token \""+st.nextToken()+"\" found!");
				return false;
			}
			
			return true;
		}		
		
		else if(para.equalsIgnoreCase("load")){
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
					if(batchPluginsList.contains(pluginName)|cloudPluginsList.contains(pluginName)){

						try {
							//System.out.println(pluginPath + File.separator + pluginName);
							pm.LoadPlugin(pluginPath + File.separator + pluginName+".jar",pluginType);

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
		else if(para.equalsIgnoreCase("unload")){
			if(!st.hasMoreTokens()){
				PrintMsg.print(DMsgType.ERROR, "expected a plugin name!");
				System.out.println("[USAGE] : plugman <unload pluginName>");
				return false;
			}
			while(st.hasMoreTokens()){
				try {
					pm.UnloadPlugin(st.nextToken());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					return false;
				}
			}			
			
		}
		else if(para.equalsIgnoreCase("info")){			
			if(!st.hasMoreTokens()){
				PrintMsg.print(DMsgType.ERROR, "expected a plugin name!");
				System.out.println("[USAGE] : plugman <unload pluginName>");
				return false;
			}
			String pluginName = st.nextToken();
			if(pm.isLoaded(pluginName)){
				System.out.println(PluginManager.loadedBatchPlugins.get(pluginName).getInfo());		
				
			}else{
				System.out.println("This plugin doesn't exist!");
			}
			
			
		}
		else{
				PrintMsg.print(DMsgType.ERROR, "no such a parameter like \""+para+"\" !");
				System.out.println("\n         [USAGE] : plugin <register plugin_name | list>");
			}
		

		if(st.hasMoreTokens()) {
			PrintMsg.print(DMsgType.ERROR, "unexpected token \""+st.nextToken()+"\" found!");
			return false;
		}

		return true;
	}

	private static void closeStream(BufferedReader in, DataOutputStream out, Socket socket)
	{
		try {
		        if (in != null) in.close();
		        if (out != null) out.close();
		        if (socket != null) socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
