package vcluster.engine.groupexecutor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import vcluster.control.VMManager;
import vcluster.global.Config;
import vcluster.monitoring.MonitoringMan;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;
import vcluster.util.Util;
import vcluster.control.cloudman.*;

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
					CloudManager.dump(token);
				else {
					System.out.println("[USAGE] : cloudman dump [<private | public>]");
					return false;
				}
				
			} else {
				CloudManager.dump();
			}
			return true;
		}

		if (token.equalsIgnoreCase("register")) {
			
			if (!st.hasMoreTokens()) {
				System.out.println("[USAGE] : cloudman register <cloudelement conf file>");
				return false;
			}

			token = st.nextToken().trim();
			return CloudManager.loadCloudElments(token);
		}
		
		if (token.equalsIgnoreCase("set")) {
			
			if (st.hasMoreTokens()) {
				
				
			} else {
				CloudManager.dump();
			}
			return true;
		}

		if (token.equalsIgnoreCase("current")) {
			Cloud cloud = CloudManager.getCurrentCloud();
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
    	        	BatchExecutor.check_pool();
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

	public static boolean help() {
		// TODO Auto-generated method stub
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("CommandList.txt")));
			String aLine = "";
			while ((aLine = br.readLine()) != null) {				
				System.out.println(aLine);
			}
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("File doesn't exist ,please check it on the directory!");
			//e.printStackTrace();
			}
		return false;
	}

}
