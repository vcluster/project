package vcluster.ui;

import java.util.HashMap;
import java.util.StringTokenizer;

import vcluster.Vcluster.uiType;
import vcluster.ui.CmdList.CMD_GROUP;

public class Command {
	
	public Command(String cmdLine){
		this.cmdLine = cmdLine;
		
		
	}

	private void cmdParser(String cmdLine){
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
		if(st.hasMoreTokens()){
			String cmdStr = st.nextToken();
			cmd = CmdList.getCommand(cmdGroup, cmdStr);
		}
		
		
		
	}
	
	public String getCmdLine() {
		return cmdLine;
	}
	public void setCmdLine(String cmdLine) {
		this.cmdLine = cmdLine;
	}
	public HashMap<String, String> getParas() {
		return paras;
	}
	public void setParas(HashMap<String, String> paras) {
		this.paras = paras;
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
	public CmdList getCmd() {
		return cmd;
	}
	public void setCmd(CmdList cmd) {
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
	private HashMap<String,String> paras;
	private long launchTime;
	private CMD_GROUP cmdGroup;
	private CmdList cmd;
	private String sourceIp;
	private uiType ui;
	
	
	
}
