package vcluster.control.cloudman;

import java.util.List;
import java.util.StringTokenizer;

import vcluster.engine.groupexecutor.PlugmanExecutor;
import vcluster.global.Config.CloudType;
import vcluster.plugman.CloudInterface;
import vcluster.plugman.PluginManager;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class Cloud{
	
	public Cloud(List<String> conf) {
		this.conf = conf;
		for(String aLine : conf){
			
			StringTokenizer st = new StringTokenizer(aLine, "=");
			
			if (!st.hasMoreTokens()) break;
			
			/* get a keyword */
			String aKey = st.nextToken().trim();
		
			/* get a value */
			if (!st.hasMoreTokens()) break;

			String aValue = st.nextToken().trim();
			
			if (aKey.equalsIgnoreCase("type")){
				setCloudType(aValue);
				break;
								
			}else if((aKey.equalsIgnoreCase("Interface"))){
				setCloudpluginName(aValue);
			}else if((aKey.equalsIgnoreCase("Name"))){
				setCloudName(aValue);
			}
		}
		if(!PluginManager.loadedCloudPlugins.containsKey(this.cloudpluginName))PlugmanExecutor.load("load -c "+cloudpluginName);		
		cp = PluginManager.loadedCloudPlugins.get(this.cloudpluginName);

		
	}
	
	public int getCurrentVMs() {
		return currentVMs;
	}
	
	public void setCurrentVMs(int vms) {
		currentVMs = vms;
	}
	
	protected void incCurrentVMs(int vms) {
		currentVMs += vms;
	}
	
	
	
	public List<String> getConf() {
		return conf;
	}

	public void setConf(List<String> conf) {
		this.conf = conf;
	}

	public void dump(){
		for(String aLine : conf){
			System.out.println(aLine);
		}
	}

	public boolean createVM(int maxCount) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		return cp.createVM(maxCount);
	}

	public boolean listVMs() {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		return	cp.listVMs();

	}
	
	
	public boolean destroyVM(String id) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		return cp.destroyVM(id);
	}

	public boolean startVM(String id) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		return cp.startVM(id);
	}

	public boolean suspendVM(String id) {
		// TODO Auto-generated method stub
		cp.RegisterCloud(conf);
		return cp.suspendVM(id);
	}
	public CloudType getCloudType() {
		
		return cloudType;
	}
	
	public void setCloudType(CloudType type) {
		cloudType = type;
	}
	
	public void setCloudType(String type) {
		if (type.equalsIgnoreCase("private")) 
			cloudType = CloudType.PRIVATE;
		else if (type.equalsIgnoreCase("public")) 
			cloudType = CloudType.PUBLIC;
		else {
			PrintMsg.print(DMsgType.ERROR, "undefined type, "+type+", found");
			cloudType = CloudType.NOT_DEFINED;
		}
		
	}
	
	public String stringCloudType() {
		
		switch(cloudType) {
		case PRIVATE: return "PRIVATE";
		case PUBLIC: return "PUBLIC";
		case NOT_DEFINED: return "NOT_DEFINED";
		}
		return "NOT_DEFINED";
	}

	public String getCloudName() {
		return cloudName;
	}

	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	
	
	public String getCloudpluginName() {
		return cloudpluginName;
	}

	public void setCloudpluginName(String cloudpluginName) {
		this.cloudpluginName = cloudpluginName;
	}



	private String cloudName;
	private String cloudpluginName;
	private List<String> conf;
	private int currentVMs;
	private CloudType cloudType;
	private CloudInterface cp;
	
}
