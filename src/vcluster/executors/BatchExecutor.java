package vcluster.executors;

import java.io.File;
import java.util.ArrayList;

import vcluster.elements.Cloud;
import vcluster.elements.PoolStatus;
import vcluster.elements.QStatus;
import vcluster.elements.Slot;
import vcluster.elements.Vm;
import vcluster.managers.CloudManager;
import vcluster.managers.PluginManager;
/**
 * A class representing a batch system executor, which involves the functions to operate on batch system, such as get the pool status and get queue status.
 */

public class BatchExecutor {	
	/**
	 * Mapping between virtual machines and slots. This function pushes the slots' activity status to the corresponding virtual machines.
	 */
	public static void mapingActivityToVm(){
		ArrayList<Slot> slotList = getPoolStatus().getSlotList();
		for(Cloud c : CloudManager.getCloudList().values()){
			if(c.getVmList()==null)continue;
			for(Vm vm : c.getVmList().values()){
				for(Slot s : slotList){
					if(s.getDomain()!=null){
						if(s.getIdType()==Slot.IdType.PRIVATEIP){
							if(s.getIdentifier().equalsIgnoreCase(vm.getPrivateIP())&&vm.getCloudName().equalsIgnoreCase(s.getDomain())){
								vm.setIsIdle(s.getActivity());
							}
						}else if(s.getIdType()==Slot.IdType.PUBLICIP){
							if(s.getIdentifier().equalsIgnoreCase(vm.getPubicIP())&&vm.getCloudName().equalsIgnoreCase(s.getDomain())){
								vm.setIsIdle(s.getActivity());
							}
						}else if(s.getIdType()==Slot.IdType.VMID){
							if(s.getIdentifier().equalsIgnoreCase(vm.getId())&&vm.getCloudName().equalsIgnoreCase(s.getDomain())){
								vm.setIsIdle(s.getActivity());
							}
						}
					}
				}
			}				
		}		
	}
	
	public static boolean ConnectTo(File conf){
		return PluginManager.current_proxyExecutor.ConnectTo(conf);
	}
	
	

	/**
	 * Get the pool status
	 * @return A instance of PoolStatus
	 * @see PoolStatus
	 */
	public static PoolStatus getPoolStatus(){
		//BatchExecutor.mapingActivityToVm();
		return PluginManager.current_proxyExecutor.getPoolStatus();
	}
	
	/**
	 * Get the queue status
	 * @return A instance of the queue status
	 * @see QStatus
	 */
	public static QStatus getQStatus(){
		return PluginManager.current_proxyExecutor.getQStatus();
	}	
}
