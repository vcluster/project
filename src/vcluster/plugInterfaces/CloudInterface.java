package vcluster.plugInterfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vcluster.elements.Vm;

/**
 *An interface for cloud plug-in, all the cloud plugins implement this interface. 
 */
public interface CloudInterface extends Serializable {
	/**
	 * Connect to a cloud system.
	 * @param Configurations, a collection of cloud connection informations.
	 */
	public boolean RegisterCloud(List<String> configurations);
	/**
	 *Create given numbers of virtual machines.
	 *@param maxCount, the number of virtual machines that's suppose to be created. 
	 */
	public ArrayList<Vm> createVM(int maxCount);
	/**
	 *Get the list of virtual machines. 
	 *@return ArrayList<Vm>, an array list of virtual machine instances . 
	 */
	public ArrayList<Vm> listVMs();
	/**
	 *Terminate a given virtual machine.
	 *@param id, the id that's suppose to be terminated.
	 * 
	 */
	public ArrayList<Vm> destroyVM(Vm vm);
	/**
	 *Start a given virtual machine from suspend status.
	 *@param id, the id of the virtual machine that is suppose to start. 
	 */
	public ArrayList<Vm> startVM(String id);
	/**
	 *Suspend a given virtual machine. 
	 *@param id, the id of the victual machine that's suppose to be suspended.
	 */
	public ArrayList<Vm> suspendVM(String id);
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
