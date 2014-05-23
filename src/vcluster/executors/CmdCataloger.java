package vcluster.executors;

import java.util.StringTokenizer;
import vcluster.managers.PluginManager;
import vcluster.managers.VmManager;
import vcluster.ui.CmdList;

/**
 * @author Seo-Young Noh, Modified by Dada Huang
 * This class 
 * 
 */

public class CmdCataloger {

	
	public static Object execute(String cmdLine)
	{
		StringTokenizer st = new StringTokenizer(cmdLine);
		
		String cmd = st.nextToken().trim();
		
		CmdList cmdList = getCommand(null,cmd);
				
		switch(cmdList.getCmdGroup()){
		case VCLMAN:return executeVCLMAN(cmdList, cmdLine);
		default:
			break;
		}

		switch (cmdList) {
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
		CmdList cmdList = getCommand(CmdList.CMD_GROUP.CLOUDMAN.toString(),cmd);
		cmdLine = cmdLine.replace(cmdg, "").trim();
		switch (cmdList) {
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
		CmdList cmdList = getCommand(CmdList.CMD_GROUP.PLUGMAN.toString(),cmd);
		
		switch (cmdList) {
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

	private static Object executeVCLMAN(CmdList cmdList, String cmdLine)
	{
		
		switch (cmdList) {
		
		case CHECK_P: 			
			return BatchExecutor.getPoolStatus();		
		case CHECK_Q: 			
	    	return PluginManager.current_proxyExecutor.getQStatus();
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
		CmdList cmdList = getCommand(CmdList.CMD_GROUP.VMMAN.toString(),cmd);
		
		switch (cmdList) {
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
	
	
}
