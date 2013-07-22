package vcluster.ui;

import java.util.StringTokenizer;

import vcluster.engine.groupexecutor.PlugmanExecutor;
import vcluster.engine.groupexecutor.VClusterExecutor;
import vcluster.global.Config;
import vcluster.plugman.PluginManager;

/**
 * @author huangdada
 * 
 */
public class CmdExecutor {

	/**
	 * if quit, it checks if monitoring process and vm manager process are still running
	 */
	public static void quit()
	{
		/* shutdown Manager first */
		if (Config.monMan != null) Config.monMan.shutDwon();
		if (Config.vmMan != null) Config.vmMan.shutDwon();
	}

	public static boolean isQuit(String aCmd)
	{
		String cmd = aCmd.trim();
		if(Command.QUIT.contains(cmd)) {

			/* shutdown Manager first */
			if (Config.monMan != null) Config.monMan.shutDwon();
			if (Config.vmMan != null) Config.vmMan.shutDwon();
			
			return true;
		}

		return false;
	}
	
	public static boolean execute(String cmdLine)
	{
		StringTokenizer st = new StringTokenizer(cmdLine);
		
		String cmd = st.nextToken().trim();
		
		Command command = getCommand(cmd);
		
		// if (command == Command.NOT_DEFINED) return false;
		
		switch (command.getCmdGroup()) {
		case VCLMAN: return executeVCLMAN(command, cmdLine);
		case VMMAN: return executeVMMAN(cmdLine);
		case PLUGMAN: return executePLUGMAN(cmdLine);		
		case NOT_DEFINED: return false;
		}
		
		return false;
	}
	
	private static boolean executePLUGMAN(String cmdLine) {
		cmdLine = cmdLine.replace("plugman ", "");
		StringTokenizer st = new StringTokenizer(cmdLine);
		String cmd = st.nextToken().trim();
		//System.out.println(cmdLine);
		Command command = getCommand(cmd);
		
		switch (command) {
		case LOAD:
			return PlugmanExecutor.load(cmdLine);
		case UNLOAD:
			return PlugmanExecutor.unload(cmdLine);
		case INFO:
			return PlugmanExecutor.info(cmdLine);
		case LIST:
			//System.out.print("                      1 : ");
			return PlugmanExecutor.list(cmdLine);
		default:
			//System.out.print("                      2 : ");
			return PlugmanExecutor.undefined(cmdLine);					
		}

	}

	private static boolean executeVCLMAN(Command command, String cmdLine)
	{
		
		switch (command) {
		case DEBUG_MODE:
			return VClusterExecutor.debug_mode(cmdLine);
		case VMMAN:
			return VClusterExecutor.vmman(cmdLine);
		case MONITOR:
			return VClusterExecutor.monitor(cmdLine);
		case CLOUDMAN:
			return VClusterExecutor.cloudman(cmdLine);
		//case SHOW:
		//	return VClusterExecutor.show(cmdLine);
		case LOADCONF:
			return VClusterExecutor.load(cmdLine);
		//case SET:
		//	return VClusterExecutor.set(cmdLine);
		case ENGMODE:
			return VClusterExecutor.engmode(cmdLine);
		case CHECK_P: 
    		return PluginManager.current_proxyExecutor.check_pool();
	    case CHECK_Q: 
	    	return PluginManager.current_proxyExecutor.check_q();		
			
		}

		
		return true;
	}
	

	

	private static boolean executeVMMAN(String cmdLine)
	{

		/*
		RUN_INSTANCE (CMD_GROUP.CLOUD, "RunInstances, runinstance, ri, runinst, runins, run"),
		START_INSTANCE (CMD_GROUP.CLOUD, "StartInstances, startinstance, si, startinst, startins, start"),
		STOP_INSTANCE (CMD_GROUP.CLOUD, "StopInstances, stopinstance, stop"),
		DESCRIBE_INSTANCE (CMD_GROUP.CLOUD, "DescribeInstances, describeinstance, din, dins, descinst, descins"),
		TERMINATE_INSTANCE (CMD_GROUP.CLOUD, "TerminateInstances, terminateinstance, terminate, ti, kill, killins"),
		DESCRIBE_IMAGE (CMD_GROUP.CLOUD, "DescribeImages, describeimage, dim, dimg, descimg"),
		*/
		
		// command.toPrint();
		
		/* first check if a vm can be launched using cloud API
		 * if so, call registered function (plug-in based).
		 * if not, call REST API for a specified cloud system,
		 * which is chosen from cloud system pool based on priority.
		 */
		
		
		cmdLine = cmdLine.replace("vmman ", "");
		StringTokenizer st = new StringTokenizer(cmdLine);
		String cmd = st.nextToken().trim();
		//System.out.println(cmdLine);
		Command command = getCommand(cmd);
		
		
		switch (command) {
		//case CREATE: return CloudExecutor.createVM(Config.cloudMan.getCurrentCloud(), cmdLine);
		//case LISTVM: System.out.println("MARK");return CloudExecutor.listVMs(Config.cloudMan.getCurrentCloud(), cmdLine);
		//case DESTROY: return CloudExecutor.destroyVM(Config.cloudMan.getCurrentCloud(), cmdLine);
		//case SUSPEND: return CloudExecutor.suspendVM(Config.cloudMan.getCurrentCloud(), cmdLine);
		//case START: return CloudExecutor.startVM(Config.cloudMan.getCurrentCloud(), cmdLine);
		default: 
			break;
		

		}
		
		return true;
	}
	
	public static Command getCommand(String aCmdLine) 
	{
		StringTokenizer st = new StringTokenizer(aCmdLine);
		String aCmd = st.nextToken().trim();
		
        for (Command cmd : Command.values())
        	if (cmd.contains(aCmd)) return cmd;
        
        return Command.NOT_DEFINED;
 	}
}
