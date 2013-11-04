package vcluster.global;

import vcluster.util.HandleXML;

public class VCluster {

	public static boolean init() {

		Config.vmMan = new vcluster.control.VMManager();
		//Config.hdXML = new vcluster.util.HandleXML();
		try{
			HandleXML.restore();
		}catch(Exception e){
			
		}
		return true;
	}
}
