package vcluster.global;

import vcluster.util.HandleXML;

public class VCluster {

	public static boolean init() {
		try{
			HandleXML.restore();
		}catch(Exception e){
			
		}
		return true;
	}
}
