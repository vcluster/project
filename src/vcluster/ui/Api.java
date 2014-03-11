package vcluster.ui;

import java.util.TreeMap;

import vcluster.control.batchsysman.PoolStatus;
import vcluster.control.batchsysman.QStatus;
import vcluster.control.cloudman.Cloud;
import vcluster.control.cloudman.CloudManager;
import vcluster.control.vmman.VmManager;
import vcluster.engine.groupexecutor.CloudmanExecutor;
import vcluster.plugins.plugman.PluginManager;

public class Api {
	
	//Get the DataStructure
	public static TreeMap<String,Cloud> getDataStructure(){
		TreeMap<String,Cloud> ds=null;
		ds = CloudManager.getCloudList();		
		return ds;		
	}
	
	//Get the Qstatus
	public static QStatus getQstatus(){
		QStatus qs = PluginManager.current_proxyExecutor.getQStatus();		
		return qs;
	}
	
	//Get the Pool Status
	
	public static PoolStatus getPoolStatus(){		
		PoolStatus ps = PluginManager.current_proxyExecutor.getPoolStatus();		
		
		return ps;		
	}
	
	//Load Cloud
	
	public static boolean loadCloud(String cloudName){
		boolean flag = false;
		flag = CloudmanExecutor.load("load "+cloudName);
		
		return flag;
	}

	//UnLoad Cloud
	public static boolean unLoadCloud(String cloudName){
		boolean flag = false;
		flag = CloudmanExecutor.unload("unload "+cloudName);
		return flag;
	}
	
	//Turn a host on
	public static boolean hostOn(String cloudName,String hostName){
		boolean flag = false;
		flag = CloudManager.getCloudList().get(cloudName).hoston(hostName);
		return flag;
	

	}
	
	//Shut down a host
	public static boolean hostOff(String cloudName,String hostName){
		boolean flag = false;
		flag = CloudManager.getCloudList().get(cloudName).hostoff(hostName);
		return flag;
	}
	
	//Create virtual machine
	public static boolean createVM(String cloudName,String hostId,int nums){
		boolean flag = false;
		Cloud c = CloudManager.getCloudList().get(cloudName);
		flag = c.createVM(nums, hostId);		
		return flag;
	}
	
	//Suspend virtual machine
	public static boolean suspendVM(String vmId){
		boolean flag = false;
		flag = VmManager.suspendVM("suspend "+vmId);
		return flag;
	}
	//Start virtual machine
	public static boolean startVM(String vmId){
		boolean flag = false;
		flag = VmManager.startVM("start "+vmId);
		return flag;
	}
	//Migrate virtual machine
	public static boolean migrateVM(String CloudName,String vmId){
		boolean flag = false;
		flag = VmManager.migrate("migrate "+vmId);
		return flag;
	}
}
