package vcluster.ui;

import java.util.StringTokenizer;

import vcluster.control.cloudman.CloudManager;
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
		
		Command command = getCommand(null,cmd);
		
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
		Command command = getCommand(Command.CMD_GROUP.PLUGMAN.toString(),cmd);
		
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
			if(PluginManager.loadedBatchPlugins.isEmpty()){
				System.out.println("[ERROR : ] No plugin system,please load it first!");
				return false;
			}
    		return PluginManager.current_proxyExecutor.check_pool();
	    case CHECK_Q: 
			if(PluginManager.loadedBatchPlugins.isEmpty()){
				System.out.println("[ERROR : ] No plugin system,please load it first!");
				return false;
			}
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
		Command command = getCommand(Command.CMD_GROUP.VMMAN.toString(),cmd);
		
		//System.out.println(cmdLine);
		switch (command) {
		case CREATE: return Config.vmMan.createVM(cmdLine);
		case LISTVM: return Config.vmMan.listVM(cmdLine);
		case DESTROY: return Config.vmMan.destroyVM(cmdLine);
		case SUSPEND: return Config.vmMan.suspendVM(cmdLine);
		case START: return Config.vmMan.startVM(cmdLine);
		default:System.out.println("command is not defined"); 
			break;
		

		}
		
		return true;
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
