package vcluster.ui.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import vcluster.executors.CmdCataloger;
import vcluster.ui.CmdSet;

public class RemoteCmd extends UnicastRemoteObject implements RemoteInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemoteCmd(String cmd) throws RemoteException {
		command = cmd;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private String command;

	public String getRemoteCommand(String command) {
		// TODO Auto-generated method stub
		String result = CmdCataloger.execute(new CmdSet(command)).toString();
		System.out.println(command);
		return result;
	}

}
