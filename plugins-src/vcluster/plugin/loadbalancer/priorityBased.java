package vcluster.plugin.loadbalancer;

import vcluster.elements.PoolStatus;
import vcluster.elements.QStatus;
import vcluster.executors.BatchExecutor;
import vcluster.plugInterfaces.LoadBalancer;

public class priorityBased implements LoadBalancer {
	private int t = 5;
	@Override
	public void activate() {
		// TODO Auto-generated method stub
		while(true){
			QStatus q = BatchExecutor.getQStatus();
			PoolStatus pool = BatchExecutor.getPoolStatus();
			
			
			try {
				Thread.sleep(t*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
