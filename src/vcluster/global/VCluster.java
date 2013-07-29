package vcluster.global;


public class VCluster {

	public static boolean init() {
		Config.cloudMan = new vcluster.control.cloudman.CloudManager();
		Config.vmMan = new vcluster.control.VMManager();
		return true;
	}
}
