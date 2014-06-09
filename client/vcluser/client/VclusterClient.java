package vcluser.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import vcluster.ui.remote.RemoteInterface;
/**
 *This class is a vcluster client 
 */
public class VclusterClient {

	/**
	 *A prompt which is waiting for accepting command line from command shell input.
	 */
	private  String promptGen() {
		
	    String userCmd = "";

	    /* prompt */
	    System.out.print("vcluter@"+"KISTI"+"> ");
		
	    InputStreamReader input = new InputStreamReader(System.in);
	    BufferedReader reader = new BufferedReader(input);
	    
	    try {
		    /* get a command string */
	    	userCmd = reader.readLine(); 
	    }
	    catch(Exception e){return null;}
	    /* execution here */
	   
	    return userCmd;
	    
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {		    
			VclusterClient vc = new VclusterClient();			
			RemoteInterface ri = (RemoteInterface) Naming.lookup("rmi://"+serverAddr+":"+serverPort+"/command");
			String more = "";
			do {
				more = vc.promptGen();
				if(more.equalsIgnoreCase("quit")||more.equalsIgnoreCase("exit")){
					System.exit(0);
				}
				String str = ri.getRemoteCommand(more);
				System.out.println(str);
			}while (true);		
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			
			System.out.println(" Connection refused to server");

		}
		

	}
	
	private static String serverAddr = "150.183.233.59";
	private static String serverPort = "1099";

}
