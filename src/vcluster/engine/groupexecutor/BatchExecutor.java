package vcluster.engine.groupexecutor;

import java.io.File;
import java.util.ArrayList;

import vcluster.control.VMelement;
import vcluster.control.batchsysman.PoolStatus;
import vcluster.control.batchsysman.QStatus;
import vcluster.control.batchsysman.Slot;
import vcluster.control.cloudman.Cloud;
import vcluster.control.cloudman.CloudManager;
import vcluster.plugman.PluginManager;

public class BatchExecutor {
	public static void mapingActivityToVm(){
		ArrayList<Slot> slotList = getPoolStatus().getSlotList();
		//System.out.println("maping");
		for(Cloud c : CloudManager.getCloudList().values()){
			//System.out.println("cloud looping");
			if(c.getVmList()==null)continue;
			for(VMelement vm : c.getVmList().values()){
				for(Slot s : slotList){
					if(s.getDomain()!=null){
						if(s.getIdType()==Slot.IdType.PRIVATEIP){
							//System.out.println(s.getActivity()+"1 : "+ vm.getId()+" : "+s.getDomain());
							if(s.getIdentifier().equalsIgnoreCase(vm.getPrivateIP())&&vm.getCloudName().equalsIgnoreCase(s.getDomain())){
								vm.setIsIdle(s.getActivity());
								//System.out.println(s.getActivity()+" : "+ vm.getId()+" : "+s.getDomain());
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

	public static PoolStatus getPoolStatus(){
		return PluginManager.current_proxyExecutor.getPoolStatus();
	}
	public static QStatus getQStatus(){
		return PluginManager.current_proxyExecutor.getQStatus();
	}
	
}
