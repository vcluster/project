
package vcluster.plugins;
import java.util.TreeMap;

import vcluster.control.*;
import vcluster.control.cloudman.*;
import vcluster.control.vmman.Vm;
import vcluster.global.Config;
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
	//////////////////////Main Algorithm////////////////////////
	
		
	public static boolean algo()
	{
		//Assign the priority
		int min=1000;
		String machineid="";
		
		
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
							c.createVM(1, machineid);
							System.out.println(machineid+" : "+h.getCurrVmNum()+" : "+h.getMaxVmNum());
						}
						
						
					}//machineid and host id equal end
				}
			}//priority == 1 loop end
			
		}// for all clouds end
		
		return true;
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

