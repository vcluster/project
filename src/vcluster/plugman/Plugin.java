package vcluster.plugman;

import vcluster.util.HandleXML;

public class Plugin {
	
	private String pluginName;
	private String pluginStatus;
	private String pluginType;
	private Object instance;
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public String getPluginStatus() {
		return pluginStatus;
	}
	public void setPluginStatus(String pluginStatus) {
		this.pluginStatus = pluginStatus;
	}
	public String getPluginType() {
		return pluginType;
	}
	public void setPluginType(String pluginType) {
		this.pluginType = pluginType;
	}
	public Object getInstance() {
		return instance;
	}
	public void setInstance(Object instance) {
		this.instance = instance;
	}

	
	
}
