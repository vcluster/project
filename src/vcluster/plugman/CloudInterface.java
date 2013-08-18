package vcluster.plugman;

import java.util.ArrayList;
import java.util.List;

import vcluster.control.VMelement;


public interface CloudInterface {
	
	//connect to a cloud system,conf is a file includes the connection informations
	public boolean RegisterCloud(List<String> configurations);
	//create a VM 
	public ArrayList<VMelement> createVM(int maxCount);
	//list all the VMS
	public ArrayList<VMelement> listVMs();
	//destroy a VM
	public ArrayList<VMelement> destroyVM(String id);
	//restart a suspendVM
	public ArrayList<VMelement> startVM(String id);
	// suspend a VM
	public ArrayList<VMelement> suspendVM(String id);
	// return the information about a cloud plugin,like a user Manual
	//public ArrayList<VMelement> getVMs();
	public String getInfo();
	
	
}
