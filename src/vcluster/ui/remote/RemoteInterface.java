package vcluster.ui.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.w3c.dom.Document;

import vcluster.elements.Cloud;
/**
 *This is the interface that the server program need to be implemented
 */
public interface RemoteInterface extends Remote{
	/**
	 *The remote client invokes this function. 
	 */
	public String getRemoteCommand(String command) throws RemoteException;
	public Document getDataStructure() throws RemoteException;
	public ArrayList<Cloud> getCloudList() throws RemoteException;

}
