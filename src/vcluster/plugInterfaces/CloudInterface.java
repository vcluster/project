package vcluster.plugInterfaces;

import java.io.Serializable;

import vcluster.elements.Cloud;


/**
 *An interface for cloud plug-in, all the cloud plugins implement this interface. 
 */
public interface CloudInterface extends Serializable {
	/**
	 * Connect to a cloud system.
	 * @param cloud TODO
	 * @param Configurations, a collection of cloud connection informations.
	 */
	public void getCloud(Cloud cloud);
	/**
	 *Create given numbers of virtual machines.
	 *@param maxCount, the number of virtual machines that's suppose to be created. 
	 */
	public boolean createVM(int maxCount);
	/**
	 *Get the list of virtual machines. 
	 *@return ArrayList<Vm>, an array list of virtual machine instances . 
	 */
	public boolean sync();
	/**
	 *Terminate a given virtual machine.
	 *@param id, the id that's suppose to be terminated.
	 * 
	 */
	public boolean destroyVM(String id);
	/**
	 *Start a given virtual machine from suspend status.
	 *@param id, the id of the virtual machine that is suppose to start. 
	 */
	public boolean startVM(String id);
	/**
	 *Suspend a given virtual machine. 
	 *@param id, the id of the victual machine that's suppose to be suspended.
	 */
	public boolean suspendVM(String id);
	/**
	 * Turn on a given host
	 * @param ipmiID, the ipmi id of the host. 
	 */
	public boolean hoston(String ipmiID);
	/**
	 *Turn the the give host's power off 
	 *@param ipmiID, the ipmi id of the host.
	 */
	public boolean hostoff(String ipmiID);
	/**
	 *Migrate a given virtual machine to the target host.
	 *@param vmid, the specified virtual machine's id 
	 *@param hostid, the target host's id.virtual machine would be migrated to it.
	 */
	public boolean migrate(String vmid,String hostid);
	
	
}
