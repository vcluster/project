package vcluser.plugin.sbs;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.sbs.remote.RemoteInterface;

public class Client {

	/**
	 * @param args
	 */
	public static ArrayList<String> getPool() {
		// TODO Auto-generated method stub
		try {
			//Thread.sleep(1000);
			RemoteInterface ri = (RemoteInterface) Naming.lookup("rmi://localhost:1099/command");
			return ri.getPool();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	public static ArrayList<String> getQueue() {
		// TODO Auto-generated method stub
		try {
			//Thread.sleep(1000);
			RemoteInterface ri = (RemoteInterface) Naming.lookup("rmi://localhost:1099/command");
			return ri.getQueue();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

}
