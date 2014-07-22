package vcluster.plugin.simulator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.cloud.rmiServer.RmiInterface;

public class Client {

	/**
	 * @param args
	 */
	public static ArrayList<String> executeRemote(String args) {
		// TODO Auto-generated method stub
		try {
			//Thread.sleep(1000);
			System.out.println(endPoint);
			RmiInterface ri = (RmiInterface) Naming.lookup(endPoint);
			return ri.executeCmd(args);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	public static String getEndPoint() {
		return endPoint;
	}
	public static void setEndPoint(String endPoint) {
		Client.endPoint = endPoint;
	}
	public static void main(String [] args){
		for(String str :executeRemote("cloud1:simvm create 10")){
			System.out.println(str);
			
		}
		
	}
	private static String endPoint;
}
