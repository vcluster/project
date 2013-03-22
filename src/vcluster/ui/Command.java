package vcluster.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public enum Command {

	/* vcluster command */
	QUIT (CMD_GROUP.VCLUSTER, "quit, exit, done, stop"),
	DEBUG_MODE (CMD_GROUP.VCLUSTER, "debug"),
	VMMAN (CMD_GROUP.VCLUSTER, "vmman, vman"),
	MONITOR (CMD_GROUP.VCLUSTER, "monitor, mon"),
	CLOUDMAN (CMD_GROUP.VCLUSTER, "cloudman, clman, cman"),
	SHOW (CMD_GROUP.VCLUSTER, "show, sh"),
	LOAD (CMD_GROUP.VCLUSTER, "load"),
	SET (CMD_GROUP.VCLUSTER, "set"),
	ENGMODE (CMD_GROUP.VCLUSTER, "engmode"),
	
	
	/* cloud API command */
	RUN_INSTANCE (CMD_GROUP.CLOUD, "RunInstances, runinstance, ri, runinst, runins, run"),
	START_INSTANCE (CMD_GROUP.CLOUD, "StartInstances, startinstance, si, startinst, startins, start"),
	STOP_INSTANCE (CMD_GROUP.CLOUD, "StopInstances, stopinstance, stop"),
	DESCRIBE_INSTANCE (CMD_GROUP.CLOUD, "DescribeInstances, describeinstance, din, dins, descinst, descins"),
	TERMINATE_INSTANCE (CMD_GROUP.CLOUD, "TerminateInstances, terminateinstance, terminate, ti, kill, killins"),
	DESCRIBE_IMAGE (CMD_GROUP.CLOUD, "DescribeImages, describeimage, dim, dimg, descimg"),
	
	/* proxy server command */ 
	CHECK_POOL (CMD_GROUP.PROXY_SERVER, "chkp, chkpool, checkpool, checkp"),
	CHECK_Q (CMD_GROUP.PROXY_SERVER, "chkq, chkqueue, checkqueue, checkq"),
	CONDOR (CMD_GROUP.PROXY_SERVER, "condor_status, condor_q"),
	ONEVM (CMD_GROUP.PROXY_SERVER, "onevm, oneimage, onevnet"),
	
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
		VCLUSTER,
		CLOUD,
		PROXY_SERVER,
		NOT_DEFINED
	}
	
}
