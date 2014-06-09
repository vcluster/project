package vcluster.executors;

import vcluster.Vcluster.uiType;
import vcluster.elements.PoolStatus;
import vcluster.elements.QStatus;
import vcluster.managers.CloudManager;
import vcluster.managers.PluginManager;
import vcluster.managers.VmManager;
import vcluster.ui.CmdComb;
import vcluster.ui.remote.CmdServer;

/**
 * @author Seo-Young Noh, Modified by Dada Huang
 * This class resolves the command and invoke the right function.
 * 
 */

public class CmdCataloger {

	/**
	 * 
	 *Deliver the command the different group executor according the group of the command. 
	 */
	public static Object execute(CmdComb cmd)
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
	
	/**
	 *Invoke the corresponding function according the command. 
	 */
	private static Object executeCLOUDMAN(CmdComb cmd) {
		// TODO Auto-generated method stub
	switch (cmd.getCmd()) {
		case REGISTER:
			return CloudmanExecutor.register(cmd); 
		case LOADCLOUD:
			return CloudmanExecutor.load(cmd);
		case LISTCLOUD:
			String str = CloudManager.dump();
			if(cmd.getUi()==uiType.CMDLINE)System.out.println(str);
			return str;
		case UNLOADCLOUD:
			return CloudmanExecutor.unload(cmd);	
		case HOSTON:
			return CloudmanExecutor.hoston(cmd);
		case HOSTOFF:
			return CloudmanExecutor.hostoff(cmd);	
		case DUMP:
			return CloudmanExecutor.dump(cmd);
		default:
			return CloudmanExecutor.undefined(cmd);					
		}		
	}
	
	/**
	 *Invoke the corresponding function according the command. 
	 */
	private static String executePLUGMAN(CmdComb cmd) {
		
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
	
	/**
	 *Invoke the corresponding function according the command. 
	 */
	private static Object executeVCLMAN(CmdComb cmd)
	{
		
		switch (cmd.getCmd()) {
		case SERVER_MODE:
			CmdServer.initiate();
			break;
		
		case CHECK_P: 			
			PoolStatus pstat =  BatchExecutor.getPoolStatus();	
			if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(pstat.printPoolStatus());
			return pstat.printPoolStatus();
		case CHECK_Q: 			
	    	QStatus qstat =  PluginManager.current_proxyExecutor.getQStatus();
	    	if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(qstat.printQStatus());
	    	return qstat.printQStatus();
		default:
			break;		
			
		}
		
		return null;
	}
	
	/**
	 *Invoke the corresponding function according the command. 
	 */
	private static String executeVMMAN(CmdComb cmd)
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
