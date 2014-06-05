package vcluster.ui;

import java.util.ArrayList;
import java.util.StringTokenizer;
import vcluster.Vcluster.uiType;
import vcluster.ui.Command.CMD_GROUP;

/**
 *This class represents a command set, a command set  consist of command group, command,and the parameters set
 */
public class CmdComb {
	
	/**
	 *Construct a command combination by the command line. 
	 */
	public CmdComb(String cmdLine){
		this.cmdLine = cmdLine;
		paraSet = new ArrayList<String>();
		cmdLineParser(cmdLine);

		
	}

	/**
	 * Resolve the command line string,separate the  command group, command ,and command parameters set.
	 */
	private void cmdLineParser(String cmdLine){
		StringTokenizer st = new StringTokenizer(cmdLine);
		String strGroup = null;
		
		if(st.hasMoreTokens()){
			strGroup = st.nextToken().trim();
		}else{
			
		}
		switch (strGroup){
			case "plugman":
				cmdGroup = CMD_GROUP.PLUGMAN;
				break;
			
			case "cloudman":
				cmdGroup = CMD_GROUP.CLOUDMAN;
				break;
			case "vmman":
				cmdGroup = CMD_GROUP.VMMAN;
				break;
			default:
				cmdGroup = CMD_GROUP.VCLMAN;
				
				break;
		}
		String cmdStr ="";
		if(cmdGroup==CMD_GROUP.VCLMAN){
			cmdStr = strGroup;
			cmd = Command.getCommand(cmdGroup, cmdStr);
		}else if(st.hasMoreTokens()){
			cmdStr = st.nextToken();
			cmd = Command.getCommand(cmdGroup, cmdStr);
		}else{
			cmd = Command.VHELP;
		}
		boolean flag = true;
		while(flag){
			if(st.hasMoreTokens()){
				paraSet.add(st.nextToken());
			}else{
				flag = false;
			}
		}
		
		
	}
	/**
	 *Get the command line string. 
	 *@return String cmdLine
	 */
	public String getCmdLine() {
		return cmdLine;
	}
	
	/**
	 *Set the command line string 
	 *@param String cmdLine
	 */
	public void setCmdLine(String cmdLine) {
		this.cmdLine = cmdLine;
	}
	
	/**
	 *Get the parameters set 
	 *@return ArrayList of parameters
	 */
	public ArrayList<String> getParaset() {
		return paraSet;
	}
	
	/**
	 * Set the parameters set
	 * @param ArrayList of parameters 
	 */
	public void setParas(ArrayList<String> paraset) {
		this.paraSet = paraset;
	}

	/**
	 * Get the command launch time.
	 * @return launch time
	 */
	public long getLaunchTime() {
		return launchTime;
	}

	/**
	 * Set the launch time of the command
	 * @param value of time in long format
	 */
	public void setLaunchTime(long launchTime) {
		this.launchTime = launchTime;
	}
	

	/**
	 * Get the group the command belongs to
	 */
	public CMD_GROUP getCmdGroup() {
		return cmdGroup;
	}

	/**
	 * Set the group the command belongs to
	 */
	public void setCmdGroup(CMD_GROUP cmdGroup) {
		this.cmdGroup = cmdGroup;
	}

	/**
	 * Get the command
	 */
	public Command getCmd() {
		return cmd;
	}

	/**
	 * Set the command
	 */
	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}
	

	/**
	 * Get the ip address where command from
	 */
	public String getSourceIp() {
		return sourceIp;
	}

	/**
	 * Set the ip address where the command from
	 */
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	/**
	 * Get the UI type where the command from
	 */
	public uiType getUi() {
		return ui;
	}

	/**
	 * Set the UI type where the command from
	 */
	public void setUi(uiType ui) {
		this.ui = ui;
	}



	private String cmdLine;
	private ArrayList<String> paraSet;
	private long launchTime;
	private CMD_GROUP cmdGroup;
	private Command cmd;
	private String sourceIp;
	private uiType ui;
	
	
	
}
