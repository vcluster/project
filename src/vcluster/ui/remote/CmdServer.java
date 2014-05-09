package vcluster.ui.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;

public class CmdServer {
	
	public static void main(String[] arg){
		try {
			System.setProperty("java.rmi.server.hostname","150.183.233.59");
			RMISocketFactory.setSocketFactory (new MyRMISocket()); 
			LocateRegistry.createRegistry(1099); 
			RemoteCmd rcmd = new RemoteCmd("command");
			Naming.rebind("command", rcmd);
			System.out.println("Command server is ready.");
		
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
