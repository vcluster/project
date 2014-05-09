package vcluster.ui.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote{
	
	public String getRemoteCommand(String command) throws RemoteException;

}
