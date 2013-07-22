package vcluster.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public enum Command {

	/* Commands of vclman Category */
	QUIT (CMD_GROUP.VCLMAN, "quit, exit, done, stop"),
	DEBUG_MODE (CMD_GROUP.VCLMAN, "debug"),
	//VMMAN (CMD_GROUP.VCLMAN, "vmman, vman"),
	MONITOR (CMD_GROUP.VCLMAN, "monitor, mon"),
	CLOUDMAN (CMD_GROUP.VCLMAN, "cloudman, clman, cman"),
	//SHOW (CMD_GROUP.VCLMAN, "show, sh"),
	LOADCONF (CMD_GROUP.VCLMAN, "loadf"),
	//SET (CMD_GROUP.VCLMAN, "set"),
	ENGMODE (CMD_GROUP.VCLMAN, "engmode"),	
	CHECK_P (CMD_GROUP.VCLMAN, "chkp, chkpool, checkpool, checkp"),
	CHECK_Q (CMD_GROUP.VCLMAN, "chkq, chkqueue, checkqueue, checkq"),
	
	
	/*Commands of vmman Category*/
	VMMAN (CMD_GROUP.VMMAN, "vmman,vm"),
	CREATE (CMD_GROUP.VMMAN, "create"),
	START (CMD_GROUP.VMMAN, "start"),
	SUSPEND (CMD_GROUP.VMMAN, "suspend"),
	LISTVM (CMD_GROUP.VMMAN, "listvm"),
	DESTROY (CMD_GROUP.VMMAN, "destroy,kill"),
	
	
	/*Commands of plugman Category*/
	PLUGMAN (CMD_GROUP.PLUGMAN, "plugin,pluginman,plugman,pm"),
	LOAD (CMD_GROUP.PLUGMAN, "load"),
	UNLOAD (CMD_GROUP.PLUGMAN, "unload"),
	LIST (CMD_GROUP.PLUGMAN, "list"),
	INFO (CMD_GROUP.PLUGMAN, "info"),
	
	
	/*parameters*/	
	HELP(CMD_GROUP.PARAMETER,"-h,-help,--help"),
	TYPE_BATCH(CMD_GROUP.PARAMETER,"-b,-batch,--type=batch"),
	TYPE_CLOUD(CMD_GROUP.PARAMETER,"-c,-cloud,--type=cloud"),
	LOADED(CMD_GROUP.PARAMETER,"-l,-loaded,--loaded"),
	
	/* cloud plugin commands which are translated to corresponding underlying
	 * cloud commands. Cloud plugin(s) can communicate with a proxy running
	 * on a cloud system or through EC2 API.
	 */
	/*
	 * The following commands now should be combined to vmman. Commands in vmman are including
	 *  create
	 *  destroy
	 *  suspend
	 *  list
	 * 
	 * For example, create command will create a virtual machine. This command 
	 * initially contacts a cloud plugin first, if the plugin implements EC2 interface, 
	 * it will generate http string to create a virtual machine; otherwise, it directly
	 * sends a cloud command like "onevm create <template>"
	 * 
	 * 
	RUN_INSTANCE (CMD_GROUP.CLOUD, "RunInstances, runinstance, ri, runinst, runins, run"),
	START_INSTANCE (CMD_GROUP.CLOUD, "StartInstances, startinstance, si, startinst, startins, start"),
	STOP_INSTANCE (CMD_GROUP.CLOUD, "StopInstances, stopinstance, stop"),
	DESCRIBE_INSTANCE (CMD_GROUP.CLOUD, "DescribeInstances, describeinstance, din, dins, descinst, descins"),
	TERMINATE_INSTANCE (CMD_GROUP.CLOUD, "TerminateInstances, terminateinstance, terminate, ti, kill, killins"),
	DESCRIBE_IMAGE (CMD_GROUP.CLOUD, "DescribeImages, describeimage, dim, dimg, descimg"),
	*/

	
	/* the following commands should be removed and integrated to vcluster commands
	 * 
	 */
	//CONDOR (CMD_GROUP.PROXY_SERVER, "condor_status, condor_q"),
	//ONEVM (CMD_GROUP.PROXY_SERVER, "onevm, oneimage, onevnet"),
	
	/* not defined */
	NOT_DEFINED (CMD_GROUP.NOT_DEFINED, "not_defined");

	private String command;
	private CMD_GROUP cmdGroup;
	private List<String> cmdList;

	
	Command(CMD_GROUP group, String cmdString) {

		cmdGroup = group;
		this.cmdList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(cmdString, ", ");
		
		boolean firstToken = true;
		String token = null;
		
		while(st.hasMoreTokens()) {
			token = st.nextToken();
			if (firstToken) {
				command = token;
				firstToken = false;
			}
			cmdList.add(token);
		}
	}
	
	public CMD_GROUP getCmdGroup() 
	{
		return cmdGroup;
	}
	
	public void toPrint()
	{
		System.out.println("Key = " + command);
		for(int i = 0; i < cmdList.size(); i++)
			System.out.println("\t cmd: " + cmdList.get(i));
	}
	
	public boolean contains(String aCmd)
	{
		return cmdList.contains(aCmd); 
	}
	
	public String getCommand()
	{
		return command;
	}

	public enum CMD_GROUP {
		VCLMAN,
		VMMAN,
		CLOUDMAN,
		PLUGMAN,		
		NOT_DEFINED, 
		PARAMETER
	}
	
}
