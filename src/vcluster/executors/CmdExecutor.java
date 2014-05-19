package vcluster.executors;

import java.util.StringTokenizer;
import vcluster.managers.PluginManager;
import vcluster.managers.VmManager;
import vcluster.ui.Command;

/**
 * @author Seo-Young Noh, Modified by Dada Huang
 * This class 
 * 
 */

public class CmdExecutor {

	
	public static Object execute(String cmdLine)
	{
		StringTokenizer st = new StringTokenizer(cmdLine);
		
		String cmd = st.nextToken().trim();
		
		Command command = getCommand(null,cmd);
				
		switch(command.getCmdGroup()){
		case VCLMAN:return executeVCLMAN(command, cmdLine);
		default:
			break;
		}

		switch (command) {
		case VMMAN: return executeVMMAN(cmdLine);
		case CLOUDMAN: return executeCLOUDMAN(cmdLine);
		case PLUGMAN: return executePLUGMAN(cmdLine);		
		case NOT_DEFINED: return null;
		default:
			break;
		}		
		return "";
	}
	
	private static Object executeCLOUDMAN(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		String cmdg= st.nextToken().trim();		
		String cmd = st.nextToken().trim();
		Command command = getCommand(Command.CMD_GROUP.CLOUDMAN.toString(),cmd);
		cmdLine = cmdLine.replace(cmdg, "").trim();
		switch (command) {
		case REGISTER:
			return CloudmanExecutor.register(cmdLine); 
		case LOADCLOUD:
			return CloudmanExecutor.load(cmdLine);
		case LISTCLOUD:
			return CloudmanExecutor.getCloudList();
		case UNLOADCLOUD:
			return CloudmanExecutor.unload(cmdLine);	
		case HOSTON:
			return CloudmanExecutor.hoston(cmdLine);
		case HOSTOFF:
			return CloudmanExecutor.hostoff(cmdLine);	
		default:
			return CloudmanExecutor.undefined(cmdLine);					
		}		
	}

	private static boolean executePLUGMAN(String cmdLine) {
		cmdLine = cmdLine.replace("plugman ", "");
		StringTokenizer st = new StringTokenizer(cmdLine);
		String cmd = st.nextToken().trim();
		//vcluster.util.Util.print(cmdLine);
		Command command = getCommand(Command.CMD_GROUP.PLUGMAN.toString(),cmd);
		
		switch (command) {
		case LOAD:
			return PlugmanExecutor.load(cmdLine); 
		case UNLOAD:
			return PlugmanExecutor.unload(cmdLine);
		case INFO:
			return PlugmanExecutor.getInfo(cmdLine);
		case LIST:
			return PlugmanExecutor.list(cmdLine);
		default:
			return PlugmanExecutor.undefined(cmdLine);					
		}

	}

	private static String executeVCLMAN(Command command, String cmdLine)
	{
		
		switch (command) {
		
		case CHECK_P: 			
			return BatchExecutor.getPoolStatus().printPoolStatus();		
		case CHECK_Q: 			
	    	return PluginManager.current_proxyExecutor.getQStatus().printQStatus();
		default:
			break;		
			
		}
		
		return null;
	}
	
	private static String executeVMMAN(String cmdLine)
	{		
		cmdLine = cmdLine.replace("vmman ", "");
		StringTokenizer st = new StringTokenizer(cmdLine);
		String cmd = st.nextToken().trim();
		Command command = getCommand(Command.CMD_GROUP.VMMAN.toString(),cmd);
		
		switch (command) {
		case SHOW: return VmManager.showVM(cmdLine);
		case CREATE: return VmManager.createVM(cmdLine);
		case LISTVM: return VmManager.listVM(cmdLine);
		case DESTROY: return VmManager.destroyVM(cmdLine);
		case SUSPEND: return VmManager.suspendVM(cmdLine);
		case START: return VmManager.startVM(cmdLine);
		case MIGRATE:
			return VmManager.migrate(cmdLine);
		default:vcluster.util.Util.print("command is not defined"); 
			break;
		}
		
		return null;
	}
	
	public static Command getCommand(String cmdGroup, String aCmdLine) 
	{
		StringTokenizer st = new StringTokenizer(aCmdLine);
		String aCmd = st.nextToken().trim();
    	if (cmdGroup==null){
            for (Command cmd : Command.values()){
            	if (cmd.contains(aCmd)) return cmd;
            }
    	}
        for (Command cmd : Command.values()){
        	if (cmd.getCmdGroup().toString().equalsIgnoreCase(cmdGroup)&cmd.contains(aCmd)) return cmd;
        }
        return Command.NOT_DEFINED;
 	}
}
