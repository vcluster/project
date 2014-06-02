package vcluster.ui;

import java.util.ArrayList;
import java.util.StringTokenizer;
import vcluster.Vcluster.uiType;
import vcluster.ui.Command.CMD_GROUP;

public class CmdSet {
	
	public CmdSet(String cmdLine){
		this.cmdLine = cmdLine;
		paraSet = new ArrayList<String>();
		cmdLineParser(cmdLine);

		
	}

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
	
	public String getCmdLine() {
		return cmdLine;
	}
	public void setCmdLine(String cmdLine) {
		this.cmdLine = cmdLine;
	}
	public ArrayList<String> getParaset() {
		return paraSet;
	}
	public void setParas(ArrayList<String> paraset) {
		this.paraSet = paraset;
	}
	public long getLaunchTime() {
		return launchTime;
	}
	public void setLaunchTime(long launchTime) {
		this.launchTime = launchTime;
	}
	public CMD_GROUP getCmdGroup() {
		return cmdGroup;
	}
	public void setCmdGroup(CMD_GROUP cmdGroup) {
		this.cmdGroup = cmdGroup;
	}
	public Command getCmd() {
		return cmd;
	}
	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	public uiType getUi() {
		return ui;
	}
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
