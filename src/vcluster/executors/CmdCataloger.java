package vcluster.executors;

import java.util.StringTokenizer;

import vcluster.Vcluster.uiType;
import vcluster.elements.PoolStatus;
import vcluster.elements.QStatus;
import vcluster.managers.PluginManager;
import vcluster.managers.VmManager;
import vcluster.ui.CmdSet;
import vcluster.ui.Command;

/**
 * @author Seo-Young Noh, Modified by Dada Huang
 * This class 
 * 
 */

public class CmdCataloger {

	
	public static Object execute(CmdSet cmd)
	{
		switch (cmd.getCmdGroup()) {
		case VMMAN: return executeVMMAN(cmd);
		case CLOUDMAN: return executeCLOUDMAN(cmd);
		case PLUGMAN: return executePLUGMAN(cmd);
		case VCLMAN: return executeVCLMAN(cmd);
		default:
			break;
		}		
		return "";
	}
	
	private static Object executeCLOUDMAN(CmdSet cmd) {
		// TODO Auto-generated method stub
	switch (cmd.getCmd()) {
		case REGISTER:
			return CloudmanExecutor.register(cmd); 
		case LOADCLOUD:
			return CloudmanExecutor.load(cmd);
		case LISTCLOUD:
			return CloudmanExecutor.getCloudList();
		case UNLOADCLOUD:
			return CloudmanExecutor.unload(cmd);	
		case HOSTON:
			return CloudmanExecutor.hoston(cmd);
		case HOSTOFF:
			return CloudmanExecutor.hostoff(cmd);	
		default:
			return CloudmanExecutor.undefined(cmd);					
		}		
	}

	private static boolean executePLUGMAN(CmdSet cmd) {
		
		switch (cmd.getCmd()) {
		case LOAD:
			return PlugmanExecutor.load(cmd); 
		case UNLOAD:
			return PlugmanExecutor.unload(cmd);
		case INFO:
			return PlugmanExecutor.getInfo(cmd);
		case LIST:
			return PlugmanExecutor.list(cmd);
		default:
			return PlugmanExecutor.undefined(cmd);					
		}

	}

	private static Object executeVCLMAN(CmdSet cmd)
	{
		
		switch (cmd.getCmd()) {
		
		case CHECK_P: 			
			PoolStatus pstat =  BatchExecutor.getPoolStatus();	
			if(cmd.getUi().equals(uiType.CMDLINE))pstat.printPoolStatus();
			return pstat;
		case CHECK_Q: 			
	    	QStatus qstat =  PluginManager.current_proxyExecutor.getQStatus();
	    	if(cmd.getUi().equals(uiType.CMDLINE))qstat.printQStatus();
	    	return qstat;
		default:
			break;		
			
		}
		
		return null;
	}
	
	private static String executeVMMAN(CmdSet cmd)
	{		
		
		switch (cmd.getCmd()) {
		case SHOW: return VmManager.showVM(cmd);
		case CREATE: return VmManager.createVM(cmd);
		case LISTVM: return VmManager.listVM(cmd);
		case DESTROY: return VmManager.destroyVM(cmd);
		case SUSPEND: return VmManager.suspendVM(cmd);
		case START: return VmManager.startVM(cmd);
		case MIGRATE:
			return VmManager.migrate(cmd);
		default:System.out.println("command is not defined"); 
			break;
		}
		
		return null;
	}
	
	
}
