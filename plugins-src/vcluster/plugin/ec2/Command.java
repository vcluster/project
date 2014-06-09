package vcluster.plugin.ec2;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
/**
 *This is a command enum for ec2 interface 
 */
public enum Command {


	RUN_INSTANCE ("RunInstances, runinstance, ri, runinst, runins, run"),
	START_INSTANCE ("StartInstances, startinstance, si, startinst, startins, start"),
	STOP_INSTANCE ("StopInstances, stopinstance, stop"),
	DESCRIBE_INSTANCE ("DescribeInstances, describeinstance, din, dins, descinst, descins"),
	TERMINATE_INSTANCE ("TerminateInstances, terminateinstance, terminate, ti, kill, killins"),
	DESCRIBE_IMAGE ("DescribeImages, describeimage, dim, dimg, descimg"),
	
	NOT_DEFINED ("not_defined");

	private String command;
	private List<String> cmdList;

	
	Command( String cmdString) {

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


	
}
