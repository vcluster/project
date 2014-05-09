package vcluster.plugInterfaces;
/**
 *Load balancer interface, all the load balancer plug-ins implement this interface. 
 */
public interface LoadBalancer {
	
	/**
	 *Vcluster invoke this function to activate the load balancer in a new thread. 
	 */
	public void activate();
}
