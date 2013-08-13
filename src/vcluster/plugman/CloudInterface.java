package vcluster.plugman;

import java.util.ArrayList;
import java.util.List;

import vcluster.control.VMelement;


public interface CloudInterface {
	
	//connect to a cloud system,conf is a file includes the connection informations
	public boolean RegisterCloud(List<String> configurations);
	//create a VM 
	public boolean createVM(int maxCount);
	//list all the VMS
	public boolean listVMs();
	//destroy a VM
	public boolean destroyVM(String id);
	//restart a suspendVM
	public boolean startVM(String id);
	// suspend a VM
	public boolean suspendVM(String id);
	// return the information about a cloud plugin,like a user Manual
	public ArrayList<VMelement> getVMs();
	public String getInfo();
	
	
}
