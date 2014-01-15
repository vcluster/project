package vcluster.plugins;

import java.util.ArrayList;
import java.util.List;

import vcluster.control.vmman.Vm;


public interface CloudInterface  {
	
	//connect to a cloud system,conf is a file includes the connection informations
	public boolean RegisterCloud(List<String> configurations);
	//create a VM 
	public ArrayList<Vm> createVM(int maxCount);
	//list all the VMS
	public ArrayList<Vm> listVMs();
	//destroy a VM
	public ArrayList<Vm> destroyVM(String id);
	//restart a suspendVM
	public ArrayList<Vm> startVM(String id);
	// suspend a VM
	public ArrayList<Vm> suspendVM(String id);
	// return the information about a cloud plugin,like a user Manual
	//public ArrayList<VMelement> getVMs();

	
	
}
