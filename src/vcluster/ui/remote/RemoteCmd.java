package vcluster.ui.remote;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.TreeMap;

import org.w3c.dom.Document;

import vcluster.Vcluster.uiType;
import vcluster.elements.Cloud;
import vcluster.executors.CmdCataloger;
import vcluster.managers.CloudManager;
import vcluster.ui.CmdComb;
import vcluster.ui.Command;
import vcluster.util.HandleXML;

/**
 *This class involve the function that can receive the command from remote client.
 */
public class RemoteCmd extends UnicastRemoteObject implements RemoteInterface{


	private static final long serialVersionUID = 1L;

	public RemoteCmd(String cmd) throws RemoteException {
	}

	/**
	 * This function gets the command from the remote client.
	 */
	public String getRemoteCommand(String command) {
		// TODO Auto-generated method stub
		CmdComb cmd = new CmdComb(command);
		
	    /*
	     * if command is not defined, no more action
	     */
	    if (cmd.getCmd() == Command.NOT_DEFINED) {
	    	return "command is not supported!";
	    	
	    }
	    
		cmd.setUi(uiType.REMOTECLIENT);
		String result = CmdCataloger.execute(cmd).toString();
		String clienthost = "";
		String clientIP="";
		try {
			clienthost =RemoteServer.getClientHost();
			InetAddress ia = InetAddress.getByName(clienthost);
			clientIP = ia.getHostAddress();
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer str = new StringBuffer();
		
		String uistr = String.format("%-9s", "UIType");
		String clientstr = String.format("%-9s", "clientIP");
		String cmdstr = String.format("%-9s", "Command");
		
		
		String uiType = String.format("%-16s", "RemoteClient");
		String clientAddr = String.format("%-16s", clientIP);
		String fcmd = String.format("%-16s", command);
		str.append(System.getProperty("line.separator")+"Request from client: "+System.getProperty("line.separator"));
		str.append("---------------------------------------------"+System.getProperty("line.separator"));		
		str.append(uistr+" : "+uiType+System.getProperty("line.separator"));
		str.append(clientstr+" : "+clientAddr+System.getProperty("line.separator"));
		str.append(cmdstr+" : "+fcmd+System.getProperty("line.separator"));
		str.append("---------------------------------------------"+System.getProperty("line.separator"));
		
		System.out.println(str);
		return result;
	}

	@Override
	public Document getDataStructure() throws RemoteException {
		// TODO Auto-generated method stub
		return HandleXML.getDataStructure();
		
	}

	@Override
	public ArrayList<Cloud> getCloudList() {
		// TODO Auto-generated method stub
		ArrayList<Cloud> cloudlist = new ArrayList<Cloud>();
		cloudlist.addAll(CloudManager.getCloudList().values());
		return cloudlist;
	}

}
