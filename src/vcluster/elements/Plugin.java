package vcluster.elements;

/**
 *A class represents plug-in 
 */
public class Plugin extends Element{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pluginName;
	private String pluginStatus;
	private String pluginType;
	private Object instance;
	
	/**
	 *Get the name of the plug-in 
	 *@return pluginName, the name of the plug-in.	 
	 */
	public String getPluginName() {
		return pluginName;
	}
	/**
	 * Set the name of the plug-in
	 * @param pluginName, the name of the plug-in 
	 */
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	
	/**
	 *Get the plug-in status,loaded or unloaded into vcluster.
	 *@return pluginStatus.
	 */
	public String getPluginStatus() {
		return pluginStatus;
	}
	
	/**
	 *Set the plug-in status
	 *@param pluginStatus, the plug-in loaded/unloaded status.
	 */
	public void setPluginStatus(String pluginStatus) {
		this.pluginStatus = pluginStatus;
	}
	
	/**
	 *Get the plug-in type, There are three types of plug-in, cloud,batch system and load balancer.
	 *@return pluginType, the type of the plug-in. 
	 */
	public String getPluginType() {
		return pluginType;
	}
	
	/**
	 *Set the type of the plug-in, There are three types of plug-in, cloud,batch system and load balancer.
	 *@param pluginType, the type of the plug-in. 
	 */
	public void setPluginType(String pluginType) {
		this.pluginType = pluginType;
	}
	
	/**
	 *Get the instance that this class represents.
	 *@return instance, cloud be an instance of cloud plug-in, batch system plug-in or load balancer plug-in. 
	 */
	public Object getInstance() {
		return instance;
	}
	
	/**
	 *Set the instance that this class represents. 
	 * @param instance, cloud be an instance of cloud plug-in, batch system plug-in or load balancer plug-in. 
	 */
	public void setInstance(Object instance) {
		this.instance = instance;
	}

	
	
}
