package vcluster.ui.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *This is the interface that the server program need to be implemented
 */
public interface RemoteInterface extends Remote{
	/**
	 *The remote client invokes this function. 
	 */
	public String getRemoteCommand(String command) throws RemoteException;

}
