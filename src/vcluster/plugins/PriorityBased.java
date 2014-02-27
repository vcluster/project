
package vcluster.plugins;
import java.util.ArrayList;
import java.util.TreeMap;

import vcluster.control.*;
import vcluster.control.batchsysman.QStatus;
import vcluster.control.cloudman.*;
import vcluster.control.vmman.Vm;
import vcluster.control.vmman.VmManager;
import vcluster.global.Config;
import vcluster.plugins.plugman.PluginManager;
/**
 * @author Amol
 *
 */
public class PriorityBased {
	private static TreeMap<String,Cloud> vclusterArch;
	static{
		vclusterArch = CloudManager.getCloudList();
		for(Cloud c: vclusterArch.values())
		{
			if(c.getCloudName().equalsIgnoreCase("Gcloud")){c.setPriority(1);}
			else if(c.getCloudName().equalsIgnoreCase("fermicloud")){
				c.setPriority(2);
				
			}else{
				c.setPriority(3);
			}
			
		}
		
	}
	
	/*for check Q test*/
	public static boolean chkq(int t){
		
		ChkqRunner chr = new ChkqRunner(t);
		Thread thread = new Thread(chr);
		thread.start();
		return true;
		
		
	}
	
	static class ChkqRunner implements Runnable{

		int t;
		ChkqRunner(int time){
			t= time;
		}
		public int deleted_vms(){
			int nums = 0;
			
			
			return nums;
		} 
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			boolean flag = true;
			QStatus qs;
			int idleJobs;
			int timer = 0;
			while(flag){
				
				VmManager.listVM("list -refresh");
				System.out.println();
				System.out.println("--------------------"+ ++timer + " loop------------------------");
				idleJobs = 0;
				do{
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					qs = PluginManager.current_proxyExecutor.getQStatus();
					
				}while(qs==null);
				idleJobs = qs.getIdleJob();
			//find idle vms
				
				
				if(idleJobs==0)
				{		
					ArrayList<Vm> idleVms = new ArrayList<Vm> ();
					for(Cloud c : CloudManager.getCloudList().values()){
						if(c.isLoaded()&&c.getCloudName().equalsIgnoreCase("gcloud")){
						for(Host h:c.getHostList().values()){
						for(Vm vm : h.getVmList().values()){
							if(vm.isIdle()==0||vm.isIdle()==3){
								idleVms.add(vm);
							}
						}
						}
						}
					}					
					for(Vm vm : idleVms){
						if(vm.isIdle()==0){
							//System.out.println(vm.getuId());
							//VmManager.destroyVM("destroy "+vm.getuId().intValue());
							CloudManager.getCloudList().get(vm.getCloudName()).destroyVM(vm.getId());
//							VmManager.getVmList().remove(vm.getId());
						}
					}				
				}else{
					int created_vm = 3; 
					while(idleJobs>0&&created_vm>=0){
						if(algo()){
							idleJobs--;
						}else{
							created_vm--;
						}
						if(created_vm==0){
							//deploy_amazon();
						}
						
					}
				}
				System.out.println();
				try {
					Thread.sleep(t*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		}	
	}
	//////////////////////Main Algorithm////////////////////////
	
		
	public static boolean algo()
	{
		//Assign the priority
		int min=1000;
		String machineid="";
		boolean flag = false;
		
		for(Cloud c: vclusterArch.values())
		{
			if(c.getPriority()==1)
			{
				for(Host h: c.getHostList().values())
				{   
					if(min > (h.getMaxVmNum()-h.getCurrVmNum()) && (h.getMaxVmNum()-h.getCurrVmNum())>0 )
					{
						min = (h.getMaxVmNum()-h.getCurrVmNum());
						
						machineid = h.getId();
					}
				
				}
				for(Host h: c.getHostList().values())
				{
					if(machineid.equalsIgnoreCase(h.getId()))
					{	
						h.setCurrVmNum(h.getCurrVmNum()+1);
						c.setCurrentVMs(c.getCurrentVMs()+1);
						
						if((h.getMaxVmNum()-h.getCurrVmNum())==0)
						{
							if(Check_Idle(c)==0)
								Change_priority(c);
						}
						
						if(c.getCloudName().equalsIgnoreCase("Gcloud")){
							flag = c.createVM(1, machineid);
							
							System.out.println(machineid+" : "+h.getCurrVmNum()+" : "+h.getMaxVmNum());
						}
						
						
					}//machineid and host id equal end
				}
			}//priority == 1 loop end
			
		}// for all clouds end
		
		return flag;
	}//function end
	////////////////////////Change the priority/////////////////////////
    public static void Change_priority(Cloud c)
    {
    	int totalCloud = 0;
    	
    	for(Cloud d:vclusterArch.values())
    		totalCloud++;
    	
    	c.setPriority(totalCloud-1);
    	
    	for(Cloud d:vclusterArch.values())
    	{
    		if((d.getPriority() != totalCloud) && (d.getCloudName().equalsIgnoreCase(c.getCloudName())))
    		{
    			d.setPriority(d.getPriority()-1);
    		}
    	}
    	
    }
    
    public static int Check_Idle(Cloud c)
    {
    	//for(Cloud c: vclusterArch.values())
		//{
    		for(Host h: c.getHostList().values())
    		{
    			for(Vm vm: h.getVmList().values())
    			{
    				if(vm.isIdle()==1)
    				{
    					c.destroyVM(vm.getId());
    					//Kill(vm.getID());
    					h.setCurrVmNum(h.getCurrVmNum()+1);
    					c.setCurrentVMs(c.getCurrentVMs()+1);
    					return 1;
    				}
    			}
    		}
		//}
    	return 0;
    }//check idle function end
	
	

}//class end

