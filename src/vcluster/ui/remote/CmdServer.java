package vcluster.ui.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;

import vcluster.Vcluster;
/**
 *This class starts the vcluster as the server mode
 */
public class CmdServer {
	/**
	 *This function initiates vcluster server
	 */
	public static void initiate(){
		try {
			System.setProperty("java.rmi.server.hostname",Vcluster.SERVER_ADDR);
			RMISocketFactory.setSocketFactory (new MyRMISocket()); 
			LocateRegistry.createRegistry(Vcluster.SERVER_PORT1); 
			RemoteCmd rcmd = new RemoteCmd("command");
			Naming.rebind("command", rcmd);
			System.out.println("Vcluster server is running... ...");
		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
